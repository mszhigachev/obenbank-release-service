package ru.openbank.releasesservice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Страница", description = "Страница пегенации с динамическим контентом")
public class PageDto<T> {

    @ApiModelProperty(value = "Список контента")
    private List<T> content;

    @ApiModelProperty(value = "Номер страницы")
    private int pageNumber;

    @ApiModelProperty(value = "Размер страницы, количество элементов на 1 странице")
    private int pageSize;

    @ApiModelProperty(value = "Всего страниц")
    private int totalPages;

    @ApiModelProperty(value = "Всего элементов")
    private long totalElements;

    public static PageDto of(Page page) {
        return new PageDto(page.getContent(), page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
    }
}