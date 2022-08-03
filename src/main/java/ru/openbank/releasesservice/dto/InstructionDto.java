package ru.openbank.releasesservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "Инструкция", description = "Инструкция к релизу или хотфиксу")
public class InstructionDto {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @ApiModelProperty(value = "Уникальный идентификатор инструкции")
    private long id;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @ApiModelProperty(value = "Идентификатор релиза\\хотфикса")
    private long releaseId;

    @ApiModelProperty(value = "Признак хотфикса",required = true)
    private boolean isHotfix;

    @ApiModelProperty(value = "Описание релиза \\хотфикса")
    private String description;

    @ApiModelProperty(value = "Дата начала релиза")
    private LocalDateTime dateStart;

    @ApiModelProperty(value = "Дата хотфикса")
    private LocalDateTime dateHotfix;

    @ApiModelProperty(value = "Дата окончания релиза")
    private LocalDateTime dateEnd;

    @ApiModelProperty(value = "Дата кодфриза")
    private LocalDateTime dateFreeze;

    @ApiModelProperty(value = "Список сервисов и их версий")
    private List<ApplicationServiceInstructionDto> services;
}
