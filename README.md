# LibTest  mytest
上传jitpack 注意事项:

1 将github 项目克隆到非空目录   可以使用tmp  新建一个空tmp 文件夹 再移动.git文件夹  再删除tmp文件夹 再更新


2  必须将gradle 相关的东西 提交到github上面去要不然会编译错误

//
//我这个也只是初级版本 还会再不断的修改的  

***************测试工具库  集成步骤**************

	注意 如果需要 切换界面的话 需要悬浮窗权限  得去添加权限为 允许
总共需要三个步骤  1 测试库初始化 在Applaction里面   初始化IP地址 
		2 设置接口地址  用于正式测试接口地址的切换
		3 发送网络请求数据 			总共就是这三部了		
		
 1,   //初始化测试库  网络请求初始化 IP地址初始化
       if (LogUtil.DEBUG) {
            //网络请求初始化
           TestLibConfig.initWindows(this);
            //IP地址初始化
            TestLibConfig.setSwitchs(this, initSwitchs());
        }


//初始化ip地址 如果有一个 只写一个


 

 public List<IpConfigBeen> initSwitchs() {
        List<IpConfigBeen> list = new ArrayList<>();
        list.add(new IpConfigBeen("http://192.168.1.103:8080/truck/api/", "接口1", false));
        list.add(new IpConfigBeen("http://192.168.1.138:6060/truck/api/", "接口2", false));
        list.add(new IpConfigBeen("http://xx.xx.xx.227:81/truck/api/", "外网1", true));//默认是这个是这个
        list.add(new IpConfigBeen("http://xx.xx.xx.229:8008/truck/api/", "外网2", false));
        list.add(new IpConfigBeen("https://www.xxxxxxxx.com/truck/api/", "正式接口", false));
        return list;
    }


2, 在接口地址里面 


     public static String switchs  = TestLibConfig.getSwitchs(MyApplication.getContext());//设置接口地址前缀   获取当前接口 是正式还是测试


3,  在请求返回结果里面 

	发送网络数据的方法
	 TestLibUtil.getInstance().sendmessage("请求头可以为空", "url 地址", "返回数据结果"));



//下面是我项目里面的片段代码 可以作为参考

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

