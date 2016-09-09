package io.craigmiller160.orgbuilder.server.data;

import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * An abstract implementation of the Dao interface.
 * It provides an implementation for the query()
 * method, which uses reflection to identify which
 * method to invoke.
 *
 * Created by craig on 8/11/16.
 *
 * @param <E> the type of the element this DAO persists.
 * @param <I> the type of the primary key, or ID, of the
 *           element this DAO persists.
 */
public abstract class AbstractDao<E,I> implements Dao<E,I> {

    @Override
    public Object query(String queryName, Object... params) {
        Object result = null;
        try{
            result = MethodUtils.invokeMethod(this, queryName, params);
        }
        catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException ex){
            OrgApiLogger.getDataLogger().error("Unable to invoke query method. QueryName: " + queryName + " Params: " + Arrays.toString(params), ex);
        }

        return result;
    }
}
