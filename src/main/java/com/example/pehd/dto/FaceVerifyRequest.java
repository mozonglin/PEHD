package com.example.pehd.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 人脸验证请求 DTO
 * iOS 客户端传入：前置摄像头拍摄的照片（Base64）+ 参考照片 URL（或学号）
 */
public class FaceVerifyRequest {

    /**
     * 前置摄像头拍摄图片的 Base64 字符串（JPEG 格式，不含 data:image/jpeg;base64, 前缀）
     */
    @NotBlank(message = "capturedImageBase64 不能为空")
    private String capturedImageBase64;

    /**
     * 参考照片 URL（可选）
     * 若为空则使用当前登录用户的学号自动构造：http://38.207.179.218:5000/face?{studentId}
     */
    private String referencePhotoUrl;

    // ---- getters / setters ----

    public String getCapturedImageBase64() {
        return capturedImageBase64;
    }

    public void setCapturedImageBase64(String capturedImageBase64) {
        this.capturedImageBase64 = capturedImageBase64;
    }

    public String getReferencePhotoUrl() {
        return referencePhotoUrl;
    }

    public void setReferencePhotoUrl(String referencePhotoUrl) {
        this.referencePhotoUrl = referencePhotoUrl;
    }
}
