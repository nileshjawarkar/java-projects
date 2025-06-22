package co.in.nnj.learn.jee.port.output.object_mapper;

import java.util.ArrayList;
import java.util.List;

import co.in.nnj.learn.jee.domain.valueobjects.Department;
import co.in.nnj.learn.jee.port.output.entity.DepartmentEntity;

public final class DepartmentMapper implements EntityMapper <DepartmentEntity, Department> {
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

    @Override
    public List<Department> toValueList(final List<DepartmentEntity> listOfEntities) {
        final List<Department> deptList = new ArrayList<>();
        for (final DepartmentEntity dept : listOfEntities) {
            final Department dto = toValue(dept);
            deptList.add(dto);
        }
        return deptList;
    }
}
