package com.linheimx.lspider.zimuku.parser;

import com.linheimx.lspider.IParser;
import com.linheimx.lspider.zimuku.Contants;
import com.linheimx.lspider.zimuku.bean.Zimu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个电影下的所有字幕的页面
 * -----------------------------
 * <p>
 * Created by LJIAN on 2017/5/19.
 */

public class AllZimuParser implements IParser<String, List<Zimu>> {

    @Override
    public List<Zimu> parse(String s) {

        try {
            Document doc = Jsoup.parse(s);

            Element tab = doc.select("table[id=subtb] tbody").first();

            List<Zimu> list_zimu = new ArrayList<>();// 电影下的一堆字幕信息
            for (Element tr : tab.select("tr")) {

                // 命中一个字幕
                Zimu zimu = new Zimu();

                Element a = tr.select("a").first();
                if (a == null) {
                    continue;
                }

                zimu.setName(a.attr("title"));

                String download_page = a.attr("href");
                zimu.setDownload_page(download_page);

                Element img = tr.select("img").first();
                zimu.setPic_url(Contants.BASE_URL + img.attr("src"));

                list_zimu.add(zimu);
            }

            return list_zimu;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}