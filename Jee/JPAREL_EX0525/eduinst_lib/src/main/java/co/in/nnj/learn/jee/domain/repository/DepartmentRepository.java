package co.in.nnj.learn.jee.domain.repository;

import java.util.List;
import java.util.UUID;

import co.in.nnj.learn.jee.domain.valueobjects.Department;

public interface DepartmentRepository {
    Department create(Department department);
    Department update(Department department);
    Department find(UUID id);
    List<Department> findAll();
    List<Department> findByName(String name);
}
