package co.in.nnj.learn.jee.adapter.output.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import co.in.nnj.learn.jee.domain.valueobjects.DepartmentType;

@Entity
@Table(name = "department")
@NamedQuery(name = DepartmentEntity.FIND_ALL, query = "select d.id from DepartmentEntity d")
@NamedQuery(name = DepartmentEntity.FIND_BY_TYPE, query = "select d.id from DepartmentEntity d where d.function = :Funtion")
@NamedQuery(name = DepartmentEntity.FIND_BY_NAME, query = "select d.id from DepartmentEntity d where d.name = :DeptName")
public class DepartmentEntity extends BaseEntity {
    public static final String FIND_ALL = "Dept.FIND_ALL";
    public static final String FIND_BY_TYPE = "Dept.FIND_BY_TYPE";
    public static final String FIND_BY_NAME = "Dept.FIND_BY_NAME";

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
        return "DepartmentEntity{name=" + name + ", getId()=" + getId() + "}";
    }

}
