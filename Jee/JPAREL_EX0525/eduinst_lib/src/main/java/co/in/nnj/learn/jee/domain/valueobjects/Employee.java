package co.in.nnj.learn.jee.domain.valueobjects;

import java.util.Date;
import java.util.UUID;

public record Employee(UUID id, String fname, String lname, Date dob, Date dateOfJoining, String qualification,
        String experties, EmployeeType type, UUID departmentId) {
    public Employee(final String fname, final String lname, final Date dob, final Date dateOfJoining, final String qualification, final String experties,
            final EmployeeType type) {
        this(null, fname, lname, dob, dateOfJoining, qualification, experties, type, null);
    }

    public Employee(final String fname, final String lname, final Date dob, final Date dateOfJoining, final String qualification, final String experties,
            final EmployeeType type, final UUID departmentId) {
        this(null, fname, lname, dob, dateOfJoining, qualification, experties, type, departmentId);
    }
}
