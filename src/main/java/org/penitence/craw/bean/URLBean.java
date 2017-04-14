package org.penitence.craw.bean;

/**
 * Created by RenJie on 2017/4/14 0014.
 */
public class URLBean {

    private String url;

    private int currentDep;

    public URLBean(String url, int currentDep) {
        this.url = url;
        this.currentDep = currentDep;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCurrentDep() {
        return currentDep;
    }

    public void setCurrentDep(int currentDep) {
        this.currentDep = currentDep;
    }
}
