package io.craigmiller160.orgbuilder.server.dto;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craigmiller on 8/12/16.
 */
public class MemberDTO implements Comparable<MemberDTO>{

    private long memberId;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;

    private List<AddressDTO> addresses = new ArrayList<>();
    private List<PhoneDTO> phones = new ArrayList<>();
    private List<EmailDTO> emails = new ArrayList<>();

    public MemberDTO(){}

    public MemberDTO(String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender){
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
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

    public void setAddresses(List<AddressDTO> addresses){
        this.addresses = addresses != null ? addresses : new ArrayList<>();
    }

    public List<AddressDTO> getAddresses(){
        return addresses;
    }

    public void setPhones(List<PhoneDTO> phones){
        this.phones = phones != null ? phones : new ArrayList<>();
    }

    public List<PhoneDTO> getPhones(){
        return phones;
    }

    public void setEmails(List<EmailDTO> emails){
        this.emails = emails != null ? emails : new ArrayList<>();
    }

    public List<EmailDTO> getEmails(){
        return emails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberDTO memberDTO = (MemberDTO) o;

        if (memberId != memberDTO.memberId) return false;
        if (firstName != null ? !firstName.equals(memberDTO.firstName) : memberDTO.firstName != null) return false;
        if (middleName != null ? !middleName.equals(memberDTO.middleName) : memberDTO.middleName != null) return false;
        if (lastName != null ? !lastName.equals(memberDTO.lastName) : memberDTO.lastName != null) return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(memberDTO.dateOfBirth) : memberDTO.dateOfBirth != null)
            return false;
        return gender == memberDTO.gender;

    }

    @Override
    public int hashCode() {
        int result = (int) (memberId ^ (memberId >>> 32));
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return memberId + " - " + firstName + " " + (middleName != null ? middleName + " " : "") + lastName;
    }

    @Override
    public int compareTo(MemberDTO o) {
        return new Long(this.memberId).compareTo(o.memberId);
    }
}
