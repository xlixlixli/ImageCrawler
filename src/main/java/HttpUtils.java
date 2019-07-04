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
import java.util.UUID;

/**
 * 工具类
 */
public class HttpUtils {

    public static String get(String url){
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        StringBuilder entityStringBuilder = new StringBuilder();
        try {
            HttpResponse res = client.execute(get);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity=res.getEntity();
                if (httpEntity!=null) {
                    BufferedReader bufferedReader = new BufferedReader
                            (new InputStreamReader(httpEntity.getContent(), "UTF-8"), 1024);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        entityStringBuilder.append(line);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally{
            if (client != null) {
                try {
                    client.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return entityStringBuilder.toString();
    }

    public static void readInputStream(InputStream inStream, String path) throws Exception{
        FileOutputStream fos = new FileOutputStream(new File(path));
        byte[] buffer = new byte[102400];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            fos.write(buffer, 0, len);
        }
        inStream.close();
        fos.flush();
        fos.close();
    }

    //链接url下载图片
    public static void downloadPicture(String urlList,String path) throws Exception {
        URL url;
        try {
            url = new URL(urlList);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
