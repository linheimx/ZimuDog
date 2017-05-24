package jsouptest;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.ProcessingInstruction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/10/10.
 */

public class Test_Shooter {

    public static final String BASE_URL = "https://secure.assrt.net";

    public static void main(String[] args) {
        Test_Shooter t = new Test_Shooter();
        t.zimusByKW("越狱");
    }


    private void zimusByKW(String kw) {
        // 匹配字幕的格式模板
        Pattern pattern = Pattern.compile("(?<=格式：\\s)\\w*\\b");

        try {
            String url = BASE_URL + "/sub/?searchword={kw}&page={page}";
            url = url.replace("{kw}", kw);
            url = url.replace("{page}", "1");
            System.out.printf("---> search url:%s\n", url);

            Document doc = Jsoup
                    .connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .get();

            List<Zimu> zimuList = new ArrayList<>();

            Element bigDiv = doc.select("div.resultcard").first();
            Elements itemDivs = bigDiv.select("div[class=subitem][onmouseover]");
            for (Element itemDiv : itemDivs) {
                Zimu zimu = new Zimu();

                try {
                    Element a = itemDiv.select("a.introtitle").first();
                    zimu.name = a.text();
                    zimu.detailPageUrl = BASE_URL + a.attr("href");

                    Element msgDiv = itemDiv.select("div#sublist_div").first();
                    Element metaDiv = msgDiv.select("div#meta_top").first();
                    Element b = metaDiv.select("b").first();
                    zimu.alias = b.text();

                    Elements spans = metaDiv.siblingElements();
                    Matcher matcher = pattern.matcher(spans.get(0).text());
                    matcher.find();
                    zimu.format = matcher.group(0);

                    zimuList.add(zimu);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

//            Element divLink = doc.select("div.pagelinkcard").first();
//            Element a = divLink.select("a#pl-current").first();
//            Element nextA = a.nextElementSibling();
//            if (nextA != null) {
//                // has more
//            }
            Zimu first = zimuList.get(0);
            getDownloadUrl(first.detailPageUrl);

            Gson gson = new Gson();
            System.out.print(gson.toJson(zimuList));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getDownloadUrl(String url) {
        try {
            Document doc = Jsoup
                    .connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .get();

            Element a = doc.select("a[href~=/download/.*]").get(0);
            String downloadUrl = BASE_URL + a.attr("href");
            System.out.println("---> download url:" + downloadUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将内容写入文件
     *
     * @param string
     */
    private static void write2File(String string) {
        try {
            File file = new File("test.html");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(string.getBytes("utf-8"));
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class Page {
        List<Zimu> zimuList = new ArrayList<>();
        boolean haMore;
    }

    class Zimu {
        String name;
        String alias;
        String format;
        String detailPageUrl;
    }
}
