package co.in.nnj.learn.jee.port.output.entity;

import java.util.Date;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "employee")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NamedQuery(name = EmployeeEntity.FIND_ALL, query = "select e from EmployeeEntity e")
@NamedQuery(name = EmployeeEntity.FIND_BY, query = "select e from EmployeeEntity e where e.fname = :FName")
public class EmployeeEntity extends BaseEntity {
    public static final String FIND_ALL = "Employee.FIND_ALL";
    public static final String FIND_BY = "Employee.FIND_BY_NAME";

    private String fname;
    private String lname;
    private String qualification;

    @Temporal(TemporalType.DATE)
    private Date joiningDate;

    @Temporal(TemporalType.DATE)
    private Date dob;

    @ManyToOne
    @JoinColumn(name = "deparment_id")
    private DepartmentEntity department;

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
}
