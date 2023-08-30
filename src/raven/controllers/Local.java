/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package raven.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class Local {
    public static String getMachineName() {
        try {
            InetAddress localMachine = InetAddress.getLocalHost();
            return localMachine.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
}
