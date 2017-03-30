package lib.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.File;

/**
 * 通用数据
 *
 * @author liuml
 * @time 2016/6/17 0017 下午 3:45
 */

public class CommonData {

    private static final String TAG = "CommonData";
    public static String BABY_MEMBER_TYPE = "member_type";

    public static final int SDK_PAY_FLAG = 1;

    public static final String PARAMS_URL = "url";//请求链接
    public static final String PARAMS_ID = "id";//请求链接
    public static final String CLASS_ID = "class_id";//请求链接
    public static final String PARAMS_TYPE = "type";//请求链接
    public static final String PARAMS_FROM = "from";//请求链接
    public static final String PARAMS_STATE = "state";//状态值
    public static final String PARAMS_OTHER = "other";//其他参数
    public static final String PARAMS_PERMISSION = "permission";//权限传递
    public static final String PARAMS_TITLE = "title";//名称
    public static final String PARAMS_CONTENT = "content";//内容
    public static final String PARAMS_HINT = "hint";//提示信息
    public static final String PARAMS_BOOLEAN = "boolean";//布尔参数
    public static final String MAX_LENGTH = "max_length";//最大的长度
    public static final String PARAMS_POSITION = "position";//位置
    public static final String PARAMS_PHOTO = "photo_url";//图片地址
    public static final String PARAMS_ACTION = "action";//广播注册
    public static final String PASSWORD = "password";//密码

    public static String MAPS = "maps";

    public static final String PARAMS_PAGE = "type";//分页请求标识
    public static final String PARAMS_PAGE_START = "0";//分页请求 起始页默认时间
    public static final String TYPE_REFRESH = "0";//刷新
    public static final String TYPE_MORE = "1";//获取更多
    public static final String PARAMS_LOGIN = "isLogin";//账号再其它设备上登录

    public static final String SHARE_SCHOOL_NAME = "school_name";//分享学校名称
    public static final String SHARE_SCHOOL_CONTENT = "school_content";//分享学校名称


    public static final int COMMON_DIVIDER_VISIBLE = 0;//item显示间隙
    public static final int COMMON_DIVIDER_GONE = 1;//item显示间隙



    /* 请求码 */
    public static final int REQUEST_CODE_ALBUM = 0;
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_VIDEO = 2;
    public static final int REQUEST_CODE_CROP = 3;
    public static final int REQUEST_CODE_CROP_ = 4;
    public static final int REQUEST_CODE_KITKAT = 5;//4.4版本以上

    public static final String CODE_TYPE_PARAMS = "codeType";//验证码类型
    public static final int CODE_TYPE_BIND = 1;//绑定手机号
    public static final int CODE_TYPE_CHANGE = 2;//更换手机
    public static final int CODE_TYPE_REGISTER = 3;//注册
    public static final int CODE_TYPE_PASSWORD = 4;//忘记密码


    private static CommonData instance;
    private Drawable drawableHot, drawableNew, drawableTu;

    public static synchronized CommonData getInstance() {
        if (instance == null) {
            instance = new CommonData();
        }
        return instance;
    }

    public CommonData() {
    }


    /**
     * 打开文件
     *
     * @param file
     */
    public static void openFile(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        //跳转
        try {
            context.startActivity(intent);     //这里最好try一下，有可能会报错。 //比如说你的MIME类型是打开邮箱，但是你手机里面没装邮箱客户端，就会报错。
        } catch (Exception e) {
            e.printStackTrace();
            String message = "没有相应的应用程序打开该文件！";
        }
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    public static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
    /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < CommonData.MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(CommonData.MIME_MapTable[i][0]))
                type = CommonData.MIME_MapTable[i][1];
        }
        return type;
    }

    public static final String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".amr", "audio/MP3"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };


}
