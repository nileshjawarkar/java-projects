package co.in.nnj.learn.jee.domain.service;

import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import co.in.nnj.learn.jee.common.exception.ConstraintVoilationException;
import co.in.nnj.learn.jee.domain.valueobjects.Employee;
import co.in.nnj.learn.jee.port.output.db.repository.EmployeeRepository;

@Stateless
public class EmployeeService {

    @Inject
    EmployeeRepository repository;

    public Employee create(final Employee emp) {
        final List<Employee> emps = findByName(emp.fname(), emp.lname());
        if (!emps.isEmpty()) {
            throw new ConstraintVoilationException(
                    String.format("Employee with name [%s] already exist.", emp.fname()));
        }
        return repository.create(emp);
    }

    public List<Employee> findAll() {
        return repository.findAll();
    }

    public List<Employee> findByName(final String fname, final String lname) {
        return repository.findByName(fname, lname);
    }

    public List<Employee> findByDepartment(final UUID id) {
        return repository.findByDepartment(id);
    }

    public Employee find(final UUID id) {
        return repository.find(id);
    }
}
