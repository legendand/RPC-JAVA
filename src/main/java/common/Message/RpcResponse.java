package common.Message;

//定义返回信息格式RpcResponse(类似http格式)

import common.pojo.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RpcResponse implements Serializable {//实现Serializable是为了序列化
    //状态码
    private int code;
    //状态信息
    private String message;
    //更新：加入传输数据的类型，以便在自定义序列化器中解析
    private Class<?> dataType;
    //具体数据
    private Object data;
    //构造成功信息
    public static RpcResponse success(Object data){
        return RpcResponse.builder().code(StatusCode.success.value())
                .message("构造成功").data(data).build();
    }
    //构造失败信息
    public static RpcResponse fail(){
        return RpcResponse.builder().code(StatusCode.fail.value())
                .message("服务器发送错误").build();
    }

}
