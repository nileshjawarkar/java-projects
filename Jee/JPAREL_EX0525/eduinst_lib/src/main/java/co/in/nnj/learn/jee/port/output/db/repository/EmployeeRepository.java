package co.in.nnj.learn.jee.port.output.db.repository;

import java.util.List;
import java.util.UUID;

import co.in.nnj.learn.jee.domain.valueobjects.Employee;

public interface EmployeeRepository {
    Employee create(final Employee employee);
    boolean update(final Employee employee);
    boolean delete(final UUID id);
    Employee find(final UUID id);
    List<Employee> findAll();
    List<Employee> findByName(final String fname, String lname);
    List<Employee> findByDepartment(final UUID deptId);
}
