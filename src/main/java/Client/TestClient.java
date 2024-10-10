package Client;

import Client.proxy.ClientProxy;
import common.pojo.User;
import common.service.UserService;

import java.lang.reflect.Proxy;

public class TestClient {
    public static void main(String[] args) {
        //创建ClientProxy对象
        ClientProxy clientProxy = new ClientProxy();
        //ClientProxy clientProxy = new ClientProxy("127.0.0.1",9999,0);
        //通过ClientProxy对象获取代理对象
        UserService proxy = clientProxy.getProxy(UserService.class);
        //调用代理对象的方法
        User user = proxy.getUserByUserId(1);
        System.out.println("从服务端得到的uesr="+user.toString());

        User u=User.builder().id(100).userName("wxx").sex(true).build();
        Integer id = proxy.insertUserId(u);
        System.out.println("向服务端插入user的id"+id);
    }
}
