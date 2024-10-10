package common.serializer.myCode;

import common.Message.MessageType;
import common.serializer.mySerializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import common.Message.MessageType;

import java.util.List;
//自定义方式解码
public class MyDecoder extends ByteToMessageDecoder {
    //MyEncoder里定义的编码格式是
    //[MessageType][SerializerType][序列化数组length][序列化数组]
    //[Short][Short][Int][byte[]]
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        //1.读取MessageType
        short messageType = in.readShort();
        //目前只支持RpcRequest和RpcResponse
        if(messageType!= MessageType.REQUEST.getCode() &&
                messageType!=MessageType.RESPONSE.getCode()){
            System.out.println("暂不支持此种消息类型");
            return;
        }
        //2.读取序列化方式SerializerType
        short serializerType = in.readShort();
        Serializer serializer = Serializer.getSerializerByCode(serializerType);
        //判断序列化器是否实例化了，有可能是null
        if(serializer==null){
            throw new RuntimeException("序列化器不存在");
        }
        //3.读取序列化数组的长度
        int length = in.readInt();
        //4.读取序列化数组
        byte[] bytes=new byte[length];
        in.readBytes(bytes);
        //将字节反序列化成对象
        Object deserialize = serializer.deserialize(bytes, messageType);
        out.add(deserialize);
    }
}
