package com.example.pehd.service;

import com.aliyun.facebody20191230.Client;
import com.aliyun.facebody20191230.models.CompareFaceAdvanceRequest;
import com.aliyun.facebody20191230.models.CompareFaceResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.example.pehd.dto.FaceVerifyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;

/**
 * 人脸识别验证服务
 * 后端代理调用阿里云 FaceBody CompareFace Advance API（v6.0.0）
 *
 * 参考 SDK：https://github.com/aliyun/alibabacloud-java-sdk/tree/master/facebody-20191230
 * Maven：com.aliyun:facebody20191230:6.0.0
 *
 * 流程：
 * 1. iOS 端上传拍摄照片（Base64 JPEG）
 * 2. 后端下载参考照片（http://38.207.179.218:5000/face?{studentId}）
 * 3. 调用阿里云 CompareFaceAdvance（两张图片均以 InputStream 方式上传，由 SDK 自动处理 OSS）
 * 4. 返回相似度和验证结果
 */
@Service
public class FaceVerificationService {

    private static final Logger logger = LoggerFactory.getLogger(FaceVerificationService.class);

    // ---- 阿里云配置（与安卓端完全一致）----
    private static final String ACCESS_KEY_ID     = "LTAI5tJqQFkcpgFGLaBrKx3V";
    private static final String ACCESS_KEY_SECRET  = "LGgixg5opofTT2RGtyhR7muOTNx1A0";
    private static final String ENDPOINT           = "facebody.cn-shanghai.aliyuncs.com";

    /** 人脸相似度阈值（与安卓端一致：70.0） */
    private static final float SIMILARITY_THRESHOLD = 70.0f;

    /** 参考照片服务 URL 模板 */
    private static final String REFERENCE_PHOTO_URL_TPL = "http://38.207.179.218:5000/face?%s";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // ---- 懒加载单例客户端 ----
    private volatile Client aliyunClient;

    private Client getClient() throws Exception {
        if (aliyunClient == null) {
            synchronized (this) {
                if (aliyunClient == null) {
                    Config config = new Config()
                            .setAccessKeyId(ACCESS_KEY_ID)
                            .setAccessKeySecret(ACCESS_KEY_SECRET);
                    config.endpoint = ENDPOINT;
                    aliyunClient = new Client(config);
                    logger.info("阿里云 FaceBody 客户端初始化成功，endpoint={}", ENDPOINT);
                }
            }
        }
        return aliyunClient;
    }

    /**
     * 执行人脸验证
     *
     * @param capturedImageBase64 iOS 前置摄像头拍摄图片的 Base64（JPEG，可带或不带 data URI 前缀）
     * @param referencePhotoUrl   参考照片 URL（为 null 时用 studentId 构造）
     * @param studentId           当 referencePhotoUrl 为 null 时使用
     * @return 验证结果
     */
    public FaceVerifyResponse verify(String capturedImageBase64,
                                     String referencePhotoUrl,
                                     String studentId) {
        try {
            // 1. 解码拍摄图片
            byte[] capturedBytes = decodeCapturedImage(capturedImageBase64);
            if (capturedBytes == null || capturedBytes.length == 0) {
                return FaceVerifyResponse.fail("拍摄图片数据为空或 Base64 格式错误");
            }
            logger.info("人脸验证：已解码拍摄图片，大小={} KB", capturedBytes.length / 1024);

            // 2. 获取参考照片
            String refUrl = resolveReferenceUrl(referencePhotoUrl, studentId);
            byte[] referenceBytes = fetchReferencePhoto(refUrl);
            if (referenceBytes == null || referenceBytes.length == 0) {
                return FaceVerifyResponse.fail("无法获取参考照片（URL=" + refUrl + "），请确认已上传人脸照片");
            }
            logger.info("人脸验证：已下载参考照片，大小={} KB，来源={}", referenceBytes.length / 1024, refUrl);

            // 3. 调用阿里云 FaceBody CompareFace Advance
            return callAliyunCompareFace(capturedBytes, referenceBytes);

        } catch (Exception e) {
            logger.error("人脸验证发生异常", e);
            return FaceVerifyResponse.fail("验证服务异常：" + e.getMessage());
        }
    }

