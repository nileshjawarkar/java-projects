package co.in.nnj.learn.jee.eduinst.boundry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import co.in.nnj.learn.jee.eduinst.entity.Employee;
import co.in.nnj.learn.jee.eduinst.repository.EmployeeRepository;

@Stateless
public class EmployeeService {

    @Inject
    EmployeeRepository repository;

    public Optional<Employee> create(final Employee emp) {
        final List<UUID> emps = findByName(emp.getFname(), emp.getLname());
        if (!emps.isEmpty()) {
            throw new ConstraintVoilationException(
                    String.format("Employee with name [%s] already exist.", emp.getFname()));
        }
        return repository.create(emp);
    }

    public List<UUID> findAll(final String attr, final String value) {
        return repository.findAllIds(attr, value);
    }

    public List<UUID> findAll() {
        return repository.findAllIds(null, null);
    }

    public List<UUID> findByName(final String fname, final String lname) {
        return repository.findByName(fname, lname);
    }

    public Optional<Employee> find(final UUID id) {
        return repository.find(id);
    }

    public boolean update(final Employee emp) {
       return repository.update(emp);
    }

    public boolean delete(final UUID id) {
       return repository.delete(id);
    }
}
