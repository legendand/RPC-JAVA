package common.serializer.mySerializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.Message.RpcRequest;
import common.Message.RpcResponse;

public class JsonSerializer implements Serializer{
    @Override
    public byte[] serialize(Object obj) {
        return JSONObject.toJSONBytes(obj);
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj=null;
        //传输的消息分为request和response
        switch (messageType){
            case 0:
                //request类型
                //把Json字串转化为对应的对象，fastjson可以读出基本数据类型，不用转化
                RpcRequest request = JSON.parseObject(bytes, RpcRequest.class);
                //对转换后的对象request中的params属性逐个进行类型判断
                Object[] params = request.getParams();
                for (int i = 0; i < params.length; i++) {
                    Class<?> paramType = request.getParamsType()[i];
                    //判断每个属性的类型是否和paramsTypes中的一致
                    if(!paramType.isAssignableFrom(request.getParams()[i].getClass())){
                        //不一致，进行类型转换 把第一个参数转换成第二个参数给定的类型
                        params[i]=JSONObject.toJavaObject((JSONObject)request.getParams()[i],request.getParamsType()[i]);
                    }else {
                        //一致直接赋值
                        params[i]=request.getParams()[i];
                    }
                }
                request.setParams(params);
                obj=request;
                break;
            case 1:
                //response类型
                //把Json字串转化为对应的对象，fastjson可以读出基本数据类型，不用转化
                RpcResponse response = JSON.parseObject(bytes, RpcResponse.class);
                Class<?> dataType = response.getDataType();
                //判断转化后的response对象中data的类型是否正确
                if(!dataType.isAssignableFrom(response.getData().getClass())){
                    //正确的就不用管，错了就改过来
                    response.setData(JSONObject.toJavaObject((JSONObject)response.getData(),dataType));
                }
                obj=response;
                break;
            default:
                System.out.println("系统暂不支持这种消息");
                throw new RuntimeException();
        }
        return obj;
    }

    @Override
    public int getType() {
        return 1;//1表示json序列化方式
    }
}
