package com.rcyouth.socialschedule.service;

import com.rcyouth.socialschedule.model.TemplateRule;
import com.rcyouth.socialschedule.repository.TemplateRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateRuleService {

    private final TemplateRuleRepository templateRuleRepository;

    public TemplateRuleService(TemplateRuleRepository templateRuleRepository) {
        this.templateRuleRepository = templateRuleRepository;
    }

    public List<TemplateRule> getAllTemplateRules() {
        return templateRuleRepository.findAll();
    }

    public TemplateRule saveTemplateRule(TemplateRule templateRule) {
        return templateRuleRepository.save(templateRule);
    }

    public void deleteTemplateRule(Long id) {
        templateRuleRepository.deleteById(id);
    }
}
