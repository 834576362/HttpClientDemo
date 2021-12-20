package org.zfjava.app.getnoargs;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * @auther zhangfen
 * @date 2021/12/17 18:23
 */
public class HttpClientFile {

    /**
     * 发送文件
     * multipart/form-data 传送文件（及相关信息）
     */
    @Test
    public void file(){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost("http://localhost:8080/file");
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        File file1 = new File("C:\\Users\\83457\\Pictures\\Camera Roll\\网图001.jpg");
        File file2 = new File("C:\\Users\\83457\\Pictures\\Camera Roll\\网图002.jpg");
        File file3 = new File("C:\\Users\\83457\\Pictures\\Camera Roll\\asdasdasd2345654345654zcc.jpg");
        multipartEntityBuilder.addBinaryBody("files",file3);
        // 防止服务端收到的文件名乱码。 我们这里可以先将文件名URLEncode，然后服务端拿到文件名时在URLDecode。就能避免乱码问题。
        // 文件名其实是放在请求头的Content-Disposition里面进行传输的
        try {
            multipartEntityBuilder.addBinaryBody("files",file1, ContentType.DEFAULT_BINARY, URLEncoder.encode(file1.getName(),"UTF-8"));
            multipartEntityBuilder.addBinaryBody("files",file2, ContentType.DEFAULT_BINARY, URLEncoder.encode(file2.getName(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //其他参数（自定义ContentType,设置UTF-8是为了防止服务端拿到的参数出现乱码）
        ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
        multipartEntityBuilder.addTextBody("name","张三",contentType);
        multipartEntityBuilder.addTextBody("age","22",contentType);

        HttpEntity requestEntity = multipartEntityBuilder.build();
        httpPost.setEntity(requestEntity);

        try {
            //发送请求
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态码："+response.getStatusLine().getStatusCode());
            if(response.getStatusLine().getStatusCode()==200 && responseEntity !=null){
                System.out.println("响应状态："+response.getStatusLine().getStatusCode());
                System.out.println("响应长度："+responseEntity.getContentLength());
                System.out.println("响应内容："+ EntityUtils.toString(responseEntity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
                //资源释放
                try {
                    if(httpClient != null){
                        httpClient.close();
                    }
                    if(response != null){
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
