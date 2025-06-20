package co.in.nnj.learn.jee.port.output.repository;

import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import co.in.nnj.learn.jee.domain.repository.DepartmentRepository;
import co.in.nnj.learn.jee.domain.valueobjects.Department;
import co.in.nnj.learn.jee.port.output.entity.DepartmentEntity;
import co.in.nnj.learn.jee.port.output.object_mapper.DepartmentMapper;

@Stateless
public class DepartmentRepositoryImpl implements DepartmentRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Department create(final Department departmentDTO) {
        final DepartmentEntity dept = DepartmentMapper.toEntity(departmentDTO);
        entityManager.persist(dept);
        return DepartmentMapper.toObject(dept);
    }

    @Override
    public Department find(final UUID id) {
        final DepartmentEntity dept = entityManager.find(DepartmentEntity.class, id);
        return DepartmentMapper.toObject(dept);
    }

    @Override
    public List<Department> findAll() {
        final List<DepartmentEntity> resultList = entityManager.createNamedQuery(DepartmentEntity.FIND_ALL, DepartmentEntity.class)
                .getResultList();
        return DepartmentMapper.toObjectList(resultList);
    }

    @Override
    public List<Department> findByName(final String name) {
        final List<DepartmentEntity> resultList = entityManager.createNamedQuery(DepartmentEntity.FIND_BY_NAME, DepartmentEntity.class)
                .setParameter("Name", name).getResultList();
        return DepartmentMapper.toObjectList(resultList);
    }

    @Override
    public Department update(final Department department) {
        final DepartmentEntity dept = DepartmentMapper.toEntity(department);
        entityManager.persist(dept);
        return DepartmentMapper.toObject(dept);
    }
}
