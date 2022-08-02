package ru.openbank.releasesservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "Инструкция сервиса", description = "Инструкция к кокнретному сервису")
public class ApplicationServiceInstructionDto {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @ApiModelProperty(value = "Идентификатор инструкции")
    private long instructionId;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @ApiModelProperty(value = "Идентификатор сервиса", required = true)
    private long serviceId;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @ApiModelProperty(value = "Идентификатор версии", required = true)
    private long versionId;

    @ApiModelProperty(value = "Имя сервиса")
    private String name;

    @ApiModelProperty(value = "Версия сервиса")
    private String version;

    @ApiModelProperty(value = "Описание инструкции к сервису", required = true)
    private String description;
}
