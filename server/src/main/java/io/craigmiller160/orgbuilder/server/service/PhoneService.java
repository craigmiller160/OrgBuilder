package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.MemberJoins;
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

    PhoneService(SecurityContext securityContext){
        this.serviceCommons = new ServiceCommons(securityContext, false);
    }

    public PhoneDTO addPhone(PhoneDTO phone, long memberId) throws OrgApiDataException, OrgApiSecurityException{
        phone.setElementId(-1L);
        phone.setMemberId(memberId);
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

    public PhoneDTO updatePhone(PhoneDTO phone, Long phoneId, long memberId) throws OrgApiDataException, OrgApiSecurityException{
        phone.setElementId(phoneId);
        phone.setMemberId(memberId);
        DataConnection connection = null;
        PhoneDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<PhoneDTO,Long> phoneDao = connection.newDao(PhoneDTO.class);

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

    public PhoneDTO getPhoneByMember(Long phoneId, long memberId) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        PhoneDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<PhoneDTO,Long> phoneDao = connection.newDao(PhoneDTO.class);

            result = (PhoneDTO) phoneDao.query(MemberJoins.GET_BY_ID_AND_MEMBER, phoneId, memberId);

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

    public PhoneListDTO getAllPhonesByMember(long memberId, long offset, long size) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        PhoneListDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<PhoneDTO,Long> phoneDao = connection.newDao(PhoneDTO.class);

            List<PhoneDTO> list = (offset >= 0 && size >= 0) ? (List<PhoneDTO>) phoneDao.query(MemberJoins.GET_ALL_BY_MEMBER, memberId, offset, size) :
                    (List<PhoneDTO>) phoneDao.query(MemberJoins.GET_ALL_BY_MEMBER, memberId);
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
