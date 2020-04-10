package com.autenticar.teste;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.UUID;

import lac.cnclib.net.NodeConnection;
import lac.cnclib.net.NodeConnectionListener;
import lac.cnclib.net.mrudp.MrUdpNodeConnection;
import lac.cnclib.sddl.message.ApplicationMessage;
import lac.cnclib.sddl.message.Message;


public class Conexao implements NodeConnectionListener {

    private static int gatewayPort  = 5500;
    private MrUdpNodeConnection connection;
    private UUID myUUID;
    private String nome;
    private boolean isConnect;

    public Conexao(String IP, String nome){
        this.nome = nome;

        InetSocketAddress address = new InetSocketAddress(IP,gatewayPort);
        try{
            connection = new MrUdpNodeConnection();
            connection.addNodeConnectionListener(this);
            connection.connect(address);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void connected(NodeConnection nodeConnection) {
        ApplicationMessage message = new ApplicationMessage();
        message.setContentObject("Conectando "+ this.nome);
        myUUID = connection.getUuid();
        this.isConnect = true;

        try{
            connection.sendMessage(message);
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

    }

    @Override
    public void unsentMessages(NodeConnection nodeConnection, List<Message> list) {


    }

    @Override
    public void internalException(NodeConnection nodeConnection, Exception e) {

    }

    public void sendAction(String direcao, int potencia) throws IOException {
        ApplicationMessage action = new ApplicationMessage();
        action.setContentObject("Direção: " + direcao + " - Potência: " + potencia + "%");
        connection.sendMessage(action);
    }

    public UUID getMyUUID() {
        return myUUID;
    }
}
