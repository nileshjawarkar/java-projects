package co.in.nnj.learn.jee.eduinst.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import co.in.nnj.learn.jee.eduinst.entity.Department;
import co.in.nnj.learn.jee.eduinst.entity.Employee;

@Stateless
public class EmployeeRepository implements Repository<Employee, UUID> {
    @PersistenceContext
    EntityManager em;

    @Override
    public Optional<Employee> create(final Employee obj) {
        if (obj == null) {
            Optional.empty();
        }
        em.persist(obj);
        return Optional.of(obj);
    }

    @Override
    public Optional<Employee> find(final UUID id) {
        return Optional.ofNullable(em.find(Employee.class, id));
    }

    @Override
    public List<UUID> findAllIds(final String attr, final String value) {
        if (attr == null) {
            return em.createNamedQuery(Employee.FIND_ALL, UUID.class)
                    .getResultList();
        } else if ("dept".equals(attr)) {
            final Department dept = new Department();
            dept.setId(UUID.fromString(value));
            return em.createNamedQuery(Employee.FIND_BY_DEPT, UUID.class)
                    .setParameter("Dept", dept)
                    .getResultList();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Employee> findAll(final String attr, final String value) {
        if ("dept".equals(attr)) {
            final Department dept = new Department();
            dept.setId(UUID.fromString(value));
            return em.createNamedQuery(Employee.FIND_BY_DEPT_A, Employee.class)
                    .setParameter("Dept", dept)
                    .getResultList();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean update(final Employee obj) {
        return find(obj.getId()).map(e -> {
            if (obj.getFname() != null) {
                e.setFname(obj.getFname());
            }
            if (obj.getLname() != null) {
                e.setLname(obj.getLname());
            }
            if (obj.getDob() != null) {
                e.setDob(obj.getDob());
            }
            if (obj.getDoj() != null) {
                e.setDoj(obj.getDoj());
            }
            if (obj.getQualification() != null) {
                e.setQualification(obj.getQualification());
            }
            em.merge(e);
            return true;
        }).orElse(false);
    }

    @Override
    public boolean delete(final UUID id) {
        return find(id).map(e -> {
            em.remove(e);
            return true;
        }).orElse(false);
    }

    public List<UUID> findByName(final String fname, final String lname) {
        return em.createNamedQuery(Employee.FIND_BY_DEPT_A, UUID.class)
                .setParameter("FName", fname)
                .setParameter("LName", lname).getResultList();
    }
}
