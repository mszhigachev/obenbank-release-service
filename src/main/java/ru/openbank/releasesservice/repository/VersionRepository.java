package ru.openbank.releasesservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.openbank.releasesservice.model.Version;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {

    Page<Version> findByServiceIdOrderByCreatedDateDesc(long id, Pageable pageable);
}
