package co.in.nnj.learn.jee.adapter.output.object_mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.in.nnj.learn.jee.adapter.output.db.entity.DepartmentEntity;
import co.in.nnj.learn.jee.adapter.output.db.entity.EmployeeEntity;
import co.in.nnj.learn.jee.adapter.output.db.entity.OperationStaffEntity;
import co.in.nnj.learn.jee.adapter.output.db.entity.TeachingStaffEntity;
import co.in.nnj.learn.jee.common.infra.EntityMapper;
import co.in.nnj.learn.jee.domain.valueobjects.Employee;
import co.in.nnj.learn.jee.domain.valueobjects.EmployeeType;

public final class EmployeeMapper extends EntityMapper<EmployeeEntity, Employee> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeMapper.class.getName());
    private final AddressMapper addressMapper = new AddressMapper();

    @Override
    public EmployeeEntity updateEntity(final EmployeeEntity staff, final Employee employee) {
        if (employee.type() != null) {
            if (employee.type() == EmployeeType.TEACHER) {
                final TeachingStaffEntity teachingStaff = (TeachingStaffEntity) staff;
                teachingStaff.setSubExperties(employee.experties());
            } else {
                final OperationStaffEntity operationStaff = (OperationStaffEntity) staff;
                operationStaff.setOperExperties(employee.experties());
            }
        }
        if (employee.fname() != null) {
            staff.setFname(employee.fname());
        }
        if (employee.lname() != null) {
            staff.setLname(employee.lname());
        }
        if (employee.dob() != null) {
            staff.setDob(employee.dob());
        }
        if (employee.dateOfJoining() != null) {
            staff.setJoiningDate(employee.dateOfJoining());
        }
        if (employee.qualification() != null) {
            staff.setQualification(employee.qualification());
        }
        if (employee.paddress() != null) {
            staff.setPAddress(addressMapper.toEntity(employee.paddress()));
        }

        if (employee.caddress() != null) {
            staff.setCAddress(addressMapper.toEntity(employee.caddress()));
        }

        final DepartmentEntity dept = new DepartmentEntity();
        dept.setId(employee.departmentId());
        if (employee.departmentId() != null) {
            staff.setDepartment(dept);
        }
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
        if (emp == null) {
            return null;
        }
        final boolean isTeacher = (emp instanceof TeachingStaffEntity);
        final EmployeeType type = (isTeacher ? EmployeeType.TEACHER : EmployeeType.OPERATIONS);
        final String experties = (isTeacher ? ((TeachingStaffEntity) emp).getSubExperties()
                : ((OperationStaffEntity) emp).getOperExperties());

        LOGGER.info(emp.toString());
        return new Employee(emp.getId(), emp.getFname(), emp.getLname(),
                emp.getDob(), emp.getJoiningDate(), emp.getQualification(),
                experties, type, emp.getDepartment().getId(),
                addressMapper.toValue(emp.getPAddress()),
                addressMapper.toValue(emp.getCAddress()));
    }
}
