package co.in.nnj.learn.jee.adapter.output.db.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;

import co.in.nnj.learn.jee.adapter.output.db.entity.DepartmentEntity;
import co.in.nnj.learn.jee.adapter.output.db.entity.EmployeeEntity;
import co.in.nnj.learn.jee.adapter.output.object_mapper.EmployeeMapper;
import co.in.nnj.learn.jee.adapter.output.object_mapper.EntityMapper;
import co.in.nnj.learn.jee.domain.valueobjects.Employee;
import co.in.nnj.learn.jee.port.output.db.repository.EmployeeRepository;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    final EntityManager entityManager;
    final EntityMapper<EmployeeEntity, Employee> mapper = new EmployeeMapper();

    public EmployeeRepositoryImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Employee create(final Employee employee) {
        final EmployeeEntity emp = mapper.toEntity(employee);
        entityManager.persist(emp);
        return mapper.toValue(emp);
    }

    @Override
    public boolean update(final Employee employee) {
        final EmployeeEntity emp = entityManager.find(EmployeeEntity.class, employee.id());
        if (emp != null) {
            mapper.updateEntity(emp, employee);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(final UUID id) {
        final EmployeeEntity emp = entityManager.find(EmployeeEntity.class, id);
        if (emp != null) {
            entityManager.remove(emp);
            return true;
        }
        return false;
    }

    @Override
    public Employee find(final UUID id) {
        final EmployeeEntity emp = entityManager.find(EmployeeEntity.class, id);
        return mapper.toValue(emp);
    }

    @Override
    public List<UUID> findAll(final String attr, final String value) {
        if (attr == null) {
            return entityManager
                    .createNamedQuery(EmployeeEntity.FIND_ALL, UUID.class)
                    .getResultList();
        } else if ("dept".equals(attr)) {
            final DepartmentEntity dept = new DepartmentEntity();
            dept.setId(UUID.fromString(value));
            return entityManager
                    .createNamedQuery(EmployeeEntity.FIND_BY_DEPT, UUID.class)
                    .setParameter("Dept", dept)
                    .getResultList();
        }
        return new ArrayList<>();
    }

    @Override
    public List<UUID> findByName(final String fname, final String lname) {
        return entityManager
                .createNamedQuery(EmployeeEntity.FIND_BY_NAME, UUID.class)
                .setParameter("FName", fname)
                .setParameter("LName", lname).getResultList();
    }

    @Override
    public List<Employee> findAllObjects(final String attr, final String value) {
        throw new UnsupportedOperationException("Unimplemented method 'findAllObjects'");
    }

}
