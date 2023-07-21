package cn.zf233.xcloud.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.FileUtils;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import cn.zf233.xcloud.common.Const;
import cn.zf233.xcloud.common.SdPathConst;
import cn.zf233.xcloud.exception.OpenFileException;

/**
 * Created by zf233 on 11/28/20
 */
public class FileUtil {

    // write to share
    public static void outputShared(Context context, String key, Object object) {
        SharedPreferences shared = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = shared.edit();
        editor.putString(key, JsonUtil.toGson(object));
        editor.apply();
        editor.commit();
    }

    // read from share
    public static <T> T inputShared(Context context, String key, Class<T> clazz) {
        SharedPreferences shared = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        String json = shared.getString(key, "");
        return JsonUtil.toObject(json, clazz);
    }

    // remove to share
    public static void removeShared(Context context, String key) {
        context.deleteSharedPreferences(key);
    }

    // write to external storage
    // 找到一个外部文件，再从文件中读入输出文件流
    public static File outputFile(InputStream inputStream, String fileName) {

        // get external storage path
        String storage = SdPathConst.sdPath;
        File dirFile = new File(storage);
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                return null;
            }
        }
        File outputFile = new File(SdPathConst.sdPath, fileName);
        OutputStream outputStream = null;
        String existsFileName;
        try {
            if (outputFile.exists()) {
                int i = 2;
                while (true) {
                    existsFileName = fileName.substring(0, fileName.indexOf(".")) + "(" + i + ")" + fileName.substring(fileName.indexOf("."));
                    outputFile = new File(SdPathConst.sdPath, existsFileName);
                    if (!outputFile.exists()) {
                       break;
                    }
                    i++;
                }
            }
            /**
             * 安卓10以上版本更新
             */
            boolean newFile = outputFile.createNewFile();
            if (newFile) {
                outputStream = new FileOutputStream(outputFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                return outputFile;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (outputFile.exists()) {
                if (outputFile.delete()) {
                    Log.i("msg", "Error saving file, remove incomplete file");
                }
            }
            return null;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // open from external storage
    public static void openFile(final String filePath, Context context) throws OpenFileException {
        if ("".equals(filePath)) {
            throw new OpenFileException("打开文件路径有误");
        }
        String ext = filePath.substring(filePath.lastIndexOf('.')).toLowerCase(Locale.US);
        StrictMode.VmPolicy defaultVmPolicy = null;
        try {
            defaultVmPolicy = StrictMode.getVmPolicy();
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String temp = ext.substring(1);
            String mime = mimeTypeMap.getMimeTypeFromExtension(temp);
            mime = TextUtils.isEmpty(mime) ? "" : mime;
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            java.io.File file = new java.io.File(filePath);
            intent.setDataAndType(Uri.fromFile(file), mime);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OpenFileException("文件打开失败,未找到对应应用");
        } finally {
            StrictMode.setVmPolicy(defaultVmPolicy);
        }
    }

    // open from external storage
//    public static void openFile(final String filePath, Context context) throws OpenFileException {
//        if ("".equals(filePath)) {
//            throw new OpenFileException("打开文件路径有误");
//        }
//        String ext = filePath.substring(filePath.lastIndexOf('.')).toLowerCase(Locale.US);
//        StrictMode.VmPolicy defaultVmPolicy = null;
//        try {
//            defaultVmPolicy = StrictMode.getVmPolicy();
//            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//            StrictMode.setVmPolicy(builder.build());
//
//            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//            String temp = ext.substring(1);
//            String mime = mimeTypeMap.getMimeTypeFromExtension(temp);
//            mime = TextUtils.isEmpty(mime) ? "" : mime;
//
//            Intent intent = new Intent();
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setAction(Intent.ACTION_VIEW);
//            File file = new File(filePath);
//            intent.setDataAndType(file2Uri(context,file), mime);
//            context.startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new OpenFileException("文件打开失败,未找到对应应用");
//        } finally {
//            StrictMode.setVmPolicy(defaultVmPolicy);
//        }
//    }

    public static Uri file2Uri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, getFileProviderName(context), file);
        } else {
            return Uri.fromFile(file);
        }

    }


    public static String getFileProviderName(Context context) {
        return "cn.zf233.xcloud.fileProvider";
    }

    // uri conversion file path
    /**
     * 对于"file"类型的Uri，直接获取路径；
     * 对于"content"类型的Uri，先通过ContentResolver获取输入流，再将文件内容拷贝到应用的沙盒目录中，并返回这个拷贝后的文件对象。
     * @param uri 文件定位
     * @param context activity context
     * @return 文件
     */
    public static File uriToFileApiQ(Uri uri, Context context) {
        File file = null;
        if (uri == null) return file;
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            String displayName = System.currentTimeMillis() + Math.round((Math.random() + 1) * 1000)
                    + "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri));

            try {
                InputStream is = contentResolver.openInputStream(uri);
                File cache = new File(context.getCacheDir().getAbsolutePath(), displayName);
                FileOutputStream fos = new FileOutputStream(cache);
                FileUtils.copy(is, fos);
                file = cache;
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    // create and write log file
    public static void createAndWriteUncaughtExceptionLog(String exceptionDetail) {
        // get external storage path
        String storage = SdPathConst.sdPath;
        File dirFile = new File(storage);
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                return;
            }
        }
        File logFile = new File(SdPathConst.sdPath, Const.LOG_FILE_NAME.getDesc());
        FileWriter fileWriter = null;
        try {
            if (!logFile.exists()) {
                if (logFile.createNewFile()) {
                    Log.i("msg", "create the log file");
                }
            }
            if (logFile.exists()) {
                fileWriter = new FileWriter(logFile, true);
                fileWriter.append(DateTimeUtil.getNowDateTime()).append("--->").append(exceptionDetail).append("\r");
                fileWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
