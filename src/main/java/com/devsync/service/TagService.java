package com.devsync.service;

import com.devsync.dao.TagDAO;
import com.devsync.model.Tag;

import java.util.List;

public class TagService {

    public List<Tag> getAllTags() {
        return TagDAO.getAllTags();
    }

    public Tag getTagById(Long id) {
        return TagDAO.findTagById(id);
    }

    public void createTag(Tag tag) {
        TagDAO.createTag(tag);
    }
}