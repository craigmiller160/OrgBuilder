package io.craigmiller160.orgbuilder.server.dto;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Period;

import static org.junit.Assert.assertEquals;

/**
 * Created by craig on 8/12/16.
 */
public class PersonDTOTest {

    /**
     * Test that the getAge() method calculates the correct age.
     */
    @Test
    public void testGetAge(){
        PersonDTO person = new PersonDTO();
        LocalDate birthDate = LocalDate.of(1988, 10, 26);
        person.setDateOfBirth(birthDate);
        LocalDate now = LocalDate.of(2016, 8, 11);
        assertEquals("Person has incorrect age", Period.between(birthDate, now).getYears(), person.getAge());
    }

}
