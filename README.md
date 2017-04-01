# 这里是1.4.1 版本的  (自用的 贡献出来)

***************测试工具库  集成步骤**************
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
	        compile 'com.github.liudao01:LibTest:v1.4.1'
	}
Share this release:

## 在你自己的 Application中 使用下面的代码

***************之前的版本*************
# 这里是1.01 版本的
# LibTest  mytest
上传jitpack 注意事项:
1 将github 项目克隆到非空目录   可以使用tmp  新建一个空tmp 文件夹 再移动.git文件夹  再删除tmp文件夹 再更新
2  必须将gradle 相关的东西 提交到github上面去要不然会编译错误

## 1,   //初始化测试库  网络请求初始化 IP地址初始化
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









//我这个也只是初级版本 还会再不断的修改的  

***********************************************************************************之前的测试工具库  集成步骤**************
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
	        compile 'com.github.liudao01:LibTest:v1.0.1'
	}
Share this release:

	注意 如果需要 切换界面的话 需要悬浮窗权限  得去添加权限为 允许
总共需要三个步骤  1 测试库初始化 在Applaction里面   初始化IP地址 
		2 设置接口地址  用于正式测试接口地址的切换
		3 发送网络请求数据 			总共就是这三部了		
		
## 1,   //初始化测试库  网络请求初始化 IP地址初始化
       if (LogUtil.DEBUG) {
            //网络请求初始化
           TestLibConfig.initWindows(this);
            //IP地址初始化
            TestLibConfig.setSwitchs(this, initSwitchs());
        }


//初始化ip地址 如果有一个 只写一个


 ```java  
  
 public List<IpConfigBeen> initSwitchs() {
        List<IpConfigBeen> list = new ArrayList<>();
        list.add(new IpConfigBeen("http://192.168.1.103:8080/truck/api/", "接口1", false));
        list.add(new IpConfigBeen("http://192.168.1.138:6060/truck/api/", "接口2", false));
        list.add(new IpConfigBeen("http://xx.xx.xx.227:81/truck/api/", "外网1", true));//默认是这个是这个
        list.add(new IpConfigBeen("http://xx.xx.xx.229:8008/truck/api/", "外网2", false));
        list.add(new IpConfigBeen("https://www.xxxxxxxx.com/truck/api/", "正式接口", false));
        return list;
    }
  
```


## 2, 设置自己url 在接口地址里面 设置自己的url 
 ```java
 public static String switchs  = TestLibConfig.getSwitchs(MyApplication.getContext());//设置接口地址前缀   获取当前接口 是正式还是测试
 ```


## 3,发送网络数据  在请求返回结果里面 
 ```java  
  
	//发送网络数据的方法
	 TestLibUtil.getInstance().sendmessage("请求头可以为空", "url 地址", "返回数据结果"));
  
```




## //下面是我项目里面的片段代码 可以作为参考

    /**
     * 请求成功发送json
     * @param url
     * @param params
     * @param Handler
     * @param bytes
     * @param type    类型  1 get 2 post
     */
    private static void toStringJson(String url, RequestParams params, AsyncHttpResponseHandler Handler, byte[] bytes, int type) {
        String json = null;
        if (Handler != null) {
            if (bytes != null) {
                json = Misc.getJson(bytes);
            }
        }

        String str = url + "?" + params.toString();
        if (LogUtil.DEBUG) {
            TestLibUtil.getInstance().sendmessage(client.getHeaderValue(), str, json);
        }
        if (type == 1) {
            str = url + "?" + params.toString();
            LogUtil.d("get请求 url = " + str + "\n" + json);
        } else if (type == 2) {
            str = url + "?" + params.toString();
            LogUtil.d("post 请求  url = " + str + "\n" + json);

        }

    }
    /**
     * 错误类型发送
     * @param url
     * @param params
     * @param Handler
     * @param bytes
     * @param type    类型  1 get 2 post
     */
    private static void toStringJsonError(String url, RequestParams params, AsyncHttpResponseHandler Handler, byte[] bytes, int type, Throwable throwable) {
        String json = null;
        if (Handler != null) {
            if (bytes != null) {
                json = Misc.getJson(bytes);
            }
        }

        String str = url + "?" + params.toString();
        if (LogUtil.DEBUG) {
            TestLibUtil.getInstance().sendmessage(client.getHeaderValue(), str, (throwable.getMessage() + json));
        }
        if (type == 1) {
            str = url + "?" + params.toString();
            LogUtil.d("get请求 url = " + str + "\n" + json);
        } else if (type == 2) {
            str = url + "?" + params.toString();
            LogUtil.d("post 请求  url = " + str + "\n" + json);

        }

    }

