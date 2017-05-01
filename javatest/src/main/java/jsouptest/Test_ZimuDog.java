package jsouptest;

import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */

public class Test_ZimuDog {

    public static final String BASE_URL = "http://www.zimuku.net";

    public static void main(String[] args) {
        Test_ZimuDog t = new Test_ZimuDog();
        t.searchKeyWord("金刚");
    }


    private void searchKeyWord(String kw) {

        try {
            String url = BASE_URL + "/search?ad=1&q=" + kw;
            System.out.printf("---> search url:%s\n", url);

            Document doc = Jsoup
                    .connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .get();

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
                for (Element tr : div_sublist.select("tr")) {

                    // 命中一个字幕
                    Movie.Zimu zimu = new Movie.Zimu();
                    Element td = tr.child(0);

                    // check
                    if (td.children().size() < 2) {
                        continue;
                    }

                    Element td_img = td.child(0);
                    zimu.setPic_url(BASE_URL + td_img.attr("src"));
                    Element td_a = td.child(1);
                    zimu.setName(td_a.attr("title"));

                    String download_page = BASE_URL + td_a.attr("href");

//                    // 去获取下载地址
//                    Document downloadDoc = Jsoup
//                            .connect(download_page)
//                            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                            .get();
//                    Element a = downloadDoc.select("a[href~=/download/.*]").get(0);
                    zimu.setDownload_page(download_page);
                    movie.addZimu(zimu);
                }
                list_movie.add(movie);
            }

            // 判断是否有更多的数据
//            Element div_page=doc.select("div[class=pagination l clearfix]").first();
//            Elements pageIndex_a=div_page.select("a");

            Gson gson = new Gson();
            System.out.print(gson.toJson(list_movie));

            /***********************************  写入文件 *********************************/
            write2File(doc.body().toString());
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


    public static class Movie {
        private String pic_url;// 电影的头像
        private String name;// 电影的名称
        private String name_alias;// 电影的别名
        private List<Zimu> list_zimu = new ArrayList<>();// 电影下的一堆字幕信息

        public void addZimu(Zimu zimu) {
            list_zimu.add(zimu);
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName_alias() {
            return name_alias;
        }

        public void setName_alias(String name_alias) {
            this.name_alias = name_alias;
        }

        public List<Zimu> getList_zimu() {
            return list_zimu;
        }

        public void setList_zimu(List<Zimu> list_zimu) {
            this.list_zimu = list_zimu;
        }

        public static class Zimu {
            private String pic_url;// 字幕的头像（国家名称）
            private String name;
            private String download_page;// 下载链接的界面
            private String download_url;// 需要去获取一个页面，再解析，考虑延迟去加载吧

            public String getPic_url() {
                return pic_url;
            }

            public void setPic_url(String pic_url) {
                this.pic_url = pic_url;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDownload_url() {
                return download_url;
            }

            public void setDownload_url(String download_url) {
                this.download_url = download_url;
            }

            public String getDownload_page() {
                return download_page;
            }

            public void setDownload_page(String download_page) {
                this.download_page = download_page;
            }
        }
    }
}
