package com.linheimx.lspider.zimuku.parser;

import com.linheimx.lspider.IParser;
import com.linheimx.lspider.zimuku.bean.Movie;
import com.linheimx.lspider.zimuku.bean.Page;
import com.linheimx.lspider.zimuku.bean.Zimu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 字幕库
 * ----------------
 * 根据关键词，搜索出来的界面
 * <p>
 * Created by x1c on 2017/5/1.
 */

public class MoviesParser implements IParser<String, Page> {

    public static final String BASE_URL = "http://www.zimuku.net";

    @Override
    public Page parse(String html) {
        try {
            Document doc = Jsoup.parse(html);

            List<Movie> list_movie = new ArrayList<>();
            List<Element> list_item = doc.select("div[class=item prel clearfix]");
            for (Element item : list_item) {
                Movie movie = new Movie();

                // movie img
                Element img = item.select("img[class=lazy][data-original]").get(0);
                movie.setPic_url("http:" + img.attr("data-original"));

                Element div_title = item.select("div.title").get(0);
                // movie name
                Element p1 = div_title.child(0);
                movie.setName(p1.text());
                // movie name alias
                Element p2 = div_title.child(1);
                movie.setName_alias(p2.text());

                // 一堆字幕
                Element div_sublist = div_title.select("div[class=sublist]").get(0);

                /*************************   先检查有没有更多字幕（要去加载的）  **********************/
                String urlMore = "";
                try {
                    for (Element tr : div_sublist.select("tr")) {
                        if (tr.attr("class").equals("msub")) {
                            // 有更多字幕
                            Element a = tr.select("a").first();
                            urlMore = a.attr("href");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                movie.setUrlMore(urlMore);

                for (Element tr : div_sublist.select("tr")) {

                    // 命中一个字幕
                    Zimu zimu = new Zimu();
                    Element td = tr.child(0);

                    // check
                    if (td.children().size() < 2) {
                        continue;
                    }

                    Element td_img = td.child(0);
                    zimu.setPic_url(BASE_URL + td_img.attr("src"));
                    Element td_a = td.child(1);
                    zimu.setName(td_a.attr("title"));

                    String download_page = td_a.attr("href");
                    zimu.setDownload_page(download_page);
                    movie.addZimu(zimu);
                }
                list_movie.add(movie);
            }

            // 是否有更多的数据
            Element div_page = doc.select("div[class=pagination l clearfix]").first();
            Elements pageIndex_a = div_page.select("a");
            boolean hasMore = pageIndex_a == null ? false : pageIndex_a.size() > 0;

            Page page = new Page(list_movie, hasMore);
            return page;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
