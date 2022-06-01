package com.springcloud.springcloudcatalogservice.repository;

import com.springcloud.springcloudcatalogservice.domain.Catalog;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CatalogRepository extends CrudRepository<Catalog, Long> {
    Optional<Catalog> findByProductId(String productId);
}
