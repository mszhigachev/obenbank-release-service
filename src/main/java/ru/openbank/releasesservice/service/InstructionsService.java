package ru.openbank.releasesservice.service;

import ru.openbank.releasesservice.dto.InstructionDto;

public interface InstructionsService {

    InstructionDto getReleaseInstructions(long releaseId);

    InstructionDto getHotfixInstructions(long hotfixId);

    InstructionDto saveInstruction(InstructionDto dto);
}
