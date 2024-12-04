package dev.usenkonastia.api.config;

import dev.usenkonastia.api.service.mapper.CategoryMapper;
import dev.usenkonastia.api.service.mapper.CosmoCatMapper;
import dev.usenkonastia.api.service.mapper.OrderMapper;
import dev.usenkonastia.api.service.mapper.ProductMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MappersTestConfiguration {

    @Bean
    public CategoryMapper categoryMapper() {
        return Mappers.getMapper(CategoryMapper.class);
    }

    @Bean
    public CosmoCatMapper cosmoCatMapper(){
        return Mappers.getMapper(CosmoCatMapper.class);
    }

    @Bean
    public OrderMapper orderMapper() {
        return Mappers.getMapper(OrderMapper.class);
    }

    @Bean
    public ProductMapper productMapper() { return Mappers.getMapper(ProductMapper.class);}
}
