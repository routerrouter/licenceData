/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package router.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rdomingos
 */
public class NetworkInfo {

    public NetworkInfo() {

    }

    private final static String getMacAddress() throws IOException {
        String os = System.getProperty("os.name");
        try {
            if (os.startsWith("Windows")) {
                return windowsParseMacAddress(windowsRunIpConfigCommand());
            } else if (os.startsWith("Linux")) {
                return linuxParseMacAddress(linuxRunIfConfigCommand());
            } else {
                throw new IOException("Sistema operacional desconhecido: " + os);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
            throw new IOException(ex.getMessage());
        }
    }

    /*
 * Linux
     */
    private final static String linuxParseMacAddress(String ipConfigResponse) throws ParseException {
        String localHost = null;
        try {
            localHost = InetAddress.getLocalHost().getHostAddress();
        } catch (java.net.UnknownHostException ex) {
            ex.printStackTrace();
            throw new ParseException(ex.getMessage(), 0);
        }

        StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
        String lastMacAddress = null;

        while (tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken().trim();
            boolean containsLocalHost = line.indexOf(localHost) >= 0;

            //IP 
            if (containsLocalHost && lastMacAddress != null) {
                return lastMacAddress;
            }

            //MAC address
            int macAddressPosition = line.indexOf("HWaddr");
            if (macAddressPosition <= 0) {
                continue;
            }

            String macAddressCandidate = line.substring(macAddressPosition + 6).trim();
            if (linuxIsMacAddress(macAddressCandidate)) {
                lastMacAddress = macAddressCandidate;
                continue;
            }
        }

        ParseException ex = new ParseException("Nao foi possível ler o MAC address para " + localHost + " de [" + ipConfigResponse + "]", 0);
        ex.printStackTrace();
        throw ex;
    }

    private final static boolean linuxIsMacAddress(String macAddressCandidate) {
        if (macAddressCandidate.length() != 17) {
            return false;
        }
        return true;
    }

    private final static String linuxRunIfConfigCommand() throws IOException {
        Process p = Runtime.getRuntime().exec("ifconfig");
        InputStream stdoutStream = new BufferedInputStream(p.getInputStream());

        StringBuffer buffer = new StringBuffer();
        for (;;) {
            int c = stdoutStream.read();
            if (c == -1) {
                break;
            }
            buffer.append((char) c);
        }
        String outputText = buffer.toString();

        stdoutStream.close();

        return outputText;
    }

    /*
 * Windows
     */
    private final static String windowsParseMacAddress(String ipConfigResponse) throws ParseException {
        String localHost = null;
        try {
            localHost = InetAddress.getLocalHost().getHostAddress();
            System.out.println("localhost: " + localHost);
        } catch (java.net.UnknownHostException ex) {
            ex.printStackTrace();
            throw new ParseException(ex.getMessage(), 0);
        }

        StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
        String lastMacAddress = null;

        while (tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken().trim();

            //IP 
            if (line.endsWith(localHost) && lastMacAddress != null) {
                return lastMacAddress;
            }

            //MAC address
            int macAddressPosition = line.indexOf(":");
            if (macAddressPosition <= 0) {
                continue;
            }

            String macAddressCandidate = line.substring(macAddressPosition + 1).trim();
            if (windowsIsMacAddress(macAddressCandidate)) {
                lastMacAddress = macAddressCandidate;
                continue;
            }
        }

        ParseException ex = new ParseException("Nao foi possível ler o MAC address de [" + ipConfigResponse + "]", 0);
        ex.printStackTrace();
        throw ex;
    }

    private final static boolean windowsIsMacAddress(String macAddressCandidate) {
        if (macAddressCandidate.length() != 17) {
            return false;
        }

        return true;
    }

    private final static String windowsRunIpConfigCommand() throws IOException {
        Process p = Runtime.getRuntime().exec("ipconfig /all");
        InputStream stdoutStream = new BufferedInputStream(p.getInputStream());

        StringBuffer buffer = new StringBuffer();
        for (;;) {
            int c = stdoutStream.read();
            if (c == -1) {
                break;
            }
            buffer.append((char) c);
        }
        String outputText = buffer.toString();

        stdoutStream.close();

        return outputText;
    }

    public final static void main(String[] args) {
        try {
            System.out.println("Informacoes");
            System.out.println("Sistema operacional: " + System.getProperty("os.name"));
            System.out.println("IP/Localhost: " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("MAC Address: " + getMacAddress());
            System.out.println("Nome da maquina: " + InetAddress.getLocalHost().getHostName());
            System.out.println("Nome completo da maquina: " + InetAddress.getLocalHost().getCanonicalHostName());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
