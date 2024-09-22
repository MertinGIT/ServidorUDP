/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.conexionudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

 
/**
 *
 * @author Martin
 */
public class cliente {
    public static void main(String[] args){
    final int PUERTO_SERVIDOR = 5000;
    byte [] buffer = new byte[1024];
    Scanner scanner = new Scanner(System.in);
        try {
            //Esta direccion es tanto para el servidor como para el cliente
            InetAddress direccionServidor = InetAddress.getByName("localhost");
            // creamos sin  puerto, ya que asi nos asigna un puerto propio
             DatagramSocket socketUDP = new DatagramSocket();
             
             System.out.println("Ingrese el mensaje");
             
             String mensaje = scanner.nextLine();
             //actualizamos los buffer de vuelta
          
             buffer = mensaje.getBytes();
             
             DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length, direccionServidor,PUERTO_SERVIDOR);
             
             socketUDP.send(pregunta);
             
             DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
             
             socketUDP.receive(peticion);
             System.out.println("Recibo peticion");
             mensaje = new String(peticion.getData());
             System.out.println(mensaje);
             
             socketUDP.close();
             
        } catch (UnknownHostException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
              
        }        
}
