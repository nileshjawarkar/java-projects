package co.in.nnj.learn.jee.adapter.output.object_mapper;

import java.util.List;

public interface EntityMapper<E, V> {
    E updateEntity(E entity, V vobj);

    E toEntity(V vobj);

    V toValue(E entity);

    List<V> toValueList(List<E> listOfEntities);
}
