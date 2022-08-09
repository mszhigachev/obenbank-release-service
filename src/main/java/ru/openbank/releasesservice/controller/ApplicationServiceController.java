package ru.openbank.releasesservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.openbank.releasesservice.dto.ApplicationServiceDto;
import ru.openbank.releasesservice.dto.ApplicationServiceVersionDto;
import ru.openbank.releasesservice.dto.PageDto;
import ru.openbank.releasesservice.dto.VersionDto;
import ru.openbank.releasesservice.service.ApplicationServiceService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/services")
@Api(tags = "Контроллер Service", description = "Получение, добавление сервисов и версий")
public class ApplicationServiceController {

    private final ApplicationServiceService applicationServiceService;

    @ApiOperation(value = "Получение списка сервисов")
    @GetMapping
    public List<ApplicationServiceDto> getAll() {
        return applicationServiceService.getAll();
    }

    @ApiOperation(value = "Cохранение сервиса")
    @PostMapping
    public ApplicationServiceDto save(@ApiParam(value = "JSON с именем сервиса и его описанием") @RequestBody ApplicationServiceDto dto, HttpServletResponse response) {
        ApplicationServiceDto applicationServiceDto = applicationServiceService.save(dto);
        response.setHeader("Location", String.format("/services/%s", applicationServiceDto.getId()));
        return applicationServiceDto;
    }

    @ApiOperation(value = "Изменение сервиса")
    @PutMapping("/{id}")
    public ApplicationServiceDto update(@ApiParam(value = "Идентификатор сервиса", required = true) @PathVariable("id") long id, @ApiParam(value = "JSON с набором изминяемых полей") @RequestBody ApplicationServiceDto dto) {
        return applicationServiceService.update(id, dto);
    }

    @ApiOperation(value = "Добавление версии")
    @PostMapping("/versions")
    public VersionDto saveVersion(@ApiParam(value = "JSON с именем сервиса и его версии") @RequestBody ApplicationServiceVersionDto dto, HttpServletResponse response) {
        VersionDto versionDto = applicationServiceService.saveVersion(dto);
        response.setHeader("Location", String.format("/services/%s/versions/%s", versionDto.getServiceId(), versionDto.getId()));
        return versionDto;
    }

    @ApiOperation(value = "Получение версий сервиса")
    @GetMapping("/{id}/versions")
    public PageDto<VersionDto> getVersionById(@ApiParam(value = "Идентификтор сервиса", required = true) @PathVariable("id") long id,
                                              @ApiParam(value = "Номер страницы версий сервиса") @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                              @ApiParam(value = "Размер страницы версий сервиса") @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        return applicationServiceService.getVersions(id, pageNumber, pageSize);

    }
}
