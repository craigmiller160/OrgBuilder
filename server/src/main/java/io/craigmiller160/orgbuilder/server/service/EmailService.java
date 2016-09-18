package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.MemberJoins;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.EmailListDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;

import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by craig on 9/11/16.
 */
public class EmailService {

    private final ServiceCommons serviceCommons;

    EmailService(SecurityContext securityContext){
        this.serviceCommons = new ServiceCommons(securityContext, false);
    }

    public EmailDTO addEmail(EmailDTO email, long memberId) throws OrgApiDataException, OrgApiSecurityException{
        email.setElementId(-1L);
        email.setMemberId(memberId);
        DataConnection connection = null;
        EmailDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<EmailDTO,Long> emailDao = connection.newDao(EmailDTO.class);

            result = emailDao.insert(email);

            connection.commit();
        }
        catch(OrgApiDataException ex){
            serviceCommons.rollback(connection, ex);
        }
        finally{
            serviceCommons.closeConnection(connection);
        }
        return result;
    }

    public EmailDTO updateEmail(EmailDTO email, Long emailId, long memberId) throws OrgApiDataException, OrgApiSecurityException{
        email.setElementId(emailId);
        email.setMemberId(memberId);
        DataConnection connection = null;
        EmailDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<EmailDTO,Long> emailDao = connection.newDao(EmailDTO.class);

            result = emailDao.update(email, emailId);

            connection.commit();
        }
        catch(OrgApiDataException ex){
            serviceCommons.rollback(connection, ex);
        }
        finally{
            serviceCommons.closeConnection(connection);
        }
        return result;
    }

    public EmailDTO deleteEmail(Long emailId) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        EmailDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<EmailDTO,Long> emailDao = connection.newDao(EmailDTO.class);

            result = emailDao.delete(emailId);

            connection.commit();
        }
        catch(OrgApiDataException ex){
            serviceCommons.rollback(connection, ex);
        }
        finally{
            serviceCommons.closeConnection(connection);
        }
        return result;
    }

    public EmailDTO getEmailByMember(Long emailId, long memberId) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        EmailDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<EmailDTO,Long> emailDao = connection.newDao(EmailDTO.class);

            result = (EmailDTO) emailDao.query(MemberJoins.GET_BY_ID_AND_MEMBER, emailId, memberId);

            connection.commit();
        }
        catch(OrgApiDataException ex){
            serviceCommons.rollback(connection, ex);
        }
        finally{
            serviceCommons.closeConnection(connection);
        }
        return result;
    }

    public EmailListDTO getAllEmailsByMember(long memberId, long offset, long size) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        EmailListDTO results = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<EmailDTO,Long> emailDao = connection.newDao(EmailDTO.class);

            List<EmailDTO> list = (offset >= 0 && size >= 0) ? (List<EmailDTO>) emailDao.query(MemberJoins.GET_ALL_BY_MEMBER, memberId, offset, size) :
                    (List<EmailDTO>) emailDao.query(MemberJoins.GET_ALL_BY_MEMBER, memberId);
            if(list.size() > 0){
                results = new EmailListDTO(list);
            }
        }
        catch(OrgApiDataException ex){
            serviceCommons.rollback(connection, ex);
        }
        finally{
            serviceCommons.closeConnection(connection);
        }
        return results;
    }

}
