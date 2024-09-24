/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.conexionudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionUDP {

    protected static ConcurrentHashMap<String, ClientHandler> direcciones_ip = new ConcurrentHashMap<>();
    protected static ConcurrentHashMap<String, ClientHandler> usuarios = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {

        //numero de pueerto donde va a escuchar por mensajes.
        final int PUERTO = 5000;
        // buffer para guardar mensajes
        byte[] buffer = new byte[1024];


        try {
            System.out.println("Iniciado el servidor UDP");
            DatagramSocket socket = new DatagramSocket(PUERTO);
            while (true) {
                buffer = new byte[1024];
                // crear una peticion, es lo que me va a llegar.
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);

                System.out.println("Recibo informacion del cliente...");

                socket.receive(peticion);
                int puertoCliente = peticion.getPort();
                InetAddress direccion = peticion.getAddress();
                String client_key = direccion.toString() + ":" + puertoCliente;

                // Si todavia no se encuentra la direccion ip
                if (!direcciones_ip.containsKey(client_key)) {
                    ClientHandler handler = new ClientHandler(socket, direccion, puertoCliente);
                    direcciones_ip.put(client_key, handler);
                    new Thread(handler).start();
                }
                direcciones_ip.get(client_key).messageHandler(peticion);
            }

        } catch (SocketException ex) {
            Logger.getLogger(ConexionUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static class ClientHandler implements Runnable {

        private final DatagramSocket socket;
        private final InetAddress direccion_cliente;
        private final int puerto_cliente;
        private String user;
        private volatile boolean running = true;

        public ClientHandler(DatagramSocket socket, InetAddress direccion_cliente, int puerto_cliente) {
            this.socket = socket;
            this.direccion_cliente = direccion_cliente;
            this.puerto_cliente = puerto_cliente;
            this.user = "";
        }

        @Override
        public void run() {
            while (running) {

            }
        }

        public void messageHandler(DatagramPacket mensaje) throws IOException {

            String client_message = new String(mensaje.getData(), 0, mensaje.getLength());
            System.out.println("Recibido de " + direccion_cliente + ":" + puerto_cliente + " - " + client_message);

            // respuesta del servidor
            String response;
            byte[] send;
            DatagramPacket send_packet;
             
            if (client_message.startsWith("usuario:")) {
                user = client_message.substring(client_message.indexOf(":") + 1);
                if(!usuarios.containsKey(user)){
                    usuarios.put(user, this);
                    response = "usuario agregado: " + user;
                }  
                
                response = "Hola " + user;
            } else if(usuarios.isEmpty()){
                response =  "Conectado al servidor. Ingrese 'usuario:(su usuario)'. Una vez ingresado el nombre de usuario, ingrese el nombre de usuario al que le quiera enviar un mensaje, seguido de " + ": y luego el mensaje, de esta forma: 'usuario':'mensaje'";
            } else{
                String receptor = client_message.substring(0, client_message.indexOf(":"));
                if(!usuarios.containsKey(receptor)){
                    response = "El ususario no se encuentre en la base de datos.";
               }
                response = "Mensaje enviado al usuario.";
                usuarios.get(receptor).sendMessage(user + ":" + client_message.substring(client_message.indexOf(":") + 1));
        }
                send = response.getBytes();
                
                send_packet = new DatagramPacket(send, send.length, direccion_cliente, puerto_cliente); 
            
                socket.send(send_packet);
        
    }
        public void sendMessage(String message) throws IOException {
            byte[] send;
            DatagramPacket send_packet;
            System.out.println("Enviando mensaje a " + message);
            send = message.getBytes();
            send_packet = new DatagramPacket(send, send.length, direccion_cliente, puerto_cliente);

            socket.send(send_packet);

        }
    }

}
