package co.in.nnj.learn.jee.port.output.object_mapper;

import java.util.ArrayList;
import java.util.List;

import co.in.nnj.learn.jee.domain.valueobjects.Employee;
import co.in.nnj.learn.jee.domain.valueobjects.EmployeeType;
import co.in.nnj.learn.jee.port.output.entity.EmployeeEntity;
import co.in.nnj.learn.jee.port.output.entity.OperationStaffEntity;
import co.in.nnj.learn.jee.port.output.entity.TeachingStaffEntity;

public final class EmployeeMapper implements EntityMapper<EmployeeEntity, Employee> {
    @Override
    public EmployeeEntity updateEntity(final EmployeeEntity staff, final Employee employee) {
        if (employee.type() == EmployeeType.TEACHER) {
            final OperationStaffEntity operationStaff = (OperationStaffEntity) staff;
            operationStaff.setOperExperties(employee.experties());
        } else {
            final TeachingStaffEntity teachingStaff = (TeachingStaffEntity) staff;
            teachingStaff.setSubExperties(employee.experties());
        }
        staff.setFname(employee.fname());
        staff.setLname(employee.lname());
        staff.setDob(employee.dob());
        staff.setJoiningDate(employee.dateOfJoining());
        staff.setQualification(employee.qualification());
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
        EmployeeType type = EmployeeType.OPERATIONS;
        String experties;
        if (emp instanceof TeachingStaffEntity) {
            type = EmployeeType.TEACHER;
            final TeachingStaffEntity teachingStaff = (TeachingStaffEntity) emp;
            experties = teachingStaff.getSubExperties();
        } else {
            final OperationStaffEntity operationStaff = (OperationStaffEntity) emp;
            experties = operationStaff.getOperExperties();
        }
        return new Employee(emp.getFname(), emp.getLname(), emp.getDob(), emp.getJoiningDate(), emp.getQualification(),
                experties, type);
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
