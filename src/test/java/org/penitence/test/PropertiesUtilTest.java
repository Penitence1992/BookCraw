package org.penitence.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.penitence.craw.config.PropertiesUtil;
import org.penitence.craw.event.HitTargetListener;
import org.penitence.craw.uitl.CustomClassLoader;
import org.penitence.craw.uitl.RegexUtil;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
    public void testFindTitle() throws IOException {
        Document document = Jsoup.connect("http://www.sanjiangge.com/book/47/47833/19586953.html").get();
        System.out.println(document.select("div[id=content][name=content]").get(0).text());
    }
}
