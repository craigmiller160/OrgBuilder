package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneListDTO;

import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by craig on 9/11/16.
 */
public class PhoneService {

    private final ServiceCommons serviceCommons;

    public PhoneService(SecurityContext securityContext){
        this.serviceCommons = new ServiceCommons(securityContext);
    }

    public PhoneDTO addPhone(PhoneDTO phone) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasWriteAccess();
        DataConnection connection = null;
        PhoneDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<PhoneDTO,Long> phoneDao = connection.newDao(PhoneDTO.class);

            result = phoneDao.insert(phone);

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

    public PhoneDTO updatePhone(PhoneDTO phone, Long phoneId) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasWriteAccess();
        DataConnection connection = null;
        PhoneDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<PhoneDTO,Long> phoneDao = connection.newDao(PhoneDTO.class);

            phone.setElementId(phoneId);
            result = phoneDao.update(phone, phoneId);

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

    public PhoneDTO deletePhone(Long phoneId) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasWriteAccess();
        DataConnection connection = null;
        PhoneDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<PhoneDTO,Long> phoneDao = connection.newDao(PhoneDTO.class);

            result = phoneDao.delete(phoneId);

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

    public PhoneDTO getPhone(Long phoneId) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasReadAccess();
        DataConnection connection = null;
        PhoneDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<PhoneDTO,Long> phoneDao = connection.newDao(PhoneDTO.class);

            result = phoneDao.get(phoneId);

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

    public PhoneListDTO getAllPhones(long offset, long size) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasReadAccess();
        DataConnection connection = null;
        PhoneListDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<PhoneDTO,Long> phoneDao = connection.newDao(PhoneDTO.class);

            List<PhoneDTO> list = (offset >= 0 && size >= 0) ? phoneDao.getAll(offset, size) : phoneDao.getAll();
            if(list.size() > 0){
                result = new PhoneListDTO(list);
            }
        }
        catch(OrgApiDataException ex){
            serviceCommons.rollback(connection, ex);
        }
        finally{
            serviceCommons.closeConnection(connection);
        }
        return result;
    }

}
