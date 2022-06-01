package com.springcloud.springcloudcatalogservice.service;

import com.springcloud.springcloudcatalogservice.domain.CatalogDto;
import com.springcloud.springcloudcatalogservice.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {
    private final CatalogRepository catalogRepository;

    @Override
    public List getAllCatalog() {
        List<CatalogDto> catalogDtoList = new ArrayList<>();
        catalogRepository.findAll().forEach(catalog -> {
            catalogDtoList.add(new ModelMapper().map(catalog, CatalogDto.class));
        });
        return catalogDtoList;
    }
}
