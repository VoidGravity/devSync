package com.devsync.service;

import com.devsync.dao.TagDAO;
import com.devsync.model.Tag;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TagService {
    private static final Logger logger = Logger.getLogger(TagService.class.getName());

    public List<Tag> getAllTags() {
        try {
            return TagDAO.getAllTags();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting all tags", e);
            throw new RuntimeException("Failed to retrieve tags", e);
        }
    }

    public Tag getTagById(Long id) {
        try {
            return TagDAO.findTagById(id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting tag by id: " + id, e);
            throw new RuntimeException("Failed to retrieve tag", e);
        }
    }

    public void createTag(Tag tag) {
        try {
            if (tag.getName() == null || tag.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Tag name cannot be empty");
            }
            TagDAO.createTag(tag);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating tag", e);
            throw new RuntimeException("Failed to create tag", e);
        }
    }
}