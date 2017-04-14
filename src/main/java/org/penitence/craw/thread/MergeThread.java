package org.penitence.craw.thread;

import org.penitence.craw.tools.Crawler;
import org.penitence.craw.uitl.ConvertUtil;
import org.penitence.craw.uitl.StreamUtil;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by RenJie on 2017/4/14 0014.
 */
public class MergeThread implements Runnable {

    private final String dirPath;
    private final String mergeName;
    private final Pattern pattern;
    private final String regex = "[个十百千一二三四五六七八九]+";
    private final Crawler crawler;

    public MergeThread(String dirPath, String mergeName, Crawler crawler) {
        this.dirPath = dirPath;
        pattern = Pattern.compile(regex);
        this.mergeName = mergeName;
        this.crawler = crawler;
    }

    @Override
    public void run() {

        while (!crawler.isComplete()) {
            System.out.println("还有" + crawler.getQueueUtil().getQueueSize() + " 条链接等待处理");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        File dir = new File(dirPath);
        if(!dir.exists()) {
            System.out.println("Directory : " + dirPath + " is not found!");
            System.exit(1);
        }
        List<File> list = Arrays.stream(dir.listFiles()).sorted(Comparator.comparingInt(this::getIndex)).collect(Collectors.toList());
        StreamUtil.mergeTextFileToDisk(list.toArray(new File[]{}),dirPath + File.separatorChar + mergeName);
    }

    private int getIndex(File file){
        String strNum = file.getName().split("\\.")[0];
        if("null".equals(strNum)) return 0;
        int index;
        if(isCNNumber(strNum)){
            index = ConvertUtil.chineseNumber2Int(strNum);
        }else{
            index = Integer.parseInt(strNum);
        }
        return index;
    }

    private boolean isCNNumber(String num){
        return pattern.matcher(num).find();
    }
}
