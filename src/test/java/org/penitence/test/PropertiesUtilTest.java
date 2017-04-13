package org.penitence.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.penitence.craw.config.PropertiesUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void testTmp(){
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        int groupCount = 3;
        int excessCount = 1;
        for (int i = 0 ; i < 3; i ++){
            int startIndex = i * groupCount;
            int endIndex = (i + 1) * groupCount;
            if( i == (3 - 1)){
                endIndex += excessCount;
            }
            for (int a : list.subList(startIndex,endIndex )){
                System.out.print(a + " " );
            }
        }
    }

    @Test
    public void testContentCraw() throws IOException {
        Document document = Jsoup.connect("http://www.luoqiu.com//read/42067/36431639.html").get();
        Elements elements = document.select("div[id=content][name=content]");
        //System.out.println(document);
        for (Element element : elements) System.out.println(element.text());
    }

    @Test
    public void testGetTitle(){
        String tmp = "****asdas<>?:adasd\"//\\asd**?asd:asdad|".replaceAll("[\\\\/*?:\"<>|]*","");
        System.out.println(tmp);
    }

    @Test
    public void testQueue(){
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12));
        System.out.println(queue.poll());
        System.out.println(queue.poll());
    }
}
