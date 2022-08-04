package ru.openbank.releasesservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.openbank.releasesservice.dto.InstructionDto;
import ru.openbank.releasesservice.exception.NotFoundException;
import ru.openbank.releasesservice.mapper.InstructionMapper;
import ru.openbank.releasesservice.model.Hotfix;
import ru.openbank.releasesservice.model.Instruction;
import ru.openbank.releasesservice.model.Release;
import ru.openbank.releasesservice.repository.HotfixRepository;
import ru.openbank.releasesservice.repository.InstructionRepository;
import ru.openbank.releasesservice.repository.ReleaseRepository;
import ru.openbank.releasesservice.service.InstructionsService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstructionServiceImpl implements InstructionsService {

    private final InstructionRepository instructionRepository;

    private final ReleaseRepository releaseRepository;

    private final HotfixRepository hotfixRepository;

    private final InstructionMapper instructionMapper;

    @Override
    public InstructionDto getReleaseInstructions(long releaseId) {
        log.info("Searching instructions for release {}", releaseId);
        Release release = releaseRepository.findById(releaseId);
        if (release == null) {
            throw new NotFoundException(String.format("Release id: %s not found", releaseId));
        }
        List<Instruction> instructions = instructionRepository.findAllByReleaseIdAndIsHotfix(release.getId(), false);

        return instructionMapper.toReleaseDto(release, instructions);
    }

    @Override
    public InstructionDto getHotfixInstructions(long hotfixId) {
        log.info("Searching instructions for hotfix {}", hotfixId);
        Hotfix hotfix = hotfixRepository.findById(hotfixId);

        if (hotfix == null) {
            throw new NotFoundException(String.format("Hotfix id: %s not found", hotfixId));
        }

        List<Instruction> instructions = instructionRepository.findAllByReleaseIdAndIsHotfix(hotfix.getId(), true);

        return instructionMapper.toHotfixDto(hotfix, instructions);
    }

    @Override
    public InstructionDto saveInstruction(InstructionDto dto) {

        List<Instruction> instructions = instructionMapper.fromInstructionDto(dto);
        if (dto.isHotfix()) {
            log.info("Searching hotfix by id {}", dto.getReleaseId());
            Hotfix hotfix = hotfixRepository.findById(dto.getReleaseId());
            log.info("Saving instructions {}", instructions);
            return instructionMapper.toHotfixResponseDto(hotfix, instructionRepository.saveAll(instructions));
        } else {
            log.info("Searching release by id {}", dto.getReleaseId());
            Release release = releaseRepository.findById(dto.getReleaseId());
            log.info("Saving instructions {}", instructions);
            return instructionMapper.toReleaseResponseDto(release, instructionRepository.saveAll(instructions));
        }

    }
}
