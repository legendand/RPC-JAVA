package Server.serviceRegister;

import java.net.InetSocketAddress;

//服务注册接口
public interface ServiceRegister {
    //注册：保存服务与对应地址
    void register(String serviceName, InetSocketAddress serviceAddress);
}
