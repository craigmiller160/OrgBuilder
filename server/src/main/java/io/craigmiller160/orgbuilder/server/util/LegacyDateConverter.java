package io.craigmiller160.orgbuilder.server.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * A utility class to facilitate converting
 * the new Java 8 LocalDateTime class to the
 * legacy java.util.Date class.
 *
 * Created by craig on 10/1/16.
 */
public class LegacyDateConverter {

    //TODO consider if this will screw things up if the server is accessed from a totally different time zone... given that the client side logic is checking the timestamp...

    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

}
