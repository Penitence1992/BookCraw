package org.penitence.craw.tools;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.penitence.craw.bean.URLBean;
import org.penitence.craw.event.HitTargetListener;
import org.penitence.craw.thread.URLCrawThread;
import org.penitence.craw.uitl.URLUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class Crawler {

    private static ConcurrentHashMap<String,String> crawCache = new ConcurrentHashMap<>(); //已经爬取过的网页的缓存

    private String url = "";
    private String suffix = "";
    private int dep = 3;
    private String baseUrl = "";

    private String selectQ = "a[href]";
    private Pattern pattern ;

    private ExecutorService pool;
    private QueueUtil queueUtil;

    public Crawler(String suffix, String tagReg ) {
        this.suffix = suffix;
        pattern = Pattern.compile(tagReg);
    }

    public Crawler (String sUrl, String suffix, String tagReg){
        this.url = sUrl;
        this.suffix = StringUtil.isBlank(suffix) ? ".html" : suffix;
        pattern = Pattern.compile(tagReg);
        if(!URLUtil.isFullUrl(url)){
            throw new IllegalArgumentException("URL不是完整的URL,需要http://或者https://开头");
        }
        baseUrl = url;
    }

    public QueueUtil getQueueUtil() {
        return queueUtil;
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
        checkAndCraw(url,0,listener);
        //doCraw(fillURL(url),0,listener);
    }

    public void startCraw(final URLBean bean, final HitTargetListener listener){
        checkAndCraw(bean.getUrl(), bean.getCurrentDep(), listener);
    }

    public void startMultipleThreadCraw(final String url, final HitTargetListener listener, final  int threadCount ) throws IOException {
        if(!URLUtil.isFullUrl(url)){
            throw new IllegalArgumentException("URL不是完整的URL,需要http://或者https://开头");
        }
        baseUrl = url;
        startMultipleThreadCraw(listener,threadCount);
    }

    public void startMultipleThreadCraw(final HitTargetListener listener,final int threadCount) throws IOException {
        List<URLBean> list = findElement(url,selectQ,suffix).stream().
                map(element -> new URLBean( URLUtil.fillNextUrl(baseUrl,element.attr("href")), 0) ).
                collect(Collectors.toList());

        queueUtil = new QueueUtil(new ConcurrentLinkedQueue<>(list));
        pool = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i ++ ){
            pool.execute(new URLCrawThread( queueUtil,listener,this));
        }
        pool.shutdown();
    }

    public boolean isComplete(){
        Optional<ExecutorService> optional = Optional.ofNullable(pool);
        return optional.orElseThrow(NullPointerException::new).isTerminated();
    }

    private void checkAndCraw( final String url,final int curDep,final HitTargetListener listener) {
        if(!isContinue(url,curDep)) return;
        if(hitTag(url)){
            triggerEvent(url, listener);
        }
        doCraw(url,curDep,listener);
    }

    private void doCraw(final String url, final int curDep, final HitTargetListener listener){
        try {
            /*findElement(url,selectQ,suffix).forEach(element -> {
                doCraw(fillURL(element.attr("href")),curDep + 1,listener);
            });*/
            List<URLBean> list = findElement(url,selectQ,suffix).stream().
                    filter(element -> !crawCache.containsKey(element.attr("href"))).
                    map(element -> new URLBean(URLUtil.fillNextUrl(url,element.attr("href")), curDep+1) ).
                    collect(Collectors.toList());
            queueUtil.put(list);
        } catch (IOException e) {
            System.out.println(e.getMessage() + " URL : " + url + "; 重试");
            doCraw(url,curDep + 1, listener);
        }
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

    private boolean isContinue(final String urls,final int curDep){
        if(crawCache.containsKey(urls) || curDep > dep){
           return false;
        }else{
            crawCache.put(urls,"a");
            return true;
        }
    }

    private boolean hitTag(String url){
        return pattern.matcher(url).find();
    }

    private void triggerEvent(final String url,final HitTargetListener listener){
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

}
