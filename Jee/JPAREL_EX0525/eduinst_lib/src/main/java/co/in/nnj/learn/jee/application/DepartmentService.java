package co.in.nnj.learn.jee.application;

import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import co.in.nnj.learn.jee.application.exception.ConstraintVoilationException;
import co.in.nnj.learn.jee.application.exception.UsageFound;
import co.in.nnj.learn.jee.domain.Department;

@Stateless
public class DepartmentService {
    // -- private static final Logger LOGGER =
    // LoggerFactory.getLogger(DepartmentService.class.getName());

    @Inject
    DepartmentRepository repository;
    @Inject
    EmployeeRepository empRepository;

    public Department create(final Department dept) {
        final List<UUID> depts = findAll("name", dept.name());
        if (depts != null && !depts.isEmpty()) {
            throw new ConstraintVoilationException(
                    String.format("Department with name [%s] already exist.", dept.name()));
        }
        return repository.create(dept);
    }

    public List<UUID> findAll(final String attr, final String value) {
        return repository.findAllIds(attr, value);
    }

    public Department find(final UUID id) {
        return repository.find(id);
    }

    public boolean update(final Department dept) {
        return repository.update(dept);
    }

    public boolean deleteDepartment(final UUID id) {
        final List<UUID> empsInDept = empRepository.findAllIds("dept", id.toString());
        if (!empsInDept.isEmpty()) {
            throw new UsageFound(String.format("Department has [%d] employess.", empsInDept.size()));
        }
        return repository.delete(id);
    }
}
