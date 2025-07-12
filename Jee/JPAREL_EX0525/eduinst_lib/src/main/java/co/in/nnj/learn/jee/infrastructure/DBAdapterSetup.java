package co.in.nnj.learn.jee.infrastructure;

import jakarta.ejb.Stateless;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import co.in.nnj.learn.jee.application.DepartmentRepository;
import co.in.nnj.learn.jee.application.EmployeeRepository;
import co.in.nnj.learn.jee.infrastructure.persistence.repository.DepartmentRepositoryImpl;
import co.in.nnj.learn.jee.infrastructure.persistence.repository.EmployeeRepositoryImpl;

@Stateless
public class DBAdapterSetup {
    @PersistenceContext
    EntityManager entityManager;

    @Produces
    public DepartmentRepository getDepartmentRepo() {
        return new DepartmentRepositoryImpl(entityManager);
    }

    @Produces
    public EmployeeRepository getEmployeeRepo() {
        return new EmployeeRepositoryImpl(entityManager);
    }
}