    /**
     * 调用阿里云 CompareFaceAdvance API
     *
     * CompareFaceAdvanceRequest 的 imageURLAObject / imageURLBObject 字段接收 InputStream，
     * SDK 内部会将其上传至阿里云 OSS，再以 URL 形式调用 CompareFace 接口，全程透明。
     */
    private FaceVerifyResponse callAliyunCompareFace(byte[] capturedBytes,
                                                      byte[] referenceBytes) throws Exception {
        Client client = getClient();

        InputStream capturedStream  = new ByteArrayInputStream(compressIfNeeded(capturedBytes));
        InputStream referenceStream = new ByteArrayInputStream(referenceBytes);

        CompareFaceAdvanceRequest request = new CompareFaceAdvanceRequest()
                .setImageURLAObject(capturedStream)    // 拍摄照片
                .setImageURLBObject(referenceStream);  // 参考照片

        // RuntimeOptions（使用默认值即可，SDK 内部已有合理超时）
        RuntimeOptions runtime = new RuntimeOptions();

        logger.info("人脸验证：正在调用阿里云 CompareFace API...");
        CompareFaceResponse response = client.compareFaceAdvance(request, runtime);

        if (response == null || response.body == null || response.body.data == null) {
            return FaceVerifyResponse.fail("阿里云 API 返回数据为空");
        }

        // confidence 在 SDK 6.0.0 中是 Float 类型
        Float confidenceObj = response.body.data.confidence;
        float confidence = confidenceObj != null ? confidenceObj : 0f;

        logger.info("人脸验证：阿里云返回相似度={}, 阈值={}", String.format("%.2f", confidence), SIMILARITY_THRESHOLD);

        if (confidence >= SIMILARITY_THRESHOLD) {
            return FaceVerifyResponse.ok(confidence);
        } else {
            return FaceVerifyResponse.fail(
                    String.format("人脸不匹配，相似度 %.1f%% < 阈值 %.0f%%", confidence, SIMILARITY_THRESHOLD),
                    confidence
            );
        }
    }

    /** 构造参考照片 URL */
    private String resolveReferenceUrl(String referencePhotoUrl, String studentId) {
        if (referencePhotoUrl != null && !referencePhotoUrl.isBlank()) {
            return referencePhotoUrl.trim();
        }
        if (studentId != null && !studentId.isBlank()) {
            return String.format(REFERENCE_PHOTO_URL_TPL, studentId.trim());
        }
        throw new IllegalArgumentException("必须提供 referencePhotoUrl 或 studentId");
    }

    /** 从 URL 下载参考照片字节数组 */
    private byte[] fetchReferencePhoto(String url) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<byte[]> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofByteArray());
            if (resp.statusCode() == 200 && resp.body() != null && resp.body().length > 0) {
                return resp.body();
            }
            logger.warn("下载参考照片失败，HTTP={}, URL={}", resp.statusCode(), url);
            return null;
        } catch (Exception e) {
            logger.error("下载参考照片异常，URL={}", url, e);
            return null;
        }
    }

    /**
     * 解码 Base64 图片（自动去除 data URI 前缀）
     * 支持：纯 Base64 / data:image/jpeg;base64,{data} 格式
     */
    private byte[] decodeCapturedImage(String base64) {
        if (base64 == null || base64.isBlank()) return null;
        try {
            String data = base64.trim();
            if (data.contains(",")) {
                data = data.substring(data.indexOf(',') + 1);
            }
            return Base64.getDecoder().decode(data);
        } catch (IllegalArgumentException e) {
            logger.error("Base64 解码失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 图片大小检查（阿里云限制：≤5MB，建议 ≤4MB）
     * iOS 端已预先压缩，此处仅记录警告
     */
    private byte[] compressIfNeeded(byte[] imageBytes) {
        final int MAX_SIZE = 4 * 1024 * 1024; // 4 MB
        if (imageBytes.length > MAX_SIZE) {
            logger.warn("拍摄图片 {} KB 超过 4MB，建议 iOS 端进一步压缩", imageBytes.length / 1024);
        }
        return imageBytes;
    }
}
