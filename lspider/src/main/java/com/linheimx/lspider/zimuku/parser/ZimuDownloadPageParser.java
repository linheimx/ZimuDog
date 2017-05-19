package com.linheimx.lspider.zimuku.parser;

import com.linheimx.lspider.IParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * 字幕库
 * ----------------
 * 根据关键词，搜索出来的界面
 * <p>
 * Created by x1c on 2017/5/1.
 */

public class ZimuDownloadPageParser implements IParser<String, String> {

    public static final String BASE_URL = "http://www.zimuku.net";

    @Override
    public String parse(String html) {
        try {
            Document document = Jsoup.parse(html);
            Element a = document.select("a[href~=/download/.*]").get(0);
            return BASE_URL + a.attr("href");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
