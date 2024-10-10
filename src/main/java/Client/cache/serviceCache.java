package Client.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//本地使用HashMap缓存serviceCache
public class serviceCache {
    //key: serviceName 服务名
    //value: addressList 服务提供者列表
    private static Map<String, List<String>> cache = new HashMap<>();

    //添加服务
    public void addServiceToCache(String serviceName,String address){
        if(cache.containsKey(serviceName)){
            List<String> addresses = cache.get(serviceName);
            addresses.add(address);
            System.out.println("将name为"+serviceName+"地址为"+address+"的服务添加到了本地缓存");
        }else{
            List<String> list = new ArrayList<>();
            list.add(address);
            cache.put(serviceName,list);
        }
    }
    //修改服务地址
    public void replaceServiceAddress(String serviceName,String oldAddress,String newAddress){
        if(cache.containsKey(serviceName)){
            List<String> addressList=cache.get(serviceName);
            addressList.remove(oldAddress);
            addressList.add(newAddress);
        }else {
            System.out.println("修改失败，服务不存在");
        }
    }

    //取服务地址
    public List<String> getServiceFromCache(String serviceName){
        if(!cache.containsKey(serviceName)){
            return null;
        }
        return cache.get(serviceName);
    }
    //删服务地址
    public void delete(String serviceName,String address){
        List<String> adds = cache.get(serviceName);
        adds.remove(address);
        System.out.println("将name为"+serviceName+"和地址为"+address+"的服务从本地缓存中删除");
    }
}
