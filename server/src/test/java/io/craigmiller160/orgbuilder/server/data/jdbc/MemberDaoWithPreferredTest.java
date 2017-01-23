package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A test class to test retrieving MemberDTOs
 * with the preferred contact DTOs joined with
 * it.
 *
 * IMPORTANT: These tests depend on the functionality
 * of several DAOs. If those DAOs fail any of their
 * unit tests, the tests here are likely to fail as well.
 *
 * Created by craig on 8/31/16.
 */
public class MemberDaoWithPreferredTest {

    private static final String TEST_SCHEMA_NAME = "test_member_preferred";
    private static final String RESET_MEMBER_AUTO_INC_SQL =
            "alter table old.members " +
            "auto_increment = 1000;";
    private static final String RESET_ADDRESS_AUTO_INC_SQL =
            "alter table addresses " +
            "auto_increment = 1;";
    private static final String RESET_PHONE_AUTO_INC_SQL =
            "alter table phones " +
            "auto_increment = 1;";
    private static final String RESET_EMAIL_AUTO_INC_SQL =
            "alter table emails " +
            "auto_increment = 1;";

    private static final DaoTestUtils daoTestUtils = new DaoTestUtils();
    private MemberDao memberDao;
    private AddressDao addressDao;
    private PhoneDao phoneDao;
    private EmailDao emailDao;

    @BeforeClass
    public static void init() throws Exception{
        daoTestUtils.initializeTestClass(TEST_SCHEMA_NAME, false);
    }

    @Before
    public void setUp() throws Exception{
        this.memberDao = daoTestUtils.prepareTestDao(MemberDao.class);
        this.addressDao = daoTestUtils.prepareTestDao(AddressDao.class);
        this.phoneDao = daoTestUtils.prepareTestDao(PhoneDao.class);
        this.emailDao = daoTestUtils.prepareTestDao(EmailDao.class);

        MemberDTO member1 = daoTestUtils.getMember1();
        AddressDTO address1 = daoTestUtils.getAddress1();
        address1.setPreferred(true);
        PhoneDTO phone1 = daoTestUtils.getPhone1();
        phone1.setPreferred(true);
        EmailDTO email1 = daoTestUtils.getEmail1();
        email1.setPreferred(true);
        this.memberDao.insert(member1);
        this.addressDao.insert(address1);
        this.phoneDao.insert(phone1);
        this.emailDao.insert(email1);

        MemberDTO member2 = daoTestUtils.getMember2();
        AddressDTO address2 = daoTestUtils.getAddress2();
        address2.setPreferred(true);
        PhoneDTO phone2 = daoTestUtils.getPhone2();
        phone2.setPreferred(true);
        EmailDTO email2 = daoTestUtils.getEmail2();
        email2.setPreferred(true);
        this.memberDao.insert(member2);
        this.addressDao.insert(address2);
        this.phoneDao.insert(phone2);
        this.emailDao.insert(email2);
    }

    @After
    public void cleanUp() throws Exception{
        daoTestUtils.cleanUpTest(RESET_MEMBER_AUTO_INC_SQL, RESET_ADDRESS_AUTO_INC_SQL, RESET_PHONE_AUTO_INC_SQL, RESET_EMAIL_AUTO_INC_SQL);
    }

    @AfterClass
    public static void tearDown() throws Exception{
        daoTestUtils.tearDownTestClass(TEST_SCHEMA_NAME);
    }

    @Test
    public void testGetSingle() throws Exception{
        MemberDTO member = memberDao.get(1000L);
        validateMember(member, 1000L);
    }

    @Test
    public void testGetAll() throws Exception{
        List<MemberDTO> members = memberDao.getAll();
        assertNotNull("Members list is null", members);
        assertEquals("Members list is the wrong size", 2, members.size());
        validateMember(members.get(0), 1000);
        validateMember(members.get(1), 1001);
    }

    private void validateMember(MemberDTO member, long memberId) throws Exception{
        assertNotNull(memberId + ": Member is null", member);
        AddressDTO address = member.getPreferredAddress();
        PhoneDTO phone = member.getPreferredPhone();
        EmailDTO email = member.getPreferredEmail();

        assertNotNull(memberId + "Preferred Address is null", address);
        assertNotNull(memberId + "Preferred Phone is null", phone);
        assertNotNull(memberId + "Preferred Email is null", email);
    }

}
