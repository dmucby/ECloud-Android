package com.boyu.wang_pan.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.IOUtils;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.boyu.wang_pan.config.OssConfig;
import org.apache.commons.codec.CharEncoding;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by zf233 on 2021/1/12
 */
public class OSSUtil {

    /**
     * 文件以数据流的形式上传
     * @param ossClient 客户端
     * @param filename 文件名
     * @param content 文件内容
     */
    public void upload(OSS ossClient, String filename, Integer user_id, byte[] content) {
        // 文件
        // Encoding failed to persist with a random name
        PutObjectRequest putObjectRequest = new PutObjectRequest(OssConfig.BUCKET_NAME, "wang_pan/" + user_id.toString() + "/" + filename, new ByteArrayInputStream(content));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentDisposition("attachment;filename=" + filename);
        metadata.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        metadata.setContentLength(content.length);
        putObjectRequest.setMetadata(metadata);

        ossClient.putObject(putObjectRequest);
    }

    public Boolean objectNameExists(OSS ossClient, String objectName) {
        return ossClient.doesObjectExist(OssConfig.BUCKET_NAME, "wang_pan/" + objectName);
    }

    public void delete(OSS ossClient, Integer user_id, String objectName) {
        ossClient.deleteObject(OssConfig.BUCKET_NAME, "wang_pan/" + user_id.toString() + "/" + objectName);
    }

    public static String download(OSS ossClient, Integer user_id, String objectName){
        OSSObject ossObject = ossClient.getObject(OssConfig.BUCKET_NAME, "wang_pan/" + user_id.toString() + "/" + objectName);
        if(ossObject == null){
            return null;
        }
        try {
            return IOUtils.readStreamAsString(ossObject.getObjectContent(),
                    CharEncoding.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
