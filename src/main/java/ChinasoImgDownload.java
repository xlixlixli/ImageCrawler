import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by cayley on 2019/6/4.
 */
public class ChinasoImgDownload {

    public static void main(String[] args) throws Exception {
        parseSomething("柳树");
    }

    public static void parseSomething(String key) throws Exception{
        int start = 0;
        int page = 1;
        int total = 1;
        while (true){
            String result = HttpUtils.get("http://image.chinaso.com/getpic?rn=72&st="+start+"&q="+key+"&t=1559618285233");
            JSONObject obj = JSON.parseObject(result);
            JSONArray arr = JSONArray.parseArray(obj.get("arrResults").toString());
            int per = 1;
            for(Object object:arr){
                JSONObject json = (JSONObject)object;
                if(json!=null && json.get("large_tfs_key")!=null && !"".equals(json.get("large_tfs_key").toString())){
                    System.out.println("正在下载第"+page+"页的第"+per+"张图片，总共完成"+total+"张图片");
                    String url = "http://n6.image.pg0.cn/"+json.get("large_tfs_key").toString()+".jpg";
                    downloadPicture(url,key,page+"--"+per);
                    per++;
                    total++;
                }
            }
            page++;
            start = start+72;
            if(page==3){
                break;
            }
        }
    }

    //链接url下载图片
    public static void downloadPicture(String urlList,String path,String name) throws Exception {
        URL url;
        try {
            url = new URL(urlList);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            String savePath = "/Users/cayley/Downloads/"+path+"/";
            File imgDir = new File(savePath);
            if(!imgDir.exists()){
                imgDir.mkdirs();
            }
            HttpUtils.readInputStream(inStream, savePath+ name+".jpg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
