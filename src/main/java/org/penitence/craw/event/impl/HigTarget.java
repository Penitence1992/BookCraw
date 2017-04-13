package org.penitence.craw.event.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.penitence.craw.event.HitTargetListener;
import org.penitence.craw.uitl.RegexUtil;
import org.penitence.craw.uitl.StreamUtil;

import java.io.IOException;

/**
 * Created by RenJie on 2017/4/13 0013.
 */
public class HigTarget implements HitTargetListener {
    private final String SPLIT_STRING = " ";
    private String saveDir;
    private String titleSelectQ = "";
    private String textSelectQ = "";
    private RegexUtil regexUtil;

    public HigTarget(String saveDir, String titleSelectQ, String textSelectQ) {
        this.saveDir = saveDir;
        regexUtil = new RegexUtil("[0-9个十百千一二三四五六七八九]+");
        this.titleSelectQ = titleSelectQ;
        this.textSelectQ = textSelectQ;
    }

    @Override
    public void hitTarget(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            String title = getTitle(document);
            Elements elements = document.select(textSelectQ);
            if(elements.size() == 0)return;
            String innerText = elements.get(0).text();
            String[] rows = innerText.split(SPLIT_STRING);
            StreamUtil.saveToDisk(rows,saveDir + "/" + regexUtil.findTheFirst(title) + "." + removeBadWorld(getTitleContent(title)) + ".txt");
        } catch (IOException e) {
            hitTarget(url);
        }
    }

    private String getTitle(Document document){
        Elements elements = document.select(titleSelectQ);
        if(elements.size() == 0)return null;
        Element element = elements.get(0);
        return element.text();
    }

    private String getTitleContent(String string){
        String[] strings = string.split(SPLIT_STRING);
        return strings[strings.length - 1];
    }

    private String removeBadWorld(String content){
        return content.replaceAll("[\\\\/*?:\"<>|]*","");
    }
}
