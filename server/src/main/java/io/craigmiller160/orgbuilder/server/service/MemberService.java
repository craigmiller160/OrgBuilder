package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.MemberJoins;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.data.OrgDataManager;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.DTO;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import io.craigmiller160.orgbuilder.server.rest.UserOrgPrincipal;

import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craigmiller on 9/8/16.
 */
public class MemberService {

    private static final int INSERT_OR_UPDATE_OPERATION = 101;
    private static final int DELETE_OPERATION = 102;

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

    private void insertOrUpdateJoinedEntities(MemberDTO member, MemberDTO result, DataConnection connection) throws OrgApiDataException{
        Dao<AddressDTO,Long> addressDao = connection.newDao(AddressDTO.class);
        Dao<PhoneDTO,Long> phoneDao = connection.newDao(PhoneDTO.class);
        Dao<EmailDTO,Long> emailDao = connection.newDao(EmailDTO.class);

        List<AddressDTO> addresses = member.getAddresses();
        List<PhoneDTO> phones = member.getPhones();
        List<EmailDTO> emails = member.getEmails();

        result.setAddresses(new ArrayList<>());
        performInsertOrUpdate(result.getAddresses(), addresses, addressDao);

        result.setPhones(new ArrayList<>());
        performInsertOrUpdate(result.getPhones(), phones, phoneDao);

        result.setEmails(new ArrayList<>());
        performInsertOrUpdate(result.getEmails(), emails, emailDao);
    }

    private void deleteJoinedEntities(long memberId, MemberDTO tempResult, DataConnection connection) throws OrgApiDataException{
        Dao<AddressDTO,Long> addressDao = connection.newDao(AddressDTO.class);
        Dao<PhoneDTO,Long> phoneDao = connection.newDao(PhoneDTO.class);
        Dao<EmailDTO,Long> emailDao = connection.newDao(EmailDTO.class);

        tempResult.setAddresses((List<AddressDTO>) addressDao.query(MemberJoins.DELETE_BY_MEMBER, memberId));
        tempResult.setPhones((List<PhoneDTO>) phoneDao.query(MemberJoins.DELETE_BY_MEMBER, memberId));
        tempResult.setEmails((List<EmailDTO>) emailDao.query(MemberJoins.DELETE_BY_MEMBER, memberId));
    }

    private <E extends DTO<I>,I> void performInsertOrUpdate(List<E> results, List<E> elements, Dao<E,I> dao) throws OrgApiDataException{
        for(E e : elements){
            e = dao.insertOrUpdate(e);
            results.add(e);
        }
    }

    public MemberDTO updateMember(MemberDTO member, Long id) throws OrgApiDataException, OrgApiSecurityException{
        if(!AccessValidator.hasWriteAccess(securityContext)){
            throw new OrgApiSecurityException("User does not have API write access. User: " + securityContext.getUserPrincipal().getName());
        }
        DataConnection connection = null;
        MemberDTO result = null;
        try{
            connection = newConnection();
            Dao<MemberDTO,Long> memberDao = connection.newDao(MemberDTO.class);

            member.setElementId(id);
            result = memberDao.update(member, id);

            insertOrUpdateJoinedEntities(member, result, connection);

            connection.commit();
        }
        catch(OrgApiDataException ex){
            rollback(connection, ex);
        }
        finally{
            closeConnection(connection);
        }
        return result;
    }

    public MemberDTO addMember(MemberDTO member) throws OrgApiDataException, OrgApiSecurityException{
        if(!AccessValidator.hasWriteAccess(securityContext)){
            throw new OrgApiSecurityException("User does not have API write access. User: " + securityContext.getUserPrincipal().getName());
        }
        DataConnection connection = null;
        MemberDTO result = null;
        try{
            connection = newConnection();
            Dao<MemberDTO,Long> memberDao = connection.newDao(MemberDTO.class);
            result = memberDao.insert(member);

            insertOrUpdateJoinedEntities(member, result, connection);

            connection.commit();
        }
        catch(OrgApiDataException ex){
            rollback(connection, ex);
        }
        finally{
            closeConnection(connection);
        }

        return result;
    }

    public MemberDTO deleteMember(Long memberId) throws OrgApiDataException, OrgApiSecurityException{
        if(!AccessValidator.hasWriteAccess(securityContext)){
            throw new OrgApiSecurityException("User does not have API write access. User: " + securityContext.getUserPrincipal().getName());
        }
        DataConnection connection = null;
        MemberDTO result = null;
        try{
            connection = newConnection();
            MemberDTO tempResult = new MemberDTO();
            deleteJoinedEntities(memberId, tempResult, connection);

            Dao<MemberDTO,Long> memberDao = connection.newDao(MemberDTO.class);
            result = memberDao.delete(memberId);
            result.setAddresses(tempResult.getAddresses());
            result.setPhones(tempResult.getPhones());
            result.setEmails(tempResult.getEmails());
        }
        catch(OrgApiDataException ex){
            rollback(connection, ex);
        }
        finally{
            closeConnection(connection);
        }
        return result;
    }

    public MemberDTO getMember(Long memberId) throws OrgApiDataException, OrgApiSecurityException{
        if(!AccessValidator.hasReadAccess(securityContext)){
            throw new OrgApiSecurityException("User does not have API read access. User: " + securityContext.getUserPrincipal().getName());
        }
        DataConnection connection = null;
        MemberDTO result = null;
        try{
            connection = newConnection();
            Dao<MemberDTO,Long> memberDao = connection.newDao(MemberDTO.class);
            Dao<AddressDTO,Long> addressDao = connection.newDao(AddressDTO.class);
            Dao<PhoneDTO,Long> phoneDao = connection.newDao(PhoneDTO.class);
            Dao<EmailDTO,Long> emailDao = connection.newDao(EmailDTO.class);

            result = memberDao.get(memberId);
            if(result == null){
                return null;
            }

            result.setAddresses((List<AddressDTO>) addressDao.query(MemberJoins.GET_ALL_BY_MEMBER, memberId));
            result.setPhones((List<PhoneDTO>) phoneDao.query(MemberJoins.GET_ALL_BY_MEMBER, memberId));
            result.setEmails((List<EmailDTO>) emailDao.query(MemberJoins.GET_ALL_BY_MEMBER, memberId));
        }
        catch(OrgApiDataException ex){
            rollback(connection, ex);
        }
        finally{
            closeConnection(connection);
        }
        return result;
    }

    public List<MemberDTO> getAllMembers(long offset, long size) throws OrgApiDataException, OrgApiSecurityException{
        if(!AccessValidator.hasReadAccess(securityContext)){
            throw new OrgApiSecurityException("User does not have API read access. User: " + securityContext.getUserPrincipal().getName());
        }
        DataConnection connection = null;
        List<MemberDTO> results = null;
        try{
            connection = newConnection();
            Dao<MemberDTO,Long> memberDao = connection.newDao(MemberDTO.class);
            results = (offset >= 0 && size > 0) ? memberDao.getAll() : memberDao.getAll(offset, size);
        }
        catch(OrgApiDataException ex){
            rollback(connection, ex);
        }
        finally {
            closeConnection(connection);
        }
        return results;
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
