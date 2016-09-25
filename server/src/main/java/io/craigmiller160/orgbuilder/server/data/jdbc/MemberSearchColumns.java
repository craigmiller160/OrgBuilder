package io.craigmiller160.orgbuilder.server.data.jdbc;

/**
 * Created by craigmiller on 9/24/16.
 */
public class MemberSearchColumns {

    public static final String FIRST_NAME = "m.first_name";
    public static final String MIDDLE_NAME = "m.middle_name";
    public static final String LAST_NAME = "m.last_name";
    public static final String GENDER = "m.gender";
    public static final String ADDRESS = "a.address";
    public static final String UNIT = "a.unit";
    public static final String CITY = "a.city";
    public static final String STATE = "a.state";
    public static final String ZIP_CODE = "a.zip_code";
    public static final String AREA_CODE = "p.area_code";
    public static final String PREFIX = "p.prefix";
    public static final String LINE_NUMBER = "p.line_number";
    public static final String EMAIL_ADDRESS = "e.email_address";
    public static final String ORDER_BY_CLAUSE = "ORDER BY m.member_id ASC";

}
