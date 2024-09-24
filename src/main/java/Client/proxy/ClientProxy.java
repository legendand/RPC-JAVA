package Client.proxy;

import Client.IOClient;
import Client.rpcClient.RpcClient;
import Client.rpcClient.impl.NettyRpcClient;
import Client.rpcClient.impl.SimpleSocketRpcCilent;
import common.Message.RpcRequest;
import common.Message.RpcResponse;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class ClientProxy implements InvocationHandler {
    //传入参数service接口的class对象，反射封装成一个request
//    private String host;//ip
//    private int port;
    private RpcClient rpcClient;

    public ClientProxy(String host,int port,int choose) {
        switch (choose){
            case 0:
                rpcClient = new NettyRpcClient(host,port);
                break;
            case 1:
                rpcClient = new SimpleSocketRpcCilent(host, port);
        }
    }
    //不给choose参数，默认是nettyclient
    public ClientProxy(String host,int port){
        rpcClient = new NettyRpcClient(host,port);
    }
    //jdk动态代理，每一次代理对象调用方法，都会经过此方法增强(反射获取request对象，socket发送到服务端)
    public Object invoke(Object proxy,//代理对象
                         Method method,//调用的方法
                         Object[] args//调用方法对应的传递的参数
                         ) throws Throwable {
        //构建request
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramsType(method.getParameterTypes()).build();
        //数据传输
        RpcResponse response = rpcClient.sendRequest(request);
        return response.getData();
    }

    public <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(
                clazz.getClassLoader(),//真正干活对象的类加载器
                new Class[]{clazz},//代理对象 把要实现的接口（方法）写入
                this//利用代理调用方法  InvocationHandler对象
                 );
        return (T)o;
    }
}
