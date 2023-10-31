package com.example.beautystore.model;

public class Members {
    String username, email, password, phoneNumber, profileImage, hinhCCCDMatTruoc, hinhCCCDMatSau, status, role;

    public Members(String username, String email, String password, String phoneNumber, String profileImage, String hinhCCCDMatTruoc, String hinhCCCDMatSau, String status, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.hinhCCCDMatTruoc = hinhCCCDMatTruoc;
        this.hinhCCCDMatSau = hinhCCCDMatSau;
        this.status = status;
        this.role = role;
    }

    public Members() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getHinhCCCDMatTruoc() {
        return hinhCCCDMatTruoc;
    }

    public void setHinhCCCDMatTruoc(String hinhCCCDMatTruoc) {
        this.hinhCCCDMatTruoc = hinhCCCDMatTruoc;
    }

    public String getHinhCCCDMatSau() {
        return hinhCCCDMatSau;
    }

    public void setHinhCCCDMatSau(String hinhCCCDMatSau) {
        this.hinhCCCDMatSau = hinhCCCDMatSau;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
