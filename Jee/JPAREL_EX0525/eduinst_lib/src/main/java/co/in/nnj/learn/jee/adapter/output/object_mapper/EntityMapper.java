package co.in.nnj.learn.jee.adapter.output.object_mapper;

import java.util.List;

public interface EntityMapper<E, V> {
    E updateEntity(final E entity, final V vobj);
    E toEntity(final V vobj);
    V toValue(final E entity);
    List<V> toValueList(final List<E> listOfEntities);
}
