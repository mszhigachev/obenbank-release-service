package ru.openbank.releasesservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tb_instructions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Instruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "release_id", nullable = false, updatable = false)
    private long releaseId;

    @Column(name = "description", length = 2555)
    private String description;

    @Column(name = "is_hotfix")
    private boolean isHotfix;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ApplicationService service;

    @ManyToOne
    @JoinColumn(name = "version_id", nullable = false)
    private Version version;
}
