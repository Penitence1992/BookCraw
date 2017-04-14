package org.penitence.craw.tools;

import org.penitence.craw.bean.URLBean;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class QueueUtil {

    private ConcurrentLinkedQueue<URLBean> queue;

    public QueueUtil(ConcurrentLinkedQueue<URLBean> queue) {
        this.queue = queue;
    }

    public URLBean getOne(){
        synchronized (QueueUtil.class){
            return queue.poll();
        }
    }

    public void put(List<URLBean> list){
        queue.addAll(list);
    }

    public int getQueueSize(){
        return queue.size();
    }

    public URLBean getOneNoBlock(){
        return queue.poll();
    }

    public boolean hasNext(){
        synchronized (QueueUtil.class){
            return queue.size() > 0;
        }
    }
}
