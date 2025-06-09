package com.example.javalabaip.controller;

import com.example.javalabaip.cache.CacheManager;
import com.example.javalabaip.dto.LocationResponseDto;
import com.example.javalabaip.dto.UserDto;
import com.example.javalabaip.model.Location;
import com.example.javalabaip.service.IpLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class IpLocationController {

    private final IpLocationService ipLocationService;
    private final CacheManager cacheManager;

    @Autowired
    public IpLocationController(IpLocationService ipLocationService, CacheManager cacheManager) {
        this.ipLocationService = ipLocationService;
        this.cacheManager = cacheManager;
    }

    @PostMapping("/location")
    public ResponseEntity<LocationResponseDto> createLocation(@RequestParam("ip") String ipAddress, @RequestBody UserDto userDto) {
        LocationResponseDto response = ipLocationService.create(ipAddress, userDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/locations/{id}")
    public ResponseEntity<LocationResponseDto> findById(@PathVariable Long id) {
        LocationResponseDto location = ipLocationService.findById(id);
        return location != null ? ResponseEntity.ok(location) : ResponseEntity.notFound().build();
    }

    @GetMapping("/locations")
    public ResponseEntity<List<LocationResponseDto>> findAll() {
        List<LocationResponseDto> locations = ipLocationService.findAll();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/locations/by-username")
    public ResponseEntity<List<LocationResponseDto>> findByUsername(@RequestParam("username") String username) {
        List<LocationResponseDto> locations = ipLocationService.findByUsername(username);
        return ResponseEntity.ok(locations);
    }

    @PutMapping("/locations/{id}")
    public ResponseEntity<LocationResponseDto> update(@PathVariable Long id, @RequestBody LocationResponseDto locationDto) {
        LocationResponseDto updatedLocation = ipLocationService.update(id, locationDto);
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/locations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ipLocationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}