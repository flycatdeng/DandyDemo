package com.dandy.helper.java.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <pre>
 *     可以将数组写入到文件中，以便直接加载（省掉一些解析过程）
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 * 2017/11/22
 */
public class JBufferCacheHelper {
    private JBufferCacheHelper() {
    }

    /**
     * 将float数组转成ByteBuffer写入到文件中
     *
     * @param buffer
     * @param filePath
     * @throws Exception
     */
    public static void writeCache(float[] buffer, String filePath) throws Exception {
        RandomAccessFile cacheFile = new RandomAccessFile(filePath, "rw");//rw:可读可写
        FileChannel outChannel = cacheFile.getChannel();
        ByteBuffer buff = ByteBuffer.allocate(buffer.length * 4);
        buff.clear();

        buff.asFloatBuffer().put(buffer);

        outChannel.write(buff);
        buff.rewind();
        outChannel.close();
    }

    /**
     * 将int数组转成ByteBuffer写入到文件中
     *
     * @param buffer
     * @param filePath
     * @throws Exception
     */
    public static void writeCache(int[] buffer, String filePath) throws Exception {
        RandomAccessFile cacheFile = new RandomAccessFile(filePath, "rw");//rw:可读可写
        FileChannel outChannel = cacheFile.getChannel();
        ByteBuffer buff = ByteBuffer.allocate(buffer.length * 4);
        buff.clear();

        buff.asIntBuffer().put(buffer);

        outChannel.write(buff);
        buff.rewind();
        outChannel.close();
    }

    /**
     * 将long数组转成ByteBuffer写入到文件中
     *
     * @param buffer
     * @param filePath
     * @throws Exception
     */
    public static void writeCache(long[] buffer, String filePath) throws Exception {
        RandomAccessFile cacheFile = new RandomAccessFile(filePath, "rw");//rw:可读可写
        FileChannel outChannel = cacheFile.getChannel();
        ByteBuffer buff = ByteBuffer.allocate(buffer.length * 8);
        buff.clear();

        buff.asLongBuffer().put(buffer);

        outChannel.write(buff);
        buff.rewind();
        outChannel.close();
    }
}
