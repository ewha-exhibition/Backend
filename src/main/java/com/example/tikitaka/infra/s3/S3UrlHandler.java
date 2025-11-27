package com.example.tikitaka.infra.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3UrlHandler {

    private final S3UrlGenerator s3UrlGenerator;

    public S3Url handle(String prefix) {
        final String url = s3UrlGenerator.generateUrl(prefix);
        return S3Url.to(url);
    }

    public String extractKeyFromUrl(String url) {
        if (url == null || url.isBlank()) {
            return "";
        }

        String baseUrl;
        String key;

        try {
            URI uri = new URI(url);

            baseUrl = uri.getScheme() + "://" + uri.getHost() + "/";

            String path = uri.getPath();
            if (path == null || path.isEmpty() || "/".equals(path)) {
                return "";
            }
            key = path.startsWith("/") ? path.substring(1) : path;

        } catch (Exception e) {
            int queryIdx = url.indexOf('?');
            String noQuery = (queryIdx > -1) ? url.substring(0, queryIdx) : url;

            int schemeIdx = noQuery.indexOf("://");
            if (schemeIdx < 0) return "";

            int hostEnd = noQuery.indexOf('/', schemeIdx + 3);
            if (hostEnd < 0) {
                return "";
            }

            baseUrl = noQuery.substring(0, hostEnd + 1);

            key = noQuery.substring(hostEnd + 1);
        }
        return baseUrl + key;
    }

}
