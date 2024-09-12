package Server;

import Server.provider.ServiceProvider;
import Server.server.RpcServer;
import Server.server.impl.SimpleRPCRPCServer;
import common.service.impl.UserServiceImpl;

public class TestServer {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.providerServiceInterface(userService);

        RpcServer rpcServer = new SimpleRPCRPCServer(serviceProvider);
        rpcServer.start(9999);
    }
}
