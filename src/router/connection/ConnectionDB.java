/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package router.connection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Router
 */
public class ConnectionDB {

   
    
    private static String DRIVER = "org.postgresql.Driver";
    private static String USER = "postgres";
    private static String PASSWORD = "s@c0m9s3rsistemas";
    public Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;

    public void conectar() throws FileNotFoundException, IOException { // Metodo responsavel por realizar a conexão;
        
        try {
            FileReader arq = new FileReader("Server.txt");
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            System.setProperty("jdbc.Drivers", DRIVER); // Seta a propriedade do driver de conexão;
            con = DriverManager.getConnection("jdbc:postgresql://"+linha+":5432/sigha-s", USER, PASSWORD); // Realiza a conexão com o banco;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro de conexão!\nERRO: " + ex.getMessage(), "Banco de Dados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void desconectar() { // Metodo responsavel por fechar a conexão
        try {
            con.close(); // Fechar conexão
            JOptionPane.showMessageDialog(null, "Conexão fechada com sucesso!", "Banco de Dados", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão!\nERRO: " + ex.getMessage(), "Banco de Dados", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public static String getIP(String file) {
        System.out.println("file: " + file);
        try {
            FileReader arq = new FileReader(file);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            System.out.println("linha " + linha);
            while (linha != null) {
                linha = lerArq.readLine(); // lê da segunda até a última linha
                return linha;
            }

            arq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
        }

        return "";
    }

 

   

}
