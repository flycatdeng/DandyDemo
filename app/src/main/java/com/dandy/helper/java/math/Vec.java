package com.dandy.helper.java.math;

/**
 * <pre>
 *     向量接口，一些必须的统一的要实现的方法
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public interface Vec<T extends Vec<T>> {
    /**
     * 复制该向量对象，其他函数会对该类数据做更改
     *
     * @return
     */
    T copy();

    /**
     * 加上一个自己同类型的向量，返回自身
     *
     * @return
     */
    T add(T vec);

    /**
     * 向量所有元素都加上一个值，并返回自身
     *
     * @param value
     * @return
     */
    T add(float value);

    /**
     * 向量所有元素分别加上参数中的标量，返回自身
     *
     * @param values 分别要被添加的标量数组，如果长度与向量个数不一致，将会抛异常
     * @return
     */
    T add(float... values);

    /**
     * 减去一个自己同类型的向量，返回自身
     *
     * @param vec
     * @return
     */
    T minus(T vec);

    /**
     * 向量所有元素都减去一个值，并返回自身
     *
     * @param value
     * @return
     */
    T minus(float value);

    /**
     * 向量所有元素分别减去参数中的标量，返回自身
     *
     * @param values 分别要被减去的标量数组，如果长度与向量个数不一致，将会抛异常
     * @return
     */
    T minus(float... values);

    /**
     * 向量所有元素分别乘以参数中的标量，返回自身
     *
     * @param sameValue
     * @return
     */
    T multiply(float sameValue);

    /**
     * 向量所有元素分别乘以向量中对应位置的标量，返回自身
     *
     * @param t
     * @return
     */
    T multiply(T t);

    /**
     * 向量归一化，返回一个新向量
     *
     * @return
     */
    T normalize();

    /**
     * 将该向量转成float数组
     *
     * @return
     */
    float[] toFloatArray();

    /**
     * 求得各元素的和
     *
     * @return
     */
    float getItemSum();
}
