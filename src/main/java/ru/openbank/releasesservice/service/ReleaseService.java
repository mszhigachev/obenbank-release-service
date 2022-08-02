package ru.openbank.releasesservice.service;

import ru.openbank.releasesservice.dto.HotfixDto;
import ru.openbank.releasesservice.dto.PageDto;
import ru.openbank.releasesservice.dto.ReleaseDto;

public interface ReleaseService {

    PageDto<ReleaseDto> getAll(boolean isIncludeHotfixes, int pageNumber, int pageSize);

    ReleaseDto save(ReleaseDto dto);

    HotfixDto saveHotfix(long id, HotfixDto dto);
}
