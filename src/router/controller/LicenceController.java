/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package router.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import router.connection.ConnectionDB;

/**
 *
 * @author rdomingos
 */
public class LicenceController {

    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private String sql;

    public boolean ativarLicenca(String d1, String d2) throws IOException {
        sql = "UPDATE parametro SET  parinicio='" + d1 + "', parfinal='" + d2 + "', dias=" + getDiasLicenca(d1, d2) + " ";

        ConnectionDB conexao = new ConnectionDB();
        conexao.conectar();

        try {
            PreparedStatement pst = conexao.con.prepareStatement(sql);
            pst.executeUpdate();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na inserção!\nErro: " + ex.getMessage());
        }

        return false;

    }
    public boolean updateDias() throws IOException {
        ConnectionDB conexao = new ConnectionDB();
        conexao.conectar();

        try {
            //date(parinicio)-current_date
            PreparedStatement pst = conexao.con.prepareStatement("update parametro set dias="+getDiaRestante());
            pst.executeUpdate();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na inserção!\nErro: " + ex.getMessage());
        }
        return false;
    }
    
    public int getDiaRestante() throws IOException {
        try {
            ConnectionDB conexao = new ConnectionDB();
            conexao.conectar();

            PreparedStatement ps = conexao.con.prepareStatement("select date(parfinal)-date(current_date)  as dias from parametro");
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    return (rs.getInt(1));
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(LicenceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
   
    public int getDiasLicenca(String d1, String d2) throws IOException {
        try {
            ConnectionDB conexao = new ConnectionDB();
            conexao.conectar();

            PreparedStatement ps = conexao.con.prepareStatement("select date('" + d2 + "')-date('" + d1 + "')  as dias");
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    return (rs.getInt(1));
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(LicenceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
   

    public List<String> getPeoples() throws IOException {
        try {
            ConnectionDB conexao = new ConnectionDB();
            conexao.conectar();

            PreparedStatement ps = conexao.con.prepareStatement("select * from entidade");

            try (ResultSet rs = ps.executeQuery()) {
                List<String> people = new ArrayList<>();
                while (rs.next()) {
                    people.add(rs.getString("entnome"));
                }
                return people;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
