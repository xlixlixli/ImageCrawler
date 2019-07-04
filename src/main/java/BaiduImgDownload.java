import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 爬取百度图片
 */
public class BaiduImgDownload {

    public static final String[] TREE_NAME_LIST = {"柳树","泡桐","梧桐","油桐","国槐","刺槐","榆树","桑树","白杨","松树","苦楝","香椿","臭椿","樟",
            "合欢树","银杏","三球悬铃木","桂花","樱花","发财树","夹竹桃","鸡爪槭","凤凰木","白兰","玉兰","广玉兰","紫玉兰","木棉","丁香","紫薇","瑞香",
            "琼花","紫荆","梨树","桃花","李树","梅花","杏花","枣树","阳桃","枸杞","芒果","柠檬","龙眼","枫树","构树","榕树","朴树","梓树","楸树","橡树",
            "栾树","白桦","乌桕","杜仲","珙桐","棕榈","杜英","香榧","冷杉","紫檀","云杉","接骨木","水东哥","木奶果","聚果榕","大果榕","嘉宝果","鹅掌楸",
            "番石榴","沙枣","蟠桃","槟榔","山竹","蓝莓","重阳木","七叶树","面包树","菜豆树","沙棘","可可","辣木","皂荚","花椒","蛋黄果","榛子","栗子",
            "木犀榄","马缨杜鹃","葡萄","绣球荚蒾","佛手","榴莲","山楂","火棘","金柑","柚","结香","椰子","雪松","无患子","无花果","水杉","十大功劳","女贞",
            "南天竹","木瓜","龙爪槐","剑麻","枫香树","茶梅","木槿","香蕉","杨梅","淡竹","紫叶李","芭蕉","石楠","樱桃","郁李","榆叶梅","西洋杜鹃",
            "皱皮木瓜","四季海棠","倒挂金钟","棣棠花","胡杨","猕猴桃","木芙蓉","枇杷","山茶","凤尾丝兰","鹅掌柴","咖啡树","软叶刺葵","杜鹃花","桉树",
            "蜡梅","青檀","甜橙","桃花心木","白蜡","巴西木","茶树","绿玉树","四合木","石榴","苏铁","荔枝","栗豆树","橡皮树","南烛","南洋杉","苹果",
            "蒲葵","洋紫荆","金鸡纳树","金钱树","卷柏","金樱子","胡桃","朱蕉","棕竹","夜香木兰","海棠花","黄栌","红豆杉","黑杨","红背桂花","常春藤",
            "海桐","金银花","锦带花","连翘","美人蕉","柿子","迎春花","玉簪花","月季","栀子"};

    public static void main(String[] args) throws Exception {
//        for(String treeName: TREE_NAME_LIST){
            parseSomething("枣树");
        System.out.println("DONE");
//        }
    }

    public static void parseSomething(String key) throws Exception{

        int curIndex=0;
        int i=1;
        int total=1;
        while (true){
            String result = HttpUtils.get("https://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=result&queryWord="+key+"&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=0&hd=&latest=&copyright=&word="+key+"&s=&se=&tab=&width=&height=&face=0&istype=2&qc=&nc=1&fr=&expermode=&force=&pn="+curIndex+"&rn=30&gsm=1e&1559200151114=");
            JSONObject obj = JSON.parseObject(result);
            JSONArray arr = JSONArray.parseArray(obj.get("data").toString());
            int j=1;
            if(arr==null || arr.size()<=1){
                break;
            }
            for(Object object: arr){
                JSONObject json = (JSONObject)object;
                if(json!=null && json.get("thumbURL")!=null && !"".equals(json.get("thumbURL").toString())){
                    System.out.println("正在下载第"+i+"页的第"+j+"张图片，总共完成"+total+"张图片");
                    HttpUtils.downloadPicture(json.get("thumbURL").toString(),key);
                    total++;
                }
                j++;

            }
            i++;
            curIndex = curIndex+30;
        }
    }

}
