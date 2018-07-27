package com.zhili.codec;

/**
 * Created by Administrator on 2018/6/7.
 */
public interface RpcSerializer {
    public <T> byte[] serialize(T obj) throws Exception;

    public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception ;
}
