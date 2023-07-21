package cn.zf233.xcloud.util;

import static java.util.concurrent.TimeUnit.*;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import cn.zf233.xcloud.common.RequestBody;
import cn.zf233.xcloud.common.RequestTypeENUM;
import cn.zf233.xcloud.common.ResponseCodeENUM;
import cn.zf233.xcloud.common.BaseResponse;
import cn.zf233.xcloud.entity.User;
import cn.zf233.xcloud.request.file.FileCreateFolderRequest;
import cn.zf233.xcloud.request.file.FileDownloadRequest;
import cn.zf233.xcloud.request.file.FileRemoveRequest;
import cn.zf233.xcloud.request.user.UserHomeRequest;
import cn.zf233.xcloud.request.user.UserHomeSearchRequest;
import cn.zf233.xcloud.request.user.UserHomeSortRequest;
import cn.zf233.xcloud.request.user.UserLoginRequest;
import cn.zf233.xcloud.request.user.UserRegistRequest;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zf233 on 11/28/20
 * Singleton
 */

public class RequestUtil {

    private Boolean isUsed;
    private Integer requestType;
    private String fileName;
    private String session = "";

    public static final RequestUtil requestUtil = new RequestUtil();

    // 只有一个能请求服务器
    public static synchronized RequestUtil getRequestUtil() {
        return requestUtil;
    }

