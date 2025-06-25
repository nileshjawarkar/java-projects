package co.in.nnj.learn.jee.port.output.db.repository;

import java.util.List;
import java.util.UUID;

import co.in.nnj.learn.jee.domain.valueobjects.Department;

public interface DepartmentRepository {
    Department create(final Department department);
    Department find(final UUID id);
    List<Department> findAll();
    List<Department> findByName(final String name);
    boolean update(final Department department);
    boolean delete(final UUID id);
}
