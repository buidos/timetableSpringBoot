package bel.dmitrui98.timetable.entity.dictionary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Тип дициплины (гуманитарная, техническая)
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class SubjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long subjectTypeId;

    @Column(nullable = false)
    private String name;
}
