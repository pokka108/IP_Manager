package com.example.ipmanager.dto;
public class SubnetUpdateRequest {
    private String description;
    private String tags;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
}
