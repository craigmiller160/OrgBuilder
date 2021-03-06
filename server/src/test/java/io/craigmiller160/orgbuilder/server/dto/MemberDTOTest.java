package io.craigmiller160.orgbuilder.server.dto;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Period;

import static org.junit.Assert.assertEquals;

/**
 * Created by craig on 8/12/16.
 */
public class MemberDTOTest {

    /**
     * Test that the getAge() method calculates the correct age.
     */
    @Test
    public void testGetAge(){
        MemberDTO person = new MemberDTO();
        LocalDate birthDate = LocalDate.of(1988, 10, 26);
        person.setDateOfBirth(birthDate);
        LocalDate now = LocalDate.now();
        assertEquals("Person has incorrect age", Period.between(birthDate, now).getYears(), person.getAge());
    }

}
