package co.in.nnj.learn.jee.eduinst.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import co.in.nnj.learn.jee.eduinst.entity.Department;
import co.in.nnj.learn.jee.eduinst.entity.DepartmentType;

@Stateless
public class DepartmentRepository implements Repository<Department, UUID> {
    @PersistenceContext
    EntityManager em;

    @Override
    public Optional<Department> create(final Department obj) {
        if (obj == null) {
            Optional.empty();
        }
        em.persist(obj);
        return Optional.of(obj);
    }

    @Override           
    public Optional<Department> find(final UUID id) {
        return Optional.ofNullable(em.find(Department.class, id));
    }

    @Override
    public List<UUID> findAllIds(final String attr, final String value) {
        if (attr == null) {
            return em.createNamedQuery(Department.FIND_ALL, UUID.class)
                    .getResultList();
        } else if ("function".equals(attr)) {
            return em.createNamedQuery(Department.FIND_BY_TYPE, UUID.class)
                    .setParameter("Funtion", DepartmentType.valueOf(value))
                    .getResultList();
        } else if ("name".equals(attr)) {
            return em.createNamedQuery(Department.FIND_BY_NAME, UUID.class)
                    .setParameter("DeptName", value)
                    .getResultList();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Department> findAll(final String attr, final String value) {
        if ("function".equals(attr)) {
            return em.createNamedQuery(Department.FIND_BY_TYPE_A, Department.class)
                    .setParameter("Funtion", DepartmentType.valueOf(value))
                    .getResultList();
        } else if ("name".equals(attr)) {
            return em.createNamedQuery(Department.FIND_BY_NAME_A, Department.class)
                    .setParameter("DeptName", value)
                    .getResultList();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean update(final Department obj) {
        return find(obj.getId()).map(e -> {
            if (obj.getName() != null) {
                e.setName(obj.getName());
            }
            if (obj.getFunction() != null) {
                e.setFunction(obj.getFunction());
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
}
