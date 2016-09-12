package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.data.OrgDataManager;
import io.craigmiller160.orgbuilder.server.rest.UserOrgPrincipal;

import javax.ws.rs.core.SecurityContext;

/**
 * Created by craig on 9/11/16.
 */
public class ServiceCommons {

    private final String schemaName;
    private final OrgDataManager dataManager;
    private final SecurityContext securityContext;
    private final boolean useAppSchema;

    public ServiceCommons(SecurityContext securityContext, boolean useAppSchema){
        this.useAppSchema = useAppSchema;
        this.dataManager = ServerCore.getOrgDataManager();
        this.securityContext = securityContext;
        this.schemaName = ((UserOrgPrincipal) securityContext.getUserPrincipal()).getOrg().getSchemaName();
    }

    public final DataConnection newConnection() throws OrgApiDataException {
        return useAppSchema ? dataManager.connectToAppSchema() : dataManager.connectToSchema(schemaName);
    }

    public final void rollback(DataConnection connection, OrgApiDataException ex) throws OrgApiDataException{
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

    public final void closeConnection(DataConnection connection) throws OrgApiDataException{
        if(connection != null){
            connection.close();
        }
    }

    public void hasWriteAccess() throws OrgApiSecurityException{
        if(!AccessValidator.hasWriteAccess(securityContext)){
            throw new OrgApiSecurityException("User does not have API write access. User: " + securityContext.getUserPrincipal().getName());
        }
    }

    public void hasReadAccess() throws OrgApiSecurityException{
        if(!AccessValidator.hasReadAccess(securityContext)){
            throw new OrgApiSecurityException("User does not have API read access. User: " + securityContext.getUserPrincipal().getName());
        }
    }

    public void hasAdminAccess() throws OrgApiSecurityException{
        if(!AccessValidator.hasAdminAccess(securityContext)){
            throw new OrgApiSecurityException("User does not have API admin access. User: " + securityContext.getUserPrincipal().getName());
        }
    }

    public void hasMasterAccess() throws OrgApiSecurityException{
        if(!AccessValidator.hasMasterAccess(securityContext)){
            throw new OrgApiSecurityException("User does not have API master access. User: " + securityContext.getUserPrincipal().getName());
        }
    }

}
