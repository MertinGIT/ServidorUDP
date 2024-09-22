/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.conexionudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionUDP {

    public static void main(String[] args) throws IOException {
        //numero de pueerto donde va a escuchar por mensajes.
        final int PUERTO = 5000;
        // buffer para guardar mensajes
        byte[] buffer = new byte[1024];
         byte[] buffer2 = new byte[1024];

        try {
             System.out.println("Iniciado el servidor UDP");
             DatagramSocket socketUDP = new DatagramSocket(PUERTO);
            while (true) {
                buffer = new byte[1024];
                buffer2 = new byte[1024];
                // crear una peticion, es lo que me va a llegar.
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);

                System.out.println("Recibo informacion del cliente...");

                socketUDP.receive(peticion);

                String mensaje = new String(peticion.getData());

                System.out.println("El mensjae es: " + mensaje);

                int puertoCliente = peticion.getPort();
                InetAddress direccion = peticion.getAddress();

                String respuestaServer = "¡HOLA respuesta del servidor!";

                buffer2 = respuestaServer.getBytes();

                DatagramPacket respuesta = new DatagramPacket(buffer2, buffer2.length, direccion, puertoCliente);

                System.out.println("envio informacion del cliente...");

                socketUDP.send(respuesta);
            }

        } catch (SocketException ex) {
            Logger.getLogger(ConexionUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
