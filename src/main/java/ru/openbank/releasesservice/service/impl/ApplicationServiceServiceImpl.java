package ru.openbank.releasesservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.openbank.releasesservice.dto.ApplicationServiceDto;
import ru.openbank.releasesservice.dto.ApplicationServiceVersionDto;
import ru.openbank.releasesservice.dto.PageDto;
import ru.openbank.releasesservice.dto.VersionDto;
import ru.openbank.releasesservice.mapper.ApplicationServiceMapper;
import ru.openbank.releasesservice.mapper.VersionMapper;
import ru.openbank.releasesservice.model.ApplicationService;
import ru.openbank.releasesservice.model.Version;
import ru.openbank.releasesservice.repository.ApplicationServiceRepository;
import ru.openbank.releasesservice.repository.VersionRepository;
import ru.openbank.releasesservice.service.ApplicationServiceService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceServiceImpl implements ApplicationServiceService {

    private final ApplicationServiceRepository applicationServiceRepository;

    private final ApplicationServiceMapper applicationServiceMapper;

    private final VersionRepository versionRepository;

    private final VersionMapper versionMapper;

    @Override
    public List<ApplicationServiceDto> getAll() {
        List<ApplicationService> services = applicationServiceRepository.findByOrderByNameAsc();
        log.info("Find all services");
        return applicationServiceMapper.toDtos(services);
    }

    @Override
    public ApplicationServiceDto getById(long id) {
        log.info("Find service by id: {}", id);
        ApplicationService service = applicationServiceRepository.findById(id);
        return applicationServiceMapper.toDto(service);
    }

    @Override
    public ApplicationServiceDto save(ApplicationServiceDto serviceDto) {
        log.info("Saving new service {}", serviceDto);
        ApplicationService service = applicationServiceMapper.fromDto(serviceDto);
        return applicationServiceMapper.toDto(applicationServiceRepository.save(service));
    }

    @Override
    public ApplicationServiceDto update(long id, ApplicationServiceDto serviceDto) {
        log.info("Searching service by id: {}", id);
        ApplicationService service = applicationServiceRepository.findById(id);
        log.info("Updating service {}, data: {}", service.getId(), serviceDto);
        service.setDescription(serviceDto.getDescription());
        return applicationServiceMapper.toDto(applicationServiceRepository.save(service));
    }

    @Override
    public PageDto<VersionDto> getVersions(long id, int pageNumber, int pageSize) {
        log.info("Searching versions by service id: {},pageNumber: {}, pageSize: {}", id, pageNumber, pageSize);
        Page<Version> versionsPage = versionRepository.findByServiceIdOrderByCreatedDateDesc(id, PageRequest.of(pageNumber, pageSize));
        PageDto page = PageDto.of(versionsPage);
        page.setContent(versionMapper.toDtos(page.getContent()));
        return page;
    }


    @Override
    public VersionDto saveVersion(ApplicationServiceVersionDto dto) {
        log.info("Saving new version by service name: {}, set version: {}", dto.getName(), dto.getVersion());

        ApplicationService service = applicationServiceRepository.findByName(dto.getName())
                .orElseGet(() -> applicationServiceRepository.save(new ApplicationService(dto.getName())));

        Version version = new Version();
        version.setVersion(dto.getVersion());
        version.setServiceId(service.getId());
        log.info("Saving new version for service id {},- {}", service.getId(), dto);
        return versionMapper.toDto(versionRepository.save(version));
    }

}