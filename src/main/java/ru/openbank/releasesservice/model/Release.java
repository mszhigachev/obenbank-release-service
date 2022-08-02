package ru.openbank.releasesservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
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
    private Timestamp dateStart;

    @Column(name = "date_end", nullable = false)
    private Timestamp dateEnd;

    @Column(name = "date_freeze", nullable = false)
    private Timestamp dateFreeze;

    @OneToMany(mappedBy = "releaseId")
    @OrderBy("dateFix DESC")
    private List<Hotfix> hotfixes = new java.util.ArrayList<>();


}
