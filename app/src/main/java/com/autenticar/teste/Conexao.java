package com.autenticar.teste;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.UUID;

import lac.cnclib.net.NodeConnection;
import lac.cnclib.net.NodeConnectionListener;
import lac.cnclib.net.groups.Group;
import lac.cnclib.net.groups.GroupCommunicationManager;
import lac.cnclib.net.groups.GroupMembershipListener;
import lac.cnclib.net.mrudp.MrUdpNodeConnection;
import lac.cnclib.sddl.message.ApplicationMessage;
import lac.cnclib.sddl.message.Message;
import lac.cnet.sddl.objects.PrivateMessage;


public class Conexao implements NodeConnectionListener, GroupMembershipListener {

    private static int gatewayPort  = 5500;
    private MrUdpNodeConnection connection;
    private UUID myUUID;
    private String nome;
    private boolean isConnect;
    private GroupCommunicationManager groupManager;
    private Group aGroup;
    String IP_VTNT;


    public Conexao(String IP, String nome){
        this.nome = nome;

        InetSocketAddress address = new InetSocketAddress(IP,gatewayPort);
        try{
            connection = new MrUdpNodeConnection();
            connection.addNodeConnectionListener(this);
            connection.connect(address);

            aGroup = new Group(250,1);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void connected(NodeConnection nodeConnection) {
        ApplicationMessage message = new ApplicationMessage();
        message.setContentObject("Conectando "+ this.nome);
        myUUID = connection.getUuid();
        groupManager = new GroupCommunicationManager(nodeConnection);

        groupManager.addMembershipListener(this);
        if(connection.getUuid() != null)
            this.isConnect = true;

        try{
            connection.sendMessage(message);
            groupManager.joinGroup(aGroup);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        return isConnect;
    }

    @Override
    public void reconnected(NodeConnection nodeConnection, SocketAddress socketAddress, boolean b, boolean b1) {
        this.isConnect = true;
        Log.i("Alerta","Reconectando");
    }

    @Override
    public void disconnected(NodeConnection nodeConnection) {
        this.isConnect = false;
        Log.i("Alerta","Desconectado");
    }

    @Override
    public void newMessageReceived(NodeConnection nodeConnection, Message message) {
//        Toast.makeText(this.context, "Mensagem Recebida", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this.context, message.getContentObject().toString(), Toast.LENGTH_SHORT).show();
        Log.i("Nova Mensagem", message.getContentObject().toString());

        String[] t = message.getContentObject().toString().split(" ");
        if ("001x".equals(t[0])){
            IP_VTNT = t[1];
        }
    }

    public String getIP_VTNT(){
        return IP_VTNT;
    }


    @Override
    public void unsentMessages(NodeConnection nodeConnection, List<Message> list) {


    }

    @Override
    public void internalException(NodeConnection nodeConnection, Exception e) {

    }

    public void sendAction(String direcao, int potencia, int angulo) throws IOException {
        ApplicationMessage action = new ApplicationMessage();
        action.setContentObject("ctrl" + " " +  direcao + " " + potencia + " " + angulo);
//        action.setRecipientID(uuidVTNT);

//        ApplicationMessage action1 = new ApplicationMessage();
//        action1.setContentObject("ctrl" + " " +  direcao + " " + potencia + " " + angulo);
//        action1.setRecipientID(uuidVTNT1);


        try {
            groupManager.sendGroupcastMessage(action, aGroup);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendLEDIR(boolean ledIR){
        ApplicationMessage action = new ApplicationMessage();
        action.setContentObject("led" + " " + ledIR);

        try {
            groupManager.sendGroupcastMessage(action, aGroup);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UUID getMyUUID() {
        return myUUID;
    }

    @Override
    public void enteringGroups(List<Group> list) {
        Log.i("Alerta","Entrando em grupo" );

//        ApplicationMessage appMsg = new ApplicationMessage();
//        appMsg.setContentObject("Hello Group");
//        try {
//            groupManager.sendGroupcastMessage(appMsg, aGroup);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void leavingGroups(List<Group> list) {
        ApplicationMessage appMsg = new ApplicationMessage();
        appMsg.setContentObject("Até mais! De "+nome);
        try {
            groupManager.sendGroupcastMessage(appMsg, aGroup);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
