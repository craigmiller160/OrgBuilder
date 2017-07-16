package io.craigmiller160.orgbuilder.server.rest;

import com.nimbusds.jwt.SignedJWT;
import io.craigmiller160.orgbuilder.server.ServerCore;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Base64;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static io.craigmiller160.orgbuilder.server.rest.TokenTestUtils.*;

/**
 * Created by craig on 10/2/16.
 */
public class JWTUtilTest {

    private static ServerCore serverCore;

    @BeforeClass
    public static void setUp(){
        serverCore = new ServerCore();
        serverCore.contextInitialized(null);
    }

    @AfterClass
    public static void tearDown(){
        serverCore.contextDestroyed(null);
    }

    @Test
    public void testValidate_Unexpired_ValidSig() throws Exception{
        String token = generateToken(false);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        boolean result = JWTUtil.isTokenExpired(jwt);
        assertFalse("Token shouldn't be expired", result);
    }

    @Test
    public void testValidate_Expired_ValidSig() throws Exception{
        String token = generateToken(true);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        boolean result = JWTUtil.isTokenExpired(jwt);
        assertTrue("Token should be expired", result);
    }

    @Test
    public void testGetTokenSchemaClaim() throws Exception{
        String token = generateToken(false);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        String schema = JWTUtil.getTokenSchemaClaim(jwt);
        assertNotNull("Token schema claim wasn't returned", schema);
        assertEquals("Wrong schema claim value returned", SCHEMA_NAME, schema);
    }

    @Test
    public void testGetTokenRolesClaim() throws Exception{
        String token = generateToken(false);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        Set<String> result = JWTUtil.getTokenRolesClaim(jwt);
        assertNotNull("Token roles claim wasn't returned", result);
        assertEquals("Wrong number of roles returned from token", 2, result.size());
        for(String r : result){
            assertTrue("Roles doesn't contain returned role", roles.contains(r));
        }
    }

    @Test
    public void testGetTokenSubjectClaim() throws Exception{
        String token = generateToken(false);
        String actualSubject = JWTUtil.combineUserNameOrgName(USER_NAME, ORG_NAME);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        String subject = JWTUtil.getTokenSubjectClaim(jwt);
        assertNotNull("Token subject claim wasn't returned", subject);
        assertEquals("Wrong subject claim value returned", actualSubject, subject);
    }

    @Test
    public void testGetTokenIdClaim() throws Exception{
        String token = generateToken(false);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        long tokenId = JWTUtil.getTokenIdClaim(jwt);
        assertTrue("Token ID claim wasn't returned", tokenId > 0);
        assertEquals("Wrong Token ID claim value returned", tokenId, 1);
    }

    @Test
    public void testGetUserIdClaim() throws Exception{
        String token = generateToken(false);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        long userId = JWTUtil.getTokenUserIdClaim(jwt);
        assertTrue("Token userId claim wasn't returned", userId > 0);
        assertEquals("Wrong Token userId claim value returned", userId, USER_ID);
    }

    @Test
    public void testGetOrgIdClaim() throws Exception{
        String token = generateToken(false);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        long orgId = JWTUtil.getTokenOrgIdClaim(jwt);
        assertTrue("Token orgId claim wasn't returned", orgId > 0);
        assertEquals("Wrong Token orgId claim value returned", orgId, ORG_ID);
    }

    @Test
    public void testGetUserNameClaim() throws Exception{
        String token = generateToken(false);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        String userName = JWTUtil.getTokenUserNameClaim(jwt);
        assertEquals("Wrong Token userName claim value returned", USER_NAME, userName);
    }

    @Test
    public void testGetOrgNameClaim() throws Exception{
        String token = generateToken(false);

        SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(token);
        assertNotNull("Token wasn't validated and returned", jwt);

        String orgName = JWTUtil.getTokenOrgNameClaim(jwt);
        assertEquals("Wrong Token orgName claim value returned", ORG_NAME, orgName);
    }

