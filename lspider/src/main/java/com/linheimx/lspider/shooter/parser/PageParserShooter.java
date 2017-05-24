package com.linheimx.lspider.shooter.parser;

import android.text.TextUtils;
import android.util.Log;

import com.linheimx.lspider.IParser;
import com.linheimx.lspider.shooter.bean.Page;
import com.linheimx.lspider.shooter.bean.Zimu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by x1c on 2017/5/24.
 */

public class PageParserShooter implements IParser<String, Page> {

    @Override
    public Page parse(String html) {

        // 匹配字幕的格式模板
        Pattern pattern = Pattern.compile("(?<=格式：\\s)\\w*\\b");

        try {
            Document doc = Jsoup.parse(html);

            List<Zimu> zimuList = new ArrayList<>();

            Element bigDiv = doc.select("div.resultcard").first();
            Elements itemDivs = bigDiv.select("div[class=subitem][onmouseover]");
            for (Element itemDiv : itemDivs) {
                Zimu zimu = new Zimu();

                try {
                    Element a = itemDiv.select("a.introtitle").first();
                    zimu.setName(a.text());
                    zimu.setDownload_page(a.attr("href"));

                    Element msgDiv = itemDiv.select("div#sublist_div").first();
                    Element metaDiv = msgDiv.select("div#meta_top").first();
                    Element b = metaDiv.select("b").first();
                    zimu.setAlias(b.text());

                    Elements spans = metaDiv.siblingElements();
                    Matcher matcher = pattern.matcher(spans.get(0).text());
                    matcher.find();
                    zimu.setFormat(matcher.group(0));

                    zimuList.add(zimu);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            boolean haMore = false;
            Element divLink = doc.select("div.pagelinkcard").first();
            Element a = divLink.select("a#pl-current").first();
            Element nextA = a.nextElementSibling();
            if (nextA != null) {
                if (!TextUtils.isEmpty(nextA.attr("href"))) {
                    haMore = true;
                }
            }
            Page page = new Page(zimuList, haMore);
            return page;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
