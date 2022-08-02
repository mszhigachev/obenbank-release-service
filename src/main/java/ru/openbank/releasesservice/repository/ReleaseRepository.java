package ru.openbank.releasesservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.openbank.releasesservice.model.Release;

@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long> {

    Page<Release> findAllByOrderByDateStartDesc(Pageable pageable);

    Release findById(long id);

}
