package server.controller;

import server.network.ServerListening;
import server.network.ServerProcessing;
import server.view.ServerMainFrm;
import shared.dto.IPAddress;
import shared.dto.ObjectWrapper;
import shared.model.Player;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerCtr {

    private ServerMainFrm view;
    private ServerSocket myServer;
    private ServerListening myListening;
    private ArrayList<ServerProcessing> myProcess;
    private IPAddress myAddress = new IPAddress("localhost", 8888);
//    private IPAddress myAddress = new IPAddress("26.87.126.183", 8888);

    public ServerCtr(ServerMainFrm view) {
        myProcess = new ArrayList<ServerProcessing>();
        this.view = view;
        openServer();
    }

    public ServerCtr(ServerMainFrm view, int serverPort) {
        myProcess = new ArrayList<ServerProcessing>();
        this.view = view;
        myAddress.setPort(serverPort);
        openServer();
    }

    private void openServer() {
        try {
            myServer = new ServerSocket(myAddress.getPort());
            myListening = new ServerListening(this);
            myListening.start();
            myAddress.setHost(InetAddress.getLocalHost().getHostAddress());
            view.showServerInfor(myAddress);
            System.out.println("server started!");
            view.showMessage("TCP server is running at the port " + myAddress.getPort() + "...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        try {
            for (ServerProcessing sp : myProcess) {
                sp.stopProcessing();
            }
            myListening.stopListening();
            myServer.close();
            view.showMessage("TCP server is stopped!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addServerProcessing(ServerProcessing sp) {
        myProcess.add(sp);
        view.showMessage("Number of client connecting to the server: " + myProcess.size());
        publicClientNumber();
    }

    public void removeServerProcessing(ServerProcessing sp) {
        myProcess.remove(sp);
        view.showMessage("Number of client connecting to the server: " + myProcess.size());
        publicClientNumber();
    }

    public ServerMainFrm getView() {
        return view;
    }

    public ServerSocket getMyServer() {
        return myServer;
    }

    public ArrayList<ServerProcessing> getMyProcess() {
        return myProcess;
    }

    public void publicClientNumber() {
        ObjectWrapper data = new ObjectWrapper(ObjectWrapper.SERVER_INFORM_CLIENT_NUMBER, myProcess.size());
        for (ServerProcessing sp : myProcess) {
            sp.sendData(data);
        }
    }

    public void sendWaitingList() {
        ArrayList<Player> listUsername = new ArrayList<>();
        System.out.println("myProcess: " + myProcess.size());
        for (ServerProcessing sp : myProcess) {
            if(sp.isIsOnline()){
                Player player = sp.getPlayer();
                if(sp.isInGame()) player.setStatus("In game");
                else player.setStatus("Online");
                listUsername.add(player);

            }
        }
        System.out.println("listUsername: " + listUsername.size());

        System.out.println("Server send waiting list:");
        System.out.println(listUsername);
        ObjectWrapper data = new ObjectWrapper(ObjectWrapper.SERVER_INFORM_CLIENT_WAITING, listUsername);
        System.out.println(data);
        for (ServerProcessing sp : myProcess) {
            sp.sendData(data);
            System.out.println("Send to: " + sp.getPlayer().getUsername());
        }

    }

}
