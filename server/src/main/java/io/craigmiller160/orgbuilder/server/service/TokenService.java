package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;

import javax.ws.rs.core.SecurityContext;

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
            connection = serviceCommons.newConnection();
            Dao<RefreshTokenDTO,Long> tokenDao = connection.newDao(RefreshTokenDTO.class);

            result = tokenDao.insertOrUpdate(token);

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
