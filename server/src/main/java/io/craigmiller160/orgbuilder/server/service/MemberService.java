package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.data.OrgDataManager;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
import io.craigmiller160.orgbuilder.server.rest.UserOrgPrincipal;

import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craigmiller on 9/8/16.
 */
public class MemberService {

    private final String schemaName;
    private final OrgDataManager dataManager;

    public MemberService(SecurityContext securityContext){
        this.dataManager = ServerCore.getOrgDataManager();
        this.schemaName = ((UserOrgPrincipal) securityContext.getUserPrincipal()).getOrg().getSchemaName();
    }

    private DataConnection newConnection() throws OrgApiDataException {
        return dataManager.connectToSchema(schemaName);
    }

    public MemberDTO addMember(MemberDTO member) throws OrgApiException{
        DataConnection connection = null;
        try{
            connection = dataManager.connectToSchema(schemaName);
            Dao<MemberDTO,Long> memberDao = connection.newDao(MemberDTO.class);
            Dao<AddressDTO,Long> addressDao = connection.newDao(AddressDTO.class);
            Dao<PhoneDTO,Long> phoneDao = connection.newDao(PhoneDTO.class);
            Dao<EmailDTO,Long> emailDao = connection.newDao(EmailDTO.class);

            List<AddressDTO> addresses = member.getAddresses();
            List<PhoneDTO> phones = member.getPhones();
            List<EmailDTO> emails = member.getEmails();

            member = memberDao.insert(member);

            member.setAddresses(new ArrayList<>());
            for(AddressDTO a : addresses){
                a = addressDao.insertOrUpdate(a);
                member.getAddresses().add(a);
            }

            member.setPhones(new ArrayList<>());
            for(PhoneDTO p : phones){
                p = phoneDao.insertOrUpdate(p);
                member.getPhones().add(p);
            }

            member.setEmails(new ArrayList<>());
            for(EmailDTO e : emails){
                e = emailDao.insertOrUpdate(e);
                member.getEmails().add(e);
            }

            connection.commit();
        }
        catch(OrgApiDataException ex){
            OrgApiLogger.getServiceLogger().error("Error while performing database operation. Needs rollback? " + (connection != null), ex);
            if(connection != null){
                try{
                    connection.rollback();
                }
                catch(OrgApiDataException ex2){
                    ex2.addSuppressed(ex);
                    throw new OrgApiException("Error while attempting to rollback database operation", ex2);
                }
            }
            throw new OrgApiException("Error while performing database operation", ex);
        }
        finally{
            if(connection != null){
                try{
                    connection.close();
                }
                catch(OrgApiDataException ex){
                    throw new OrgApiException("Unable to close database connection", ex);
                }
            }
        }

        return member;
    }

}
