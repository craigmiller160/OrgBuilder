package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.OrgListDTO;

import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by craig on 9/11/16.
 */
public class OrgService {

    private final ServiceCommons serviceCommons;

    OrgService(SecurityContext securityContext){
        this.serviceCommons = new ServiceCommons(securityContext, true);
    }

    public OrgDTO addOrg(OrgDTO org) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasMasterAccess();
        DataConnection connection = null;
        OrgDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<OrgDTO,Long> orgDao = connection.newDao(OrgDTO.class);

            result = orgDao.insert(org);
            ServerCore.getOrgDataManager().createOrgSchema(result.getSchemaName());

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

    public OrgDTO updateOrg(OrgDTO org, Long orgId) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasAdminAccess();
        DataConnection connection = null;
        OrgDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<OrgDTO,Long> orgDao = connection.newDao(OrgDTO.class);

            org.setElementId(orgId);
            result = orgDao.update(org, orgId);

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

    public OrgDTO deleteOrg(Long orgId) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasMasterAccess();
        DataConnection connection = null;
        OrgDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<OrgDTO,Long> orgDao = connection.newDao(OrgDTO.class);

            result = orgDao.delete(orgId);
            ServerCore.getOrgDataManager().deleteSchema(result.getSchemaName(), true);

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

    public OrgDTO getOrg(Long orgId) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasReadAccess();
        DataConnection connection = null;
        OrgDTO result = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<OrgDTO,Long> orgDao = connection.newDao(OrgDTO.class);

            result = orgDao.get(orgId);

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

    public OrgListDTO getAllOrgs(long offset, long size) throws OrgApiDataException, OrgApiSecurityException{
        serviceCommons.hasMasterAccess();
        DataConnection connection = null;
        OrgListDTO results = null;
        try{
            connection = serviceCommons.newConnection();
            Dao<OrgDTO,Long> orgDao = connection.newDao(OrgDTO.class);

            List<OrgDTO> list = (offset >= 0 && size >= 0) ? orgDao.getAll(offset, size) : orgDao.getAll();
            if(list.size() > 0){
                results = new OrgListDTO(list);
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