    @Test
    public void testSplitSubject() throws Exception{
        String user = "User";
        String org = "Org";
        String subject1 = user + "::" + org;

        //Two valid entries
        String[] sub1Split = JWTUtil.splitUserNameOrgName(subject1);
        assertEquals("Subject1 split user invalid", user, sub1Split[0]);
        assertEquals("Subject1 split org invalid", org, sub1Split[1]);

        //Only user entry
        String subject2 = user + "::";
        String[] sub2Split = JWTUtil.splitUserNameOrgName(subject2);
        assertEquals("Subject2 split user invalid", user, sub2Split[0]);
        assertEquals("Subject2 split org invalid", "", sub2Split[1]);

        //No valid entries
        String subject3 = "";
        String[] sub3Split = JWTUtil.splitUserNameOrgName(subject3);
        assertEquals("Subject3 split user invalid", "", sub3Split[0]);
        assertEquals("Subject3 split org invalid", "", sub3Split[1]);

        //No valid entries, but with "::"
        String subject4 = "::";
        String[] sub4Split = JWTUtil.splitUserNameOrgName(subject4);
        assertEquals("Subject4 split user invalid", "", sub4Split[0]);
        assertEquals("Subject4 split org invalid", "", sub4Split[1]);

        //Only org entry
        String subject5 = "::" + org;
        String[] sub5Split = JWTUtil.splitUserNameOrgName(subject5);
        assertEquals("Subject5 split user invalid", "", sub5Split[0]);
        assertEquals("Subject5 split org invalid", org, sub5Split[1]);
    }

    //TODO delete this
    public static void main(String[] args){
        String s = "eyJzdWIiOiJjcmFpZ0BnbWFpbC5jb206Ok5PTkUiLCJ1aWQiOjEsInVubSI6ImNyYWlnQGdtYWlsLmNvbSIsInNtYSI6Im9yZ19hcHAiLCJpc3MiOiJPcmdCdWlsZGVyIEFQSSIsIm9pZCI6MCwiZXhwIjoxNDkwMjA0NTk2LCJpYXQiOjE0OTAyMDMzOTYsImp0aSI6IjI2Iiwicm9sIjpbIk1BU1RFUiJdLCJvbm0iOiIifQ";
        String s2 = "eyJhbGciOiJSUzI1NiJ9";
        String s3 = "h8RtRKL51FU3IE8hO2YYkwhFfk8mPLRFWonjzeMBPHs4mLfIDZSaHt0XxRVshpZtNPiZfm1xOZA6v0pI4aFFz3WMb_7qyGV6BsWF5DIXBuAjtAySclRqvKVuee7bWdJS8snZulw604KP06uVWN3MgeK3EYYnuPnPnxqRIzA-ItZJb2uBc--EKhYilYRZ1GfcMwRH5AwfdIqTVfku7GOi5HNr6zQry617VYaayNUWmALlVdXq4VegtBSDTKyTt8_yjyrLP47YGyNXOFBTaJ0rereZGgQikwd8YA4-b1rj-RpfUdI3p8U8LeN1M53k1A2tkoEGog8pEtOso2Sk_-PLIT-HIMny-JpcGpBgq19kWYmFWtcy_IzLBO0BM5mjRgJ4CGCePju5upO1FKnNxooDGpILVgfjpFnB7nqHGQz7DKXohrGus2oWtQTausyHL56KtKLMua7vU1gsy1MX5IKLsQFI4YR4sIk_eUIHmq5IY2Ao8Oilo2zzTuuHNjJuWJ1NRyK4-Ryj2EeLmAlo3ICcG8SzEi3jfMZvWOsiZdfFsUGQBWVcMa71qpmCn3uKNvNDLp9xKpBEjExOgTqcFQzTpzsdPN3YOEU7WqiicnYmLRT3f8tFgOyXWUpMH9CSO2II6zhKpLYQFTT228uFW4nhUqM4WABiNq3zCdCM26MF2FE";
        byte[] bytes = Base64.getDecoder().decode(s3.getBytes());
        System.out.println(new String(bytes));

    }

}
