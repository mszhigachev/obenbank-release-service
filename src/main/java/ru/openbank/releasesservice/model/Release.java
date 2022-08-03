package ru.openbank.releasesservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_releases")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Release {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 2555)
    private String description;

    @Column(name = "date_start", nullable = false)
    private LocalDateTime dateStart;

    @Column(name = "date_end", nullable = false)
    private LocalDateTime dateEnd;

    @Column(name = "date_freeze", nullable = false)
    private LocalDateTime dateFreeze;

    @OneToMany(mappedBy = "releaseId")
    @OrderBy("dateFix DESC")
    private List<Hotfix> hotfixes = new java.util.ArrayList<>();

}
