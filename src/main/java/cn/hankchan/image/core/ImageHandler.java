package cn.hankchan.image.core;

import cn.hankchan.util.UniqueUtils;
import cn.hankchan.util.ValueCheckUtils;
import com.alibaba.simpleimage.ImageRender;
import com.alibaba.simpleimage.SimpleImageException;
import com.alibaba.simpleimage.render.*;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理功能类
 * @author hankChan
 *         2017/9/29 0029.
 */
public class ImageHandler {

    /**
     * 删除文件
     * @param fileName 文件绝对路径
     */
    public void deleteFile(String fileName) {
        cn.hankchan.util.IOUtils.deleteFile(fileName);
    }

    /**
     * 对图片进行裁剪操作，返回裁剪后的图片二进制流
     * <p>无需处理临时生成的图片，已经自动删除</p>
     * @param srcBytes 源图
     * @param x 裁剪起始x坐标。(0,0)为左上角顶点
     * @param y 裁剪起始y坐标。(0,0)为左上角顶点
     * @param width 裁剪后宽度
     * @param height 裁剪后高度
     * @param tempSrcDir 源图临时目录
     * @param tempDesDir 结果图片临时目录
     * @return 成功操作后的图片二进制流，失败返回null
     */
    public byte[] getImageCropBytes(byte[] srcBytes, float x, float y, int width, int height,
                                    String tempSrcDir, String tempDesDir) {
        // 压缩，获取返回图片路径
        String desFile = getImageCropDir(srcBytes, x, y, width, height, tempSrcDir, tempDesDir);
        // 从图片路径获取图片流
        byte[] result = cn.hankchan.util.IOUtils.file2Bytes(desFile);
        // 删除临时图片
        deleteFile(desFile);
        return result;
    }

    /**
     * 对图片进行压缩操作，返回压缩后的图片二进制流
     * 注：如果图片达不到maxWidth x maxHeight，将不会做任何处理
     * <p>无需处理临时生成的图片，已经自动删除</p>
     * @param srcBytes 图片二进制流
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @param tempSrcDir 源图临时目录
     * @param tempDesDir 结果图片临时目录
     * @return 成功操作的图片二进制流，失败返回null
     */
    public byte[] getImageScaleBytes(byte[] srcBytes, int maxWidth, int maxHeight,
                                     String tempSrcDir, String tempDesDir) {
        // 压缩，获取返回图片路径
        String desFile = getImageScaleDir(srcBytes, maxWidth, maxHeight, tempSrcDir, tempDesDir);
        // 从图片路径获取图片流
        byte[] result = cn.hankchan.util.IOUtils.file2Bytes(desFile);
        // 删除临时图片
        deleteFile(desFile);
        return result;
    }