    // common request
    public <T> BaseResponse<T> requestUserXCloudServer(String url, String requestName, RequestBody requestBody, TypeToken<BaseResponse<T>> token) {
        try {
            OkHttpClient client = new OkHttpClient();
            FormBody body = new FormBody.Builder()
                    .add(requestName, JsonUtil.toGson(requestBody))
                    .build();
            Request request = new Request.Builder()
                    .addHeader("cookie", session)
                    .url(url).post(body)
                    .build();
            Response response;
            response = client.newCall(request).execute();
            System.out.println(response);
            System.out.println(response);
            if (response.isSuccessful()) {
                saveSession(response);
                String json = response.body() != null ? response.body().string() : null;
                if (json != null) {
                    return JsonUtil.toObject(json, token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(ResponseCodeENUM.SYSTEM_ERROR.getCode());
        baseResponse.setMsg("请求超时");
        return baseResponse;
    }

    public <T> BaseResponse<T> requestUserXCloudServer(String url, String requestName, UserRegistRequest requestBody, TypeToken<BaseResponse<T>> token) {
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, JsonUtil.toGson(requestBody));
            Request request = new Request.Builder()
                    .addHeader("cookie", session)
                    .url(url).post(body)
                    .build();
            Response response;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                saveSession(response);
                String json = response.body() != null ? response.body().string() : null;
//                Log.d("===","json:"+json);
                return JsonUtil.toObject(json, token);
//                if (json != null) {
//                    return JsonUtil.toObject(json, token);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(ResponseCodeENUM.SYSTEM_ERROR.getCode());
        baseResponse.setMsg("请求超时");
        return baseResponse;
    }


    public <T> BaseResponse<T> requestUserXCloudServer(String url, String requestName, UserLoginRequest requestBody, TypeToken<BaseResponse<T>> token) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
                    .build();

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, JsonUtil.toGson(requestBody));
            Request request = new Request.Builder()
                    .addHeader("cookie", session)
                    .url(url).post(body)
                    .build();
            Response response;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                saveSession(response);
                String json = response.body() != null ? response.body().string() : null;
//                Log.d("===","json:"+json);
                if (json != null) {
                    return JsonUtil.toObject(json, token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(ResponseCodeENUM.SYSTEM_ERROR.getCode());
        baseResponse.setMsg("请求超时");
        return baseResponse;
    }

    public <T> BaseResponse<T> requestUserXCloudServer(String url, String requestName, UserHomeRequest requestBody, TypeToken<BaseResponse<T>> token) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
                    .build();

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, JsonUtil.toGson(requestBody));
            Request request = new Request.Builder()
                    .addHeader("cookie", session)
                    .url(url).post(body)
                    .build();
            Response response;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                saveSession(response);
                String json = response.body() != null ? response.body().string() : null;
//                Log.d("===","json:"+json);
                if (json != null) {
                    return JsonUtil.toObject(json, token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(ResponseCodeENUM.SYSTEM_ERROR.getCode());
        baseResponse.setMsg("请求超时");
        return baseResponse;
    }

    public <T> BaseResponse<T> requestUserXCloudServer(String url, String requestName, UserHomeSortRequest requestBody, TypeToken<BaseResponse<T>> token) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
                    .build();

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, JsonUtil.toGson(requestBody));
            Request request = new Request.Builder()
                    .addHeader("cookie", session)
                    .url(url).post(body)
                    .build();
            Response response;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                saveSession(response);
                String json = response.body() != null ? response.body().string() : null;
//                Log.d("===","json:"+json);
                if (json != null) {
                    return JsonUtil.toObject(json, token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(ResponseCodeENUM.SYSTEM_ERROR.getCode());
        baseResponse.setMsg("请求超时");
        return baseResponse;
    }

    public <T> BaseResponse<T> requestUserXCloudServer(String url, String requestName, UserHomeSearchRequest requestBody, TypeToken<BaseResponse<T>> token) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
                    .build();

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, JsonUtil.toGson(requestBody));
            Request request = new Request.Builder()
                    .addHeader("cookie", session)
                    .url(url).post(body)
                    .build();
            Response response;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                saveSession(response);
                String json = response.body() != null ? response.body().string() : null;
//                Log.d("===","json:"+json);
                if (json != null) {
                    return JsonUtil.toObject(json, token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(ResponseCodeENUM.SYSTEM_ERROR.getCode());
        baseResponse.setMsg("请求超时");
        return baseResponse;
    }

    // file download
    public <T> File fileDownload(String url, FileDownloadRequest requestBody, TypeToken<BaseResponse<T>> token) {
        try {
            OkHttpClient client = new OkHttpClient
                    .Builder()
                    .connectTimeout(60, SECONDS)
                    .readTimeout(120, SECONDS)
                    .build();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, JsonUtil.toGson(requestBody));
            Request request = new Request.Builder()
                    .addHeader("cookie", session)
                    .url(url).post(body)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                saveSession(response);
                // 从HTTP响应中获取名为"Content-Disposition"的响应头信息
                String header = response.header("Content-Disposition");
                if (header != null && !"".equals(header)) {
                    // 它通过在头信息中查找"filename"字符串的位置，并取出双引号之间的内容，来获取文件名。
                    String filename = header.substring(header.indexOf("filename=") + 9);
                    filename = java.net.URLDecoder.decode(filename, "UTF-8");
//                    Log.d("=====", filename);
//                    Log.d("===", response.body().string());
                    String json = response.body() != null ? response.body().string() : null;
                    if (json != null) {
                        BaseResponse<T> responseData = JsonUtil.toObject(json, token);
                        String data = (String) responseData.getData();
                        byte[] byteArray = data.getBytes(StandardCharsets.UTF_8);
                        // Create an InputStream from the byte array
                        InputStream inputStream = new ByteArrayInputStream(byteArray);
                        if (null == inputStream) {
                            return null;
                        }
                        return FileUtil.outputFile(inputStream, filename);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // file remove
    public BaseResponse fileRemove(String url, FileRemoveRequest requestBody) {
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, JsonUtil.toGson(requestBody));
            Request request = new Request.Builder()
                    .addHeader("cookie", session)
                    .url(url)
                    .post(body)
                    .build();
            Response response;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                saveSession(response);
                String json = response.body() != null ? response.body().string() : null;
                if (json != null) {
                    return JsonUtil.toObject(json, BaseResponse.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(ResponseCodeENUM.SYSTEM_ERROR.getCode());
        baseResponse.setMsg("请求超时");
        return baseResponse;
    }

    // create folder
    public BaseResponse createFolder(String url, FileCreateFolderRequest requestBody) {
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, JsonUtil.toGson(requestBody));
            Request request = new Request.Builder()
                    .addHeader("cookie", session)
                    .url(url)
                    .post(body)
                    .build();
            Response response;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                saveSession(response);
                String json = response.body() != null ? response.body().string() : null;
                if (json != null) {
                    return JsonUtil.toObject(json, BaseResponse.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(ResponseCodeENUM.SYSTEM_ERROR.getCode());
        baseResponse.setMsg("请求超时");
        return baseResponse;
    }

    // file upload
    public BaseResponse uploadFile(String url, User user, File uploadFile, Integer parentid) {
        OkHttpClient client = new OkHttpClient
                .Builder()
                .connectTimeout(60, SECONDS)
                .readTimeout(120, SECONDS)
                .build();
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("myFile", uploadFile.getName(),
                        okhttp3.RequestBody.create(MediaType.parse("multipart/form-data"), uploadFile))
                .addFormDataPart("id", user.getId().toString())
                .addFormDataPart("parentid", parentid != null ? parentid.toString() : "")
//                .addFormDataPart("appVersionCode", RequestTypeENUM.VERSION_FAILURE.getDesc())
                .build();
        Request request = new Request.Builder()
                .addHeader("cookie", session)
                .url(url)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                saveSession(response);
                String json = response.body() != null ? response.body().string() : null;
                if (json != null) {
                    return JsonUtil.toObject(json, BaseResponse.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(ResponseCodeENUM.SYSTEM_ERROR.getCode());
        baseResponse.setMsg("请求超时");
        return baseResponse;
    }

    private void saveSession(Response response) {
        String jsession = response.headers().get("Set-Cookie");
        if (StringUtils.isNotBlank(jsession)) {
            this.session = jsession.substring(0, jsession.indexOf(";"));
        }
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean used) {
        isUsed = used;
    }

    public Integer getRequestType() {
        return requestType;
    }

    public void setRequestType(Integer requestType) {
        this.requestType = requestType;
    }

    public String getFilename() {
        return fileName;
    }

    public void setFilename(String fileName) {
        this.fileName = fileName;
    }

    private RequestUtil() {
        this.isUsed = false;
        this.requestType = RequestTypeENUM.UNKNOWN_TYPE.getCode();
        this.fileName = null;
    }
}
