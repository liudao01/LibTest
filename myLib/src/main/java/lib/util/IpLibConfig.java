package lib.util;

import android.content.Context;

import java.util.List;

import httploglib.lib.been.IpConfigBeen;


/**
 * @author liuml
 * @explain 当前库的服务器IP设置
 * @time 2016/12/8 14:17
 */

public class IpLibConfig {

    Context mContext;
    List<IpConfigBeen> list;
    ListDataSave sp;
    private static IpLibConfig ipLibConfig;


    public static synchronized IpLibConfig getInstance(Context context) {
        if (ipLibConfig == null) {        //先判断是否为空
            ipLibConfig = new IpLibConfig(context);
        }
        return ipLibConfig;
    }

    public IpLibConfig(Context context) {
        this.mContext = context;
        sp = ListDataSave.getInstance(mContext, ListDataSave.ListDataSave);
    }

    /**
     * ip 初始化 存入list集合
     * @param list
     */
    public void initIpConfig(List<IpConfigBeen> list) {
        this.list = list;
        //保存
        if (list != null && list.size() > 0) {
            sp.setDataList(ListDataSave.listTag, list);
        }
    }

    /**
     * 添加ip
     * @param ipConfigBeen
     */
    public void AddIp(IpConfigBeen ipConfigBeen) {
        list = sp.getDataList(ListDataSave.listTag);
        list.add(ipConfigBeen);
        setListSelect(list);
    }

    /**
     * 删除ip
     * @param i
     */
    public void DelIp(int i) {
        list = sp.getDataList(ListDataSave.listTag);
        list.remove(i);
        setListSelect(list);
    }

    /**
     * 删除全部ip
     */
    public void DelIpAll() {
        sp.setStringToPreference(ListDataSave.listTag, "");
    }


    /**
     * 获取全部ip
     * @return
     */
    public List<IpConfigBeen> getIpList(){
        list = sp.getDataList(ListDataSave.listTag);
        return  list;
    }
    /**
     * 设置list数据
     * @param list
     */
    public void setListSelect(List<IpConfigBeen> list) {
        //保存
        if (list != null && list.size() > 0) {
            sp.setDataList(ListDataSave.listTag, list);
        }
    }
}
