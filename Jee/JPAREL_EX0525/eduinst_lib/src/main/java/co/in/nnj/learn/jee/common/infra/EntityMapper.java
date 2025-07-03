package co.in.nnj.learn.jee.common.infra;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityMapper<E, V> {
    public abstract E updateEntity(E entity, V vobj);

    public abstract E toEntity(V vobj);

    public abstract V toValue(E entity);

    public List<V> toValueList(final List<E> entityList) {
        final List<V> list = new ArrayList<>();
        for (final E entity : entityList) {
            list.add(toValue(entity));
        }
        return list;
    }
}
