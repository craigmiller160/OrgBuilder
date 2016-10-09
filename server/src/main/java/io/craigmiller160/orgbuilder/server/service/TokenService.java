package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.data.AdditionalQueries;
import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import javax.ws.rs.core.SecurityContext;

/**
 * Created by craig on 10/1/16.
 */
public class TokenService {

    private final ServiceCommons serviceCommons;

    //TODO make sure proper access restrictions are ultimately placed on this service

    TokenService(SecurityContext securityContext){
        this.serviceCommons = new ServiceCommons(securityContext, true);
    }

    public RefreshTokenDTO addRefreshToken(RefreshTokenDTO token) throws OrgApiDataException, OrgApiSecurityException{
        token.setElementId(-1L);
        DataConnection connection = null;
        RefreshTokenDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Adding new refresh token. Subject: " + serviceCommons.getSubjectName() +
                    " | Schema: " + serviceCommons.getSchemaName());
            connection = serviceCommons.newConnection();
            Dao<RefreshTokenDTO,Long> tokenDao = connection.newDao(RefreshTokenDTO.class);

            RefreshTokenDTO exists = (RefreshTokenDTO) tokenDao.query(AdditionalQueries.GET_WITH_HASH, token.getTokenHash());
            if(exists != null){
                tokenDao.delete(exists.getElementId());
            }

            result = tokenDao.insert(token);

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

    public RefreshTokenDTO deleteRefreshToken(Long tokenId) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        RefreshTokenDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Deleting refresh token. Subject: " + serviceCommons.getSubjectName() +
                    " | Schema: " + serviceCommons.getSchemaName() + " | ID: " + tokenId);
            connection = serviceCommons.newConnection();
            Dao<RefreshTokenDTO,Long> tokenDao = connection.newDao(RefreshTokenDTO.class);

            result = tokenDao.delete(tokenId);

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

    public RefreshTokenDTO getRefreshToken(Long tokenId) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        RefreshTokenDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Retrieving refresh token by ID. Subject: " + serviceCommons.getSubjectName() +
                    " Schema: " + serviceCommons.getSchemaName() + " | ID: " + tokenId);
            connection = serviceCommons.newConnection();
            Dao<RefreshTokenDTO,Long> tokenDao = connection.newDao(RefreshTokenDTO.class);

            result = tokenDao.get(tokenId);

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
