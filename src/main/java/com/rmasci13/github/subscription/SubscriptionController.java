package com.rmasci13.github.subscription;

import com.rmasci13.github.enums.BillingCycle;
import com.rmasci13.github.enums.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path="/api/subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // Get all Subscriptions
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SubscriptionDTO>> getSubscriptions() {
        List<SubscriptionDTO> dtos = subscriptionService.getSubscriptionDTOs();
        return ResponseEntity.ok(dtos);
    }

    // Get a single subscription by Subscription ID
    @GetMapping(path="{id}")
    @PreAuthorize("hasRole('ADMIN') or @subscriptionService.isOwner(authentication.principal.id, #id)")
    public ResponseEntity<SubscriptionDTO> getSubscription(@PathVariable Integer id) {
        SubscriptionDTO dto = subscriptionService.getSubscriptionDTOsById(id);
        return ResponseEntity.ok(dto);
    }

    // Create a new Subscription
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or #dto.userID == authentication.principal.id")
    public ResponseEntity<SubscriptionDTO> createSubscription(@RequestBody SubscriptionDTO dto) {
        SubscriptionDTO created = subscriptionService.createSubscription(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    // Edit a current Subscription
    @PutMapping(path="{id}")
    @PreAuthorize("hasRole('ADMIN') or @subscriptionService.isOwner(authentication.principal.id, #id)")
    public ResponseEntity<SubscriptionDTO> updateSubscription(@PathVariable Integer id, @RequestBody SubscriptionDTO dto) {
        SubscriptionDTO updatedDTO = subscriptionService.updateSubscription(id, dto);
        return ResponseEntity.ok(updatedDTO);
    }

    //Delete a Subscription
    @DeleteMapping(path="{id}")
    @PreAuthorize("hasRole('ADMIN') or @subscriptionService.isOwner(authentication.principal.id, #id)")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Integer id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}

