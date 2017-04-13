package org.penitence.craw.tools;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.penitence.craw.event.HitTargetListener;
import org.penitence.craw.thread.URLCrawThread;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class Crawler {

    private static ConcurrentHashMap<String,String> crawCache = new ConcurrentHashMap<>(); //已经爬取过的网页的缓存

    private String url = "";
    private String suffix = "";
    private String tagReg = "";
    private int dep = 3;
    private String hostName = "";
    private String basePath = "";
    private String currentPath = "";
    private String selectQ = "a[href]";
    private Pattern pattern ;

    public Crawler(String suffix, String tagReg) {
        this.suffix = suffix;
        this.tagReg = tagReg;
    }

    public Crawler (String sUrl, String suffix, String tagReg){
        this.url = sUrl;
        this.suffix = StringUtil.isBlank(suffix) ? ".html" : suffix;
        pattern = Pattern.compile(tagReg);
        hostName = getHostName();
        basePath = getBasePath(sUrl);
    }

    public void setCrawDepth(int dep){
        this.dep = dep;
    }

    public int getCrawDepth(){
        return dep;
    }

    public String getSelectQ() {
        return selectQ;
    }

    public void setSelectQ(String selectQ) {
        this.selectQ = selectQ;
    }

    public void startCraw(final HitTargetListener listener){
        startCraw(url,listener);
    }

    public void startCraw(final String url, final HitTargetListener listener){
        hostName = getHostName();
        basePath = getBasePath(url);
        doCraw(fillURL(url),0,listener);
    }
    public void startMultipleThreadCraw(final HitTargetListener listener,final int threadCount) throws IOException {
        List<String> list = findElement(url,selectQ,suffix).stream().
                map(element -> fillURL(element.attr("href"))).
                collect(Collectors.toList());

        QueueUtil queueUtil = new QueueUtil(new ConcurrentLinkedQueue<>(list));
        for (int i = 0; i < threadCount; i ++ ){
            new Thread(new URLCrawThread( queueUtil,listener,this)).start();
        }
    }

    private void doCraw(final String url, final int curDep, final HitTargetListener listener){
        basePath = getBasePath(url);
        if(doContinue(url,curDep)) return;
        try {
            if(hitTag(url)){
                doDispatcher(url, listener);
            }
            findElement(url,selectQ,suffix).forEach(element -> {
                doCraw(fillURL(element.attr("href")),curDep + 1,listener);
            });
        } catch (IOException e) {
            System.out.println(e.getMessage() + "; 重试");
            doCraw(url,curDep + 1, listener);
        }
    }

    private String getHostName(){
        if(url.startsWith("http://")){
            return splitHostName(url,7);
        }else if (url.startsWith("https://")){
            return splitHostName(url,8);
        }else {
            return url;
        }
    }

    private String getBasePath(final String url){
        if(url.startsWith("http://")){
            return splitBasePath(url,7);
        }else if (url.startsWith("https://")){
            return splitBasePath(url,8);
        }else {
            return url;
        }
    }

    private String splitHostName(final String url,final int count){
        String tmp = url.substring(count);
        return tmp.substring(0,tmp.indexOf("/"));
    }

    private String splitBasePath(final String url,final int count){
        String tmp = url.substring(count);
        return tmp.substring(0,tmp.lastIndexOf("/")); //www.a.com/abc/aaaa -> www.a.com/abc
    }

    private String rmParams(final String url){
        return url.substring(0,url.indexOf("?")); // www.a.com/abc?a=b -> www.a.com/abc
    }


    private List<Element> filterElements(final Elements elements,final String suffix){
        List<Element> list = new ArrayList<>();
        for (Element element : elements){
            Optional<String> optional = Optional.ofNullable(element.attr("href"));
            optional.ifPresent(val -> {
                if(val.endsWith(suffix)) list.add(element);
            });
        }
        return list;
    }

    private boolean doContinue(final String urls,final int curDep){
        if(crawCache.containsKey(urls) || curDep > dep){
           return true;
        }else{
            crawCache.put(urls,"a");
            return false;
        }
    }

    private boolean hitTag(String url){
        return pattern.matcher(url).find();
    }

    private void doDispatcher(final String url,final HitTargetListener listener){
        Optional<HitTargetListener> optional = Optional.ofNullable(listener);
        optional.ifPresent(li -> li.hitTarget(url));
    }

    private List<Element> findElement(final String url, final String selectQ, final String suffix) throws IOException {
        Document document = Jsoup.connect(url).get();
       return filterElements(document.select(selectQ),suffix);
    }

    private int computeGroupCount(int all, int count){
        if( all % count == 0) return all / count;
        else return all/count + 1;
    }

    private String fillURL(String url){
        String tmp = "";
        if (url.startsWith("http://") || url.startsWith("https://")) return url;
        else if (url.startsWith("/"))   tmp = hostName + "/" + url;
        else tmp = basePath + "/" + url;
        if (!tmp.startsWith("http://")) return "http://" + tmp;
        else return tmp;
    }
}
