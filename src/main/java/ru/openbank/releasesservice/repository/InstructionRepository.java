package ru.openbank.releasesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.openbank.releasesservice.model.Instruction;

import java.util.List;

@Repository
public interface InstructionRepository extends JpaRepository<Instruction, Long> {

    List<Instruction> findAllByReleaseIdAndIsHotfix(long releaseId, boolean isHotfix);
}
