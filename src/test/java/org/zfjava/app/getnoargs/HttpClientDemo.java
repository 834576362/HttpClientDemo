package org.zfjava.app.getnoargs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.zfjava.app.dto.User;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther zhangfen
 * @date 2021/12/16 15:01
 */
public class HttpClientDemo {

    /**
     * Get---无参测试
     */
    @Test
    public void doGetTestOne(){
        //获取Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //创建Get请求
        HttpGet httpGet = new HttpGet("http://localhost:8080/doGetControllerOne");
        //响应模型
        CloseableHttpResponse response = null;
        try {
            //客户端执行发送
            response = httpClient.execute(httpGet);
            //从相应中获取实体
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态："+response.getStatusLine());
            System.out.println("响应状态🐎："+response.getStatusLine().getStatusCode());
            if(responseEntity != null){
                System.out.println("响应长度："+responseEntity.getContentLength());
                System.out.println("响应内容："+ EntityUtils.toString(responseEntity));
            }else {
                System.out.println("没有响应内容");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
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

    /**
     * Get---有参测试(方式一:手动在url后面加上参数)
     * @Date
     */
    @Test
    public void doGetTestTwo(){
        //创建HttpClient客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        //参数
        StringBuffer params = new StringBuffer();
        // 字符数据最好encoding以下;这样一来，某些特殊字符才能传过去(如:某人的名字就是“&”,不encoding的话,传不过去)
        try {
            params.append("name="+ URLEncoder.encode("张三丰","UTF-8"));
            params.append("&");
            params.append("age=24");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //创建Get请求
        HttpGet httpGet = new HttpGet("http://localhost:8080/doGetTestTwo"+"?"+params);
        //响应模型
        CloseableHttpResponse response = null;
        try {

            // 配置信息
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间(单位毫秒)
                    .setConnectTimeout(5000)
                    // 设置请求超时时间(单位毫秒)
                    .setConnectionRequestTimeout(5000)
                    // socket读写超时时间(单位毫秒)
                    .setSocketTimeout(5000)
                    // 设置是否允许重定向(默认为true)
                    .setRedirectsEnabled(true).build();
            // 将上面的配置信息 运用到这个Get请求里
            httpGet.setConfig(requestConfig);

            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态："+response.getStatusLine());
            System.out.println("响应状态码："+response.getStatusLine().getStatusCode());
            String s =  EntityUtils.toString(responseEntity);
            System.out.println("响应内容："+ s);
            System.out.println("响应长度："+responseEntity.getContentLength());
            JSONObject jsonObject = JSONObject.parseObject(s);
            System.out.println("name:"+jsonObject.get("name"));
            System.out.println("age:"+jsonObject.get("age"));
            System.out.println("jsonObject.toJSONString()："+jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }{
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

    /**
     * GET---有参（方式二:将参数放入键值对类中,再放入URI中,从而通过URI得到HttpGet实例）
     */
    @Test
    public void doGetTestWayOne(){
        //获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //参数
        URI uri =null;
        //将参数放入键值对类NameValuePair中，在放入集合中
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("name","张三"));
        params.add(new BasicNameValuePair("age","18"));
        //设置uri信息，并将参数集合放入uri
        //注:这里也支持一个键值对一个键值对地往里面放setParameter(String key, String value)
        try {
            uri = new URIBuilder().
                    setScheme("http").
                    setHost("localhost").
                    setPort(8080).
                    setPath("/doGetTestWayOne").
                    setParameters(params).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //创建Get请求
        HttpGet httpGet = new HttpGet(uri);

        //响应模型
        CloseableHttpResponse response = null;

        RequestConfig requestConfig = RequestConfig.custom().
                //设置连接超时时间（毫秒）
                setConnectTimeout(5000).
                //设置请求超时时间（毫秒）
                setConnectionRequestTimeout(5000).
                //socket读写超时时间（毫秒）
                setSocketTimeout(5000).
                //设置是否重定向（默认true）
                setRedirectsEnabled(true).build();

        //引用配置信息
        httpGet.setConfig(requestConfig);

        try {
            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态："+response.getStatusLine());
            System.out.println("响应状态码："+response.getStatusLine().getStatusCode());
            System.out.println("响应内容长度："+responseEntity.getContentLength());
            System.out.println("响应内容："+EntityUtils.toString(responseEntity));
            System.out.println("请求地址："+uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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

    /**
     *POST---无参
     */
    @Test
    public void doPostTestOne(){
        //获取客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //创建post请求
        HttpPost httpPost = new HttpPost("http://localhost:8080/doPostTestOne");
        //响应模型
        CloseableHttpResponse response=null;

        try {
            //发送Post请求
            response = httpClient.execute(httpPost);
            //从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态："+response.getStatusLine());
            System.out.println("响应状态码："+response.getStatusLine().getStatusCode());

            if(responseEntity != null){
                System.out.println("响应长度："+responseEntity.getContentLength());
                System.out.println("响应内容："+EntityUtils.toString(responseEntity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * POST---有参测试（普通参数）
     */
    @Test
    public void doPostTestFour(){
        //获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        //参数
        StringBuffer params = new StringBuffer();

        try {
            params.append("name="+URLEncoder.encode("张三","UTF-8"));
            params.append("&");
            params.append("age=24");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpPost httpPost = new HttpPost("http://localhost:8080/doPostTestFour"+"?"+params);
        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * POST---有参测试(对象参数)
     */
    @Test
    public void doPostTestTwo(){
        //创建Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        //创建Post请求
        HttpPost httpPost = new HttpPost("http://localhost:8080/doPostTestTwo");
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        //参数
        User user = new User();
        user.setName("李四");
        user.setAge(28);
        //利用阿里的fastjson，将Object转换为json字符串
        String jsonString = JSON.toJSONString(user);
        StringEntity entity = new StringEntity(jsonString, "UTF-8");

        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);

        //响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * POST---有参（普通参数+对象参数）
     */
    @Test
    public void doPostTestThree(){
        //获得HTTP客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //创建Post请求
        //参数
        URI uri = null;
        //将参数放入键值对类NameValuePair中，在放入集合
        //List<NameValuePair> params = new ArrayList<>();
        //params.add(new BasicNameValuePair("color","红色"));
        //设置uri信息，并将参数集合放入uri
        //注：这里也支持一个键值对的王往里面放addParameter(String key,String value);
        try {
            uri = new URIBuilder().setScheme("http")
                    .setHost("localhost").setPort(8080).setPath("/doPostTestThree").
                    addParameter("color","红色").
                    addParameter("sex","男").build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //创建对象参数
        User user = new User();
        user.setAge(18);
        user.setName("掌声在哪里");
        //连接参数
        RequestConfig config = RequestConfig.custom().
                //设置请求超时时长（毫秒）
                setConnectionRequestTimeout(5000).
                //设置连接超时时长
                setConnectTimeout(5000).
                //设置读写超时时长
                setSocketTimeout(5000).
                //是否重定向
                setRedirectsEnabled(true).build();

        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        String s = JSON.toJSONString(user);
        StringEntity stringEntity = new StringEntity(s,"UTF-8");
        httpPost.setEntity(stringEntity);
        httpPost.setConfig(config);

        System.out.println("请求地址"+uri);

        CloseableHttpResponse response =null;
        try {
            response =httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            System.out.println("响应状态码："+response.getStatusLine().getStatusCode());
            System.out.println("响应状态："+response.getStatusLine().getStatusCode());
            if(httpClient != null){
                System.out.println("响应长度："+httpEntity.getContentLength());
                System.out.println("响应内容："+EntityUtils.toString(httpEntity));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * 发送文件
     *
     * multipart/form-data传递文件(及相关信息)
     *
     * 注:如果想要灵活方便的传输文件的话，
     *    除了引入org.apache.httpcomponents基本的httpclient依赖外
     *    再额外引入org.apache.httpcomponents的httpmime依赖。
     *    追注:即便不引入httpmime依赖，也是能传输文件的，不过功能不够强大。
     *
     */
    @Test
    public void test4() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://localhost:8080/file");
        CloseableHttpResponse response = null;
        try {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            // 第一个文件
            String filesKey = "files";
            File file1 = new File("C:\\Users\\83457\\Pictures\\Camera Roll\\123r.jpg");
            multipartEntityBuilder.addBinaryBody(filesKey, file1);
            // 第二个文件(多个文件的话，使用同一个key就行，后端用数组或集合进行接收即可)
            File file2 = new File("C:\\Users\\JustryDeng\\Desktop\\头像.jpg");
            // 防止服务端收到的文件名乱码。 我们这里可以先将文件名URLEncode，然后服务端拿到文件名时在URLDecode。就能避免乱码问题。
            // 文件名其实是放在请求头的Content-Disposition里面进行传输的，如其值为form-data; name="files"; filename="头像.jpg"
            multipartEntityBuilder.addBinaryBody(filesKey, file2, ContentType.DEFAULT_BINARY, URLEncoder.encode(file2.getName(), "utf-8"));
            // 其它参数(注:自定义contentType，设置UTF-8是为了防止服务端拿到的参数出现乱码)
            ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
            multipartEntityBuilder.addTextBody("name", "王五", contentType);
            multipartEntityBuilder.addTextBody("age", "25", contentType);

            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);

            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            System.out.println("HTTPS响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("HTTPS响应内容长度为:" + responseEntity.getContentLength());
                // 主动设置编码，来防止响应乱码
                String responseStr = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                System.out.println("HTTPS响应内容为:" + responseStr);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}


















