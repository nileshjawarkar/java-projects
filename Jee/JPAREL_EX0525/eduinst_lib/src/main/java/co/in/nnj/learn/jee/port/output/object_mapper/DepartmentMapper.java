package co.in.nnj.learn.jee.port.output.object_mapper;

import java.util.ArrayList;
import java.util.List;

import co.in.nnj.learn.jee.domain.valueobjects.Department;
import co.in.nnj.learn.jee.port.output.entity.DepartmentEntity;

public final class DepartmentMapper {
    private DepartmentMapper() {
    }

    public static DepartmentEntity toEntity(final Department department) {
        final DepartmentEntity dept = new DepartmentEntity();
        dept.setName(department.name());
        dept.setFunction(department.function());
        return dept;
    }

    public static Department toObject(final DepartmentEntity dept) {
        return new Department(dept.getName(), dept.getFunction(), dept.getId());
    }

    public static List<Department> toObjectList(final List<DepartmentEntity> resultList) {
        final List<Department> deptList = new ArrayList<>();
        for (final DepartmentEntity dept : resultList) {
            final Department dto = toObject(dept);
            deptList.add(dto);
        }
        return deptList;
    }

}
