package ru.openbank.releasesservice.mapper;


import org.springframework.stereotype.Component;
import ru.openbank.releasesservice.dto.HotfixDto;
import ru.openbank.releasesservice.model.Hotfix;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HotfixMapper {

    public List<HotfixDto> toDtos(List<Hotfix> hotfixes) {
        return hotfixes.stream().map(this::toDto).collect(Collectors.toList());
    }

    public HotfixDto toDto(Hotfix hotfix) {
        return HotfixDto.builder()
                .id(hotfix.getId())
                .releaseId(hotfix.getReleaseId())
                .dateFix(hotfix.getDateFix())
                .description(hotfix.getDescription())
                .build();
    }

    public List<HotfixDto> toReleaseDtos(List<Hotfix> hotfixes) {
        return hotfixes.stream().map(this::toReleaseDto).collect(Collectors.toList());
    }

    public HotfixDto toReleaseDto(Hotfix hotfix) {
        return HotfixDto.builder()
                .id(hotfix.getId())
                .dateFix(hotfix.getDateFix())
                .description(hotfix.getDescription())
                .build();
    }

    public Hotfix fromDto(HotfixDto dto) {
        Hotfix hotfix = new Hotfix();
        hotfix.setReleaseId(dto.getReleaseId());
        hotfix.setDescription(dto.getDescription());
        hotfix.setDateFix(dto.getDateFix());
        return hotfix;
    }
}
