package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
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

    public EmailService(SecurityContext securityContext){
        this.serviceCommons = new ServiceCommons(securityContext);
    }

    public EmailDTO addEmail(EmailDTO email) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasWriteAccess();
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

    public EmailDTO updateEmail(EmailDTO email, Long emailId) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasWriteAccess();
        DataConnection connection = null;
        EmailDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<EmailDTO,Long> emailDao = connection.newDao(EmailDTO.class);

            email.setElementId(emailId);
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
        serviceCommons.hasWriteAccess();
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

    public EmailDTO getEmail(Long emailId) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasReadAccess();
        DataConnection connection = null;
        EmailDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<EmailDTO,Long> emailDao = connection.newDao(EmailDTO.class);

            result = emailDao.get(emailId);

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

    public EmailListDTO getAllEmails(long offset, long size) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasReadAccess();
        DataConnection connection = null;
        EmailListDTO results = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<EmailDTO,Long> emailDao = connection.newDao(EmailDTO.class);

            List<EmailDTO> list = (offset >= 0 && size >= 0) ? emailDao.getAll(offset, size) : emailDao.getAll();
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
