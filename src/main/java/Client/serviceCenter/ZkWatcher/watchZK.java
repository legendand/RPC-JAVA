package Client.serviceCenter.ZkWatcher;

import Client.cache.serviceCache;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;

public class watchZK {
    //curator提供的zookeeper客户端
    private CuratorFramework zooclient;
    //本地缓存
    private serviceCache cache;

    public watchZK(CuratorFramework zooclient, serviceCache cache) {
        this.zooclient = zooclient;
        this.cache = cache;
    }

    //监听当前结点和子节点的更新、创建和删除
    public void watchToUpdate(String path){
        CuratorCache curatorCache = CuratorCache.build(zooclient, path);
        curatorCache.listenable().addListener(new CuratorCacheListener() {
            @Override
            public void event(Type type, ChildData childData, ChildData childData1) {
                //第一个参数：事件类型（枚举）
                //第二个参数：更新前结点的状态、数据
                //第三个参数：更新后节点的状态、数据
                //创建节点时，更新前结点为空，即第二个参数为null
                //删除节点时，更新后节点为空，即第三个参数为null
                // 节点创建时没有赋予值 create /curator/app1 只创建节点，在这种情况下，更新前节点的 data 为 null，获取不到更新前节点的数据
                switch (type.name()){
                    case "NODE_CREATED"://监听器第一次执行时节点存在也会触发此事件
                        //获取更新的节点的路径
                        //按照格式，读取
                        String[] pathList = pasrePath(childData1);
                        if(pathList.length<=2) break;
                        String serviceName = pathList[1];
                        String address = pathList[2];
                        //将新注册的服务加入到本地缓存
                        cache.addServiceToCache(serviceName,address);
                        break;
                    case "NODE_CHANGED"://节点更新
                        if(childData.getData()!=null){
                            System.out.println("修改前的数据:"+new String(childData.getData()));
                        }else{
                            System.out.println("节点第一次赋值");
                        }
                        String[] oldPathList = pasrePath(childData);
                        String[] newPathList = pasrePath(childData1);
                        cache.replaceServiceAddress(oldPathList[1],oldPathList[2],newPathList[2]);
                        System.out.println("修改后的数据："+new String(childData1.getData()));
                        break;
                    case "NODE_DELETED"://节点删除
                        String[] nodePathList = pasrePath(childData);
                        if(nodePathList.length<=2) break;
                        //本地缓存中删除
                        cache.delete(nodePathList[1],nodePathList[2]);
                        break;
                    default:
                        break;
                }
            }
        });
        //开启监听
        curatorCache.start();
    }

    //解析节点对应地址
    public String[] pasrePath(ChildData childData){
        //获取更新的节点的路径
        String path=new String(childData.getPath());
        //按照格式 ，读取
        return path.split("/");
    }
}
