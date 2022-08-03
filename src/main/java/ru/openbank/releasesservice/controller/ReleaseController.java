package ru.openbank.releasesservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.openbank.releasesservice.dto.HotfixDto;
import ru.openbank.releasesservice.dto.PageDto;
import ru.openbank.releasesservice.dto.ReleaseDto;
import ru.openbank.releasesservice.exception.ReleaseNotFoundException;
import ru.openbank.releasesservice.service.ReleaseService;

import javax.management.ServiceNotFoundException;
import javax.servlet.http.HttpServletResponse;


@RestController
@Api(tags = "Контроллер Release", description = "Получение, добавление релизов")
@RequestMapping("/releases")
@RequiredArgsConstructor
public class ReleaseController {

    private final ReleaseService releaseService;

    @ApiOperation(value = "Получеие пегенируемого списка релизов")
    @GetMapping
    public PageDto<ReleaseDto> getAll(@ApiParam(value = "Признак необходимости вернуть список хотфиксов к каждому релизу", defaultValue = "false") @RequestParam(name = "is_include_hotfixes", defaultValue = "false", required = false) boolean isIncludeHotfixes,
                                      @ApiParam(value = "Номер страницы релизов", defaultValue = "0") @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                      @ApiParam(value = "Размер страницы релизов", defaultValue = "10") @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        return releaseService.getAll(isIncludeHotfixes, pageNumber, pageSize);
    }

    @ApiOperation(value = "Добавление релиза")
    @PostMapping
    public ReleaseDto save(@ApiParam(value = "JSON с полями релиза", required = true) @RequestBody ReleaseDto dto, HttpServletResponse response) {
        ReleaseDto releaseDto = releaseService.save(dto);
        response.setHeader("Location", String.format("/releases/%s", releaseDto.getId()));
        return releaseDto;
    }

    @ApiOperation(value = "Добавление хотфикса к релизу")
    @PostMapping("/{id}/hotfixes")
    public HotfixDto saveHotfix(@ApiParam(value = "Идентификатор релиза", required = true) @PathVariable(name = "id") long id,
                                @ApiParam(value = "JSON с полями хотфикса", required = true) @RequestBody HotfixDto dto,
                                HttpServletResponse response) {
        try {
            HotfixDto hotfixDto = releaseService.saveHotfix(id, dto);
            response.setHeader("Location", String.format("/releases/%s/hotfixes/%s", hotfixDto.getReleaseId(), hotfixDto.getId()));

            return hotfixDto;
        }
        catch (ReleaseNotFoundException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,String.format("Release id: %s not found hotfix cant be saved",id),e
            );
        }
    }

}
