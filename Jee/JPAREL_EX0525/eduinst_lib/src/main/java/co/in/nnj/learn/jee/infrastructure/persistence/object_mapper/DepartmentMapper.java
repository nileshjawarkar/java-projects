package co.in.nnj.learn.jee.infrastructure.persistence.object_mapper;

import co.in.nnj.learn.jee.domain.Department;
import co.in.nnj.learn.jee.infrastructure.persistence.entity.DepartmentEntity;

public final class DepartmentMapper extends EntityMapper <DepartmentEntity, Department> {
    @Override
    public DepartmentEntity updateEntity(final DepartmentEntity entity, final Department vobj) {
        entity.setName(vobj.name());
        entity.setFunction(vobj.function());
        return entity;
    }

    @Override
    public DepartmentEntity toEntity(final Department vobj) {
        final DepartmentEntity dept = new DepartmentEntity();
        return updateEntity(dept, vobj);
    }

    @Override
    public Department toValue(final DepartmentEntity entity) {
        return new Department(entity.getName(), entity.getFunction(), entity.getId());
    }
}
