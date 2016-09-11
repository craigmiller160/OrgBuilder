package io.craigmiller160.orgbuilder.server.service;

import javax.ws.rs.core.SecurityContext;

/**
 * Created by craigmiller on 9/8/16.
 */
public class ServiceFactory {

    public static ServiceFactory newInstance() {
        return new ServiceFactory();
    }

    private ServiceFactory(){

    }

    public MemberService newMemberService(SecurityContext securityContext){
        return new MemberService(securityContext);
    }

    public AddressService newAddressService(SecurityContext securityContext){
        return new AddressService(securityContext);
    }

    public PhoneService newPhoneService(SecurityContext securityContext){
        return new PhoneService(securityContext);
    }

}
