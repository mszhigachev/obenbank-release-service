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
@ApiModel(value = "Релиз", description = "Релиз")
public class ReleaseDto {

    @ApiModelProperty(value = "Уникальный идентификатор релиза")
    private long id;

    @ApiModelProperty(value = "Имя релиза", required = true)
    private String name;

    @ApiModelProperty(value = "Описание релиза")
    private String description;

    @ApiModelProperty(value = "Дата начала релиза", required = true)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateStart;

    @ApiModelProperty(value = "Дата конца релиза", required = true)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateEnd;

    @ApiModelProperty(value = "Дата кодфриза", required = true)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateFreeze;

    @ApiModelProperty(value = "Опциональный список хотфиксов релиза")
    private List<HotfixDto> hotFixes;
}