package com.hit.userservice.base;

import org.springframework.http.ResponseEntity;

public class VsResponseUtil {
    private VsResponseUtil() {}

    public static <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok(body);
    }
}
