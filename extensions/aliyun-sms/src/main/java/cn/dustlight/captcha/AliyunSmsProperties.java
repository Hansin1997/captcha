package cn.dustlight.captcha;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("dustlight.captcha.sender.alibaba.sms")
public class AliyunSmsProperties {

    private String accessKeyId,accessKeySecret;

    private String defaultSignName, defaultTemplateCode;

    private String endpoint = "dysmsapi.aliyuncs.com",protocol="HTTPS";

    private String signatureAlgorithm ="HMAC-SHA1";

    private Integer httpTimeout = 60000;

    private String phoneParamName = "phone";

    public String getPhoneParamName() {
        return phoneParamName;
    }

    public void setPhoneParamName(String phoneParamName) {
        this.phoneParamName = phoneParamName;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getDefaultSignName() {
        return defaultSignName;
    }

    public void setDefaultSignName(String defaultSignName) {
        this.defaultSignName = defaultSignName;
    }

    public String getDefaultTemplateCode() {
        return defaultTemplateCode;
    }

    public void setDefaultTemplateCode(String defaultTemplateCode) {
        this.defaultTemplateCode = defaultTemplateCode;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public Integer getHttpTimeout() {
        return httpTimeout;
    }

    public void setHttpTimeout(Integer httpTimeout) {
        this.httpTimeout = httpTimeout;
    }
}
