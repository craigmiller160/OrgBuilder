package io.craigmiller160.orgbuilder.server.data;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerProps;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by craig on 8/20/16.
 */
public class OrgDataSource {

    private static final String MYSQL_USE_SSL_PROP = "useSSL";
    private static final String MYSQL_REQUIRE_SSL_PROP = "requireSSL";
    private static final String MYSQL_TRUST_STORE_URL_PROP = "trustCertificateKeyStoreUrl";
    private static final String MYSQL_TRUST_STORE_TYPE_PROP = "trustCertificateKeyStoreType";
    private static final String MYSQL_TRUST_STORE_PASS_PROP = "trustCertificateKeyStorePassword";

    private final BasicDataSource dataSource;

    public OrgDataSource(Properties serverProps){
        dataSource = new BasicDataSource();
        String clazz = ServerCore.getProperty(ServerProps.DB_CLASS_PROP);
        String url = ServerCore.getProperty(ServerProps.DB_URL_PROP);
        String user = ServerCore.getProperty(ServerProps.DB_USER_PROP);
        String pass = ServerCore.getProperty(ServerProps.DB_PASS_PROP);
        String initString = ServerCore.getProperty(ServerProps.POOL_INIT_SIZE_PROP);
        String maxString = ServerCore.getProperty(ServerProps.POOL_MAX_SIZE_PROP);
        int init = StringUtils.isNumeric(initString) ? Integer.parseInt(initString) : -1;
        int max = StringUtils.isNumeric(maxString) ? Integer.parseInt(maxString) : -1;

        dataSource.setDriverClassName(clazz);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(pass);
        if(init >= 0){
            dataSource.setInitialSize(init);
        }

        if(max > 0){
            dataSource.setMaxTotal(max);
            dataSource.setMaxIdle(max);
        }

        if(Boolean.parseBoolean(serverProps.getProperty(ServerProps.USE_SSL_PROP))){
            dataSource.addConnectionProperty(MYSQL_USE_SSL_PROP, "true");
            dataSource.addConnectionProperty(MYSQL_REQUIRE_SSL_PROP, "true");
            URL keyStoreUrl = OrgDataSource.class.getClassLoader().getResource(serverProps.getProperty(ServerProps.KEYSTORE_PATH));
            dataSource.addConnectionProperty(MYSQL_TRUST_STORE_URL_PROP, keyStoreUrl.toString());
            dataSource.addConnectionProperty(MYSQL_TRUST_STORE_TYPE_PROP, serverProps.getProperty(ServerProps.KEYSTORE_TYPE));
            dataSource.addConnectionProperty(MYSQL_TRUST_STORE_PASS_PROP, serverProps.getProperty(ServerProps.KEYSTORE_PASS));
        }
    }

    public synchronized Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public synchronized void closeDataSource() throws SQLException{
        dataSource.close();
    }

    public static void main(String[] args) throws Exception{
        URL url = OrgDataSource.class.getClassLoader().getResource("io/craigmiller160/orgbuilder/server/keys/orgKeyStore.jceks");
        System.out.println(url);
    }

}
