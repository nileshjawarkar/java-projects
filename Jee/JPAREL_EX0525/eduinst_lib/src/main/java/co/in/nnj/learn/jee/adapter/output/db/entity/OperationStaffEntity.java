package co.in.nnj.learn.jee.adapter.output.db.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("NTS")
public class OperationStaffEntity extends EmployeeEntity{
    private String operExperties;

    public String getOperExperties() {
        return operExperties;
    }

    public void setOperExperties(final String operationExperties) {
        this.operExperties = operationExperties;
    }
}
