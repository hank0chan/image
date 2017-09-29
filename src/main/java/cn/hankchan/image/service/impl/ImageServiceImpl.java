package cn.hankchan.image.service.impl;

import cn.hankchan.image.core.ImageHandler;
import cn.hankchan.image.service.ImageService;
import cn.hankchan.util.Base64Utils;

/**
 * @author hankChan
 *         2017/9/29 0029.
 */
public class ImageServiceImpl implements ImageService {

    private String srcDir;
    private String desDir;

    /**
     * 构造器
     * @param srcDir 源图图片本地临时目录
     * @param desDir 生成图片本地临时目录
     */
    public ImageServiceImpl(String srcDir, String desDir) {
        this.srcDir = srcDir;
        this.desDir = desDir;
    }

    @Override
    public byte[] scaleImageByBytes(byte[] imgBytes, int maxWidth, int maxHeight) {
        if(null == imgBytes) {
            return null;
        }
        ImageHandler imageHandler = new ImageHandler();
        return imageHandler.getImageScaleBytes(imgBytes, maxWidth, maxHeight, srcDir, desDir);
    }

    @Override
    public byte[] scaleImageByBase64(String imageBase64, int maxWidth, int maxHeight) {
        byte[] imgBytes = Base64Utils.base64Decode(imageBase64);
        if(null == imgBytes) {
            return null;
        }
        ImageHandler imageHandler = new ImageHandler();
        return imageHandler.getImageScaleBytes(imgBytes, maxWidth, maxHeight, srcDir, desDir);
    }

    @Override
    public byte[] cropImageByBytes(byte[] imgBytes, float x, float y, int width, int height) {
        if(null == imgBytes) {
            return null;
        }
        ImageHandler imageHandler = new ImageHandler();
        return imageHandler.getImageCropBytes(imgBytes, x, y, width, height, srcDir, desDir);
    }

    @Override
    public byte[] cropImageByBase64(String imageBase64, float x, float y, int width, int height) {
        byte[] imgBytes = Base64Utils.base64Decode(imageBase64);
        if(null == imgBytes) {
            return null;
        }
        ImageHandler imageHandler = new ImageHandler();
        return imageHandler.getImageCropBytes(imgBytes, x, y, width, height, srcDir, desDir);
    }

}
