package jsouptest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Administrator on 2016/10/10.
 */

public class Study {
    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://www.baidu.com/").get();
            System.out.print(doc.body().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
