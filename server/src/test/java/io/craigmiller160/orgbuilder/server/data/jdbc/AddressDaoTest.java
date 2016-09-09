package io.craigmiller160.orgbuilder.server.data.jdbc;

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
import static org.junit.Assert.assertTrue;

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
    private final MemberJoinDaoTestMethods<AddressDTO,Long,AddressDao> daoTestMethods;

    public AddressDaoTest(){
        this.daoTestMethods = new MemberJoinDaoTestMethods<>(AddressDTO.class.getSimpleName());
    }

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
        daoTestMethods.testInsert(address, addressDao, 1L);
    }

    @Test
    public void testUpdateAndGet() throws Exception{
        AddressDTO address1 = daoTestUtils.getAddress1();
        AddressDTO address2 = daoTestUtils.getAddress2();
        daoTestMethods.testUpdateAndGet(address1, address2, addressDao, 1L);
    }

    @Test
    public void testDeleteAndGet() throws Exception{
        AddressDTO address1 = daoTestUtils.getAddress1();
        daoTestMethods.testDeleteAndGet(address1, addressDao, 1L);
    }

    @Test
    public void testCount() throws Exception{
        daoTestMethods.testCount(this::insertManyAddresses, addressDao, 15);
    }

    @Test
    public void testGetAll() throws Exception{
        daoTestMethods.testGetAll(this::insertManyAddresses, addressDao, 15);
    }

    @Test
    public void testGetAllLimit() throws Exception{
        daoTestMethods.testGetAllLimit(this::insertManyAddresses, addressDao, 3, 3, new Long[] {4L, 5L, 6L});
    }

    @Test
    public void testGetAllByMember() throws Exception{
        daoTestMethods.testGetAllByMember(this::insertManyAddresses, addressDao, 1001L, 5);
    }

    @Test
    public void testGetAllByMemberLimit() throws Exception{
        daoTestMethods.testGetAllByMemberLimit(this::insertManyAddresses, addressDao, 1001L, 1, 3, new Long[]{12L, 13L, 14L});
    }

    @Test
    public void testGetCountByMember() throws Exception{
        daoTestMethods.testGetCountByMember(this::insertManyAddresses, addressDao, 1001, 5);
    }

    @Test
    public void testGetPreferredForMember() throws Exception{
        AddressDTO address1 = daoTestUtils.getAddress1();
        daoTestMethods.testGetPreferredForMember(address1, addressDao, 1000);
    }

    @Test
    public void testDeleteByMember() throws Exception{
        AddressDTO address1 = daoTestUtils.getAddress1();
        daoTestMethods.testDeleteByMember(address1, addressDao, 1000, 1);
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
        List<AddressDTO> addresses = (List<AddressDTO>) addressDao.query(MemberJoins.GET_ALL_BY_MEMBER, 1001L);
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
        List<AddressDTO> addresses = (List<AddressDTO>) addressDao.query(MemberJoins.GET_ALL_BY_MEMBER, 1001L, 1, 3);
        assertNotNull("Addresses list is null", addresses);
        assertEquals("Wrong number of addresses returned for member with ID 1001", 3, addresses.size());
        assertTrue("First returned address is incorrect", 12L == addresses.get(0).getElementId());
        assertTrue("Second returned address is incorrect", 13L == addresses.get(1).getElementId());
        assertTrue("Third returned address is incorrect", 14L == addresses.get(2).getElementId());
    }

    @Test
    public void testInsertOrUpdate() throws Exception{
        AddressDTO addressToUpdate = daoTestUtils.getAddress1();
        AddressDTO addressToInsert = daoTestUtils.getAddress2();
        daoTestMethods.testInsertOrUpdate(addressToUpdate, addressToInsert, addressDao, 1L, 2L);
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