    /**
     * 对图片进行裁剪操作，返回裁剪后的绝对路径图片名称
     * <p>建议：调用该方法获取图片路径并处理后，再调用一次删除图片方法{@link #deleteFile(String)}将生成后的临时图片文件删除</p>
     * @param srcBytes 源图
     * @param x 裁剪起始x坐标。(0,0)为左上角顶点
     * @param y 裁剪起始y坐标。(0,0)为左上角顶点
     * @param width 裁剪后宽度
     * @param height 裁剪后高度
     * @param tempSrcDir 源图临时目录
     * @param tempDesDir 结果图片临时目录
     * @return 成功操作的图片路径，失败返回null
     */
    public String getImageCropDir(byte[] srcBytes, float x, float y, int width, int height,
                                  String tempSrcDir, String tempDesDir) {
        // 非空校验，快速失败

        ValueCheckUtils.checkedNotNull(srcBytes, tempSrcDir, tempDesDir);
        if(tempDesDir.equals(tempSrcDir)) {
            throw new RuntimeException("请不要将源图临时目录和生成图临时目录设置为同一个目录");
        }
        tempSrcDir = tempSrcDir.endsWith("/") ? tempSrcDir : tempSrcDir + "/";
        tempDesDir = tempDesDir.endsWith("/") ? tempDesDir : tempDesDir + "/";
        long picId = UniqueUtils.getId();
        String srcFile = tempSrcDir + picId + ".jpg";
        File in = null;
        try {
            in = cn.hankchan.util.IOUtils.bytes2File(srcBytes, srcFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String desFile = tempDesDir + picId + ".jpg";
        File out = new File(desFile);
        CropParameter cropParam = new CropParameter(x, y, width, height);
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        WriteRender wr = null;
        try {
            inStream = new FileInputStream(in);
            outStream = new FileOutputStream(out);
            ImageRender rr = new ReadRender(inStream);
            ImageRender sr = new CropRender(rr, cropParam);
            wr = new WriteRender(sr, outStream);
            // 触发图片处理
            wr.render();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inStream);         //图片文件输入输出流必须记得关闭
            IOUtils.closeQuietly(outStream);
            if (wr != null) {
                try {
                    wr.dispose();                   //释放simpleImage的内部资源
                } catch (SimpleImageException ignore) {
                    // skip ...
                }
            }
            // 注意，一定要删除源图片的临时文件
            cn.hankchan.util.IOUtils.deleteFile(srcFile);
        }
        return desFile;
    }

    /**
     * 对图片进行压缩操作，返回压缩后的绝对路径图片名称
     * <p>注：如果图片达不到maxWidth x maxHeight，将不会做任何处理</p>
     * <p>建议：调用该方法获取图片路径并处理后，再调用一次删除图片方法{@link #deleteFile(String)}将生成后的临时图片文件删除</p>
     * @param srcBytes 图片二进制流
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @param tempSrcDir 源图临时目录
     * @param tempDesDir 结果图片临时目录
     * @return 成功操作的图片路径，失败返回null
     */
    public String getImageScaleDir(byte[] srcBytes, int maxWidth, int maxHeight,
                                   String tempSrcDir, String tempDesDir) {
        // 非空校验，快速失败
        ValueCheckUtils.checkedNotNull(srcBytes, tempSrcDir, tempSrcDir);

        if(tempDesDir.equals(tempSrcDir)) {
            throw new RuntimeException("请不要将源图临时目录和生成图临时目录设置为同一个目录");
        }
        tempSrcDir = tempSrcDir.endsWith("/") ? tempSrcDir : tempSrcDir + "/";
        tempDesDir = tempDesDir.endsWith("/") ? tempDesDir : tempDesDir + "/";

        long picId = UniqueUtils.getId();
        String srcFile = tempSrcDir + picId + ".jpg";
        File in = null;
        try {
            in = cn.hankchan.util.IOUtils.bytes2File(srcBytes, srcFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String desFile = tempDesDir + picId + ".jpg";
        File out = new File(desFile);

        // handler start
        //将图像缩略到maxWidth x maxHeight以内，不足则不做任何处理
        ScaleParameter scaleParam = new ScaleParameter(maxWidth, maxHeight);

        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        WriteRender wr = null;
        try {
            inStream = new FileInputStream(in);
            outStream = new FileOutputStream(out);
            ImageRender rr = new ReadRender(inStream);
            ImageRender sr = new ScaleRender(rr, scaleParam);
            wr = new WriteRender(sr, outStream);
            //触发图像处理
            wr.render();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inStream);         //图片文件输入输出流必须记得关闭
            IOUtils.closeQuietly(outStream);
            if (wr != null) {
                try {
                    wr.dispose();                   //释放simpleImage的内部资源
                } catch (SimpleImageException ignore) {
                    // skip ...
                }
            }
            // 注意，一定要删除源图片的临时文件
            cn.hankchan.util.IOUtils.deleteFile(srcFile);
        }
        // handler end
        // 由于需要返回生成图片的完整路径，所以不要在这里删除
        return desFile;
    }
}
