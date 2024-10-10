package Client.serviceCenter.impl;

import Client.cache.serviceCache;
import Client.serviceCenter.ServiceCenter;
import Client.serviceCenter.ZkWatcher.watchZK;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.net.InetSocketAddress;
import java.util.List;

public class ZKServiceCenter implements ServiceCenter {
    //curator提供的zookeeper客户端
    private CuratorFramework Zooclient;
    //zookeeper的根路径节点
    private static final String ROOT_PATH ="MyRPC";
    //serviceCache
    private serviceCache cache;

    //负责zookeeper客户端初始化，并与zookeeper服务端进行连接
    public ZKServiceCenter(){
        //指数时间重试 基本沉睡1s，最多重试三次
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        //zookeeper地址固定，无论是服务提供者还是消费者，都要与之建立连接
        this.Zooclient= CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                //sessionTimeoutMs 与 zoo.cfg中的tickTime 有关系
                // zk还会根据minSessionTimeout与maxSessionTimeout两个参数重新调整最后的超时值。默认分别为tickTime 的2倍和20倍
                // 使用心跳监听状态
                .sessionTimeoutMs(40000).retryPolicy(retry).namespace(ROOT_PATH).build();
        this.Zooclient.start();
        System.out.println("zookeeper连接成功");
        //初始化本地缓存
        cache=new serviceCache();
        //加入zookeeper事件监听器
        watchZK watcher = new watchZK(Zooclient,cache);
        //监听启动
        watcher.watchToUpdate(ROOT_PATH);
    }

    //根据服务名（接口名）返回地址
    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            //先从客户端本地缓存找
            List<String> serviceList = cache.getServiceFromCache(serviceName);
            //没有再去Zookeeper找
            //这种情况基本不会发生，或者说只会出现在初始化阶段
            if(serviceList==null){
                serviceList= Zooclient.getChildren().forPath("/" + serviceName);
            }
            //这里先用第一个，后面加负载均衡
            String s = serviceList.get(0);
            return parseAddress(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //地址变成 xxx.xxx.xxx.xxx:port字符串
    private String getServiceAddress(InetSocketAddress serveaddress){
        return serveaddress.getHostName()+":"+serveaddress.getPort();
    }
    //字符串解析为地址
    private InetSocketAddress parseAddress(String address){
        String[] result = address.split(":");
        return new InetSocketAddress(result[0],Integer.parseInt(result[1]));
    }
}
