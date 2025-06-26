package co.in.nnj.learn.jee.port.output.db.repository;

import java.util.List;
import java.util.UUID;

import co.in.nnj.learn.jee.domain.valueobjects.Department;

public interface DepartmentRepository {
    Department create(Department department);

    Department find(UUID id);

    List<Department> findAll();

    List<Department> findByName(String name);

    boolean update(Department department);

    boolean delete(UUID id);
}
