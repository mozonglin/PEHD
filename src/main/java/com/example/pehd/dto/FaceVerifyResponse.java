package com.example.pehd.dto;

/**
 * 人脸验证响应 DTO
 */
public class FaceVerifyResponse {

    /** 是否验证通过 */
    private boolean success;

    /** 说明消息 */
    private String message;

    /** 阿里云返回的人脸相似度（0-100，threshold=70） */
    private Float confidence;

    public FaceVerifyResponse() {}

    public FaceVerifyResponse(boolean success, String message, Float confidence) {
        this.success = success;
        this.message = message;
        this.confidence = confidence;
    }

    public static FaceVerifyResponse ok(float confidence) {
        return new FaceVerifyResponse(true, "验证成功", confidence);
    }

    public static FaceVerifyResponse fail(String message) {
        return new FaceVerifyResponse(false, message, null);
    }

    public static FaceVerifyResponse fail(String message, float confidence) {
        return new FaceVerifyResponse(false, message, confidence);
    }

    // ---- getters / setters ----

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Float getConfidence() { return confidence; }
    public void setConfidence(Float confidence) { this.confidence = confidence; }
}
