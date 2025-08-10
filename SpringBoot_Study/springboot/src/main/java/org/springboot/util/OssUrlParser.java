package org.springboot.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OssUrlParser {

    /**
     * 从 OSS URL 解析文件内容
     *
     * @param ossUrl OSS 文件 URL
     *
     * @param fileType 文件类型：pdf / docx / xlsx
     * @return 解析后的文本内容
     * @throws IOException 网络或解析失败
     */
    public static String parseOssUrl(String ossUrl, String fileType) throws IOException {
        // 获取文件流
        InputStream inputStream = getInputStreamFromOssUrl(ossUrl);

        // 传入 DocumentParserUtil 解析
        return DocumentParserUtil.parseFromStream(inputStream, fileType);
    }

    /**
     * 通过 HTTP 请求获取 OSS 文件流
     */
    private static InputStream getInputStreamFromOssUrl(String ossUrl) throws IOException {
        URL url = new URL(ossUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);

        if (connection.getResponseCode() != 200) {
            throw new IOException("OSS 文件访问失败: HTTP " + connection.getResponseCode());
        }

        return connection.getInputStream();
    }

    public static void main(String[] args) {
        try {
            String result = parseOssUrl("https://paiqingnian.oss-cn-shenzhen.aliyuncs.com/2025/08/09/b9ada1595c2b40f1b8a37e1dd2dd3f74.docx", "docx");
            System.out.println("解析结果：\n" + result);
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常，否则你看不到错误
        }
    }
}
