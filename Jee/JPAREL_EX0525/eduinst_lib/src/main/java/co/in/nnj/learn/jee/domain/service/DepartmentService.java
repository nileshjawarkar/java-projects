package co.in.nnj.learn.jee.domain.service;

import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.in.nnj.learn.jee.common.exception.ConstraintVoilationException;
import co.in.nnj.learn.jee.domain.valueobjects.Department;
import co.in.nnj.learn.jee.port.output.db.repository.DepartmentRepository;

@Stateless
public class DepartmentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class.getName());

    @Inject
    DepartmentRepository repository;

    public Department create(final Department dept) {
        final List<Department> depts = findByName(dept.name());
        if (!depts.isEmpty()) {
            throw new ConstraintVoilationException(
                    String.format("Department with name [%s] already exist.", dept.name()));
        }
        return repository.create(dept);
    }

    public List<Department> findAll() {
        return repository.findAll();
    }

    public List<Department> findByName(final String name) {
        final List<Department> deps = repository.findByName(name);
        LOGGER.info("Result size = " + deps.size());
        return deps;
    }

    public Department find(final UUID id) {
        return repository.find(id);
    }
}
