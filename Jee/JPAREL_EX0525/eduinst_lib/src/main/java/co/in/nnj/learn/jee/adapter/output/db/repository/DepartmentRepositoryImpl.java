package co.in.nnj.learn.jee.adapter.output.db.repository;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;

import co.in.nnj.learn.jee.adapter.output.db.entity.DepartmentEntity;
import co.in.nnj.learn.jee.adapter.output.object_mapper.DepartmentMapper;
import co.in.nnj.learn.jee.adapter.output.object_mapper.EntityMapper;
import co.in.nnj.learn.jee.domain.valueobjects.Department;
import co.in.nnj.learn.jee.port.output.db.repository.DepartmentRepository;

public class DepartmentRepositoryImpl implements DepartmentRepository {
    //-- private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentRepositoryImpl.class.getName());

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
    public boolean update(final Department department) {
        final DepartmentEntity dept = entityManager.find(DepartmentEntity.class, department.id());
        if (dept != null) {
            if(department.name() != null) dept.setName(department.name());
            if(department.function() != null) dept.setFunction(department.function());
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

    @Override
    public List<Department> findByName(final String name) {
        final List<DepartmentEntity> resultList = entityManager
                .createNamedQuery(DepartmentEntity.FIND_BY_NAME, DepartmentEntity.class)
                .setParameter("DeptName", name).getResultList();
        return mapper.toValueList(resultList);
    }
}
