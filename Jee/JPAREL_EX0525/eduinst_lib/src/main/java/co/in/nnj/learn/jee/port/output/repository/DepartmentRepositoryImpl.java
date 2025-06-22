package co.in.nnj.learn.jee.port.output.repository;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;

import co.in.nnj.learn.jee.domain.repository.Repository;
import co.in.nnj.learn.jee.domain.valueobjects.Department;
import co.in.nnj.learn.jee.port.output.entity.DepartmentEntity;
import co.in.nnj.learn.jee.port.output.object_mapper.DepartmentMapper;
import co.in.nnj.learn.jee.port.output.object_mapper.EntityMapper;

public class DepartmentRepositoryImpl implements Repository<Department, UUID> {

    EntityManager entityManager;
    EntityMapper <DepartmentEntity, Department> mapper = new DepartmentMapper();

    public DepartmentRepositoryImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Department create(final Department department) {
        final DepartmentEntity dept = mapper.toEntity(department);
        entityManager.persist(dept);
        return mapper.toValue(dept);
    }

    @Override
    public Department find(final UUID id) {
        final DepartmentEntity dept = entityManager.find(DepartmentEntity.class, id);
        return mapper.toValue(dept);
    }

    @Override
    public List<Department> findAll() {
        final List<DepartmentEntity> resultList = entityManager
                .createNamedQuery(DepartmentEntity.FIND_ALL, DepartmentEntity.class)
                .getResultList();
        return mapper.toValueList(resultList);
    }

    @Override
    public List<Department> findBy(final String value) {
        final List<DepartmentEntity> resultList = entityManager
                .createNamedQuery(DepartmentEntity.FIND_BY, DepartmentEntity.class)
                .setParameter("Name", value).getResultList();
        return mapper.toValueList(resultList);
    }

    @Override
    public boolean update(final Department department) {
        final DepartmentEntity dept = entityManager.find(DepartmentEntity.class, department.id());
        if (dept != null) {
            dept.setName(department.name());
            dept.setFunction(department.function());
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(final UUID id) {
        final DepartmentEntity dept = entityManager.find(DepartmentEntity.class, id);
        if (dept != null) {
            entityManager.remove(dept);
            return true;
        }
        return false;
    }
}
