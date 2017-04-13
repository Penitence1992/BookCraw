package org.penitence.craw;

import org.penitence.craw.config.ConfigBean;
import org.penitence.craw.config.PropertiesUtil;
import org.penitence.craw.event.HitTargetListener;
import org.penitence.craw.event.impl.HigTarget;
import org.penitence.craw.tools.Crawler;
import org.penitence.craw.uitl.ReflectUtil;

import java.io.IOException;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        PropertiesUtil.putStartArgs(args);
        /*
        *  预计6个参数
        *  使用--xxx=xxx的方式进行设置
        *  参数1 源URL --url=xx
        *  参数2 后缀名 --suffix=xxx
        *  参数3 目标格式使用正则 --tagReg=xxx
        *  参数4 深度 --dep=1
        *  参数5 保存路径 --save=xxx
        *  参数6 线程数 --threadCount=8
        */
        ConfigBean bean = new ConfigBean();
        HitTargetListener listener = getListener(bean);
        Crawler crawler = new Crawler(bean.getUrl(), bean.getSuffix(), bean.getTagRex());
        crawler.setCrawDepth(bean.getDep());
        crawler.startMultipleThreadCraw(listener, bean.getThreadCount());

        //crawler.startCraw(System.out::println);
    }

    private static HitTargetListener getListener(ConfigBean bean){
        boolean isExternal = Boolean.parseBoolean(PropertiesUtil.getPropertyValue("external"));
        if(isExternal){
            String classPath = PropertiesUtil.getPropertyValue("classPath");
            String externalClass = PropertiesUtil.getPropertyValue("externalClass");
            if(classPath == null || externalClass == null){
                throw new NullPointerException("args classPath and externalClass can't be null, Please use --classPath=xxx and --externalClass=xxx to import the ages");
            }
            System.out.println("user external class : " + externalClass + " to craw ");
            return ReflectUtil.getClassByExternalClass(externalClass,classPath,new Class[]{String.class},new Object[]{bean.getSavePath()});
        }else{
            System.out.println("user interior class : " + PropertiesUtil.getPropertyValue("crawClass") + " to craw");
            return ReflectUtil.getClassInstance(PropertiesUtil.getPropertyValue("crawClass"),
                    new Class[]{String.class},new Object[]{bean.getSavePath()});
        }
    }
}
