package co.in.nnj.learn.jee.port.output.db.repository;

import java.util.List;
import java.util.UUID;

import co.in.nnj.learn.jee.common.infra.Repository;
import co.in.nnj.learn.jee.domain.valueobjects.Employee;

public interface EmployeeRepository extends Repository<Employee, UUID> {
    @Override
    Employee create(Employee employee);

    @Override
    boolean update(Employee employee);

    @Override
    boolean delete(UUID id);

    @Override
    Employee find(UUID id);

    @Override
    List<UUID> findAllIds(String attr, String value);

    List<UUID> findByName(String fname, String lname);
}
