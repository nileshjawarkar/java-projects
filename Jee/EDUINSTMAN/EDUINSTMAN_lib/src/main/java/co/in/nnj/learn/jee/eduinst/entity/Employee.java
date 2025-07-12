package co.in.nnj.learn.jee.eduinst.entity;

import java.io.Serializable;
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
@NamedQuery(name = Employee.FIND_ALL, query = "select e.id from Employee e")
@NamedQuery(name = Employee.FIND_BY_DEPT, query = "select e.id from Employee e where e.department = :Dept")
@NamedQuery(name = Employee.FIND_BY_DEPT_A, query = "select e from Employee e where e.department = :Dept")
@NamedQuery(name = Employee.FIND_BY_NAME_A, query = "select e from Employee e where e.fname = :FName and e.lname = :LName")
public class Employee extends Person implements Serializable {

    public static final String FIND_ALL = "Employee.FIND_ALL";
    public static final String FIND_BY_NAME_A = "Employee.FIND_BY_NAME_A";
    public static final String FIND_BY_DEPT = "Employee.FIND_BY_DEPT";
    public static final String FIND_BY_DEPT_A = "Employee.FIND_BY_DEPT_A";

    private String qualification;

    @Temporal(TemporalType.DATE)
    private Date doj;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public String getQualification() {
        return qualification;
    }

    public void setQualification(final String qualification) {
        this.qualification = qualification;
    }

    public Date getDoj() {
        return doj;
    }

    public void setDoj(final Date joiningDate) {
        this.doj = joiningDate;
    }

    @Override
    public String toString() {
        return super.toString() + " => Employee{qualification=" + qualification + ", doj=" + doj + ", department=" + department + "}";
    }
}
