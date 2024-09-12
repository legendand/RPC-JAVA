package Client;

import common.Message.RpcRequest;
import common.Message.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOClient {
    //这里负责底层与服务端的通信，发送request，返回response

    //这里用了static
    public static RpcResponse sendRequest(String host, int port, RpcRequest request){
        try {
            Socket socket = new Socket(host, port);
            //oos将request从内存写入网卡发给服务器
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //ois把网卡的response(来自服务器)写入内存 返回给调用sendRequest者
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(request);
            oos.flush();

            RpcResponse response = (RpcResponse) ois.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
