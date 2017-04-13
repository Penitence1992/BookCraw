package org.penitence.craw.thread;

import org.penitence.craw.event.HitTargetListener;
import org.penitence.craw.tools.Crawler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class URLCrawThread implements Runnable {

    private List<String> urls = new ArrayList<>();
    private HitTargetListener listener;
    private Crawler crawler;
    public URLCrawThread(List<String> urls, HitTargetListener listener,final Crawler crawler) {
        this.urls = urls;
        this.listener = listener;
        this.crawler = crawler;
    }

    @Override
    public void run() {
        urls.forEach(url -> {
            crawler.startCraw(url,listener);
        });
    }
}
