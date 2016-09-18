package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.MemberJoins;
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

    AddressService(SecurityContext securityContext){
        this.serviceCommons = new ServiceCommons(securityContext, false);
    }

    public AddressDTO addAddress(AddressDTO address, long memberId) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasWriteAccess();
        address.setMemberId(memberId);
        address.setElementId(-1L);
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

    public AddressDTO updateAddress(AddressDTO address, Long addressId, long memberId) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasWriteAccess();
        address.setElementId(addressId);
        address.setMemberId(memberId);
        DataConnection connection = null;
        AddressDTO result = null;
        try{
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

    public AddressDTO getAddressByMember(Long addressId, long memberId) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasReadAccess();
        DataConnection connection = null;
        AddressDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<AddressDTO,Long> addressDao = connection.newDao(AddressDTO.class);

            result = (AddressDTO) addressDao.query(MemberJoins.GET_BY_ID_AND_MEMBER, addressId, memberId);

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
        serviceCommons.hasReadAccess();
        DataConnection connection = null;
        AddressListDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<AddressDTO,Long> addressDao = connection.newDao(AddressDTO.class);

            List<AddressDTO> list = (offset >= 0 && size >= 0) ? (List<AddressDTO>) addressDao.query(MemberJoins.GET_ALL_BY_MEMBER, memberId, offset, size) :
                    (List<AddressDTO>) addressDao.query(MemberJoins.GET_ALL_BY_MEMBER, memberId);
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
