package com.springcloud.springcloudcatalogservice.controller;

import com.springcloud.springcloudcatalogservice.domain.CatalogDto;
import com.springcloud.springcloudcatalogservice.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/catalog-service")
public class CatalogController {
    private final Environment env;

    @Value("${messages.health-check}")
    private String healthCheckMessage;

    private final CatalogService catalogService;

    @GetMapping("/health-check")
    public String status() {
        return String.format("[msg] %s  [port] %s", healthCheckMessage, env.getProperty("local.server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List> getCatalogs() {
        List<CatalogDto> catalogDtoList = catalogService.getAllCatalog();

        return ResponseEntity.status(HttpStatus.OK).body(catalogDtoList);
    }
}
