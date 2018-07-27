package com.zhili.codec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2018/6/7.
 */
public class KryoSerializer implements RpcSerializer {

    private ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            // kryo.setRegistrationRequired(false);
            // kryo.setReferences(true);
            // kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
            return kryo;
        }
    };

    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos) ;
        Kryo kryo = kryos.get();
        // writeObjectOrNull object.getClass()
        kryo.writeClassAndObject(output, obj);
        byte[] bs = output.toBytes();
        return bs;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        Input input = new Input(bis) ;
        Kryo kryo = kryos.get();
        Object bean = kryo.readClassAndObject(input);
        return clazz.cast(bean);

    }

}
