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
import wazac.entity.Activity;
import wazac.entity.Profile;
import wazac.entity.User;
import static wazac.service.ProfileService.save;
import wazac.util._FileRW;
import wazac.util.JsonArray;
import wazac.util.JsonObject;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class ActivityService {

    private static final Logger LOG = Logger.getLogger(ActivityService.class.getName());
    
    public static List<Activity> find () throws IOException {
        String jsonText = _FileRW.read("collections/activities.txt");
        
        if(!jsonText.equals("")) {
            Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());

            List<Activity> activities = new ArrayList<Activity>();

            for(Object obj : (Set)set) {
                if(obj instanceof Map) {
                    Activity a = new Activity();
                    a.setId(Util.stringMapEval("id", (Map<Object, Object>) obj));
                    a.setCity(Util.stringMapEval("city", (Map<Object, Object>) obj));
                    a.setCountry(Util.stringMapEval("country", (Map<Object, Object>) obj));
                    a.setLocation(Util.stringMapEval("location", (Map<Object, Object>) obj));
                    a.setShopname(Util.stringMapEval("shopname", (Map<Object, Object>) obj));
                    a.setTitle(Util.stringMapEval("title", (Map<Object, Object>) obj));
                    a.setUserId(Util.stringMapEval("userId", (Map<Object, Object>) obj));
                    activities.add(a);
                }
            }

            return activities;
        }
        return null;
    }
    
    public static Activity findByUserId(String userId) throws IOException, NoSuchFieldException {
        if(find() != null) {
            for(Activity a : find()) {
                return a.getUserId().equals(Util.stringEval(userId)) ? a : null;
            }
        }
        return null;
    }
    
    public static Set<Activity> filter (String predicat) throws IOException {
        String jsonText = _FileRW.read("collections/activities.txt");
        if(!jsonText.equals("")) {
            Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());

            Set<Activity> activities = new HashSet<Activity>();

            for(Activity a : find()) {
                if(a.getCity().equals(predicat) || a.getCountry().equals(predicat)
                   || a.getId().equals(predicat) || a.getLocation().equals(predicat)
                   || a.getUserId().equals(predicat) || a.getShopname().equals(predicat)
                   || a.getTitle().equals(predicat)) {
                    activities.add(a);
                }
            }

            return activities;
        }
        return null;
    }
    
    public static Activity findOne(String id) throws IOException, NoSuchFieldException {
        if(find() != null) {
            for(Activity a : find()) {
                return a.getId().equals(id) ? a : null;
            }
        }
        return null;
    }
    
    public static Activity save(Activity activity) throws IOException, FileNotFoundException, URISyntaxException {
        
        JsonObject.JsonBuilder builder = new JsonObject.JsonBuilder();
        builder.add("id", activity.getId())
               .add("city", activity.getCity())
               .add("shopname", activity.getShopname())
               .add("title", activity.getTitle())
               .add("userId", activity.getUserId())
               .add("location", activity.getLocation())
               .add("country", activity.getCountry())
               .createBuilder();
        
        JsonObject data = new JsonObject(builder);
        
        String jsonText = _FileRW.read("collections/activities.txt");
        
        _FileRW.write("collections/activities.txt", data.build().toString());
        
        return activity;
    }
    
    public static Activity update(Activity activity, String id) throws IOException, NoSuchFieldException, FileNotFoundException, URISyntaxException {
        Activity a = findByUserId(id);
        if(a == null && id != "") {
            return save(activity);
        }
        if (a == null) {
            throw new Error("No user Exist");
        } else {
            a.setId(Util.stringEval(id));
            a.setCity(activity.getCity());
            a.setCountry(activity.getCountry());
            a.setId(activity.getId());
            a.setLocation(activity.getLocation());
            a.setShopname(activity.getShopname());
            a.setTitle(activity.getTitle());
            a.setUserId(activity.getUserId());
        }
        return save(a);
    }
    
    public static void delete(String id) throws IOException, NoSuchFieldException, FileNotFoundException, URISyntaxException {
        Activity a = findOne(id);
        if(a == null) {
            throw new Error("No user Exist");
        } else {
            find().remove(a);
            
            JsonArray.JsonArrayBuilder jsonArrayBuilder = new JsonArray.JsonArrayBuilder();
            jsonArrayBuilder.setObject(listToSet(find()));
            JsonArray jsonArray = new JsonArray(jsonArrayBuilder);

            _FileRW.write("collections/activities.txt", jsonArray.build().toString());
        }
    }
    
    public static Set<Object> listToSet(List<Activity> data) {
        Set<Object> newSet = new HashSet<Object>();
        for(Activity a : data) {
            Map<Object, Object> map = new HashMap<>();
            map.put("id", a.getId());
            map.put("city", a.getCity());
            map.put("country", a.getCountry());
            map.put("location", a.getLocation());
            map.put("shopname", a.getShopname());
            map.put("title", a.getTitle());
            map.put("userId", a.getUserId());
            newSet.add(map);
        }
        return newSet;
    }
    
}
