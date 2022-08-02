package ru.openbank.releasesservice.mapper;

import org.springframework.stereotype.Component;
import ru.openbank.releasesservice.dto.ApplicationServiceDto;
import ru.openbank.releasesservice.model.ApplicationService;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApplicationServiceMapper {

    public List<ApplicationServiceDto> toDtos(List<ApplicationService> services) {
        return services.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ApplicationServiceDto toDto(ApplicationService service) {
        return ApplicationServiceDto.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .build();
    }

    public ApplicationService fromDto(ApplicationServiceDto serviceDto) {
        ApplicationService service = new ApplicationService();
        service.setName(serviceDto.getName());
        service.setDescription(serviceDto.getDescription());
        return service;
    }
}
