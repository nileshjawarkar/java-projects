package co.in.nnj.learn.jee.infrastructure.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;

import co.in.nnj.learn.jee.application.DepartmentRepository;
import co.in.nnj.learn.jee.domain.Department;
import co.in.nnj.learn.jee.domain.DepartmentType;
import co.in.nnj.learn.jee.infrastructure.persistence.entity.DepartmentEntity;
import co.in.nnj.learn.jee.infrastructure.persistence.object_mapper.DepartmentMapper;
import co.in.nnj.learn.jee.infrastructure.persistence.object_mapper.EntityMapper;

public class DepartmentRepositoryImpl implements DepartmentRepository {
    // -- private static final Logger LOGGER =
    // LoggerFactory.getLogger(DepartmentRepositoryImpl.class.getName());

    EntityManager entityManager;
    EntityMapper<DepartmentEntity, Department> mapper = new DepartmentMapper();

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
        if (dept != null) {
            return mapper.toValue(dept);
        }
        return null;
    }

    @Override
    public List<UUID> findAllIds(final String attr, final String value) {
        if (attr == null) {
            return entityManager
                    .createNamedQuery(DepartmentEntity.FIND_ALL, UUID.class)
                    .getResultList();
        } else if ("function".equals(attr)) {
            return entityManager
                    .createNamedQuery(DepartmentEntity.FIND_BY_TYPE, UUID.class)
                    .setParameter("Funtion", DepartmentType.valueOf(value))
                    .getResultList();
        } else if("name".equals(attr)) {
            return entityManager
                    .createNamedQuery(DepartmentEntity.FIND_BY_NAME, UUID.class)
                    .setParameter("DeptName", value)
                    .getResultList();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean update(final Department department) {
        final DepartmentEntity dept = entityManager.find(DepartmentEntity.class, department.id());
        if (dept != null) {
            if (department.name() != null) {
                dept.setName(department.name());
            }
            if (department.function() != null) {
                dept.setFunction(department.function());
            }
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

    public Department findByName(final String name) {
        final List<DepartmentEntity> resultList = entityManager
                .createNamedQuery(DepartmentEntity.FIND_BY_NAME, DepartmentEntity.class)
                .setParameter("DeptName", name).getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return mapper.toValue(resultList.get(0));
    }

    @Override
    public List<Department> findAll(final String attr, final String value) {
        throw new UnsupportedOperationException("Unimplemented method 'findAllObjects'");
    }
}
