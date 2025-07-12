package co.in.nnj.learn.jee.eduinst.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, U> {
    Optional<T> create(T obj);

    Optional<T> find(U id);

    List<U> findAllIds(String attr, String value);

    List<T> findAll(String attr, String value);

    boolean update(T obj);

    boolean delete(U id);
}
