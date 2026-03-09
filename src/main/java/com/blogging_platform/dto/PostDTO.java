package com.blogging_platform.dto;

import com.blogging_platform.models.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class PostDTO {

    @NotEmpty(message = "Title can't be empty")
    @Size(max = 100, message = "Title can be max 100 symbols")
    private String title;

    @NotEmpty(message = "Content can't be empty")
    private String content;

    @NotEmpty(message = "Category can't be empty")
    private String category;

    private List<String> tags;

    public PostDTO(){
        this.tags = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
