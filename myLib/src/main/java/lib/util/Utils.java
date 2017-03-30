package lib.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String TAG = "Utils";

    public static enum NetWorkMethod {NO, CMNET, CMWAP, WIFI}

    //注，正式上线后要改为false
    public static final boolean isTranslucentStatus = false;
    public static final boolean debugMode = true;
    private static final boolean isShowToast = true;

    public static int getDisplayHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int displayHeight = wm.getDefaultDisplay().getHeight();
        return displayHeight;
    }

    public static int getDisplayWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int displaywidth = wm.getDefaultDisplay().getWidth();
        return displaywidth;
    }

    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dip2px(Context context, float px) {
        final float scale = getScreenDensity(context);
        return (int) (px * scale + 0.5);
    }

    public static byte[] readStream(InputStream inputStream) {
        byte[] bre = null;
        if (inputStream != null) {
            byte[] buf = new byte[64];
            int len;
            ByteArrayOutputStream bouf = new ByteArrayOutputStream();
            while (true) {
                try {
                    len = inputStream.read(buf);
                    if (len == -1) {
                        break;
                    }
                    bouf.write(buf, 0, len);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            bre = bouf.toByteArray();
        }

        return bre;
    }

    /*
   * 字符串截取
   * @param String
   * return List<String>
   * */
    public static List<String> strSplit(String str) {
        List<String> lists = new ArrayList<String>();
        String temp_str = str;
        String[] strings = temp_str.split(",");
        lists = Arrays.asList(strings);
        return lists;
    }

    public static String getString(String string) {
        String result = null;
        String v = "'" + string + "'";
        try {
            result = new JSONTokener(v).nextValue().toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getString(Context context, int resId) {
        String str = "";
        if (context != null) {
            Resources resources = context.getResources();
            str = resources.getString(resId);
        }
        return str;
    }

    /**
     * 检测网络连接是否可用
     *
     * @param ctx
     * @return true 可用; false 不可用
     */
    public static boolean isNetwork(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo[] netinfo = cm.getAllNetworkInfo();
        if (netinfo == null) {
            return false;
        }
        for (int i = 0; i < netinfo.length; i++) {
            if (netinfo[i].isConnected()) {
                return true;
            }
        }
        return false;
    }


    /**
     * @param context
     * @return
     * @author sky
     * 获取当前的网络状态  -1：没有网络NO  1：WIFI网络 WIFI 2：wap网络CMWAP 3：net网络 CMNET
     */
    public static NetWorkMethod getAPNType(Context context) {
        NetWorkMethod netType = NetWorkMethod.NO;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = NetWorkMethod.CMNET;
            } else {
                netType = NetWorkMethod.CMWAP;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NetWorkMethod.WIFI;
        }
        return netType;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        return width;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        return height;
    }

    public static void showLog(String TAG, String message) {
        if (debugMode)
            Log.d(TAG, message);
    }

    public static void showLog(String message) {
        if (debugMode)
            Log.d("Debug", message);
    }

    public static void showLog(int i, String message) {
        if (debugMode)
            Log.d("Debug", i + "==" + message);
    }

    public static void showTAGLog(Activity context, String message) {
        if (debugMode)
            Log.d(context.getClass().getCanonicalName(), message);
    }


    public static void showToastShort(Context context, String message) {
        if (context != null && isShowToast)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    public static String GetCurrentTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new Date());
        return date;
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param a
     * @return
     */
    public static String parseTime(String a) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);//MMM dd hh:mm:ss Z yyyy
        Date date = sdf.parse(a);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }


    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param a
     * @return
     */
    public static String parseDate(String a) throws ParseException {
        String dateString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (a == null) {
            Date date = new Date();
            dateString = sdf.format(date);
        } else {
            dateString = sdf.format(a);
        }

        return dateString;
    }

    /**
     * 获取时间格式
     */
    public static String getTimeHH(Object time) {
        try {
            long date = Long.valueOf((String) time);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return df.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            e.getStackTrace();
        }
        return "";
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 获取时间格式
     */
    public static String getTime(Object time) {
        try {
            long date = Long.valueOf((String) time);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            e.getStackTrace();
        }
        return "";
    }

    public static String getCurrentTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new Date());
        return date;
    }

    /**
     * 获取时间格式
     */
    public static String getTime(String time) {
        String result = "";
        if (!TextUtils.isEmpty(time)) {
            try {
                long date = Long.valueOf(time);
                if (date > 0) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    result = df.format(date);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.getStackTrace();
            }
        }

        return result;
    }

    /**
     * 获取时间格式
     */
    public static String getTimeFormat(String time, String format) {
        try {
            long date = Long.valueOf(time);
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            e.getStackTrace();
        }
        return "";
    }

    public static String getTimeDifference(long timesamp) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        int temp = Integer.parseInt(sdf.format(today)) - Integer.parseInt(sdf.format(otherDay));

        switch (temp) {
            case 0:
                result = "今天 ";
                break;
            case 1:
                result = "昨天 ";
                break;
            case 2:
                result = "前天 ";
                break;

            default:
                // result = temp + "天前 ";
                result = getTime(timesamp);
                break;
        }

        return result;
    }

    /**
     * 获取网络状态
     *
     * @return 0--无网络连接，1--wifi连接，2--GPRS连接
     */

    public static int GetNetworkType(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        showLog(TAG, "info===" + info);
        if (info == null) {
            return 0;
        } else {
            int type = info.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                return 1;
            } else {
                showLog(TAG, "info type ===" + type);
                return 2;
            }
        }

    }


    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(new Date());
    }

    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    public static String getJson(byte[] bytes) {
        String json = null;
        try {
            json = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 判断是否有sd卡
     *
     * @return boolean true 是 ， false 否
     */
    public static boolean isSDCARDMounted() {
        String status = Environment.getExternalStorageState();

        if (status.equals(Environment.MEDIA_MOUNTED))
            return true;
        return false;
    }

    //浮点型数据保留小数点
    public static double getDouble(float d) {
        BigDecimal bg = new BigDecimal(d);
        double f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    //获取手机型号
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    //浮点型数据保留小数点
    public static String getFloatFormat(float d) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(d);
    }

    //浮点型数据保留小数点
    public static String getFloatFormat(String str) {
        float f2 = Float.parseFloat(str);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(f2);

    }

    //获取TextView现实的数据
    public static String getTextString(String str) {
        if (!TextUtils.isEmpty(str) && !str.equals("null")) {
            return str;
        } else {
            return "";
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmptyString(String str) {
        return str == null || str.trim().length() == 0 || str.trim().equals("");
    }

    /**
     * 查询手机内非系统应用
     *
     * @param context
     * @return
     */
    public List<PackageInfo> getAllAppsNoSystem(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps;
    }


    public static DatePicker getDatePicker(DatePicker datePicker, String time) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(time) && date != null) {
            cal.setTimeInMillis(date.getTime());
        } else {
            cal.setTimeInMillis(System.currentTimeMillis());
        }

        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), null);
        return datePicker;
    }

    public static void tel(Context context, String tel) {
        if (!TextUtils.isEmpty(tel)) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


    public static void copy(Context context, String content) {
//        ClipboardManager cmb = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
//        cmb.setText(content);
        android.text.ClipboardManager clip = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(content); //复制到剪切板

        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。

        Utils.showToastShort(context, "已复制到剪切板");
    }

    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }

    private static final String EXTRA_ISINITDB = "ISINITDB";
    private static final String EXTRA_DEF_KEYBOARDHEIGHT = "DEF_KEYBOARDHEIGHT";
    /**
     * 键盘默认高度 (dp)
     */
    private static int sDefKeyboardHeight = 300;
    public static int sDefRow = 7;
    public static int sDefLine = 3;

    public static boolean isInitDb(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(EXTRA_ISINITDB, false);
    }

    public static void setIsInitDb(Context context, boolean b) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putBoolean(EXTRA_ISINITDB, b).commit();
    }

    public static int getDefKeyboardHeight(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        int height = settings.getInt(EXTRA_DEF_KEYBOARDHEIGHT, 0);
        if (height > 0 && sDefKeyboardHeight != height) {
            Utils.setDefKeyboardHeight(context, height);
        }
        return sDefKeyboardHeight;
    }

    public static void setDefKeyboardHeight(Context context, int height) {
        if (sDefKeyboardHeight != height) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            settings.edit().putInt(EXTRA_DEF_KEYBOARDHEIGHT, height).commit();
        }
        Utils.sDefKeyboardHeight = height;
    }

    /**
     * 屏幕宽度
     */
    private static int DisplayWidthPixels = 0;
    /**
     * 屏幕高度
     */
    private static int DisplayheightPixels = 0;

    private static void getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        DisplayWidthPixels = dm.widthPixels;// 宽度
        DisplayheightPixels = dm.heightPixels;// 高度
    }

    public static int getWidth(int height) {
        int temp = height / 9;

        return 1;
    }

    public static int getHeight(int width) {
        int temp = width / 16;

        return temp * 9;
    }

    public static int getDisplayWidthPixels(Context context) {
        if (context == null) {
            return -1;
        }
        if (DisplayWidthPixels == 0) {
            getDisplayMetrics(context);
        }
        return DisplayWidthPixels;
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }

    /**
     * 开启软键盘
     */
    public static void openSoftKeyboard(EditText et) {
        InputMethodManager inputManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(et, 0);
    }

    /**
     * 关闭软键盘
     */
    public static void closeSoftKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && ((Activity) context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 根据屏幕宽度 作为图片的 宽度 获得16:9的高度的图片
     *
     * @param context
     * @return
     */
    public static int moblieHeight(Context context) {
        int displayWidth = Utils.getDisplayWidth(context);

        int displayHeight = Utils.getDisplayHeight(context);
        int temp = Utils.getHeight(Utils.px2dip(context, displayWidth));
//        Utils.showLog("屏幕宽度 =" + Utils.px2dip(context, displayWidth) + "  屏幕高度=" + Utils.px2dip(context, displayHeight)
//                + "转化 后根据宽度获得的高度 = " + temp);

        return temp;
    }

    /**
     * 手机号校验
     */
    public static boolean PhoneFormat(String phone) {//手机判断正则表达式
        Pattern pattern = Pattern.compile("^[1][0-9][0-9]{9}$");
        Matcher mc = pattern.matcher(phone);
        return mc.matches();
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(is, null, opt);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bitmap = null;
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr 时间戳
     * @return
     */
    public static String getStandardDate(String timeStr) {

        StringBuffer sb = new StringBuffer();

        long t = Long.parseLong(timeStr);
        long l = System.currentTimeMillis();
        long time = l - (t);
        long mill = (long) Math.ceil(time / 1000);//秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    public static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static void setTranslucentStatus(Context context, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isTranslucentStatus) {
            if (view != null) {
                int height = Utils.dip2px(context, 35);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                if (view != null)
                    view.setLayoutParams(params);
            }
        }
    }

    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param mContext
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */

    public static boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;

        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> serviceList

                = activityManager.getRunningServices(100);

        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {

            if (serviceList.get(i).service.getClassName().equals(className) == true) {

                isRunning = true;

                break;
            }
        }
        return isRunning;

    }
}
