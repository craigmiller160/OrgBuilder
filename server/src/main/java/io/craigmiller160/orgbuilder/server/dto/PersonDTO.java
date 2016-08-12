package io.craigmiller160.orgbuilder.server.dto;

import java.time.LocalDate;
import java.time.Period;

/**
 * Created by craigmiller on 8/12/16.
 */
public class PersonDTO implements Comparable<PersonDTO>{

    private long personId;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public int getAge(){
        if(dateOfBirth == null){
            return 0;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonDTO personDTO = (PersonDTO) o;

        if (personId != personDTO.personId) return false;
        if (firstName != null ? !firstName.equals(personDTO.firstName) : personDTO.firstName != null) return false;
        if (middleName != null ? !middleName.equals(personDTO.middleName) : personDTO.middleName != null) return false;
        if (lastName != null ? !lastName.equals(personDTO.lastName) : personDTO.lastName != null) return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(personDTO.dateOfBirth) : personDTO.dateOfBirth != null)
            return false;
        return gender == personDTO.gender;

    }

    @Override
    public int hashCode() {
        int result = (int) (personId ^ (personId >>> 32));
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return personId + " - " + firstName + " " + (middleName != null ? middleName + " " : "") + lastName;
    }

    @Override
    public int compareTo(PersonDTO o) {
        return new Long(this.personId).compareTo(o.personId);
    }
}
