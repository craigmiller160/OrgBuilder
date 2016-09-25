package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.data.*;
import io.craigmiller160.orgbuilder.server.data.jdbc.SearchQuery;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.DTO;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberListDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import io.craigmiller160.orgbuilder.server.rest.MemberFilterBean;

import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craigmiller on 9/8/16.
 */
public class MemberService {

    private final ServiceCommons serviceCommons;

    MemberService(SecurityContext securityContext){
        this.serviceCommons = new ServiceCommons(securityContext, false);
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

        tempResult.setAddresses((List<AddressDTO>) addressDao.query(AdditionalQueries.DELETE_BY_MEMBER, memberId));
        tempResult.setPhones((List<PhoneDTO>) phoneDao.query(AdditionalQueries.DELETE_BY_MEMBER, memberId));
        tempResult.setEmails((List<EmailDTO>) emailDao.query(AdditionalQueries.DELETE_BY_MEMBER, memberId));
    }

    private <E extends DTO<I>,I> void performInsertOrUpdate(List<E> results, List<E> elements, Dao<E,I> dao) throws OrgApiDataException{
        for(E e : elements){
            e = dao.insertOrUpdate(e);
            results.add(e);
        }
    }

    public MemberDTO updateMember(MemberDTO member, Long id) throws OrgApiDataException, OrgApiSecurityException{
        member.setElementId(id);
        DataConnection connection = null;
        MemberDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<MemberDTO,Long> memberDao = connection.newDao(MemberDTO.class);

            result = memberDao.update(member, id);

            insertOrUpdateJoinedEntities(member, result, connection);

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

    public MemberDTO addMember(MemberDTO member) throws OrgApiDataException, OrgApiSecurityException{
        member.setElementId(-1L);
        DataConnection connection = null;
        MemberDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<MemberDTO,Long> memberDao = connection.newDao(MemberDTO.class);
            result = memberDao.insert(member);

            insertOrUpdateJoinedEntities(member, result, connection);

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

    public MemberDTO deleteMember(Long memberId) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        MemberDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            MemberDTO tempResult = new MemberDTO();
            deleteJoinedEntities(memberId, tempResult, connection);

            Dao<MemberDTO,Long> memberDao = connection.newDao(MemberDTO.class);
            result = memberDao.delete(memberId);
            if(result != null){
                result.setAddresses(tempResult.getAddresses());
                result.setPhones(tempResult.getPhones());
                result.setEmails(tempResult.getEmails());
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

    public MemberDTO getMember(Long memberId) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        MemberDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<MemberDTO,Long> memberDao = connection.newDao(MemberDTO.class);
            Dao<AddressDTO,Long> addressDao = connection.newDao(AddressDTO.class);
            Dao<PhoneDTO,Long> phoneDao = connection.newDao(PhoneDTO.class);
            Dao<EmailDTO,Long> emailDao = connection.newDao(EmailDTO.class);

            result = memberDao.get(memberId);
            if(result == null){
                return null;
            }

            result.setAddresses((List<AddressDTO>) addressDao.query(AdditionalQueries.GET_ALL_BY_MEMBER, memberId));
            result.setPhones((List<PhoneDTO>) phoneDao.query(AdditionalQueries.GET_ALL_BY_MEMBER, memberId));
            result.setEmails((List<EmailDTO>) emailDao.query(AdditionalQueries.GET_ALL_BY_MEMBER, memberId));

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

    public MemberListDTO getMemberList(MemberFilterBean memberFilterBean) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        MemberListDTO results = null;
        long offset = memberFilterBean.getOffset();
        long size = memberFilterBean.getSize();
        try{
            connection = serviceCommons.newConnection();
            Dao<MemberDTO,Long> memberDao = connection.newDao(MemberDTO.class);
            List<MemberDTO> list = null;
            if(memberFilterBean.isSearch()){
                list = (List<MemberDTO>) memberDao.query(AdditionalQueries.SEARCH, memberFilterBean);
            }
            else{
                list = (offset >= 0 && size >= 0) ? memberDao.getAll(offset, size) : memberDao.getAll();
            }

            if(list.size() > 0){
                results = new MemberListDTO(list);
            }

            connection.commit();
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
