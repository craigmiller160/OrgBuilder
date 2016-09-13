package io.craigmiller160.orgbuilder.server.dto;

import io.craigmiller160.orgbuilder.server.data.jdbc.DaoTestUtils;
import io.craigmiller160.orgbuilder.server.rest.Role;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by craig on 9/13/16.
 */
public class UserDTOTest {

    private DaoTestUtils daoTestUtils = new DaoTestUtils();

    @Test
    public void testConvertStringToRoles(){
        String roles = "ADMIN,READ,WRITE";
        UserDTO user = new UserDTO();
        user.convertStringToRoles(roles);
        assertTrue("User does not have Admin role", user.getRoles().contains(Role.ADMIN));
        assertTrue("User does not have Write role", user.getRoles().contains(Role.WRITE));
        assertTrue("User does not have Read role", user.getRoles().contains(Role.READ));
    }

    @Test
    public void testConvertRolesToString(){
        UserDTO user1 = daoTestUtils.getUser1();
        String roles = user1.convertRolesToString();
        assertEquals("Roles converted to String are invalid", "ADMIN,READ,WRITE", roles);
    }

}
