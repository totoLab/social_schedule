package com.rcyouth.socialschedule.service;

import com.rcyouth.socialschedule.model.ContentType;
import com.rcyouth.socialschedule.repository.ContentTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentTypeService {

    private final ContentTypeRepository contentTypeRepository;

    public ContentTypeService(ContentTypeRepository contentTypeRepository) {
        this.contentTypeRepository = contentTypeRepository;
    }

    public List<ContentType> getAllContentTypes() {
        return contentTypeRepository.findAll();
    }

    public ContentType saveContentType(ContentType contentType) {
        return contentTypeRepository.save(contentType);
    }

    public void deleteContentType(Long id) {
        contentTypeRepository.deleteById(id);
    }
}
