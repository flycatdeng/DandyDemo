package com.dandy.helper.java.basedata;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/4/14.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class FloatHelper {

    /**
     * 单位化
     *
     * @param vec
     * @return
     */
    public static float[] normalize(float[] vec) {
        return normalize(vec, vec.length);
    }

    /**
     * 单位化
     *
     * @param vec
     * @param n
     * @return
     */
    public static float[] normalize(float[] vec, int n) {
        int dimension = (n > 0 && n <= vec.length) ? n : vec.length;
        float mag, sum = 0;

        for (int i = 0; i < dimension; i++) {
            sum += vec[i] * vec[i];
        }
        mag = (float) Math.sqrt(sum);

        for (int j = 0; j < dimension; j++) {
            vec[j] /= mag;
        }

        return vec;
    }

    /**
     * 对所有元素进行求和
     *
     * @param floats
     * @return
     */
    public static float sum(float[] floats) {
        float sum = 0f;
        if (floats == null || floats.length == 0) {
            return sum;
        }
        for (int i = 0; i < floats.length; i++) {
            sum += floats[i];
        }
        return sum;
    }
}
