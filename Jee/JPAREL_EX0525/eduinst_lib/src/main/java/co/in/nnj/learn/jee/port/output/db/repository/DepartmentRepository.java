package co.in.nnj.learn.jee.port.output.db.repository;

import java.util.List;
import java.util.UUID;

import co.in.nnj.learn.jee.common.infra.Repository;
import co.in.nnj.learn.jee.domain.valueobjects.Department;

public interface DepartmentRepository extends Repository<Department, UUID> {
    @Override
    Department create(Department department);

    @Override
    Department find(UUID id);

    @Override
    List<UUID> findAllIds(String attr, String value);

    @Override
    List<Department> findAll(String attr, String value);

    @Override
    boolean update(Department department);

    @Override
    boolean delete(UUID id);
}
