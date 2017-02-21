package httploglib.lib.been;

import java.io.Serializable;

/**
 * @author liuml
 * @explain
 * @time 2016/12/6 23:25
 */

public class IpConfigBeen implements Serializable {

    private String url;
    private boolean isSelect;
    private String urlName;

    public IpConfigBeen(String url, String urlName, boolean isSelect) {
        this.url = url;
        this.isSelect = isSelect;
        this.urlName = urlName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    @Override
    public String toString() {
        return "IpConfigBeen{" +
                "url='" + url + '\'' +
                ", isSelect=" + isSelect +
                ", urlName='" + urlName + '\'' +
                '}';
    }
}
