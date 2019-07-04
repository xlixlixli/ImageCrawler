import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 爬取搜狗图片
 */
public class SogouImgDownload {

    public static void main(String[] args) throws Exception {
        //匹配程度一般
        Map<String,Object> map = new HashMap<>();
        parseSomething("柳树");
    }

    public static void parseSomething(String key) throws Exception {
        int curIndex=0;
        int i=1;
        int total=1;
        while (true){
            String result = HttpUtils.get("https://pic.sogou.com/pics?query="+key+"&mode=1&start="+curIndex+"&reqType=ajax&reqFrom=result&tn=0");
            JSONObject obj = JSON.parseObject(result);
            JSONArray arr = JSONArray.parseArray(obj.get("items").toString());
            int j=1;
            if(arr==null || arr.size()<=1){
                break;
            }
            for(Object object: arr){
                JSONObject json = (JSONObject)object;
                if(json!=null && json.get("thumbUrl")!=null && !"".equals(json.get("thumbUrl").toString())){
                    System.out.println("正在下载第"+i+"页的第"+j+"张图片，总共完成"+total+"张图片");
                    HttpUtils.downloadPicture(json.get("thumbUrl").toString(),key);
                    total++;
                }
                j++;

            }
            i++;
            curIndex = curIndex+48;
        }

    }

}
