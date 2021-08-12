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
import java.util.Set;
import java.util.logging.Logger;
import wazac.entity.Profile;
import wazac.entity.User;
import wazac.util.FileRW;
import wazac.util.JsonArray;
import wazac.util.JsonObject;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class ProfileService {

    private static final Logger LOG = Logger.getLogger(ProfileService.class.getName());
    
    public static List<Profile> find () throws IOException, URISyntaxException {
        String jsonText = FileRW.readContent("collections/profiles.txt");
        if(!jsonText.equals("")) {
            Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());
        
            List<Profile> profiles = new ArrayList<Profile>();

            for(Object obj : (Set)set) {
                if(obj instanceof Map) {
                    Profile p = new Profile();
                    p.setId((Util.stringMapEval("id", (Map<Object, Object>) obj)));
                    p.setFirstname(Util.stringMapEval("firstname", (Map<Object, Object>) obj));
                    p.setLastname(Util.stringMapEval("lastname", (Map<Object, Object>) obj));
                    p.setBirthday(Util.stringMapEval("birthday", (Map<Object, Object>) obj));
                    p.setGender(Util.stringMapEval("gender", (Map<Object, Object>) obj));
                    p.setPhone(Util.stringMapEval("phone", (Map<Object, Object>) obj));
                   // p.setJob((Util.stringMapEval("job", (Map<Object, Object>) obj)));
                    p.setUserId((Util.stringMapEval("userId", (Map<Object, Object>) obj)));
                    profiles.add(p);
                }
            }
        
            return profiles;
        } 
        return null; 
        
    }
    
    public static Profile findByUserId(String userId) throws IOException, NoSuchFieldException, URISyntaxException {
        if(find() != null) {
            for(Profile p : find()) {
                return p.getUserId().equals(Util.stringEval(userId)) ? p : null;
            }
        }
        return null;
    }
    
    public static Set<Profile> filter (String predicat) throws IOException, URISyntaxException {
        String jsonText = FileRW.read("collections/profiles.txt");
        Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());
        
        Set<Profile> profiles = new HashSet<Profile>();
        
        for(Profile p : find()) {
            if(p.getBirthday().equals(Util.stringEval(predicat)) || p.getGender().equals(Util.stringEval(predicat))
               || p.getId().equals(Util.stringEval(predicat)) || p.getPhone().equals(predicat)
               || p.getFirstname().equals(predicat) || p.getLastname().equals(predicat)) {
                profiles.add(p);
            }
        }
        
        return profiles;
    }
    
    public static Profile findOne(String id) throws IOException, NoSuchFieldException, URISyntaxException {
        if(find() != null) {
           for(Profile p : find()) {
                return p.getId().equals(id) ? p : null;
           } 
        }
        
        return null;
    }
    
    public static Profile save(Profile profile) throws IOException, FileNotFoundException, URISyntaxException {
        
        JsonObject.JsonBuilder builder = new JsonObject.JsonBuilder();
        builder.add("id", profile.getId())
               .add("firstname", profile.getFirstname())
               .add("lastname", profile.getLastname())
               .add("birthday", profile.getBirthday())
               .add("gender", profile.getGender())
               .add("phone", profile.getPhone())
               .add("userId", profile.getUserId())
               .createBuilder();
        
        JsonObject data = new JsonObject(builder);
        
        String jsonText = FileRW.readContent("collections/profiles.txt");
        
        FileRW.write("collections/profiles.txt", data.build().toString());
        
        return profile;
    }
    
    public static Profile update(Profile profile, String id) throws IOException, NoSuchFieldException, FileNotFoundException, URISyntaxException {
        Profile p = findByUserId(id);
        if(p == null && id != "") {
            return save(profile);
        }
        
        if (p == null) {
            throw new Error("No user Exist");
        } else {
            p.setFirstname(profile.getFirstname());
            p.setLastname(profile.getLastname());
            p.setBirthday(profile.getBirthday());
            p.setGender(profile.getGender());
            p.setJob(profile.getJob());
            p.setPhone(profile.getPhone());
            p.setUserId(profile.getUserId());
        }
        return save(p);
    }
    
    public static void delete(String id) throws IOException, NoSuchFieldException, URISyntaxException {
        Profile p = findOne(id);
        if(p == null) {
            throw new Error("No user Exist");
        } else {
            find().remove(p);
            
            JsonArray.JsonArrayBuilder jsonArrayBuilder = new JsonArray.JsonArrayBuilder();
            jsonArrayBuilder.setObject(listToSet(find()));
            JsonArray jsonArray = new JsonArray(jsonArrayBuilder);

//            FileRW.write("collections/profiles.txt", jsonArray.build().toString());
        }
    }
    
    public static Set<Object> listToSet(List<Profile> data) {
        Set<Object> newSet = new HashSet<Object>();
        for(Profile p : data) {
            Map<Object, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("birthday", p.getBirthday());
            map.put("gender", p.getGender());
            map.put("job", p.getJob());
            map.put("phone", p.getPhone());
            map.put("userId", p.getUserId());
            newSet.add(map);
        }
        return newSet;
    }
}
