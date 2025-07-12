package co.in.nnj.learn.jee.eduinst.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TS")
public class TeachingStaff extends Employee {
    private String subExperties;

    public String getSubExperties() {
        return subExperties;
    }

    public void setSubExperties(final String subjectExperties) {
        this.subExperties = subjectExperties;
    }
}
