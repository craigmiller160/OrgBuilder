package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.dto.JoinedWithMemberDTO;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by craig on 9/4/16.
 */
public class MemberJoinDaoTestMethods<E extends JoinedWithMemberDTO<I>,I,D extends AbstractJdbcMemberJoinDao<E,I>> extends DaoTestMethods<E,I,D> {

    public MemberJoinDaoTestMethods(String elementName) {
        super(elementName);
    }

    public void testGetAllByMember(InsertManyElements insertFunction, D dao, long memberId, int expectedSize) throws Exception{
        insertFunction.insertMany();
        List<E> elements = dao.getAllByMember(memberId);
        assertEquals("Wrong number of " + elementName + " returned for member with ID " + memberId, expectedSize, elements.size());
    }

    public void testGetAllByMemberLimit(InsertManyElements insertFunction, D dao, long memberId, long offset, long size, I[] expectedIds) throws Exception{
        insertFunction.insertMany();
        List<E> elements = dao.getAllByMember(memberId, offset, size);
        assertEquals("Wrong number of " + elementName + " returned for member with ID " + memberId, size, elements.size());
        for(int i = 0; i < expectedIds.length; i++){
            assertEquals(elementName + " " + i + " is incorrect", expectedIds[i], elements.get(i).getElementId());
        }
    }

    public void testGetCountByMember(InsertManyElements insertFunction, D dao, long memberId, long expectedCount) throws Exception{
        insertFunction.insertMany();
        long count = dao.getCountByMember(memberId);
        assertEquals("Wrong count of " + elementName + " returned for member with ID " + memberId, expectedCount, count);
    }

    public void testGetPreferredForMember(E element, D dao, long memberId) throws Exception{
        element.setPreferred(true);
        E result1 = dao.insert(element);

        E result2 = dao.getPreferredForMember(memberId);
        assertNotNull("Preferred " + elementName + " for member " + memberId + " is null", result2);
        assertEquals(elementName + " returned does not equal expected", result1, result2);

    }

}
