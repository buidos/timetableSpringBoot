package bel.dmitrui98.timetable.entity.dictionary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * специальность группы
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}, name = "spec_name_constr")
})
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long specialtyId;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;
}
