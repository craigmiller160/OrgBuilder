package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
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
 * Created by craig on 8/21/16.
 */
public class OrgDaoTest {

    private static final String TEST_SCHEMA_NAME = "test_org";

    private static final String REST_AUTO_INC_SQL =
            "alter table orgs " +
                    "auto_increment = 1;";

    private static final DaoTestUtils daoTestUtils = new DaoTestUtils();
    private OrgDao orgDao;

    @BeforeClass
    public static void init() throws Exception{
        daoTestUtils.initializeTestClass(TEST_SCHEMA_NAME, true);
    }

    @Before
    public void setUp() throws Exception{
        this.orgDao = daoTestUtils.prepareTestDao(OrgDao.class);
    }

    @After
    public void cleanUp() throws Exception{
        daoTestUtils.cleanUpTest(REST_AUTO_INC_SQL);
    }

    @AfterClass
    public static void tearDown() throws Exception{
        daoTestUtils.tearDownTestClass(TEST_SCHEMA_NAME);
    }

    @Test
    public void testInsert() throws Exception{
        OrgDTO org = daoTestUtils.getOrg1();
        org = orgDao.insert(org);
        assertEquals("Failed to insert OrgDTO", 1, org.getOrgId());
    }

    @Test
    public void testUpdateAndGet() throws Exception{
        testInsert();
        OrgDTO org = daoTestUtils.getOrg1();
        org.setOrgName("Org2");
        org.setOrgId(1L);
        orgDao.update(org, org.getOrgId());
        OrgDTO memberDTO2 = orgDao.get(1L);
        assertNotNull("OrgDTO is null", memberDTO2);
        assertEquals("OrgDTO has the wrong org name", "Org2", memberDTO2.getOrgName());
    }

    @Test
    public void testDeleteAndGet() throws Exception{
        testInsert();
        OrgDTO org1 = daoTestUtils.getOrg1();
        org1 = orgDao.get(1L);
        OrgDTO org2 = orgDao.delete(1L);
        assertEquals("Deleted OrgDTO is not the same as inserted OrgDTO", org1, org2);
        org2 = orgDao.get(1L);
        assertNull("OrgDTO record was not deleted", org2);
    }

    @Test
    public void testCount() throws Exception{
        testInsert();
        long count = orgDao.getCount();
        assertEquals("Incorrect count of OrgDTO in database", 1, count);
    }

    @Test
    public void testGetAll() throws Exception{
        insertManyOrgs();

        List<OrgDTO> orgs = orgDao.getAll();
        assertEquals("Get All returned the wrong number of orgs", 10, orgs.size());
    }

    @Test
    public void testGetAllLimit() throws Exception{
        insertManyOrgs();

        List<OrgDTO> orgs = orgDao.getAll(3, 3);
        assertEquals("Get All Limit returned the wrong number of orgs", 3, orgs.size());
        assertEquals("First returned org is incorrect", 4, orgs.get(0).getOrgId());
        assertEquals("Second returned org is incorrect", 5, orgs.get(1).getOrgId());
        assertEquals("Third returned org is incorrect", 6, orgs.get(2).getOrgId());
    }

    private void insertManyOrgs() throws Exception{
        for(int i = 0; i < 10; i++){
            OrgDTO org = daoTestUtils.getOrg1();
            orgDao.insert(org);
        }
    }
}
