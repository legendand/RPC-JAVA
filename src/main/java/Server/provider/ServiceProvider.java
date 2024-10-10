package Server.provider;

import Server.serviceRegister.ServiceRegister;
import Server.serviceRegister.impl.ZKServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

//在服务端上线时调用ServiceProvider的provideServiceInterface方法中添加逻辑，使得本地注册服务时也注册服务到注册中心上

//因为一个服务器会有多个服务，所以需要设置一个本地服务存放器serviceProvider存放服务
//在接收到服务端的request信息之后，我们在本地服务存放器找到需要的服务，通过反射调用方法，得到结果并返回
//本地服务存放器
public class ServiceProvider {
    //集合中存放服务的实例
    private Map<String,Object> interfaceProvider;

    private String host;
    private int port;
    //注册服务类
    private ServiceRegister serviceRegister;

    public ServiceProvider(String host,int port) {
        //需要传入服务端自身的网络地址
        this.host=host;
        this.port=port;
        this.interfaceProvider=new HashMap<>();
        this.serviceRegister=new ZKServiceRegister();
    }

    //本地注册服务
    public void providerServiceInterface(Object service){
        //String serviceName = service.getClass().getName();
        Class<?>[] interfaceName = service.getClass().getInterfaces();

        for (Class<?> clazz : interfaceName) {
            //本机的映射表
            interfaceProvider.put(clazz.getName(),service);
            //在注册中心注册服务
            serviceRegister.register(clazz.getName(),new InetSocketAddress(host,port));
        }
    }

    //获取服务实例
    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
