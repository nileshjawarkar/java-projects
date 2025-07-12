package co.in.nnj.learn.jee.eduinst.boundry;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import co.in.nnj.learn.jee.eduinst.entity.Department;
import co.in.nnj.learn.jee.eduinst.repository.DepartmentRepository;
import co.in.nnj.learn.jee.eduinst.repository.EmployeeRepository;

@Stateless
public class DepartmentService {
    // -- private static final Logger LOGGER =
    // LoggerFactory.getLogger(DepartmentService.class.getName());

    @Inject
    DepartmentRepository repository;
    @Inject
    EmployeeRepository empRepository;

    public Optional<Department> create(final Department dept) {
        final List<UUID> depts = findAll("name", dept.getName());
        if (depts != null && !depts.isEmpty()) {
            throw new ConstraintVoilationException(
                    String.format("Department with name [%s] already exist.", dept.getName()));
        }
        return repository.create(dept);
    }

    public List<UUID> findAll(final String attr, final String value) {
        return repository.findAllIds(attr, value);
    }

    public Optional<Department> find(final UUID id) {
        return repository.find(id);
    }

    public boolean update(final Department dept) {
        return repository.update(dept);
    }

    public boolean deleteDepartment(final UUID id) {
        final List<UUID> empsInDept = empRepository.findAllIds("dept", id.toString());
        if (!empsInDept.isEmpty()) {
            throw new UsageFoundException(String.format("Department has [%d] employess.", empsInDept.size()));
        }
        return repository.delete(id);
    }
}
