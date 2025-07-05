package com.rcyouth.socialschedule.controller;

import com.rcyouth.socialschedule.model.TemplateRule;
import com.rcyouth.socialschedule.service.TemplateRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/template-rules")
public class TemplateRuleController {

    private final TemplateRuleService templateRuleService;

    @Autowired
    public TemplateRuleController(TemplateRuleService templateRuleService) {
        this.templateRuleService = templateRuleService;
    }

    @GetMapping
    public ResponseEntity<List<TemplateRule>> getAllTemplateRules() {
        List<TemplateRule> templateRules = templateRuleService.getAllTemplateRules();
        return ResponseEntity.ok(templateRules);
    }

    @PostMapping
    public ResponseEntity<TemplateRule> saveTemplateRule(@RequestBody TemplateRule templateRule) {
        TemplateRule savedTemplateRule = templateRuleService.saveTemplateRule(templateRule);
        return ResponseEntity.ok(savedTemplateRule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplateRule(@PathVariable Long id) {
        templateRuleService.deleteTemplateRule(id);
        return ResponseEntity.noContent().build();
    }
}
