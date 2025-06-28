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
        final List<UUID> emps = findByName(emp.fname(), emp.lname());
        if (!emps.isEmpty()) {
            throw new ConstraintVoilationException(
                    String.format("Employee with name [%s] already exist.", emp.fname()));
        }
        return repository.create(emp);
    }

    public List<UUID> findAll(final String attr, final String value) {
        return repository.findAllIds(attr, value);
    }

    public List<UUID> findByName(final String fname, final String lname) {
        return repository.findByName(fname, lname);
    }

    public Employee find(final UUID id) {
        return repository.find(id);
    }

    public boolean update(final Employee emp) {
       return repository.update(emp);
    }

    public boolean delete(final UUID id) {
       return repository.delete(id);
    }
}
