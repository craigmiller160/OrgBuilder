package io.craigmiller160.orgbuilder.server.data.mysql;

import io.craigmiller160.orgbuilder.server.data.MemberJoins;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by craigmiller on 8/24/16.
 */
public class AddressDaoTest {

    private static final String TEST_SCHEMA_NAME = "test_address";
    private static final String RESET_ADDRESS_AUTO_INC_SQL =
            "ALTER TABLE addresses " +
            "AUTO_INCREMENT = 1;";
    private static final String RESET_MEMBER_AUTO_INC_SQL =
            "ALTER TABLE members " +
            "AUTO_INCREMENT = 1000;";

    private static final DaoTestUtils daoTestUtils = new DaoTestUtils();
    private AddressDao addressDao;

    @BeforeClass
    public static void init() throws Exception{
        daoTestUtils.initializeTestClass(TEST_SCHEMA_NAME, false);
    }

    @Before
    public void setUp() throws Exception{
        this.addressDao = daoTestUtils.prepareTestDao(AddressDao.class);
        MemberDao memberDao = daoTestUtils.prepareTestDao(MemberDao.class);
        MemberDTO member = daoTestUtils.getMember1();
        //If this fails, the issue is with the MemberDao
        memberDao.insert(member);
        member = daoTestUtils.getMember2();
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
        addressDao.update(address, address.getAddressId());
        address = addressDao.get(1L);
        assertNotNull("AddressDTO is null", address);
        assertEquals("AddressDTO has the wrong address value", newAddress, address.getAddress());
    }

    @Test
    public void testDeleteAndGet() throws Exception{
        testInsert();
        AddressDTO address1 = daoTestUtils.getAddress1();
        address1.setAddressId(1);
        AddressDTO address2 = addressDao.delete(1L);
        assertEquals("Deleted address is not the same as inserted address", address1, address2);
        address2 = addressDao.get(1L);
        assertNull("Address record was not deleted", address2);
    }

    @Test
    public void testCount() throws Exception{
        insertManyAddresses();
        long count = addressDao.getCount();
        assertEquals("Incorrect count of addresses", 15, count);
    }

    @Test
    public void testGetAll() throws Exception{
        insertManyAddresses();
        List<AddressDTO> addresses = addressDao.getAll();
        assertEquals("Get All returned the wrong number of addresses", 15, addresses.size());
    }

    @Test
    public void testGetAllLimit() throws Exception{
        insertManyAddresses();
        List<AddressDTO> addresses = addressDao.getAll(3, 3);
        assertEquals("Get All Limit returned the wrong number of addresses", 3, addresses.size());
        assertEquals("First returned address is incorrect", 4, addresses.get(0).getAddressId());
        assertEquals("Second returned address is incorrect", 5, addresses.get(1).getAddressId());
        assertEquals("Third returned address is incorrect", 6, addresses.get(2).getAddressId());
    }

    @Test
    public void testGetAllByMember() throws Exception{
        insertManyAddresses();
        List<AddressDTO> addresses = addressDao.getAllByMember(1001L);
        assertEquals("Wrong number of addresses returned for member with ID 1001", 5, addresses.size());
    }

    @Test
    public void testGetAllByMemberLimit() throws Exception{
        insertManyAddresses();
        List<AddressDTO> addresses = addressDao.getAllByMember(1001L, 1, 3);
        assertEquals("Wrong number of addresses returned for member with ID 1001", 3, addresses.size());
        assertEquals("First returned address is incorrect", 12, addresses.get(0).getAddressId());
        assertEquals("Second returned address is incorrect", 13, addresses.get(1).getAddressId());
        assertEquals("Third returned address is incorrect", 14, addresses.get(2).getAddressId());
    }

    @Test
    public void testGetCountByMember() throws Exception{
        insertManyAddresses();
        long count = addressDao.getCountByMember(1001L);
        assertEquals("Wrong count of addresses returned for member with ID 1001", 5, count);
    }

    /**
     * This method tests the reflective query() method invocation.
     *
     * The results apply to all DAOs that use this method.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testReflectiveQueryMethod() throws Exception{
        insertManyAddresses();
        List<AddressDTO> addresses = addressDao.query(MemberJoins.GET_ALL_BY_MEMBER, 1001L);
        assertNotNull("Addresses list is null", addresses);
        assertEquals("Wrong number of addresses returned for member with ID 1001", 5, addresses.size());
    }

    /**
     * This method tests the reflective query() method invocation.
     * Specifically, it tests an invocation where there are multiple
     * methods with the same name, but different parameters.
     *
     * The results apply to all DAOs that use this method.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testReflectiveQueryMethodWithParams() throws Exception{
        insertManyAddresses();
        List<AddressDTO> addresses = addressDao.query(MemberJoins.GET_ALL_BY_MEMBER, 1001L, 1, 3);
        assertNotNull("Addresses list is null", addresses);
        assertEquals("Wrong number of addresses returned for member with ID 1001", 3, addresses.size());
        assertEquals("First returned address is incorrect", 12, addresses.get(0).getAddressId());
        assertEquals("Second returned address is incorrect", 13, addresses.get(1).getAddressId());
        assertEquals("Third returned address is incorrect", 14, addresses.get(2).getAddressId());
    }

    private void insertManyAddresses() throws Exception{
        //10 of address1, tied to member1
        for(int i = 0; i < 10; i++){
            AddressDTO address = daoTestUtils.getAddress1();
            addressDao.insert(address);
        }

        //5 of address2, tied to member2
        for(int i = 0; i < 5; i++){
            AddressDTO address = daoTestUtils.getAddress2();
            addressDao.insert(address);
        }
    }

}
