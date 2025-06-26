package co.in.nnj.learn.jee.adapter.output.object_mapper;

import java.util.ArrayList;
import java.util.List;

import co.in.nnj.learn.jee.adapter.output.db.entity.DepartmentEntity;
import co.in.nnj.learn.jee.adapter.output.db.entity.EmployeeEntity;
import co.in.nnj.learn.jee.adapter.output.db.entity.OperationStaffEntity;
import co.in.nnj.learn.jee.adapter.output.db.entity.TeachingStaffEntity;
import co.in.nnj.learn.jee.domain.valueobjects.Employee;
import co.in.nnj.learn.jee.domain.valueobjects.EmployeeType;

public final class EmployeeMapper implements EntityMapper<EmployeeEntity, Employee> {
    @Override
    public EmployeeEntity updateEntity(final EmployeeEntity staff, final Employee employee) {
        if (employee.type() == EmployeeType.TEACHER) {
            final TeachingStaffEntity teachingStaff = (TeachingStaffEntity) staff;
            teachingStaff.setSubExperties(employee.experties());
        } else {
            final OperationStaffEntity operationStaff = (OperationStaffEntity) staff;
            operationStaff.setOperExperties(employee.experties());
        }
        staff.setFname(employee.fname());
        staff.setLname(employee.lname());
        staff.setDob(employee.dob());
        staff.setJoiningDate(employee.dateOfJoining());
        staff.setQualification(employee.qualification());
        final DepartmentEntity dept = new DepartmentEntity();
        dept.setId(employee.departmentId());
        staff.setDepartment(dept);
        return staff;
    }

    @Override
    public EmployeeEntity toEntity(final Employee employee) {
        if (employee.type() == EmployeeType.TEACHER) {
            return updateEntity(new TeachingStaffEntity(), employee);
        }
        return updateEntity(new OperationStaffEntity(), employee);
    }

    @Override
    public Employee toValue(final EmployeeEntity emp) {
        final boolean isTeacher = (emp instanceof TeachingStaffEntity);
        final EmployeeType type = (isTeacher ? EmployeeType.TEACHER : EmployeeType.OPERATIONS);
        final String experties = (isTeacher ? ((TeachingStaffEntity) emp).getSubExperties()
                : ((OperationStaffEntity) emp).getOperExperties());
        return new Employee(emp.getId(), emp.getFname(), emp.getLname(), emp.getDob(), emp.getJoiningDate(),
                emp.getQualification(),
                experties, type, emp.getDepartment().getId());
    }

    @Override
    public List<Employee> toValueList(final List<EmployeeEntity> resultList) {
        final List<Employee> emps = new ArrayList<>();
        for (final EmployeeEntity employeeEntity : resultList) {
            emps.add(toValue(employeeEntity));
        }
        return emps;
    }
}
