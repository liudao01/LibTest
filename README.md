[![](https://jitpack.io/v/liudao01/LibTest.svg)](https://jitpack.io/#liudao01/LibTest)
# 主要功能 1 查看网络请求地址  2 收集carsh 3 切换服务器ip 
# 最新版本 看上面那个小按钮 点一下就知道
# 效果图 网络的  
 ![img](https://github.com/liudao01/LibTest/blob/master/demo.gif)
## 好处  给后端人员看  自己也不用每次看开发工具
# 这里是v1.6.0 版本的  (自用的 贡献出来)  这个版本号看发布的版本

***************测试工具库  集成步骤**************

---
具体版本看这里 [![](https://jitpack.io/v/liudao01/LibTest.svg)](https://jitpack.io/#liudao01/LibTest)

2020.5.8 更新 
```
    debugImplementation 'com.github.liudao01.LibTest:myLib:v1.6.0'
    releaseImplementation 'com.github.liudao01.LibTest:mylibrary-no-op:v1.6.0'
```

使用这种方式集成 release 打包的时候不会把代码打进去
---


## 引入
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	       
   	 debugImplementation 'com.github.liudao01.LibTest:myLib:v1.5.4'
   	 releaseImplementation 'com.github.liudao01.LibTest:mylibrary-no-op:v1.5.4'
	}
Share this release:

## 在 application 加上
根目录加上android:supportsRtl="true"
     tools:replace="android:icon,android:name,android:theme,android:allowBackup,android:supportsRtl,android:label"
     
 # 在AndroidManifest 根目录 增加 如下
 
 
         xmlns:tools="http://schemas.android.com/tools"
         
         
## 在你自己的 welcome页面 (第一个页面) 使用下面的代码


```java
# 做一下悬浮窗判断
    在起始页面 或者欢迎页面加入下面代码,或者你自己
    手动加上悬浮窗权限也行 建议用这种方式自动判断
    /**
         * 开始的时候 的权限 判断是否可以让悬浮窗 悬浮到所有应用的前面
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent drawOverlaysSettingsIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                drawOverlaysSettingsIntent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(drawOverlaysSettingsIntent);
            }
        }
	
```


## 1,   //初始化测试库  网络请求初始化 IP地址初始化

```java
if (LogUtil.DEBUG) {//我这里判断的是否是debug版本
            //工具类 初始化
           TestLibUtil.getInstance().startUtil(this);
            //IP地址初始化  这个用不用都行 你想有切换ip的功能 你就加上下面的代码
           TestLibConfig.setSwitchs(this, initSwitchs());
	    IPinit();
        }
        
        
	 public void IPinit() {
        List<IpConfigBeen> list = new ArrayList<>();
        list.add(new IpConfigBeen("http://xx.xx.xx.227:81", "测试服务器", true));//别照抄..你自己的测试服务器地址
       list.add(new IpConfigBeen("http://www.xxxxxx.com:8008", "正式接口", false));
        TestLibUtil.getInstance().initIpSwitchs(this.getApplicationContext(), list);
    }
______________________________________
```


## 2, 如果要有切换ip的功能,设置自己url 在接口地址里面 设置自己的url 
 ```java
 public static String switchs  = TestLibConfig.getSwitchs(MyApplication.getContext());//设置接口地址前缀   获取当前接口 是正式还是测试
 ```
## 3,发送网络数据  在请求返回结果里面 (这里分两种一种用普通的方式, 另外一种用okhttp 或者retrofit拦截的方式(建议用这种))
 ```java  
 //普通方式 发送网络数据的方法
 	 HttpTransaction httpBeen;//请求数据都放在这里面了 自己看下里面可以放些什么
	  httpBeen = new HttpTransaction();
            httpBeen.setMethod(request.method());//请求方式
            httpBeen.setUrl(request.urlString());//请求的url (建议get 或者post 都拼上 )
	    TestLibUtil.getInstance().sendmessage(httpBeen);//发送
	    
	    
// 推荐方式 在Okhttp中加上两行代码
.addInterceptor(new ChuckInterceptor(MyApplication.getInstance()))
.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

//下面是完整的
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new ChuckInterceptor(MyApplication.getInstance()))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(headerInterceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)//超时时间
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)//超时时间
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)//超时时间
                .build();

        return okHttpClient;
  
```

----end----


上传jitpack 注意事项:
1 将github 项目克隆到非空目录   可以使用tmp  新建一个空tmp 文件夹 再移动.git文件夹  再删除tmp文件夹 再更新
2  必须将gradle 相关的东西 提交到github上面去要不然会编译错误


