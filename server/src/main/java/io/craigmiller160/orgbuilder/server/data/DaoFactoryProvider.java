package io.craigmiller160.orgbuilder.server.data;

/**
 * Created by craig on 8/10/16.
 */
public class DaoFactoryProvider {

    //TODO move this to a properties file
    private static final String DEFAULT_FACTORY = "io.craigmiller160.orgbuilder.server.data.dbutils.DbUtilsDaoFactory";

    public static DaoFactory newDefaultFactory(){
        return newFactory(DEFAULT_FACTORY);
    }

    public static DaoFactory newFactory(String className){
        DaoFactory daoFactory = null;
        try{
            Class<?> clazz = Class.forName(className);
            if(clazz != null && clazz.isAssignableFrom(DaoFactory.class)){
                daoFactory = (DaoFactory) clazz.newInstance();
            }
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException ex){
            //TODO handle this
        }

        return daoFactory;
    }

}
