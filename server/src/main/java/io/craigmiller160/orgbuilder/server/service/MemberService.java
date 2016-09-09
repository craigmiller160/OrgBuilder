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
    private final SecurityContext securityContext;

    public MemberService(SecurityContext securityContext){
        this.dataManager = ServerCore.getOrgDataManager();
        this.securityContext = securityContext;
        this.schemaName = ((UserOrgPrincipal) securityContext.getUserPrincipal()).getOrg().getSchemaName();
    }

    private DataConnection newConnection() throws OrgApiDataException {
        return dataManager.connectToSchema(schemaName);
    }

    public MemberDTO addMember(MemberDTO member) throws OrgApiDataException, OrgApiSecurityException{
        if(!AccessValidator.hasWriteAccess(securityContext)){
            throw new OrgApiSecurityException("User does not have API write access. User: " + securityContext.getUserPrincipal().getName());
        }
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
            rollback(connection, ex);
        }
        finally{
            closeConnection(connection);
        }

        return member;
    }

    private void rollback(DataConnection connection, OrgApiDataException ex) throws OrgApiDataException{
        if(connection != null){
            try{
                connection.rollback();
            }
            catch(OrgApiDataException ex2){
                ex2.addSuppressed(ex);
                throw ex2;
            }
        }
        throw ex;
    }

    private void closeConnection(DataConnection connection) throws OrgApiDataException{
        if(connection != null){
            connection.close();
        }
    }

}
