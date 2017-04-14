package org.penitence.craw.thread;

import org.penitence.craw.bean.URLBean;
import org.penitence.craw.event.HitTargetListener;
import org.penitence.craw.tools.Crawler;
import org.penitence.craw.tools.QueueUtil;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class URLCrawThread implements Runnable {

    private HitTargetListener listener;
    private Crawler crawler;
    private QueueUtil queueUtil;

    public URLCrawThread(QueueUtil queueUtil, HitTargetListener listener, final Crawler crawler) {
        this.listener = listener;
        this.crawler = crawler;
        this.queueUtil = queueUtil;
    }

    @Override
    public void run() {
        URLBean url;
        while ( ( url = queueUtil.getOneNoBlock()) != null){
            crawler.startCraw(url,listener);
        }
    }
}
