package cn.hankchan.image;

import cn.hankchan.image.service.ImageService;
import cn.hankchan.image.service.impl.ImageServiceImpl;
import cn.hankchan.util.Base64Utils;
import cn.hankchan.util.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author hankChan
 *         2017/9/29 0029.
 */
public class ImageServiceTest {

    static ImageService imageService;
    static String url = "https://img.alicdn.com/imgextra/i2/805771588/TB2pfk8z.dnpuFjSZPhXXbChpXa_!!805771588.jpg";
    static {
        String srcDir = "F:/img/srcDir";
        String desDir = "F:/img/desDir";
        imageService = new ImageServiceImpl(srcDir, desDir);
    }

    @Test
    public void testCropImageByBase64() throws IOException {
        byte[] imgBytes = download(url);
        String imageBase64 = Base64Utils.base64Encode(imgBytes);
        byte[] imageBase64Result = imageService.cropImageByBase64(imageBase64, 0F, 0F, 300, 200);
        IOUtils.bytes2File(imageBase64Result, "F:/img/imageService-cropImageByBase64.jpg");
    }

    @Test
    public void testCropImageByBytes() throws IOException {
        byte[] imgBytes = download(url);
        byte[] imgBytesResult = imageService.cropImageByBytes(imgBytes, 0F, 0F, 400, 220);
        IOUtils.bytes2File(imgBytesResult, "F:/img/imageService-cropImageByBytes.jpg");
    }

    @Test
    public void testScaleImageByBase64() throws IOException {
        byte[] imgBytes = download(url);
        String imageBase64 = Base64Utils.base64Encode(imgBytes);
        byte[] imageBase64Result = imageService.scaleImageByBase64(imageBase64, 300, 500);
        IOUtils.bytes2File(imageBase64Result, "F:/img/imageService-scaleImageByBase64.jpg");
    }

    @Test
    public void testScaleImageByBytes() throws IOException {
        byte[] imgBytes = download(url);
        byte[] imgBytesResult = imageService.scaleImageByBytes(imgBytes, 500, 400);
        IOUtils.bytes2File(imgBytesResult, "F:/img/imageService-scaleImageByBytes.jpg");
    }

    private byte[] download(String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            InputStream inputStream = response.getEntity().getContent();
            return readInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果len为-1，表示读取完毕
        int len = 0;
        while((len = inputStream.read(buffer)) != -1) {
            // 用输出流往buffer中写入数据
            byteArrayOutputStream.write(buffer, 0, len);
        }
        // 关闭资源
        inputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
