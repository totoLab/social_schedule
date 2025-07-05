package com.rcyouth.socialschedule.controller;

import com.rcyouth.socialschedule.config.Config;
import com.rcyouth.socialschedule.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/schedules/{scheduleName}/config")
public class ScheduleConfigController {

    private final ConfigService configService;

    @Autowired
    public ScheduleConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping
    public ResponseEntity<Config> getScheduleBoundConfig(@PathVariable String scheduleName) {
        // For now, this returns the global config as per the API spec.
        // In the future, if schedules can have their own specific configs, this would change.
        Config config = configService.getGlobalConfig();
        return ResponseEntity.ok(config);
    }
}
