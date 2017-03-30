package lib.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;


import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

/**
 * @author liuml.
 * @explain 文件操作工具类
 * @time 2016/12/7 16:29
 */
public class FileUtils {
    private static final String TAG = "FileUtils";
    public final static String sd_card = Environment.getExternalStorageDirectory() + "/";
    public String cacheDir;
    private String extension = "";
    Context context;
    public final static String Crash = "crash";

    public static String getCrashPath() {
        String path = sd_card + Crash;
        return sd_card + Crash;
    }

    public static File getCrashFile() {
        File dir = new File(getCrashPath());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static void getRootPath() {
        File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        Logger.d(sdDir.getAbsolutePath().toString());
    }


    /**
     * 创建文件夹
     * * @param context
     * * @String dir  文件夹目录名
     */
    public void mkDir(String dir) {
        File file;
        file = new File(dir + "/");
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 图片保存工具类 文件形式
     *
     * @param path
     * @param fileName
     * @param data
     * @throws IOException
     */
    private void saveData(String path, String fileName, String data) throws IOException {
        // String name = MyHash.mixHashStr(AdName);
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdir();
        }
        File file = new File(path + fileName + extension);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream outStream = new FileOutputStream(file);
        byte[] bytes = data.getBytes();
        outStream.write(bytes);
        outStream.close();
    }


    public String getExtension(String url) {
        String extension = "";
        if (url != null) {
            int position = url.lastIndexOf(".");
            if (position != -1) {
                extension = url.substring(position);
            }
        }
        return extension;
    }


    //转成16进制字符串
    private String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    public void deleteFile(File file) {
        File[] list = file.listFiles();
        for (int i = 0; i < list.length; i++) {
            if (list[i].isDirectory()) {
                File file1 = new File(list[i].getAbsolutePath());
                deleteFile(file1);
            } else {
                list[i].delete();
            }
        }
    }


    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long 单位为M
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size;
        //1048576
    }

    /**
     * 文件大小单位转换
     *
     * @param size
     * @return
     */
    public static String setFileSize(long size) {
        DecimalFormat df = new DecimalFormat("###.##");
        float f = ((float) size / (float) (1024 * 1024));

        if (f < 1.0) {
            float f2 = ((float) size / (float) (1024));

            return df.format(new Float(f2).doubleValue()) + "KB";

        } else {
            return df.format(new Float(f).doubleValue()) + "M";
        }

    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath)
            throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);

            if (file.isDirectory()) {// 处理目录
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {// 如果是文件，删除
                    file.delete();
                } else {// 目录
                    if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                        file.delete();
                    }
                }
            }
        }
    }


    public static boolean getFileFromServerWithName(String urlDownload, String fileName) {
        File f = new File(fileName.substring(0, fileName.lastIndexOf("/")));
        if (!f.exists()) {
            f.mkdirs();
        }

        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            URL url = new URL(urlDownload);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(fileName);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.close();
            is.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public String[] searchFile(String keyword) {
        String result = "";
        File[] files = new File(sd_card).listFiles();
        String str[] = new String[files.length];
        int count = 0;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.getName().indexOf(keyword) >= 0) {
                boolean b = file.isFile();
                if (b) {

                    str[count++] = file.getName();
                }

            }
        }
        return str;
    }

    /**
     * 获取文件的十六进制 字符串
     *
     * @param path 文件所在的路径
     * @return String
     */
    public static String getVoiceData(String path) {
        String voice = null;
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                byte[] bytes = FileUtils.getByteArrayFromFile(path);
                if (bytes != null && bytes.length > 0) {
                    voice = FileUtils.byte2hex(bytes);
                }
            }
        }
        return voice;
    }

    /**
     * 获取文件的字节数组
     *
     * @param fileName 文件所在的路径
     * @return byte[]
     */
    public static byte[] getByteArrayFromFile(String fileName) {
        File file = null;
        try {
            file = new File(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            return null;
        }
        byte[] byteArray = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = fis.read()) != -1) {
                bytestream.write(ch);
            }
            byteArray = bytestream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return byteArray;
    }

    /**
     * 十六进制转字符串
     *
     * @param b 字节数组
     * @return String
     */
    public static String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                sb.append("0" + stmp);
            } else {
                sb.append(stmp);
            }
        }
        return sb.toString();
    }

    /* 在手机上打开文件的method */
    public static void openFile(File f, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

		    /* 调用getMIMEType()来取得MimeType */
        String type = getMIMEType(f);
            /* 设置intent的file与MimeType */
        Uri uri = Uri.fromFile(f);
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }

    /* 判断文件MimeType的method */
    private static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
            /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".")
                + 1, fName.length()).toLowerCase();

		    /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
              /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        } else {
            type = "*";
        }
            /*如果无法直接打开，就跳出软件列表给用户选择 */
        if (end.equals("apk")) {
        } else {
            type += "/*";
        }
        return type;
    }
}
