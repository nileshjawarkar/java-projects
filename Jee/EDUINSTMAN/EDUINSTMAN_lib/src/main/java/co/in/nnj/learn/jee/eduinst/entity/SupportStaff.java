package co.in.nnj.learn.jee.eduinst.entity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("NTS")
public class SupportStaff extends Employee {
    private String operExperties;

    public String getOperExperties() {
        return operExperties;
    }

    public void setOperExperties(final String operationExperties) {
        this.operExperties = operationExperties;
    }
}
