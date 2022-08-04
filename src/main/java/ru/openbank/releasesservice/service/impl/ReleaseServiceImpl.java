package ru.openbank.releasesservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.openbank.releasesservice.dto.HotfixDto;
import ru.openbank.releasesservice.dto.PageDto;
import ru.openbank.releasesservice.dto.ReleaseDto;
import ru.openbank.releasesservice.exception.NotFoundException;
import ru.openbank.releasesservice.mapper.HotfixMapper;
import ru.openbank.releasesservice.mapper.ReleaseMapper;
import ru.openbank.releasesservice.model.Hotfix;
import ru.openbank.releasesservice.model.Release;
import ru.openbank.releasesservice.repository.HotfixRepository;
import ru.openbank.releasesservice.repository.ReleaseRepository;
import ru.openbank.releasesservice.service.ReleaseService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReleaseServiceImpl implements ReleaseService {

    private final ReleaseRepository releaseRepository;

    private final ReleaseMapper releaseMapper;

    private final HotfixMapper hotfixMapper;

    private final HotfixRepository hotfixRepository;

    @Override
    public PageDto<ReleaseDto> getAll(boolean isIncludeHotfixes, int pageNumber, int pageSize) {
        log.info("Get all releases");
        Page<Release> releasesPage = releaseRepository.findAllByOrderByDateStartDesc(PageRequest.of(pageNumber, pageSize));
        if (releasesPage.getContent().isEmpty()) {
            log.info("No releases found");
            throw new NotFoundException("No releases found");
        }
        PageDto page = PageDto.of(releasesPage);
        page.setContent(releaseMapper.toDtos(page.getContent(), isIncludeHotfixes));
        return page;
    }

    @Override
    public ReleaseDto save(ReleaseDto dto) {
        log.info("Saving new release {}", dto);
        Release release = releaseMapper.fromDto(dto);
        return releaseMapper.toDto(releaseRepository.save(release), false);
    }

    @Override
    public HotfixDto saveHotfix(long id, HotfixDto dto) {
        if (releaseRepository.findById(id) == null) {
            log.warn("Release id: {} not found, hotfix cant be saved!", id);
            throw new NotFoundException(String.format("Release id: %s not found, hotfix cant be saved!", id));
        }
        dto.setReleaseId(id);
        Hotfix hotfix = hotfixMapper.fromDto(dto);
        log.info("Saving new hotfix {}", dto);
        return hotfixMapper.toDto(hotfixRepository.save(hotfix));
    }
}