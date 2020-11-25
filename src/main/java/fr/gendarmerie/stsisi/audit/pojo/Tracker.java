package fr.gendarmerie.stsisi.audit.pojo;

public class Tracker {

    private int id;
    private String name;
    private String description;
    private String creation_date;
    private String code_signature;
    private String network_signature;
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

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getCode_signature() {
        return code_signature;
    }

    public void setCode_signature(String code_signature) {
        this.code_signature = code_signature;
    }

    public String getNetwork_signature() {
        return network_signature;
    }

    public void setNetwork_signature(String network_signature) {
        this.network_signature = network_signature;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}