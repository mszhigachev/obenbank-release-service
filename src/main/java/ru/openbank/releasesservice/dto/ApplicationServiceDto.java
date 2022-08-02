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
@ApiModel(value = "Сервис", description = "Сервис приложений")
public class ApplicationServiceDto {

    @ApiModelProperty(value = "Уникальный идентификатор сервиса")
    private long id;

    @ApiModelProperty(value = "Уникальное имя сервиса", required = true)
    private String name;

    @ApiModelProperty(value = "Описание сервиса")
    private String description;

}
