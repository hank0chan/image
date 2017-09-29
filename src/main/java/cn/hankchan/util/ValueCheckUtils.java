package cn.hankchan.util;

import java.util.Objects;

/**
 * 参数校验工具类
 * @author hankChan
 *         2017/8/7 0007.
 */
public class ValueCheckUtils {

    /**
     * 如果有任意的请求参数为null，直接抛出空指针异常。快速失败
     * @param args 请求参数
     * @return 所有必须的请求参数不为null，返回true。否则抛出空指针异常
     */
    public static boolean checkedNotNull(Object ... args) {
        if (null == args) {
            return false;
        }
        for(Object arg : args) {
            if(Objects.isNull(arg)) {
                throw new NullPointerException("必须的请求参数不得为null");
            }
        }
        return true;
    }

}
