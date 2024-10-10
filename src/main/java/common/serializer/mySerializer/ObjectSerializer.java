package common.serializer.mySerializer;

import java.io.*;

public class ObjectSerializer implements Serializer {
    //利用Java io 实现 对象-->字节数组
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            //oos是对象输出流，将java对象序列化成字节数组，直接连在bos上
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            //刷新oos，确保缓冲区所有数据都被写到底层流中。
            oos.flush();
            //将bos内部缓冲区的数据转换为字节数组
            bytes = bos.toByteArray();
            //关闭流
            oos.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    //利用Java io 实现 字节数组--->对象
    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        //创建字节输入流，直接将参数bytes直接输到bis的buf里
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public int getType() {
        //0表示java原始序列化器
        return 0;
    }
}
