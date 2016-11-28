package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerProps;
import io.craigmiller160.orgbuilder.server.dto.*;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
import io.craigmiller160.orgbuilder.server.rest.Role;

import javax.ws.rs.core.SecurityContext;

/**
 * Created by craig on 9/18/16.
 */
public class InfoService {

    private final ServiceCommons serviceCommons;

    InfoService(SecurityContext securityContext){
        this.serviceCommons = new ServiceCommons(securityContext, false);
    }

    public SexListDTO getSexes(){
        OrgApiLogger.getServiceLogger().debug("Getting list of Gender values");
        return new SexListDTO(Sex.values());
    }

    public StateListDTO getStates(){
        OrgApiLogger.getServiceLogger().debug("Getting list of State values");
        return new StateListDTO(State.values());
    }

    public RoleListDTO getRoles(){
        OrgApiLogger.getServiceLogger().debug("Getting list of Role values");
        return new RoleListDTO(Role.ALL);
    }

    public AppInfoDTO getAppInfo(){
        OrgApiLogger.getServiceLogger().debug("Getting application info");
        return new AppInfoDTO(ServerCore.getProperty(ServerProps.API_NAME), ServerCore.getProperty(ServerProps.API_VERSION));
    }

}
