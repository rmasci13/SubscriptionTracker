package com.rmasci13.github.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path="/api/subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionDTO>> getSubscriptions() {
        List<SubscriptionDTO> dtos = subscriptionService.getSubscriptionDTOs();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<SubscriptionDTO> getSubscription(@PathVariable Integer id) {
        SubscriptionDTO dto = subscriptionService.getSubscriptionDTOsById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<SubscriptionDTO> createSubscription(@RequestBody SubscriptionDTO dto) {
        SubscriptionDTO created = subscriptionService.createSubscription(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<SubscriptionDTO> updateSubscription(@PathVariable Integer id, @RequestBody SubscriptionDTO dto) {
        SubscriptionDTO updatedDTO = subscriptionService.updateSubscription(id, dto);
        return ResponseEntity.ok(updatedDTO);
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Integer id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}

