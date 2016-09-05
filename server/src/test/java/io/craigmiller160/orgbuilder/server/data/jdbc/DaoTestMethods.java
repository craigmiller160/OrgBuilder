package io.craigmiller160.orgbuilder.server.data.jdbc;

import io.craigmiller160.orgbuilder.server.data.Dao;
import io.craigmiller160.orgbuilder.server.dto.DTO;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by craig on 9/4/16.
 */
public class DaoTestMethods<E extends DTO<I>,I,D extends Dao<E,I>> {

    protected String elementName;

    public DaoTestMethods(String elementName){
        this.elementName = elementName;
    }

    public void testInsert(E element, D dao, I expectedId) throws Exception{
        E result = dao.insert(element);
        assertEquals("Failed to insert " + elementName, expectedId, result.getElementId());
    }

    public void testUpdateAndGet(E element1, E element2, D dao, I expectedId) throws Exception{
        testInsert(element1, dao, expectedId);
        element2.setElementId(expectedId);
        dao.update(element2, expectedId);
        E result = dao.get(expectedId);
        assertNotNull(elementName + " is null", result);
        assertEquals(elementName + " is wrong", element2, result);
    }

    public void testDeleteAndGet(E element, D dao, I expectedId) throws Exception{
        testInsert(element, dao, expectedId);
        element.setElementId(expectedId);

        E result = dao.delete(expectedId);
        assertEquals("Deleted " + elementName + " is not the same as inserted " + elementName, element, result);
        result = dao.get(expectedId);
        assertNull(elementName + " record was not deleted", result);
    }

    public void testCount(InsertManyElements insertFunction, D dao, long expectedCount) throws Exception{
        insertFunction.insertMany();
        long count = dao.getCount();
        assertEquals("Incorrect count of " + elementName + " in database", expectedCount, count);
    }

    public void testGetAll(InsertManyElements insertFunction, D dao, long expectedSize) throws Exception{
        insertFunction.insertMany();

        List<E> elements = dao.getAll();
        assertNotNull("Get all " + elementName + " returned null collection", elements);
        assertEquals("Get all " + elementName + " returned wrong number of elements", expectedSize, elements.size());
    }

    public void testGetAllLimit(InsertManyElements insertFunction, D dao, long offset, long size, I[] expectedIds) throws Exception{
        insertFunction.insertMany();

        List<E> elements = dao.getAll(offset, size);
        assertNotNull("Get all " + elementName + " with limit returned null collection", elements);
        assertEquals("Get all " + elementName + " with limit returned wrong number of elements", size, elements.size());
        for(int i = 0; i < expectedIds.length; i++){
            assertEquals(elementName + " " + i + " is incorrect", expectedIds[i], elements.get(i).getElementId());
        }
    }

    public void testInsertOrUpdate(E elementToUpdate, E elementToInsert, D dao, I updateExpectedId, I insertExpectedId) throws Exception{
        testInsert(elementToInsert, dao, updateExpectedId);
        elementToUpdate.setElementId(updateExpectedId);
        dao.insertOrUpdate(elementToUpdate);

        E result = dao.get(updateExpectedId);
        assertNotNull(elementName + " update result is null", result);
        assertEquals(elementName + " insertOrUpdate did not update existing record", elementToUpdate, result);

        dao.insertOrUpdate(elementToInsert);
        elementToInsert.setElementId(insertExpectedId);
        result = dao.get(insertExpectedId);
        assertNotNull(elementName + " insert result is null", result);
        assertEquals(elementName + " insertOrUpdate did not insert new record", elementToInsert, result);
    }

    @FunctionalInterface
    public interface InsertManyElements {

        void insertMany() throws Exception;

    }

}
