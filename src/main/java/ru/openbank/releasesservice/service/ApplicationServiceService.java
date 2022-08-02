package ru.openbank.releasesservice.service;

import ru.openbank.releasesservice.dto.ApplicationServiceDto;
import ru.openbank.releasesservice.dto.ApplicationServiceVersionDto;
import ru.openbank.releasesservice.dto.PageDto;
import ru.openbank.releasesservice.dto.VersionDto;

import java.util.List;

public interface ApplicationServiceService {

    PageDto<VersionDto> getVersions(long id, int pageNumber, int pageSize);


    List<ApplicationServiceDto> getAll();

    ApplicationServiceDto getById(long serviceId);

    ApplicationServiceDto save(ApplicationServiceDto service);

    ApplicationServiceDto update(long id, ApplicationServiceDto serviceDto);

    VersionDto saveVersion(ApplicationServiceVersionDto dto);
}
