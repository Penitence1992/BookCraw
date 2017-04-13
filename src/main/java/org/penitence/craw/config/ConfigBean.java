package org.penitence.craw.config;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class ConfigBean {

    private String savePath;
    private String url;
    private String suffix;
    private String tagRex;

    private int dep;
    private int threadCount;

    public ConfigBean() {
        savePath = PropertiesUtil.getPropertyValue("savePath");
        url = PropertiesUtil.getPropertyValue("url");
        suffix = PropertiesUtil.getPropertyValue("suffix");
        tagRex = PropertiesUtil.getPropertyValue("tagRex");

        dep = Integer.parseInt(PropertiesUtil.getPropertyValue("dep"));
        threadCount = Integer.parseInt(PropertiesUtil.getPropertyValue("threadCount"));
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getTagRex() {
        return tagRex;
    }

    public void setTagRex(String tagRex) {
        this.tagRex = tagRex;
    }

    public int getDep() {
        return dep;
    }

    public void setDep(int dep) {
        this.dep = dep;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }
}
