package ru.openbank.releasesservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "tb_versions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "service_id", nullable = false, updatable = false)
    private long serviceId;

    @Column(name = "version", length = 32, nullable = false)
    private String version;

    @Column(name = "created_date", nullable = false, columnDefinition = "DEFAULT now()")
    private Timestamp createdDate;

    @PrePersist
    private void save() {
        this.createdDate = new Timestamp(System.currentTimeMillis());
    }
}
