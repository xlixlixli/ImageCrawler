import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;

/**
 * 爬取必应图片
 */
public class BingImgDownload {

    public static void main(String[] args) {
        //搜索出的结课匹配的正确率也不是很高
        parseSomething("柳树");
    }

    public static void parseSomething(String key){
        int pageNumber = 1;
        int pageSize = 35;
        int startIndex = 1;
        int count = 0;
        while (true){
            try {
                int perPageCount = 0;
                String keyEncoder = URLEncoder.encode(key,"utf-8");
                String url = "https://cn.bing.com/images/async?q="+keyEncoder+"&&first="+startIndex+"&count="+pageSize+"&relp="+pageSize+"&scenario=ImageBasicHover&datsrc=N_I&layout=RowBased&mmasync=1&dgState=x*493_y*864_h*201_c*2_i*141_r*19&IG=3F80E9C35DE04B888FC8FB68CF0D8EB5&SFX=1&iid=images.5700";
                Connection conn = Jsoup.connect(url);
                System.out.println(url);
                conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                conn.header("Accept-Encoding", "gzip, deflate, sdch");
                conn.header("Accept-Language", "zh-CN,zh;q=0.8");
                conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
                Document doc = conn.validateTLSCertificates(false).get();
                Elements aList = doc.getElementsByClass("mimg");
                if(aList==null || aList.size()==0){
                    break;
                }
                for(Element e:aList){
                    String src = e.attr("src");
                    HttpUtils.downloadPicture(src,key);
                    System.out.println("正在下载第"+pageNumber+"页，"+count);
                    count++;
                    perPageCount++;
                }
                pageNumber++;
                startIndex = (pageNumber-1)*pageSize;
                if(perPageCount<pageSize){
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
                pageNumber++;
            }
        }

    }

}
