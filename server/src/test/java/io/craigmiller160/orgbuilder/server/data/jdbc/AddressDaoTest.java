package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.State;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by craigmiller on 8/24/16.
 */
public class AddressDaoTest {

    private static final String TEST_SCHEMA_NAME = "test_address";
    private static final String RESET_ADDRESS_AUTO_INC_SQL =
            "ALTER TABLE addresses " +
            "AUTO_INCREMENT = 0;";
    private static final String RESET_MEMBER_AUTO_INC_SQL =
            "ALTER TABLE members " +
            "AUTO_INCREMENT = 1000;";

    private static final DaoTestUtils daoTestUtils = new DaoTestUtils();
    private AddressDao addressDao;

    @BeforeClass
    public static void init() throws Exception{
        daoTestUtils.initializeTestClass(TEST_SCHEMA_NAME);
    }

    @Before
    public void setUp() throws Exception{
        this.addressDao = daoTestUtils.prepareTestDao(AddressDao.class);
        MemberDao memberDao = daoTestUtils.prepareTestDao(MemberDao.class);
        MemberDTO member = daoTestUtils.getMember1();
        //If this fails, the issue is with the MemberDao
        memberDao.insert(member);
    }

    @After
    public void cleanUp() throws Exception{
        daoTestUtils.cleanUpTest(RESET_ADDRESS_AUTO_INC_SQL, RESET_MEMBER_AUTO_INC_SQL);
    }

    @AfterClass
    public static void tearDown() throws Exception{
        daoTestUtils.tearDownTestClass(TEST_SCHEMA_NAME);
    }

    @Test
    public void testInsert() throws Exception{
        AddressDTO address = daoTestUtils.getAddress1();
        address = addressDao.insert(address);
        assertEquals("Failed to insert address", 1, address.getAddressId());
    }

    @Test
    public void testUpdateAndGet() throws Exception{
        String newAddress = "10306 Casa Palarmo Dr";
        testInsert();
        AddressDTO address = daoTestUtils.getAddress1();
        address.setAddressId(1);
        address.setAddress(newAddress);
        addressDao.update(address);
        address = addressDao.get(1L);
        assertNotNull("AddressDTO is null", address);
        assertEquals("AddressDTO has the wrong address value", newAddress, address.getAddress());
    }

}
