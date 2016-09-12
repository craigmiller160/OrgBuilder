package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.AddressListDTO;

import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by craig on 9/11/16.
 */
public class AddressService {

    private final ServiceCommons serviceCommons;

    public AddressService(SecurityContext securityContext){
        this.serviceCommons = new ServiceCommons(securityContext, false);
    }

    public AddressDTO addAddress(AddressDTO address) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasWriteAccess();
        DataConnection connection = null;
        AddressDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<AddressDTO,Long> addressDao = connection.newDao(AddressDTO.class);

            result = addressDao.insert(address);

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

    public AddressDTO updateAddress(AddressDTO address, Long addressId) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasWriteAccess();
        DataConnection connection = null;
        AddressDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<AddressDTO,Long> addressDao = connection.newDao(AddressDTO.class);

            address.setElementId(addressId);
            result = addressDao.update(address, addressId);

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

    public AddressDTO deleteAddress(Long addressId) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasWriteAccess();
        DataConnection connection = null;
        AddressDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<AddressDTO,Long> addressDao = connection.newDao(AddressDTO.class);

            result = addressDao.delete(addressId);

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

    public AddressDTO getAddress(Long addressId) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasReadAccess();
        DataConnection connection = null;
        AddressDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<AddressDTO,Long> addressDao = connection.newDao(AddressDTO.class);

            result = addressDao.get(addressId);

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

    public AddressListDTO getAllAddresses(long offset, long size) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasReadAccess();
        DataConnection connection = null;
        AddressListDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<AddressDTO,Long> addressDao = connection.newDao(AddressDTO.class);

            List<AddressDTO> list = (offset >= 0 && size >= 0) ? addressDao.getAll(offset, size) : addressDao.getAll();
            if(list.size() > 0){
                result = new AddressListDTO(list);
            }

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

}
