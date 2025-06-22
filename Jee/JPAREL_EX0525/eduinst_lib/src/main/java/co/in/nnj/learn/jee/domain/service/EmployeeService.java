package co.in.nnj.learn.jee.domain.service;

import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import co.in.nnj.learn.jee.common.exception.ConstraintVoilationException;
import co.in.nnj.learn.jee.domain.repository.Repository;
import co.in.nnj.learn.jee.domain.valueobjects.Employee;

@Stateless
public class EmployeeService {

    @Inject
    Repository<Employee, UUID> repository;

    public Employee create(final Employee emp) {
        final List<Employee> emps = findBy(emp.fname());
        if (!emps.isEmpty()) {
            throw new ConstraintVoilationException(
                    String.format("Employee with name [%s] already exist.", emp.fname()));
        }
        return repository.create(emp);
    }

    public List<Employee> findAll() {
        return repository.findAll();
    }

    public List<Employee> findBy(final String name) {
        return repository.findBy(name);
    }

    public Employee find(final UUID id) {
        return repository.find(id);
    }
}
