package com.springcloud.EurekaClientTest.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * FeignClient 기반 Exception 처리(Error 발생시 API response에 대해 Error 분기 등)
 */
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                break;
            case 404:
                if(methodKey.contains("getOrders")) {
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), "User Orders is empty.");
                }
                break;
            default:
                return new Exception(response.reason());
        }

        return null;
    }
}
