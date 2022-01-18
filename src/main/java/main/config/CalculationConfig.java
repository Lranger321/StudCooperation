package main.config;

import main.service.calculation.PageBuilder;
import main.service.calculation.PageType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class CalculationConfig {

    @Bean
    public Map<PageType, PageBuilder> pageTypePageBuilderMap(List<PageBuilder> builders) {
        return builders.stream()
                .collect(Collectors.toMap(PageBuilder::type, pageBuilder -> pageBuilder));
    }
}
