package co.in.nnj.learn.jee.adapter.output.db.entity;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "employee")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NamedQuery(name = EmployeeEntity.FIND_ALL, query = "select e.id from EmployeeEntity e")
@NamedQuery(name = EmployeeEntity.FIND_BY_DEPT, query = "select e.id from EmployeeEntity e where e.department = :Dept")
@NamedQuery(name = EmployeeEntity.FIND_BY_NAME, query = "select e.id from EmployeeEntity e where e.fname = :FName and e.lname = :LName")
public class EmployeeEntity extends BaseEntity {
    public static final String FIND_ALL = "Employee.FIND_ALL";
    public static final String FIND_BY_NAME = "Employee.FIND_BY_NAME";
    public static final String FIND_BY_DEPT = "Employee.FIND_BY_DEPT";

    private String fname;
    private String lname;
    private String qualification;

    @Temporal(TemporalType.DATE)
    private Date joiningDate;

    @Temporal(TemporalType.DATE)
    private Date dob;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AddressEntity paddress;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AddressEntity caddress;

    public AddressEntity getPAddress() {
        return paddress;
    }

    public void setPAddress(final AddressEntity address) {
        this.paddress = address;
    }

    public AddressEntity getCAddress() {
        return caddress;
    }

    public void setCAddress(final AddressEntity address) {
        this.caddress = address;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }

    public void setDepartment(final DepartmentEntity department) {
        this.department = department;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(final String qualification) {
        this.qualification = qualification;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(final Date dob) {
        this.dob = dob;
    }

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

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(final Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    @Override
    public String toString() {
        return "EmployeeEntity{fname=" + fname + ", lname=" + lname + ", qualification=" + qualification
                + ", job=" + joiningDate + ", dob=" + dob + ", department=" + department + ", paddress="
                + paddress + "}";
    }

}
