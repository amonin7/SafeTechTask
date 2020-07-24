package com.safetech.jobtask.service1.controller;

public class CustomResponseEntity {

    private static final String key = "Verification status";

    private String verificationStatus;

    public CustomResponseEntity(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public CustomResponseEntity() {
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

}
