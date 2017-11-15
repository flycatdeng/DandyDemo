package com.dandy.glengine;

/**
 * <pre>
 *     作为一个Actor的延迟方面的一些基本的方法
 *     （将一些任务添加到队列，等待时机成熟了再全部取出来一个个执行，队列中不再有则不需要再执行）
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public interface IActorPendding {

    /**
     * 添加需要在创建program时的一些任务，在onDraw之前执行
     *
     * @param runnable
     */
    void addRunOnceCreateProgramBeforeDraw(Runnable runnable);

    /**
     * 处理创建programID的已经预置的任务们
     */
    void dealCreateProgramBeforeDraw();

    /**
     * 添加在调用onDraw之前（使用programID之前）需要执行的一些任务
     *
     * @param runnable
     */
    void addRunOnceBeforeDraw(Runnable runnable);

    /**
     * 处理在调用onDraw之前（使用programID之前）需要执行的一些已经预置的任务
     */
    void dealRunOnceBeforeDraw();

    /**
     * 添加在onDraw的时候需要执行的一些任务
     *
     * @param runnable
     */
    void addRunOnceOnDraw(Runnable runnable);

    /**
     * 处理onDraw的时候需要执行的一些已经预置的任务
     */
    void dealRunOnceOnDraw();

}
