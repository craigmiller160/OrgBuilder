package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.data.*;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.AddressListDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by craig on 9/11/16.
 */
public class AddressService {

    private final ServiceCommons serviceCommons;

    AddressService(SecurityContext securityContext){
        this.serviceCommons = new ServiceCommons(securityContext, false);
    }

    public AddressDTO addAddress(AddressDTO address, long memberId) throws OrgApiDataException, OrgApiSecurityException{
        address.setMemberId(memberId);
        address.setElementId(-1L);
        DataConnection connection = null;
        AddressDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Adding new address. Subject: " + serviceCommons.getSubjectName() +
                    " | Schema: " + serviceCommons.getSchemaName() + " | MemberId: " + memberId);
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

    public AddressDTO updateAddress(AddressDTO address, Long addressId, long memberId) throws OrgApiDataException, OrgApiSecurityException{
        address.setElementId(addressId);
        address.setMemberId(memberId);
        DataConnection connection = null;
        AddressDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Updating existing address. Subject: " + serviceCommons.getSubjectName() +
                    " | Schema: " + serviceCommons.getSchemaName() + " | ID: " + addressId + " | MemberId: " + memberId);
            connection = serviceCommons.newConnection();
            Dao<AddressDTO,Long> addressDao = connection.newDao(AddressDTO.class);

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
        DataConnection connection = null;
        AddressDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Deleting address. Subject: " + serviceCommons.getSubjectName() +
                    " | Schema: " + serviceCommons.getSchemaName() + " | ID: " + addressId);
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

    public AddressDTO getAddressByMember(Long addressId, long memberId) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        AddressDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Retrieving address by ID and member. Subject: " + serviceCommons.getSubjectName() +
                    " | Schema: " + serviceCommons.getSchemaName() + " | ID: " + addressId + " | MemberId: " + memberId);
            connection = serviceCommons.newConnection();
            Dao<AddressDTO,Long> addressDao = connection.newDao(AddressDTO.class);

            result = (AddressDTO) addressDao.query(AdditionalQueries.GET_BY_ID_AND_MEMBER, addressId, memberId);

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

    public AddressListDTO getAllAddressesByMember(long memberId, long offset, long size) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        AddressListDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Getting list of addresses for member. Subject: " + serviceCommons.getSubjectName() +
                    " | Schema: " + serviceCommons.getSchemaName() + " | MemberId: " + memberId);
            connection = serviceCommons.newConnection();
            Dao<AddressDTO,Long> addressDao = connection.newDao(AddressDTO.class);

            List<AddressDTO> list = (offset >= 0 && size >= 0) ? (List<AddressDTO>) addressDao.query(AdditionalQueries.GET_ALL_BY_MEMBER, memberId, offset, size) :
                    (List<AddressDTO>) addressDao.query(AdditionalQueries.GET_ALL_BY_MEMBER, memberId);
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
