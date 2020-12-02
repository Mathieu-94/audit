package fr.gendarmerie.stsisi.audit.pojo;

import com.google.gson.annotations.SerializedName;

public class Tracker {

    private int id;
    private String name;
    private String description;
    @SerializedName("creation_date")
    private String creationDate;
    @SerializedName("code_signature")
    private String codeSignature;
    @SerializedName("network_signature")
    private String networkSignature;
    private String website;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCodeSignature() {
        return codeSignature;
    }

    public void setCodeSignature(String codeSignature) {
        this.codeSignature = codeSignature;
    }

    public String getNetworkSignature() {
        return networkSignature;
    }

    public void setNetworkSignature(String networkSignature) {
        this.networkSignature = networkSignature;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}