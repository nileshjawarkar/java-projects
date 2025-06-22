package co.in.nnj.learn.jee.port.output.repository;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;

import co.in.nnj.learn.jee.domain.repository.Repository;
import co.in.nnj.learn.jee.domain.valueobjects.Employee;
import co.in.nnj.learn.jee.port.output.entity.EmployeeEntity;
import co.in.nnj.learn.jee.port.output.object_mapper.EmployeeMapper;
import co.in.nnj.learn.jee.port.output.object_mapper.EntityMapper;

public class EmployeeRepositoryImpl implements Repository<Employee, UUID> {
    EntityManager entityManager;
    EntityMapper<EmployeeEntity, Employee> mapper = new EmployeeMapper();

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
        if(emp != null) {
            mapper.updateEntity(emp, employee);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(final UUID id) {
        final EmployeeEntity emp = entityManager.find(EmployeeEntity.class, id);
        if(emp != null) {
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
    public List<Employee> findAll() {
        final List<EmployeeEntity> resultList = entityManager
                .createNamedQuery(EmployeeEntity.FIND_ALL, EmployeeEntity.class)
                .getResultList();
        return mapper.toValueList(resultList);
    }

    @Override
    public List<Employee> findBy(final String value) {
        final List<EmployeeEntity> resultList = entityManager
                .createNamedQuery(EmployeeEntity.FIND_BY, EmployeeEntity.class)
                .setParameter("FName", value).getResultList();
        return mapper.toValueList(resultList);
    }
}
