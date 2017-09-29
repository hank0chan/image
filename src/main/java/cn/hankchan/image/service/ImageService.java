package cn.hankchan.image.service;

/**
 * 图片处理服务
 * @author hankChan
 *         2017/9/28 0028.
 */
public interface ImageService {

    /**
     * 缩放图片到等比例的指定宽高范围内
     * @param imgBytes 图片二进制流
     * @param maxWidth 最大图片宽度
     * @param maxHeight 最大图片高度
     * @return 操作成功返回二进制流，否则null
     */
    byte[] scaleImageByBytes(byte[] imgBytes, int maxWidth, int maxHeight);

    /**
     * 缩放图片到等比例的指定宽高范围内
     * @param imageBase64 图片base64字符串
     * @param maxWidth 最大图片宽度
     * @param maxHeight 最大图片高度
     * @return 操作成功返回二进制流，否则null
     */
    byte[] scaleImageByBase64(String imageBase64, int maxWidth, int maxHeight);

    /**
     * 裁剪图片
     * @param imgBytes 图片二进制流
     * @param x 起始x坐标。(0,0)为左上角顶点
     * @param y 起始y坐标。(0,0)为左上角顶点
     * @param width 裁剪后宽度，不可以大于原图
     * @param height 裁剪后高度，不可以大于原图
     * @return 操作成功返回二进制流，否则null
     */
    byte[] cropImageByBytes(byte[] imgBytes, float x, float y, int width, int height);

    /**
     * 裁剪图片
     * @param imageBase64 图片base64字符串
     * @param x 起始x坐标。(0,0)为左上角顶点
     * @param y 起始y坐标。(0,0)为左上角顶点
     * @param width 裁剪后宽度，不可以大于原图
     * @param height 裁剪后高度，不可以大于原图
     * @return 操作成功返回二进制流，否则null
     */
    byte[] cropImageByBase64(String imageBase64, float x, float y, int width, int height);

}
