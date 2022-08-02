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
@ApiModel(value = "Версия сервиса", description = "Контракт для добавления версси к сервису")
public class ApplicationServiceVersionDto {

    @ApiModelProperty(value = "Имя сервиса", required = true)
    private String name;

    @ApiModelProperty(value = "Версия сервиса", required = true)
    private String version;

}