package co.in.nnj.learn.jee.eduinst.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "department")
@NamedQuery(name = Department.FIND_ALL, query = "select d.id from Department d")
@NamedQuery(name = Department.FIND_BY_TYPE, query = "select d.id from Department d where d.function = :Funtion")
@NamedQuery(name = Department.FIND_BY_NAME, query = "select d.id from Department d where d.name = :DeptName")
@NamedQuery(name = Department.FIND_BY_TYPE_A, query = "select d from Department d where d.function = :Funtion")
@NamedQuery(name = Department.FIND_BY_NAME_A, query = "select d from Department d where d.name = :DeptName")
public class Department extends BaseEntity implements Serializable {
    public static final String FIND_ALL = "Dept.FIND_ALL";
    public static final String FIND_BY_TYPE = "Dept.FIND_BY_TYPE";
    public static final String FIND_BY_NAME = "Dept.FIND_BY_NAME";
    public static final String FIND_BY_TYPE_A = "Dept.FIND_BY_TYPE_A";
    public static final String FIND_BY_NAME_A = "Dept.FIND_BY_NAME_A";

    @Column(name = "name", unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private DepartmentType function;

    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    public DepartmentType getFunction() {
        return function;
    }
    public void setFunction(final DepartmentType function) {
        this.function = function;
    }
    @Override
    public String toString() {
        return "Department{id=" + id + ", name=" + name + ", function=" + function + "}";
    }

}
