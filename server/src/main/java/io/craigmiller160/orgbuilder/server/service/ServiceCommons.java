package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.data.DataConnection;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.data.OrgDataManager;
import io.craigmiller160.orgbuilder.server.rest.OrgApiPrincipal;
import io.craigmiller160.orgbuilder.server.rest.OrgApiSecurityContext;
import io.craigmiller160.orgbuilder.server.rest.Role;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by craig on 9/11/16.
 */
public class ServiceCommons {

    private final String schemaName;
    private final String subjectName;
    private final OrgDataManager dataManager;
    private final SecurityContext securityContext;
    private final boolean useAppSchema;

    public ServiceCommons(SecurityContext securityContext, boolean useAppSchema){
        if(securityContext == null){
            throw new IllegalArgumentException("No SecurityContext provided");
        }

        this.useAppSchema = useAppSchema;
        this.dataManager = ServerCore.getOrgDataManager();
        this.securityContext = securityContext;
        this.schemaName = ((OrgApiPrincipal) securityContext.getUserPrincipal()).getSchema();
        this.subjectName = securityContext.getUserPrincipal().getName();
    }

    public String getSchemaName(){
        return schemaName;
    }

    public String getSubjectName(){
        return subjectName;
    }

    public final DataConnection newConnection() throws OrgApiDataException {
        return useAppSchema ? dataManager.connectToAppSchema() : dataManager.connectToSchema(schemaName);
    }

    public final void rollback(DataConnection connection, Exception ex) throws OrgApiDataException{
        if(connection != null){
            try{
                connection.rollback();
            }
            catch(OrgApiDataException ex2){
                ex2.addSuppressed(ex);
                throw ex2;
            }
        }

        if(ex instanceof OrgApiDataException){
            throw (OrgApiDataException) ex;
        }
        else if(ex instanceof ForbiddenException){
            throw (ForbiddenException) ex;
        }
        else{
            throw new RuntimeException("CRITICAL ERROR! EXCEPTION IS NOT OF EXPECTED TYPE FOR CASTING: " + ex.getClass().getName(), ex);
        }
    }

    public final void closeConnection(DataConnection connection) throws OrgApiDataException{
        if(connection != null){
            connection.close();
        }
    }

    public void hasWriteAccess() throws OrgApiSecurityException{
        if(!securityContext.isUserInRole(Role.WRITE.toString())){
            throw new OrgApiSecurityException("User does not have API write access. User: " + securityContext.getUserPrincipal().getName());
        }
    }

    public void hasReadAccess() throws OrgApiSecurityException{
        if(!securityContext.isUserInRole(Role.READ.toString())){
            throw new OrgApiSecurityException("User does not have API read access. User: " + securityContext.getUserPrincipal().getName());
        }
    }

    public void hasAdminAccess() throws OrgApiSecurityException{
        if(!securityContext.isUserInRole(Role.ADMIN.toString())){
            throw new OrgApiSecurityException("User does not have API admin access. User: " + securityContext.getUserPrincipal().getName());
        }
    }

    public void hasMasterAccess() throws OrgApiSecurityException{
        if(!securityContext.isUserInRole(Role.MASTER.toString())){
            throw new OrgApiSecurityException("User does not have API master access. User: " + securityContext.getUserPrincipal().getName());
        }
    }

}
