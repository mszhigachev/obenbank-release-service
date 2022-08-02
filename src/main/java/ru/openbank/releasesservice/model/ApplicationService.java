package ru.openbank.releasesservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tb_services",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        },
        indexes = {
                @Index(name = "tb_services_name_idx", columnList = "name")
        })
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 2555)
    private String description;

    public ApplicationService(String name) {
        this.name = name;
    }

}
