package ru.openbank.releasesservice.mapper;


import org.springframework.stereotype.Component;
import ru.openbank.releasesservice.dto.VersionDto;
import ru.openbank.releasesservice.model.Version;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VersionMapper {

    public List<VersionDto> toDtos(List<Version> versions) {
        return versions.stream().map(this::toDto).collect(Collectors.toList());
    }

    public VersionDto toDto(Version version) {
        return VersionDto.builder()
                .id(version.getId())
                .serviceId(version.getServiceId())
                .version(version.getVersion())
                .createdDate(version.getCreatedDate())
                .build();
    }

    public Version fromDto(VersionDto dto) {
        Version version = new Version();
        version.setServiceId(dto.getServiceId());
        version.setVersion(dto.getVersion());
        version.setCreatedDate(dto.getCreatedDate());
        return version;
    }
}