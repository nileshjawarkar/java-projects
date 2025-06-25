package co.in.nnj.learn.jee.adapter.output.db.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TS")
public class TeachingStaffEntity extends EmployeeEntity {
    private String subExperties;

    public String getSubExperties() {
        return subExperties;
    }

    public void setSubExperties(final String subjectExperties) {
        this.subExperties = subjectExperties;
    }
}
