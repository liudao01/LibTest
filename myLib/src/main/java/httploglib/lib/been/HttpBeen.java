package httploglib.lib.been;

import java.io.Serializable;

/**
 * @author liuml
 * @explain
 * @time 2016/12/6 23:25
 */

public class HttpBeen implements Serializable {

    private String url;
    private String json;
    private String httpHeader;

    public String getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(String httpHeader) {
        this.httpHeader = httpHeader;
    }

    public HttpBeen(String url, String json) {
        this.url = url;
        this.json = json;
    }

    public HttpBeen(String url, String json, String httpHeader) {
        this.url = url;
        this.json = json;
        this.httpHeader = httpHeader;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

}
