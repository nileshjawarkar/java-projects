package co.in.nnj.learn.jee.domain.service;

import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import co.in.nnj.learn.jee.common.exception.ConstraintVoilationException;
import co.in.nnj.learn.jee.domain.repository.DepartmentRepository;
import co.in.nnj.learn.jee.domain.valueobjects.Department;

@Stateless
public class DepartmentService {

    @Inject
    DepartmentRepository repository;

    public Department create(final Department dept) {
        final List<Department> depts = findByName(dept.name());
        if(!depts.isEmpty()) {
            throw new ConstraintVoilationException(String.format("Department with name [%s] already exist.", dept.name()));
        }
        return repository.create(dept);
    }

    public List<Department> findAll() {
        return repository.findAll();
    }

    public List<Department> findByName(final String name) {
        return repository.findByName(name);
    }

    public Department find(final UUID id) {
        return repository.find(id);
    }
}
