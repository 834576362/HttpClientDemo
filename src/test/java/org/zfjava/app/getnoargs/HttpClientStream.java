package org.zfjava.app.getnoargs;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @auther zhangfen
 * @date 2021/12/20 11:20
 */
public class HttpClientStream {

    /**
     * 发送六
     */
    @Test
    public void sendStream(){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost httpPost =
                    new HttpPost("http://localhost:8080/sendStream?name="+URLEncoder.encode(URLEncoder.encode("张三，你好啊","UTF-8"),"UTF-8"));
            //经过测试使用一次URLEncoder，传输过程会自动解密一次
            //            HttpPost httpPost = new HttpPost("http://localhost:8080/sendStream?name="+URLEncoder.encode("张三，你好啊","UTF-8"));
            CloseableHttpResponse response = null;
            InputStream is = new ByteArrayInputStream("流啊流啊流流啊流".getBytes());
            InputStreamEntity ise = new InputStreamEntity(is);
            httpPost.setEntity(ise);
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            System.out.println("请求地址："+httpPost.getURI().toString());
            System.out.println("响应状态："+response.getStatusLine());
            System.out.println("响应状态码："+response.getStatusLine().getStatusCode());
            if(response.getStatusLine().getStatusCode()==200 && responseEntity!=null){
                // 主动设置编码，来防止响应乱码
                String responseStr = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                System.out.println("响应内容:"+ responseStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
