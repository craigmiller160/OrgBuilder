package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.util.LegacyDateConverter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by craig on 10/2/16.
 */
public class TokenTestUtils {

    public static final String SCHEMA_NAME = "TestSchema";
    public static final String USER_NAME = "Bob";
    public static final String ORG_NAME = "TestOrg";
    public static final long USER_ID = 101;
    public static final long ORG_ID = 201;
    public static final String ROLE_0 = "Me";
    public static final String ROLE_1 = "You";
    public static final Set<String> roles = new HashSet<String>(){{
        add(ROLE_0);
        add(ROLE_1);
    }};

    public static String generateToken(boolean isExpired) throws Exception{
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime exp = isExpired ? now.minusMinutes(20) : now.plusMinutes(20);
        Date expiration = LegacyDateConverter.convertLocalDateTimeToDate(exp);
        return JWTUtil.generateNewToken(1, JWTUtil.combineUserNameOrgName(USER_NAME, ORG_NAME), 101, 201, SCHEMA_NAME, roles, expiration);
    }

}
