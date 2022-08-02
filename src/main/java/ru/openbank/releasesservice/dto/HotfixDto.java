package ru.openbank.releasesservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "Хотфикс", description = "Хотфикс к релизу")
public class HotfixDto {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @ApiModelProperty(value = "Уникальный идентификатор хотфикса")
    private long id;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @ApiModelProperty(value = "Уникальный идентификатор релиза")
    private long releaseId;

    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "Дата хотфикса", required = true)
    private Timestamp dateFix;

    @ApiModelProperty(value = "Описание хотфикса", required = true)
    private String description;
}
