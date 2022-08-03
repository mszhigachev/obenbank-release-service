package ru.openbank.releasesservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_hotfixes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hotfix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "release_id", nullable = false, updatable = false)
    private long releaseId;

    @Column(name = "date_fix", nullable = false)
    private LocalDateTime dateFix;

    @Column(name = "description", length = 2555, nullable = false)
    private String description;
}
