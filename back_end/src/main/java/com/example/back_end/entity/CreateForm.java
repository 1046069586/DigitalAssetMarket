package com.example.back_end.entity;


public class CreateForm {

    private String name;
    private String externalLink;
    private String description;
    private String url;
    private String type;
    private long size;
    private String createUser;
    private String id;
    private String rightURI;

    @Override
    public String toString() {
        return "CreateForm{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", externalLink='" + externalLink + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", createUser='" + createUser + '\'' +
                ", rightURI='" + rightURI + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRightURI() {
        return rightURI;
    }

    public void setRightURI(String rightURI) {
        this.rightURI = rightURI;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}