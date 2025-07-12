package co.in.nnj.learn.jee.eduinst.entity;

import java.util.Date;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@MappedSuperclass
public class Person extends BaseEntity {
    private String fname;
    private String lname;
   
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Temporal(TemporalType.DATE)
    private Date dob;

    public String getFname() {
        return fname;
    }
    public void setFname(final String fname) {
        this.fname = fname;
    }
    public String getLname() {
        return lname;
    }
    public void setLname(final String lname) {
        this.lname = lname;
    }
    public Date getDob() {
        return dob;
    }
    public void setDob(final Date dob) {
        this.dob = dob;
    }
    @Override
    public String toString() {
        return "Person{id=" + id.toString() + ", fname=" + fname + ", lname=" + lname + ", address=" + address + ", dob=" + dob + "}";
    }
}
