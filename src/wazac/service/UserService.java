/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import wazac.entity.Article;
import wazac.entity.User;
import static wazac.service.ActivityService.save;
import wazac.util._FileRW;
import wazac.util.JsonArray;
import wazac.util.JsonArray.JsonArrayBuilder;
import wazac.util.JsonObject;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class UserService {

    private static final Logger LOG = Logger.getLogger(UserService.class.getName());

    public static List<User> find () throws IOException, NoSuchFieldException {
        String jsonText = _FileRW.read("collections/users.txt");
        if(!jsonText.equals("")) {
            Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());
        
            //Map<Object, Object> obj = (Map<Object, Object>) Util.mapToEntity(new User(), (Set<Map<Object, Object>>) set);
        
            List<User> users = new ArrayList<User>();
        
        
            for (Object obj : (Set)set) {
                if(obj instanceof Map) {
                    User u = new User();
                    u.setId(Util.stringMapEval("id", (Map<Object, Object>) obj));
                    u.setEmail(Util.stringMapEval("email", (Map<Object, Object>) obj));
                    u.setUsername(Util.stringMapEval("username", (Map<Object, Object>) obj));
                    u.setPassword(Util.stringMapEval("password", (Map<Object, Object>) obj));
                    users.add(u);
                }
            }

            return users;
        }
        return null;
    }
    
    public static Set<User> filter (String predicat) throws IOException, NoSuchFieldException {
        String jsonText = _FileRW.read("collections/users.txt");
        
        Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());
        
        Set<User> users = new HashSet<User>();
        
        for(User u : find()) {
            if(u.getEmail().equals(predicat) || u.getId().equals(predicat)
               || u.getUsername().equals(predicat) || u.getPassword().equals(predicat)) {
                users.add(u);
            }
        }
        
        return users;
    }
    
    public static User findOne(String id) throws IOException, NoSuchFieldException {
        if(find() != null) {
            for(User u : find()) {
                return u.getId().equals(Util.stringEval(id)) ? u : null;
            }
        }
        
        return null;
    }
    
    public static User findByUsername(String username) throws IOException, NoSuchFieldException {
        if(find() != null) {
            for(User u : find()) {
                return u.getUsername().equals(Util.stringEval(username)) ? u : null;
            }
        }
        
        return null;
    }
    
    public static User save(User user) throws IOException, FileNotFoundException, URISyntaxException {
        
        JsonObject.JsonBuilder builder = new JsonObject.JsonBuilder();
        builder.add("id", user.getId())
               .add("username", user.getUsername())
               .add("email", user.getEmail())
               .add("password", user.getPassword())
               .createBuilder();
        
        JsonObject data = new JsonObject(builder);
        
        String jsonText = _FileRW.read("collections/users.txt");
        _FileRW.write("collections/users.txt", data.build().toString());
        
        
        return user;
    }
    
    public static User update(User user, String id) throws IOException, NoSuchFieldException, FileNotFoundException, URISyntaxException {
        User u = findOne(id);
        if(u == null && id != "") {
            return save(user);
        }
        if (u == null) {
            throw new Error("No user Exist");
        } else {
            u.setId(Util.stringEval(id));
            u.setUsername(user.getUsername());
            u.setEmail(user.getEmail());
            u.setPassword(user.getPassword());
;        }
        return save(u);
    }
    
    public static void delete(String id) throws IOException, NoSuchFieldException, FileNotFoundException, URISyntaxException {
        User u = findOne(id);
        if(u == null) {
            throw new Error("No user Exist");
        } else {
            find().remove(u);
            
            JsonArrayBuilder jsonArrayBuilder = new JsonArray.JsonArrayBuilder();
            jsonArrayBuilder.setObject(listToSet(find()));
            JsonArray jsonArray = new JsonArray(jsonArrayBuilder);

            _FileRW.write("collections/users.txt", jsonArray.build().toString());
        }
    }
    
    public static Set<Object> listToSet(List<User> data) {
        Set<Object> newSet = new HashSet<Object>();
        for(User u : data) {
            Map<Object, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("username", u.getUsername());
            map.put("email", u.getEmail());
            map.put("password", u.getPassword());
            newSet.add(map);
        }
        return newSet;
    }
    
    
}
