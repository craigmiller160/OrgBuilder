package io.craigmiller160.orgbuilder.server.rest;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by craig on 10/17/16.
 */
public class LocalDateTimeAdapter extends XmlAdapter<String,LocalDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime unmarshal(String value) throws Exception {
        return LocalDateTime.parse(value, formatter);
    }

    @Override
    public String marshal(LocalDateTime value) throws Exception {
        return formatter.format(value);
    }
}
