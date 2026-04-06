package com.example.ipmanager.dto;
import jakarta.validation.constraints.NotBlank;
public class SubnetRequest {
    @NotBlank(message = "CIDR is required")
    private String cidr;
    private String description;
    private String tags;

    public String getCidr() { return cidr; }
    public void setCidr(String cidr) { this.cidr = cidr; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
}
