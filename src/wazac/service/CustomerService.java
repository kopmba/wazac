/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac.service;

import database.config.WewazDbConnection;
import database.config.WewazDbState;
import database.factory.DaoFactory;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.wewaz.auth.bean.builder.Entity;
import main.wewaz.auth.bean.builder.Entity.EntityBuilder;
import wazac.dto.Customer;

/**
 *
 * @author Pro3010
 */
public class CustomerService {
    
    
    public static Customer findOne(String username) {
        Customer cu = null;
        try {
            String[] params = {"jdbc:mariadb://localhost:3306/samplebooking", "root", ""};
            String query = "select * from customer where username = ?";
            DaoFactory<Entity<Customer>> daoFactory = new DaoFactory<>();
            WewazDbConnection wewazDbConnection = new WewazDbConnection();
            wewazDbConnection.createConnection(params[0], params[1], params[2]);
            Connection c = wewazDbConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(query);
            EntityBuilder<Customer> ud = new Entity.EntityBuilder<>(new Customer());
            Entity<Customer> udr = new Entity<Customer>(ud);
            WewazDbState dbState = udr.getBuilder(null);
            Object result = daoFactory.requestFindOneFactory(query, query, dbState, c, ps);
            udr.setBuilder((EntityBuilder<Customer>) result);
            cu = udr.getModel();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cu;
    }
   
    public static void create(Entity<Customer> entity) {
        try {
            String[] params = {"jdbc:mariadb://localhost:3306/samplebooking", "root", ""};
            String query = "insert into customer(username, fullname, email, gender, phone, birthday, activity,"
                    + " shopname, address, city, country, data) values(?,?,?,?,?,?,?,?,?,?,?,?)";
            DaoFactory<Entity<Customer>> daoFactory = new DaoFactory<>();
            WewazDbConnection wewazDbConnection = new WewazDbConnection();
            wewazDbConnection.createConnection(params[0], params[1], params[2]);
            Connection c = wewazDbConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(query);
            WewazDbState dbState = (WewazDbState) entity.getBuilder(ps);
            daoFactory.requestCreateFactory(query, dbState, c, ps);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void update(Entity<Customer> entity, String userId) {
        try {
            String[] params = {"jdbc:mariadb://localhost:3306/samplebooking", "root", ""};
            String query = "update customer set username=?, fullname=?, email=?, gender=?, phone=?, birthday=?, activity=?,"
                    + " shopname=?, address=?, city=?, country=?, data=? where username=?";
            DaoFactory<Entity<Customer>> daoFactory = new DaoFactory<>();
            WewazDbConnection wewazDbConnection = new WewazDbConnection();
            wewazDbConnection.createConnection(params[0], params[1], params[2]);
            Connection c = wewazDbConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(query);
            EntityBuilder<Customer> ud = new Entity.EntityBuilder<>(new Customer());
            Entity<Customer> udr = new Entity<Customer>(ud);
            WewazDbState dbState = (WewazDbState) udr.getBuilder(null);
            daoFactory.requestUpdateFactory(query, query, dbState, c, ps);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
