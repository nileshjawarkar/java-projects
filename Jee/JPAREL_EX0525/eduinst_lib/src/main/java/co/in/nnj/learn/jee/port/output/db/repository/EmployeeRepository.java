package co.in.nnj.learn.jee.port.output.db.repository;

import java.util.List;
import java.util.UUID;

import co.in.nnj.learn.jee.domain.valueobjects.Employee;

public interface EmployeeRepository {
    Employee create(Employee employee);

    boolean update(Employee employee);

    boolean delete(UUID id);

    Employee find(UUID id);

    List<Employee> findAll();

    List<Employee> findByName(String fname, String lname);

    List<Employee> findByDepartment(UUID deptId);
}
