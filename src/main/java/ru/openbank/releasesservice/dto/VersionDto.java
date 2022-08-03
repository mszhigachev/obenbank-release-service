package ru.openbank.releasesservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "Хотфикс", description = "Хотелизу")
public class VersionDto {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @ApiModelProperty(value = "Уникальный идентификатор версии сервиса")
    private long id;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @ApiModelProperty(value = "Уникальный идентификатор сервиса")
    private long serviceId;

    @ApiModelProperty(value = "Версия", required = true)
    private String version;

    @ApiModelProperty(value = "Дата создания версии")
    private LocalDateTime createdDate;
}