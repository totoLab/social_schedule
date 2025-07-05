package com.rcyouth.socialschedule.controller;

import com.rcyouth.socialschedule.config.Config;
import com.rcyouth.socialschedule.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/config")
public class ConfigController {

    private final ConfigService configService;

    @Autowired
    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping
    public ResponseEntity<Config> getGlobalConfig() {
        Config config = configService.getGlobalConfig();
        return ResponseEntity.ok(config);
    }

    @PutMapping
    public ResponseEntity<Config> updateGlobalConfig(@RequestBody Config newConfig) {
        Config updatedConfig = configService.updateGlobalConfig(newConfig);
        return ResponseEntity.ok(updatedConfig);
    }
}
