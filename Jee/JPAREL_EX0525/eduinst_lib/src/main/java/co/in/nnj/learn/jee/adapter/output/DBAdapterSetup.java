package co.in.nnj.learn.jee.adapter.output;

import jakarta.ejb.Stateless;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import co.in.nnj.learn.jee.adapter.output.db.repository.DepartmentRepositoryImpl;
import co.in.nnj.learn.jee.adapter.output.db.repository.EmployeeRepositoryImpl;
import co.in.nnj.learn.jee.port.output.db.repository.DepartmentRepository;
import co.in.nnj.learn.jee.port.output.db.repository.EmployeeRepository;

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
