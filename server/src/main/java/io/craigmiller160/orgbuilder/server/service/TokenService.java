package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.data.AdditionalQueries;
import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenListDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by craig on 10/1/16.
 */
public class TokenService {

    private final ServiceCommons serviceCommons;

    TokenService(SecurityContext securityContext){
        this.serviceCommons = new ServiceCommons(securityContext, true);
    }

    public RefreshTokenDTO addRefreshToken(RefreshTokenDTO token) throws OrgApiDataException, OrgApiSecurityException{
        token.setElementId(-1L);
        DataConnection connection = null;
        RefreshTokenDTO result = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Adding new refresh token. Subject: " + serviceCommons.getSubjectName());
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
            OrgApiLogger.getServiceLogger().debug("Deleting refresh token. Subject: " + serviceCommons.getSubjectName() + " | ID: " + tokenId);
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
            OrgApiLogger.getServiceLogger().debug("Retrieving refresh token by ID. Subject: " + serviceCommons.getSubjectName() + " | ID: " + tokenId);
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

    public RefreshTokenListDTO getAllRefreshTokens(long offset, long size) throws OrgApiDataException, OrgApiSecurityException{
        DataConnection connection = null;
        RefreshTokenListDTO results = null;
        try{
            OrgApiLogger.getServiceLogger().debug("Retrieving all refresh tokens. Subject: " + serviceCommons.getSubjectName());
            connection = serviceCommons.newConnection();
            Dao<RefreshTokenDTO,Long> tokenDao = connection.newDao(RefreshTokenDTO.class);

            List<RefreshTokenDTO> list = (offset >= 0 && size >= 0) ? tokenDao.getAll(offset, size) : tokenDao.getAll();
            if(list.size() > 0){
                results = new RefreshTokenListDTO(list);
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
