package com.example.assignment.util;

import org.springframework.stereotype.Component;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
@Component
public class FactoryConfig {
    private static FactoryConfig INSTANCE;

    private FactoryConfig() {

    }

    public static FactoryConfig getInstance() {
        return INSTANCE == null ? INSTANCE = new FactoryConfig() : INSTANCE;
    }

    public Connection getConnection() {
        try {
            InitialContext ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/emp");
            return pool.getConnection();
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e.getCause().getMessage(), e);
        }
    }

}
