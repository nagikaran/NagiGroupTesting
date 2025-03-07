package com.NagiGroup.utility;

import java.sql.Connection;
import java.sql.DriverManager;

public class AzurePostgresTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://nagi-group.postgres.database.azure.com:5432/postgres?sslmode=require&ssl=true";
        String user = "postgres";
        String password = "Nagikaran@1010";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to Azure PostgreSQL successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

