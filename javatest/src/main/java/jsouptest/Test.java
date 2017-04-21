package jsouptest;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/10/10.
 */

public class Test {

    public static final String BASE_URL = "http://www.zimuku.net";
    public static final String KEYWORD = "本杰明·巴顿奇事";

    public static void main(String[] args) {
        try {
            String url = BASE_URL + "/search?ad=1&q=" + KEYWORD;
            System.out.printf("---> search url:%s %1$f %<10.1f", url,5999.33);

            Connection connection = Jsoup.connect(url);
            connection.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36");

            Document doc = connection.get();
//            System.out.print(doc.body().toString());

            //////////////////////////////////////  oooooo  ////////////////////////////////
            write2File(doc.body().toString());
        } catch (IOException e) {
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
}
