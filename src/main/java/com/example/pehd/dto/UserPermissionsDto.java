package com.example.pehd.dto;

public class UserPermissionsDto {
    
    private Boolean cameraPermission;
    private Boolean locationPermission;
    private Boolean storagePermission;
    private Boolean notificationPermission;
    private Boolean phonePermission;
    
    // Constructors
    public UserPermissionsDto() {}
    
    public UserPermissionsDto(Boolean cameraPermission, Boolean locationPermission, 
                             Boolean storagePermission, Boolean notificationPermission, 
                             Boolean phonePermission) {
        this.cameraPermission = cameraPermission;
        this.locationPermission = locationPermission;
        this.storagePermission = storagePermission;
        this.notificationPermission = notificationPermission;
        this.phonePermission = phonePermission;
    }
    
    // Getters and Setters
    public Boolean getCameraPermission() { return cameraPermission; }
    public void setCameraPermission(Boolean cameraPermission) { this.cameraPermission = cameraPermission; }
    
    public Boolean getLocationPermission() { return locationPermission; }
    public void setLocationPermission(Boolean locationPermission) { this.locationPermission = locationPermission; }
    
    public Boolean getStoragePermission() { return storagePermission; }
    public void setStoragePermission(Boolean storagePermission) { this.storagePermission = storagePermission; }
    
    public Boolean getNotificationPermission() { return notificationPermission; }
    public void setNotificationPermission(Boolean notificationPermission) { this.notificationPermission = notificationPermission; }
    
    public Boolean getPhonePermission() { return phonePermission; }
    public void setPhonePermission(Boolean phonePermission) { this.phonePermission = phonePermission; }
} 