package com.rcyouth.socialschedule.service;

import com.rcyouth.socialschedule.model.ScheduleTemplate;
import com.rcyouth.socialschedule.repository.ScheduleTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleTemplateService {

    private final ScheduleTemplateRepository scheduleTemplateRepository;

    public ScheduleTemplateService(ScheduleTemplateRepository scheduleTemplateRepository) {
        this.scheduleTemplateRepository = scheduleTemplateRepository;
    }

    public List<ScheduleTemplate> getAllScheduleTemplates() {
        return scheduleTemplateRepository.findAll();
    }

    public ScheduleTemplate saveScheduleTemplate(ScheduleTemplate scheduleTemplate) {
        return scheduleTemplateRepository.save(scheduleTemplate);
    }

    public void deleteScheduleTemplate(Long id) {
        scheduleTemplateRepository.deleteById(id);
    }
}
