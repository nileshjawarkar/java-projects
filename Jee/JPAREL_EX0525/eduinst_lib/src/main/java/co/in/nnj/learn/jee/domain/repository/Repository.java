package co.in.nnj.learn.jee.domain.repository;

import java.util.List;

public interface Repository <E, K> {
    E create(E department);
    boolean update(E department);
    boolean delete(K id);
    E find(K id);
    List<E> findAll();
    List<E> findBy(String value);
}
