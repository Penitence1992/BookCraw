package org.penitence.craw;

import org.penitence.craw.config.ConfigBean;
import org.penitence.craw.config.PropertiesUtil;
import org.penitence.craw.event.HitTargetListener;
import org.penitence.craw.thread.MergeThread;
import org.penitence.craw.tools.Crawler;
import org.penitence.craw.uitl.ReflectUtil;

import java.io.IOException;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        PropertiesUtil.putStartArgs(args);

        ConfigBean bean = new ConfigBean();
        Crawler crawler = new Crawler(bean.getUrl(), bean.getSuffix(), bean.getTagRex());
        crawler.setCrawDepth(bean.getDep());
        crawler.startMultipleThreadCraw(getListener(bean), bean.getThreadCount());

        boolean isMerge = Boolean.parseBoolean(PropertiesUtil.getPropertyValue("merge","false"));
        if(isMerge){
            new Thread(new MergeThread(bean.getSavePath(),PropertiesUtil.getPropertyValue("mergeName","merge.txt"),crawler)).start();
        }
        //crawler.startCraw(System.out::println);
    }

    private static HitTargetListener getListener(ConfigBean bean){
        boolean isExternal = Boolean.parseBoolean(PropertiesUtil.getPropertyValue("external"));
        Class[] classes = {String.class, String.class, String.class};
        Object[] objects = {bean.getSavePath(), bean.getTitleSelectQ(), bean.getTextSelectQ()};
        if(isExternal){
            String classPath = PropertiesUtil.getPropertyValue("classPath");
            String externalClass = PropertiesUtil.getPropertyValue("externalClass");
            if(classPath == null || externalClass == null){
                throw new NullPointerException("args classPath and externalClass can't be null, Please use --classPath=xxx and --externalClass=xxx to import the ages");
            }
            System.out.println("user external class : " + externalClass + " to craw ");
            return ReflectUtil.getClassByExternalClass(externalClass,classPath, classes, objects);
        }else{
            System.out.println("user interior class : " + PropertiesUtil.getPropertyValue("crawClass") + " to craw");
            return ReflectUtil.getClassInstance(PropertiesUtil.getPropertyValue("crawClass"),
                    classes, objects);
        }
    }
}
