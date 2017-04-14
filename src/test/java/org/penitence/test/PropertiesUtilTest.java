package org.penitence.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import org.penitence.craw.config.PropertiesUtil;
import org.penitence.craw.event.HitTargetListener;
import org.penitence.craw.tools.QueueUtil;
import org.penitence.craw.uitl.ConvertUtil;
import org.penitence.craw.uitl.RegexUtil;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class PropertiesUtilTest {

    @Test
    public void testGetValue(){
        Assert.assertNull(PropertiesUtil.getPropertyValue(UUID.randomUUID().toString()));
    }

    @Test
    public void testGetDefaultValue(){
        Assert.assertEquals(PropertiesUtil.getPropertyValue(UUID.randomUUID().toString(),"testValue"),"testValue");
    }

    @Test
    public void testReadLoadFile(){
        PropertiesUtil.setConfigFile("test.properties");
        PropertiesUtil.reload();
        Assert.assertEquals(PropertiesUtil.getPropertyValue("test.key"),"test.value");
    }

    @Test
    public void testTmp() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> tClass = Class.forName("org.penitence.craw.event.impl.HigTarget");
        Constructor<?> constructor = tClass.getConstructor(String.class,String.class,String.class);
        HitTargetListener listener = (HitTargetListener) constructor.newInstance("aaa","bbb","ccc");
        Assert.assertNotNull(listener);
    }

    @Test
    public void testCustomClassLoader() throws ClassNotFoundException {
        //Class<?> tClass = Class.forName("a.b.c.abc",true, new CustomClassLoader("D:\\abc.class",null));
    }

    @Test
    public void testRegex(){
        RegexUtil regexUtil = new RegexUtil("[1-9个十百千一二三四五六七八九]+");
        System.out.printf(regexUtil.findTheFirst("第九百八十二章 十百千"));
    }

    @Test
    public void testFindTitle() throws IOException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i ++){
            pool.submit(() -> {
                try {
                    Thread.sleep(10000);
                    System.out.println("线程完成");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();

        while (!pool.isTerminated()){
            System.out.println("没有完成");
            Thread.sleep(1000);
        }

        System.out.println("完成");
    }

    @Test
    public void testNumConvert(){
        String title = "二百零一";
        Assert.assertEquals(ConvertUtil.chineseNumber2Int(title),201);
    }

    @Test
    public void testQueueUtil(){
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>(list);
        System.out.println(queue.size());
        queue.poll();
        System.out.println(queue.size());
    }

    @Test
    public void testTTT() throws IOException {
        Document document = Jsoup.connect("http://www.luoqiu.com/read/276121/43720338.html").get();
        for (Element element : document.select("a[href]")){
            System.out.println(element);
        }
    }

}
