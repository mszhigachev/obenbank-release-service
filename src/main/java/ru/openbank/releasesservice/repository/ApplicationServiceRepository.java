package ru.openbank.releasesservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.openbank.releasesservice.model.ApplicationService;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationServiceRepository extends JpaRepository<ApplicationService, Long> {

    List<ApplicationService> findByOrderByNameAsc();

    ApplicationService findById(long serviceId);

    Optional<ApplicationService> findByName(String name);
}
