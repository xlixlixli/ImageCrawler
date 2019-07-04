import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 爬取秘迹搜图片
 */
public class MijisouImgDownload {

    public static void main(String[] args) {
        //图片有些大，目前见到最大的6M，平均在三四百K左右
        parseSomething("柳树");
    }

    public static void parseSomething(String key){
        Map<String,String> map = new HashMap<String, String>();

        int pageNumber = 1;
        int count = 0;
        int errorCount = 0;
        while (true){
            try {
                String keyEncoder = URLEncoder.encode(key,"utf-8");
                Long s1 = System.currentTimeMillis();
                Connection conn = Jsoup.connect("http://mijisou.com/?q="+keyEncoder+"&category_images=on&time_range=&language=zh-CN&pageno="+pageNumber);
                conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                conn.header("Accept-Encoding", "gzip, deflate, sdch");
                conn.header("Accept-Language", "zh-CN,zh;q=0.8");
                conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
                Document doc = conn.validateTLSCertificates(false).get();
                Long s2 = System.currentTimeMillis();
                System.out.println("======="+(s2-s1));
                Elements aList = doc.getElementsByClass("img-content");
                if(aList==null || aList.size()==0){
                    break;
                }
                for(Element e:aList){
                    String src = e.child(1).attr("src");
                    HttpUtils.downloadPicture(src,key);
                    System.out.println("正在下载第"+pageNumber+"页，"+count);
                    count++;
                }
                pageNumber++;
            }catch (Exception e){
                if(errorCount>20){
                    break;
                }
                e.printStackTrace();
                pageNumber++;
                errorCount++;
            }
        }

    }

}
