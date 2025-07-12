package co.in.nnj.learn.jee.application;

import java.util.List;
import java.util.UUID;

import co.in.nnj.learn.jee.domain.Department;

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
