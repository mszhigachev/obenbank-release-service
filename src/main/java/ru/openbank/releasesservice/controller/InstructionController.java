package ru.openbank.releasesservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.openbank.releasesservice.dto.InstructionDto;
import ru.openbank.releasesservice.service.InstructionsService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/instructions")
@Api(tags = "Контроллер Instructions", description = "Получение, добавление инструкций к релизам\\хотфиксам")
public class InstructionController {

    private final InstructionsService instructionsService;

    @ApiOperation(value = "Получение инструкций к релизу")
    @GetMapping(value = "/releases/{releaseId}")
    public InstructionDto getReleaseInstruction(@ApiParam(value = "Идентификатор релиза", required = true)
                                                @PathVariable(name = "releaseId") long releaseId) {
        return instructionsService.getReleaseInstructions(releaseId);
    }

    @ApiOperation(value = "Получение инструкций к хотфиксу")
    @GetMapping(value = "/hotfixes/{hotfixId}")
    public InstructionDto getHotfixInstruction(@ApiParam(value = "Идентификатор хотфикса", required = true)
                                               @PathVariable(name = "hotfixId") long hotfixId) {
        return instructionsService.getHotfixInstructions(hotfixId);
    }

    @PostMapping
    public InstructionDto saveInstruction(@ApiParam(value = "JSON с инструкцией", required = true)
                                          @RequestBody InstructionDto dto, HttpServletResponse response) {
        InstructionDto instructionDto = instructionsService.saveInstruction(dto);
        String url;
        if (instructionDto.isHotfix()) {
            url = String.format("/instructions/hotfixes/%s", instructionDto.getReleaseId());
        } else {
            url = String.format("/instructions/releases/%s", instructionDto.getReleaseId());
        }
        response.setHeader("Location", url);
        return instructionDto;
    }
}
