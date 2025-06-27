package co.in.nnj.learn.jee.domain.service;

import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import co.in.nnj.learn.jee.common.exception.ConstraintVoilationException;
import co.in.nnj.learn.jee.common.exception.UsageFound;
import co.in.nnj.learn.jee.domain.valueobjects.Department;
import co.in.nnj.learn.jee.domain.valueobjects.Employee;
import co.in.nnj.learn.jee.port.output.db.repository.DepartmentRepository;
import co.in.nnj.learn.jee.port.output.db.repository.EmployeeRepository;

@Stateless
public class DepartmentService {
    //-- private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class.getName());

    @Inject
    DepartmentRepository repository;
    @Inject
    EmployeeRepository empRepository;

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
        return repository.findByName(name);
    }

    public Department find(final UUID id) {
        return repository.find(id);
    }

    public boolean update(final Department dept) {
       return repository.update(dept);
    }
    public boolean deleteDepartment(final UUID id) {
        final List<Employee> empsInDept = empRepository.findByDepartment(id);
        if(!empsInDept.isEmpty()) {
            throw new UsageFound(String.format("Department has [%d] employess.", empsInDept.size()));
        }
        return repository.delete(id);
    }
}
