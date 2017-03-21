package com;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by jacobth on 2016-08-10.
 */
public class DatabaseAction {

    private Connection connection;

    public DatabaseAction() {
        //connect();
    }

    private void connect(String database) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + database, "root", "jacke1");
        } catch (SQLException e) {
            System.out.println("Can't connect");
            e.printStackTrace();
        }
    }

    public void updateCharacter(String name, String newName, String level, String gold, String gender) {
        try {
            connect("arc_character");
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE characters SET name='" + newName  + "', level=" + level + ",gold=" + gold + ",gender=" + gender + " WHERE name='" + name + "'");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePosition(String x, String y, String z, String orientation, String mapId, String zoneId, String name) {
        try {
            connect("arc_character");
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE characters SET positionX=" + x  + ", positionY=" + y + ",positionZ=" + z + ",orientation=" + orientation + ",mapId=" + mapId + ",zoneId=" + zoneId + " WHERE name='" + name + "'");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeWeather(String zoneId, String type) {
        try {
            int high = 100;
            int low = 0;
            int mid = low;
            connect("arc_world");
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE weather SET high_chance='" + high + "', high_type=" + type + ",med_chance=" + mid + ",low_chance=" + low + " WHERE zoneId='" + zoneId + "'");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAccount(String username) {
        ArrayList<String> list= new ArrayList<>();
        String password = "";
        String acct = "";
        try {
            connect("arc_logon");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT password,acct FROM accounts WHERE login='" + username + "'");
            while(rs.next()) {
                password = rs.getString("password");
                int acctInt = rs.getInt("acct");
                acct = acctInt+"";
            }
            list.add(password);
            list.add(acct);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<String> getCharacters(String acct) {
        ArrayList<String> list = new ArrayList();
        try {
            connect("arc_character");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT name,race,level,gold,gender FROM characters WHERE acct=" + acct);
            while(rs.next()) {
                list.add(rs.getString("name"));
                list.add(rs.getString("race"));
                list.add(rs.getString("level"));
                list.add(rs.getString("gold"));
                list.add(rs.getString("gender"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<String> getZones() {
        ArrayList<String> list = new ArrayList();
        try {
            connect("arc_world");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT zoneId FROM weather");
            while(rs.next()) {
                list.add(rs.getString("zoneId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
