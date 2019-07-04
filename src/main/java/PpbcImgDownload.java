import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * 爬取中国植物图像库
 */
public class PpbcImgDownload {

    public static void main(String[] args) {
        //图像有水印
        parseSomething("法国梧桐");
    }

    public static void parseSomething(String key){
        try{
            int total = 0;
            Document doc = Jsoup.connect("http://ppbc.iplant.cn/list?keyword="+key).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").timeout(10000).get();
            if(doc!=null){
                // dom解析获得指定元素
                Element count = doc.getElementById("form1");
                String id = count.baseUri().substring(count.baseUri().lastIndexOf("/")+1,count.baseUri().length());
                if(count.attr("action").indexOf("./list")==0){
                    String result = HttpUtils.get("http://ppbc.iplant.cn/ashx/getotherinfo.ashx?t=getsyn&cname="+key);
                    JSONObject obj = JSON.parseObject(result);
                    if(obj!=null && obj.get("cid")!=null){
                        id = obj.get("cid").toString();
                        System.out.println("找到"+obj.get("name")+"的图片");
                    }else{
                        System.out.println("找不到"+key+"相关图片图片");
                        return;
                    }
                }

                int i=1;
                while (true){
                    Document listDoc = Jsoup.connect("http://ppbc.iplant.cn/ashx/getphotopage.ashx?page="+i+"&n=2&group=sp&cid="+id).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").timeout(10000).get();
                    if(listDoc.toString().indexOf("暂无可显示图片")>=0){
                        break;
                    }
                    int j = 1;
                    for(Element e:listDoc.getElementsByClass("masonry_brick")){
                        String src = e.child(0).child(0).child(0).child(0).attr("src");
                        String imgUrl = "http://ppbc.iplant.cn"+src;
                        downloadPicture(imgUrl,key);
                        System.out.println("正在下载"+key+"的第"+i+"页的第"+j+"张图片，总下载完成"+total+"张图片");
                        j++;
                        total++;
                    }
                    i++;
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //链接url下载图片
    private static void downloadPicture(String urlList,String path) throws Exception {
        try {
            URL url = new URL(urlList);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("referer", "http://ppbc.iplant.cn");
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            String savePath = "/root/images/"+path+"/";
            File imgDir = new File(savePath);
            if(!imgDir.exists()){
                imgDir.mkdirs();
            }
            HttpUtils.readInputStream(inStream, savePath+ UUID.randomUUID().toString()+".jpg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
