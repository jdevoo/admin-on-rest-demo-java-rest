package aor.demo.crud;

import aor.demo.crud.interceptors.IdWrapperSerializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Where(clause="published=1")
public class ExampleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String clientComment;
    public String dateStarted;
    public boolean isResolved = false;
    public String status;
    public int kmCharged = 0;
    public int hoursWorkedAfter2h = 0;
    public boolean workCompleted;
    public boolean excludeFromReporting = false;
    public boolean zeroCost = false;

    public int partsCost = 0;
    public String disapproveReason;
    public boolean disapproved = false;

    public boolean published = true;


    @OneToMany(cascade = {CascadeType.DETACH})
    public Set<UploadFile> fileA = new HashSet<>();

    @OneToMany(cascade = {CascadeType.DETACH})
    public Set<UploadFile> fileB = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.DETACH})
    public Client client;


//    @Formula("(SELECT an id from ... ")
//    @JsonSerialize(using = IdWrapperSerializer.class)
//    public Integer distantId;
//
//    @Formula("(SELECT an id from ...")
//    @JsonSerialize(using = IdWrapperSerializer.class)
//    public Integer distantId2;


    @PrePersist
    public void prePersist() {
        excludeNoCostConsistency();
        setDisapproved();
        initStatus();
    }

    @PreUpdate
    public void preUpdate() {
        excludeNoCostConsistency();
        setDisapproved();
        initStatus();

    }

    private void initStatus() {
        if (isResolved) {
            status = "Resolved";
        }
        else if (disapproved) {
            status = "Rejected";
        }
        else if (workCompleted) {
            status = "Completed";
        }
        else if(client != null) {
            status = "Assigned";
        }
        else {
            status = "Unassigned";
        }
    }

    private void setDisapproved() {
        if (disapproveReason != null || ((disapproveReason != null) && !disapproveReason.isEmpty())) {
            disapproved = true;
        }
        else {
            disapproved = false;
        }
    }

    private void excludeNoCostConsistency() {
        if (excludeFromReporting) {
            zeroCost = true;
        }
    }

    @JsonCreator
    public ExampleEntity(int id) {
        this.id = id;
    }

    public ExampleEntity() {
        this.id = id;
    }


}
