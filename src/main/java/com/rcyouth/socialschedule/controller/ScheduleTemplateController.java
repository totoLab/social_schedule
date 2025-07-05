package com.rcyouth.socialschedule.controller;

import com.rcyouth.socialschedule.model.ScheduleTemplate;
import com.rcyouth.socialschedule.service.ScheduleTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule-templates")
public class ScheduleTemplateController {

    private final ScheduleTemplateService scheduleTemplateService;

    @Autowired
    public ScheduleTemplateController(ScheduleTemplateService scheduleTemplateService) {
        this.scheduleTemplateService = scheduleTemplateService;
    }

    @GetMapping
    public ResponseEntity<List<ScheduleTemplate>> getAllScheduleTemplates() {
        List<ScheduleTemplate> scheduleTemplates = scheduleTemplateService.getAllScheduleTemplates();
        return ResponseEntity.ok(scheduleTemplates);
    }

    @PostMapping
    public ResponseEntity<ScheduleTemplate> saveScheduleTemplate(@RequestBody ScheduleTemplate scheduleTemplate) {
        ScheduleTemplate savedScheduleTemplate = scheduleTemplateService.saveScheduleTemplate(scheduleTemplate);
        return ResponseEntity.ok(savedScheduleTemplate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduleTemplate(@PathVariable Long id) {
        scheduleTemplateService.deleteScheduleTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
