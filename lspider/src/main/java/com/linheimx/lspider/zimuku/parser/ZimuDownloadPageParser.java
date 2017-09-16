package com.linheimx.lspider.zimuku.parser;

import com.linheimx.lspider.IParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static com.linheimx.lspider.Contants.BASE_URL_ZIMUKU;

/**
 * 字幕的下载页面
 * -------------------------
 * <p>
 * Created by x1c on 2017/5/1.
 */

public class ZimuDownloadPageParser implements IParser<String, String> {

    @Override
    public String parse(String html) {
        try {
            Document document = Jsoup.parse(html);
            Element a = document.select("a[dlurl~=/download/.*]").get(0);
            return BASE_URL_ZIMUKU + a.attr("dlurl");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
