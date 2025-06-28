package co.in.nnj.learn.jee.port.output.db.repository;

import java.util.List;

public interface Repository <T,U>{
    T create(T obj);

    T find(U id);

    List<U> findAll(String attr, String value);

    List<T> findAllObjects(String attr, String value);

    boolean update(T obj);

    boolean delete(U id);
}
