package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.dto.Gender;
import io.craigmiller160.orgbuilder.server.dto.GenderListDTO;
import io.craigmiller160.orgbuilder.server.dto.State;
import io.craigmiller160.orgbuilder.server.dto.StateListDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import javax.ws.rs.core.SecurityContext;

/**
 * Created by craig on 9/18/16.
 */
public class InfoService {

    private final ServiceCommons serviceCommons;

    InfoService(SecurityContext securityContext){
        this.serviceCommons = new ServiceCommons(securityContext, false);
    }

    public GenderListDTO getGenders() throws OrgApiSecurityException{
        OrgApiLogger.getServiceLogger().debug("Getting list of Gender values");
        return new GenderListDTO(Gender.values());
    }

    public StateListDTO getStates() throws OrgApiSecurityException{
        OrgApiLogger.getServiceLogger().debug("Getting list of State values");
        return new StateListDTO(State.values());
    }

}
