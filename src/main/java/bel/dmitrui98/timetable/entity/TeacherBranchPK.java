package bel.dmitrui98.timetable.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TeacherBranchPK implements Serializable {
    private Long teacherBranchId;
    @ManyToOne
    @JoinColumn(nullable = false, name = "teacherId")
    private Teacher teacher;
}
