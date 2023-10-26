package com.wr.utils.uuid;

import java.util.concurrent.ThreadLocalRandom;

/**
 * ID生成器工具类
 * 
 * @author wr
 */
public class IdUtils
{

    /**
     * 获取随机十八位
     * @return
     */
    public static String getUUID()
    {
        return String.valueOf(SnowflakeIdWorker.INSTANCE.nextId());
    }

    /**
     * 获取随机UUID
     * 
     * @return 随机UUID
     */
    public static String randomUUID()
    {
        return UUID.randomUUID().toString();
    }


    /**
     * 获取四位随机数
     * @return
     */
    public static Integer getFourNum(){
        return ThreadLocalRandom.current().nextInt(1000, 10000);
    }

    /**
     * 简化的UUID，去掉了横线
     * 
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID()
    {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     * 
     * @return 随机UUID
     */
    public static String fastUUID()
    {
        return UUID.fastUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     * 
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUUID()
    {
        return UUID.fastUUID().toString(true);
    }
}
