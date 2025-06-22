package co.in.nnj.learn.jee.port.output.repository;

import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import co.in.nnj.learn.jee.domain.repository.Repository;
import co.in.nnj.learn.jee.domain.valueobjects.Department;
import co.in.nnj.learn.jee.domain.valueobjects.Employee;

@Stateless
public class RepositoryProducer {

    @PersistenceContext
    EntityManager entityManager;

    @Produces
    public Repository<Department, UUID> getDepartmentRepo() {
        return new DepartmentRepositoryImpl(entityManager);
    }
    
    @Produces
    public Repository<Employee, UUID> getEmployeeRepo() {
        return new EmployeeRepositoryImpl(entityManager);
    }
}
