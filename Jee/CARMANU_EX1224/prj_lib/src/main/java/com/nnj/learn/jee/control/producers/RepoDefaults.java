package com.nnj.learn.jee.control.producers;

import java.util.List;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import com.nnj.learn.jee.control.repo.CarRepo;
import com.nnj.learn.jee.control.repo.CarRepoDBBased;
import com.nnj.learn.jee.control.repo.CarRepoInMem;
import com.nnj.learn.jee.entity.Car;

public class RepoDefaults {

    @Resource(lookup = "java:global/h2db/cards")
    private DataSource dataSource;
    
    @Produces
    @Named("in-mem")
    public CarRepo inMemoryRepo() {
        return new CarRepoInMem();
    }

    @Produces
    @Named("in-db")
    public CarRepo defaultRepo() {
        /* Using explicit JNDI to lookup the resource.
        ==============================================
        Context initContext;
        try {
            initContext = new InitialContext();
            final Context envContext = (Context) initContext.lookup("java:/comp/env");
            final DataSource dataSource = (DataSource) envContext.lookup("java:global/h2db/cards");
            return new CarRepoDBBased(dataSource);
        } catch (final NamingException e) {
        } 
        =============================================*/

        if(dataSource != null) {
            return new CarRepoDBBased(dataSource);
        }
       
        System.out.println("DataSource injecttion failed");
        return new CarRepo() {
            private final String msg = "DB dataSource lookup failed. Please check your dataSource config.";
            @Override
            public void store(final Car car) {
                throw new UnsupportedOperationException(msg);
            }

            @Override
            public Car getCar(final String id) {
                throw new UnsupportedOperationException(msg);
            }

            @Override
            public List<Car> getCars(final Integer limit, final Integer offset) {
                throw new UnsupportedOperationException(msg);
            }

            @Override
            public void cleanup() {
                throw new UnsupportedOperationException(msg);
            }

            @Override
            public void setup() {
                throw new UnsupportedOperationException(msg);
            }
        };
    }
}
