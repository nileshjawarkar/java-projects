package co.in.nnj.learn.jee.infrastructure.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;

import co.in.nnj.learn.jee.application.EmployeeRepository;
import co.in.nnj.learn.jee.domain.Employee;
import co.in.nnj.learn.jee.infrastructure.persistence.entity.DepartmentEntity;
import co.in.nnj.learn.jee.infrastructure.persistence.entity.EmployeeEntity;
import co.in.nnj.learn.jee.infrastructure.persistence.object_mapper.EmployeeMapper;
import co.in.nnj.learn.jee.infrastructure.persistence.object_mapper.EntityMapper;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    //-- private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class.getName());
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
        return mapper.toValue(entityManager.find(EmployeeEntity.class, id));
    }

    @Override
    public List<UUID> findAllIds(final String attr, final String value) {
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
    public List<Employee> findAll(final String attr, final String value) {
        throw new UnsupportedOperationException("Unimplemented method 'findAllObjects'");
    }

}
