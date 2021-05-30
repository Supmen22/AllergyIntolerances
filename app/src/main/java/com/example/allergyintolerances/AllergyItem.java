package com.example.allergyintolerances;

public class AllergyItem {
    private String patients;
    private String manifestation;
    private String type;
    private String substance;
    private String id;

    public AllergyItem() {
    }

    public AllergyItem(String patients, String manifestation, String type, String substance) {
        this.patients = patients;
        this.manifestation = manifestation;
        this.type = type;
        this.substance = substance;
    }

    public String getPatients() {
        return patients;
    }
    public void setPatients(String patients) {
        this.patients = patients;
    }
    public String getManifestation() {
        return manifestation;
    }
    public void setManifestation(String manifestation) {
        this.manifestation = manifestation;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSubstance() {
        return substance;
    }
    public void setSubstance(String substance) {
        this.substance = substance;
    }


    public String _getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}