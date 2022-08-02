package ru.openbank.releasesservice.mapper;

import org.springframework.stereotype.Component;
import ru.openbank.releasesservice.dto.ApplicationServiceInstructionDto;
import ru.openbank.releasesservice.dto.InstructionDto;
import ru.openbank.releasesservice.model.*;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InstructionMapper {

    public InstructionDto toReleaseDto(Release release, List<Instruction> instructions) {
        List<ApplicationServiceInstructionDto> applicationServiceInstructionDto = instructions.stream().map(this::toApplicationServiceInstructionDto).collect(Collectors.toList());
        return InstructionDto.builder()
                .releaseId(release.getId())
                .isHotfix(false)
                .description(release.getDescription())
                .dateStart(release.getDateStart())
                .dateEnd(release.getDateEnd())
                .dateFreeze(release.getDateFreeze())
                .services(applicationServiceInstructionDto)
                .build();
    }

    public InstructionDto toHotfixDto(Hotfix hotfix, List<Instruction> instructions) {
        List<ApplicationServiceInstructionDto> applicationServiceInstructionDto = instructions.stream().map(this::toApplicationServiceInstructionDto).collect(Collectors.toList());
        return InstructionDto.builder()
                .releaseId(hotfix.getId())
                .isHotfix(true)
                .description(hotfix.getDescription())
                .dateHotfix(hotfix.getDateFix())
                .services(applicationServiceInstructionDto)
                .build();
    }

    public ApplicationServiceInstructionDto toApplicationServiceInstructionDto(Instruction instruction) {
        return ApplicationServiceInstructionDto.builder()
                .name(instruction.getService().getName())
                .version(instruction.getVersion().getVersion())
                .description(instruction.getDescription())
                .build();
    }

    public List<Instruction> fromInstructionDto(InstructionDto instructionDto) {
        return instructionDto.getServices().stream().map(v -> fromApplicationServiceInstructionDto(v, instructionDto.getReleaseId(), instructionDto.isHotfix())).collect(Collectors.toList());
    }

    public Instruction fromApplicationServiceInstructionDto(ApplicationServiceInstructionDto applicationServiceInstructionDto, long releaseId, boolean isHotfix) {
        Instruction instruction = new Instruction();
        ApplicationService applicationService = new ApplicationService();
        Version version = new Version();

        applicationService.setId(applicationServiceInstructionDto.getServiceId());
        version.setId(applicationServiceInstructionDto.getVersionId());

        instruction.setReleaseId(releaseId);
        instruction.setHotfix(isHotfix);
        instruction.setDescription(applicationServiceInstructionDto.getDescription());
        instruction.setService(applicationService);
        instruction.setVersion(version);
        return instruction;
    }

    public InstructionDto toReleaseResponseDto(Release release, List<Instruction> instructions) {
        List<ApplicationServiceInstructionDto> applicationServiceInstructionDto = instructions.stream().map(this::toResponseApplicationServiceInstructionDto).collect(Collectors.toList());
        return InstructionDto.builder()
                .releaseId(release.getId())
                .isHotfix(false)
                .services(applicationServiceInstructionDto)
                .build();
    }

    public InstructionDto toHotfixResponseDto(Hotfix hotfix, List<Instruction> instructions) {
        List<ApplicationServiceInstructionDto> applicationServiceInstructionDto = instructions.stream().map(this::toResponseApplicationServiceInstructionDto).collect(Collectors.toList());
        return InstructionDto.builder()
                .releaseId(hotfix.getId())
                .isHotfix(true)
                .services(applicationServiceInstructionDto)
                .build();
    }

    public ApplicationServiceInstructionDto toResponseApplicationServiceInstructionDto(Instruction instruction) {
        return ApplicationServiceInstructionDto.builder()
                .instructionId(instruction.getId())
                .serviceId(instruction.getService().getId())
                .versionId(instruction.getVersion().getId())
                .description(instruction.getDescription())
                .build();
    }
}
