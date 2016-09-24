package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerTestUtils;
import org.junit.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by craigmiller on 9/24/16.
 */
public class SearchQueryTest {

    @BeforeClass
    public static void init() throws Exception{
        ServerCore serverCore = new ServerCore();
        serverCore.contextInitialized(null);
    }

    @Test
    public void testSearchQueryBuilder() throws Exception{
        String baseQuery = ServerTestUtils.getJdbcManager(ServerCore.getOrgDataManager()).getMappedQueries().get(MemberDao.class).get(JdbcManager.Query.SEARCH_BASE);
        SearchQuery searchQuery = new SearchQuery.Builder(baseQuery)
                .addParameter("a.foo", "Value")
                .build();
        String resultQuery = baseQuery + "WHERE a.foo = ?";
        assertEquals("Search Query invalid", resultQuery.trim(), searchQuery.getQuery().trim());
    }

}
