package io.craigmiller160.orgbuilder.server;

/**
 * Created by craig on 8/13/16.
 */
public class ServerProps {

    public static final String DB_URL_PROP = "io.craigmiller160.orgbuilder.server.data.dbUrl";
    public static final String DB_CLASS_PROP = "io.craigmiller160.orgbuilder.server.data.dbClass";
    public static final String DB_USER_PROP = "io.craigmiller160.orgbuilder.server.data.dbUser";
    public static final String DB_PASS_PROP = "io.craigmiller160.orgbuilder.server.data.dbPass";

    public static final String POOL_INIT_SIZE_PROP = "io.craigmiller160.orgbuilder.server.data.poolInitSize";
    public static final String POOL_MAX_SIZE_PROP = "io.craigmiller160.orgbuilder.server.data.poolMaxSize";

    public static final String DEV_EMAIL = "io.craigmiller160.orgbuilder.server.devEmail";

    public static final String KEYSTORE_PATH = "io.craigmiller160.orgbuilder.server.keystore.ath";
    public static final String KEYSTORE_PASS = "io.craigmiller160.orgbuilder.server.keystore.pass";

    public static final String ACCESS_EXP_MINS = "io.craigmiller160.orgbuilder.server.token.accessExpMins";
    public static final String REFRESH_EXP_MINS = "io.craigmiller160.orgbuilder.server.token.refreshExpMins";
    public static final String REFRESH_MAX_EXP_HRS = "io.craigmiller160.orgbuilder.server.token.refreshMaxExpHrs";

}
