package com.kthorpe.urlshortener.repository;

import com.kthorpe.urlshortener.entity.UrlEntity;
import org.springframework.data.repository.Repository;

public interface IUrlRepository extends Repository<UrlEntity, String> {
    UrlEntity findByUrl(String url);

    UrlEntity findByEncodedUrl(String encodedUrl);

    UrlEntity save(UrlEntity urlEntity);
}
