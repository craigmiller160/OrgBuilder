package io.craigmiller160.orgbuilder.server.rest;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by craig on 9/10/16.
 */
public class LocalDateAdapter extends XmlAdapter<String,LocalDate> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    @Override
    public LocalDate unmarshal(String value) throws Exception {
        return LocalDate.parse(value, formatter);
    }

    @Override
    public String marshal(LocalDate value) throws Exception {
        return formatter.format(value);
    }
}
