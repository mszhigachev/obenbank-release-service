package ru.openbank.releasesservice.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.openbank.releasesservice.dto.ReleaseDto;
import ru.openbank.releasesservice.model.Release;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReleaseMapper {

    public List<ReleaseDto> toDtos(List<Release> releases, boolean isIncludeHotfixes) {
        return releases.stream().map(v -> toDto(v, isIncludeHotfixes)).collect(Collectors.toList());
    }

    public ReleaseDto toDto(Release release, boolean isIncludeHotfixes) {
        ReleaseDto dto = new ReleaseDto();
        dto.setId(release.getId());
        dto.setName(release.getName());
        dto.setDateEnd(release.getDateEnd());
        dto.setDateFreeze(release.getDateFreeze());
        dto.setDateStart(release.getDateStart());
        dto.setDescription(release.getDescription());
        dto.setHotFixes(isIncludeHotfixes ? new HotfixMapper().toReleaseDtos(release.getHotfixes()) : null);
        return dto;
    }

    public Release fromDto(ReleaseDto releaseDto) {
        Release release = new Release();
        release.setName(releaseDto.getName());
        release.setDateStart(releaseDto.getDateStart());
        release.setDateEnd(releaseDto.getDateEnd());
        release.setDateFreeze(releaseDto.getDateFreeze());
        release.setDescription(releaseDto.getDescription());
        return release;
    }
}
