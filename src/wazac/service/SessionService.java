/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import wazac.entity.User;
import wazac.util.FileRW;
import wazac.util.JsonObject;
import wazac.util.JsonObject.JsonBuilder;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class SessionService {

    private static final Logger LOG = Logger.getLogger(SessionService.class.getName());
    
    public static Map<Object, Object> getSession() throws IOException {
        String jsonText = FileRW.read("collections/session.txt");
        
        Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());
        Map map = new HashMap();
        for (Object object : (Set) set) {
            
            if(object instanceof Map) {
                Object[] val = ((Map) object).values().toArray();
                map.put("userId", val[0]);
            }
        }
        return map;
    }
    
    public static void save(String id) throws IOException, FileNotFoundException, URISyntaxException {
        
        JsonBuilder sessionBuilder = new JsonObject.JsonBuilder();
        sessionBuilder.add("userId", id);
        JsonObject sessionData = new JsonObject(sessionBuilder);
        //FileRW.remove("collections/session.txt");
        FileRW.write("collections/session.txt", sessionData.build().toString());
        
    }
    
    public static Map<Object, Object> getCurrentId() throws IOException {
        String jsonText = FileRW.read("collections/info.txt");
        
        Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());
        Map map = new HashMap();
        for (Object object : (Set) set) {
            
            if(object instanceof Map) {
                Object[] val = ((Map) object).values().toArray();
                map.put("currentId", val[0]);
            }
        }
        return map;
    }
    
    public static Map<Object, Object> getParam() throws IOException {
        String jsonText = FileRW.read("collections/param.txt");
        
        Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());
        Map map = new HashMap();
        for (Object object : (Set) set) {
            
            if(object instanceof Map) {
                Object[] val = ((Map) object).values().toArray();
                map.put("param", val[0]);
            }
        }
        return map;
    }
    
    public static void saveCurrentId(String id) throws IOException, FileNotFoundException, URISyntaxException {
        
        JsonObject.JsonBuilder sessionBuilder = new JsonObject.JsonBuilder();
        sessionBuilder.add("currentId", id);
        JsonObject sessionData = new JsonObject(sessionBuilder);
        //FileRW.remove("collections/session.txt");
        FileRW.write("collections/info.txt", sessionData.build().toString());
        
    }
    
    public static void saveParam(String id) throws IOException, FileNotFoundException, URISyntaxException {
        
        JsonObject.JsonBuilder sessionBuilder = new JsonObject.JsonBuilder();
        sessionBuilder.add("param", id);
        JsonObject sessionData = new JsonObject(sessionBuilder);
        //FileRW.remove("collections/session.txt");
        FileRW.write("collections/param.txt", sessionData.build().toString());
        
    }
}
