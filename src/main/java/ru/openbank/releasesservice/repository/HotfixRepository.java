package ru.openbank.releasesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.openbank.releasesservice.model.Hotfix;

import java.util.List;

@Repository
public interface HotfixRepository extends JpaRepository<Hotfix, Long> {

    List<Hotfix> findAllByReleaseIdOrderByDateFixDesc(long releaseId);

    Hotfix findById(long id);

}
