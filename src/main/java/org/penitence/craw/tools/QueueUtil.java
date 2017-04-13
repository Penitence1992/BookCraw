package org.penitence.craw.tools;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class QueueUtil {

    private ConcurrentLinkedQueue<String> queue;

    public QueueUtil(ConcurrentLinkedQueue<String> queue) {
        this.queue = queue;
    }

    public String getOne(){
        synchronized (QueueUtil.class){
            return queue.poll();
        }

    }

    public String getOneNoBlock(){
        return queue.poll();
    }

    public boolean hasNext(){
        synchronized (QueueUtil.class){
            return queue.size() > 0;
        }
    }
}
