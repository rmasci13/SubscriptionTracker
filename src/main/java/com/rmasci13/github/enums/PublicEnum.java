package com.rmasci13.github.enums;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/public/api/enum")
public class PublicEnum {

    @GetMapping
    public ResponseEntity<Map<String, List<String>>> getEnums() {
        Map<String, List<String>> map = new HashMap<>();

        map.put("categories", Arrays.stream(Category.values())
                .map(Enum::name)
                .toList());

        map.put("billingCycles", Arrays.stream(BillingCycle.values())
                .map(Enum::name)
                .toList());

        map.put("statuses", Arrays.stream(Status.values())
                .map(Enum::name)
                .toList());

        return ResponseEntity.ok(map);
    }
}
