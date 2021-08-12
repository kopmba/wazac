/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import wazac.entity.User;

/**
 *
 * @author mardets
 */
public class Util {
    
    private static final Map<Object, Object> mapper = new HashMap<Object, Object>();
    
    private static final Pattern p = Pattern.compile("[A-Z]+\\.[0-9]");
    
    public static void main(String[] args) throws NoSuchFieldException {
        
        
        
        String str = " hehe ";
        String str1 = "bangladesh";
        String test = "[{gender=Male,phone=63828998,id=1,lastname=Doe,userId=username,birthday=03/10/1980,firstname=John}]";
        String strTest = test.replace(String.valueOf(' '), String.valueOf(""));
        System.out.println(stringEval(str));
        System.out.println(stringEval(str1));
        
        String other = "[{name:Category1,id:0,description:qsdqsdqsjkdjkqsdqsdsqj},{name:Category2,id:0,description:qsdsqkdjkqsjkdjkqs},{name:zuieryze,description:qsdgfqsjdqsdqs,id:3}]";
        
        Map map = new HashMap<Object, Object>();
        Set set1 = new HashSet<Map<Object, Object>>();
        Set set2 = new HashSet<Map<Object, Object>>();
        Set set3 = new HashSet<Map<Object, Object>>();
        Set set4 = new HashSet<Map<Object, Object>>();
        
        String imageUrl = "url:file:/images/url.png";
        System.err.println(stringEvalUrl(imageUrl));
                
        String fullname = "{id:1,username:Doe,password:John,email:doe@wazac.com}";
        
        String user = "{id:1,username:Doe,password:John,email:doe@wazac.com,role:{name:admin,desc:can updated all contents}}";
        
        String users = "[{id:1,username:Doe,password:John,email:doe@wazac.com,roles:[{name:admin,desc:control},{name:user,desc:navigating}]},{id:2,username:Johnson,password:Ben,email:johnson@wazac.com,roles:[{name:user,desc:navigating}]}]";
        
        String userWithRoles = "{id:1,username:Doe,password:John,email:doe@wazac.com,roles:[{name:admin,desc:control},{name:user,desc:navigating}]}";
        String data = "[{name:qsdqszerze,id:1,description:azeazeazeazezaeaz},{name:jkldjklfjkldsjklfjklsdfsdjklf,id:1,description:ouioazuioeuioazeuioazeazioejqsdjqsdjklqsdqsjlmdqslmwxcwxcklkldklqs}]";
        System.out.println("fullname map : " + jsonMapper(fullname, set1));
        System.out.println("user map : " + jsonMapper(user, set2));
        System.out.println("usersµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµ : " + jsonMapper(other, set3));
        System.out.println("users with roles : " + jsonMapper(userWithRoles, set4));
        
        User u = new User();
        System.out.println("fullobject user : " + mapToEntity(u,set1));
        //System.out.println("user object : " + mapToEntity(u,set2));
        //System.out.println("users : " + mapToEntity(u,set3));
        System.out.println("users with roles : " + mapToEntity(u,set4));
        
        String eval = "[{ name : qsdqszerze , id : 1 , description : azeazeazeazezaeaz }, { name : jkldjklfjkldsjklfjklsdfsdjklf , id : 1 , description : ouioazuioeuioazeuioazeazioejqsdjqsdjklqsdqsjlmdqslmwxcwxcklkldklqs }]";
        jsonEvalChar(eval);
        System.out.println(jsonEvalChar(eval));
        
    }
    
    public static String param(String id) {
        return id.substring(id.indexOf('-') + 1);
    }
    
    public static String stringEvalUrl(String uri) {
        String url = uri.replace('-', '_');
        int len = url.length();
        String str = "";
        String format = "";
        String eval = "";
        for(int i = len-1; i > 0; i--) {
            if(url.charAt(i) == '.' ) {
                format = url.substring(i, len);
                for(int j=i-1; j > 0; j--) {
                    if(url.charAt(j) != '/') {
                       str += url.charAt(j);
                    }else {
                        break;
                    }
                }
                StringBuilder builder = new StringBuilder(str);
                eval = builder.reverse().toString();
                eval = eval + format;
                break;
            }
        }
        return "images/" + eval;
    }
    
    public static String jsonEvalChar(String json) {
        String jsonResult = "";
        String strValue = "";
        int len = json.length();
        
        for(int i=0; i < len; i++) {
            if(json.charAt(i) == ' ') {
                continue;
            } else {
                jsonResult += json.charAt(i);
            }
        }
        return jsonResult;
    }
    
    public static String stringEval(String value) {
        if(value.charAt(0) == ' ' && value.charAt(value.length()-1) == ' ') {
            return value.substring(1, value.length()-1);
        } else {
            return value;
        }
    }
    
    public static String stringMapEval(String key, Map<Object, Object> map) {
        if(map.get(key.toString()) != null) {
            if(key.toString().charAt(0) == ' ' && key.toString().charAt(key.toString().length()-1) == ' ') {
                return stringEval(map.get(key).toString());
            } 
            return map.get(key).toString();
        } else if(map.size() == 1) {
            Map value = new HashMap<Object, Object>();
            for (Entry<Object, Object> entry : map.entrySet()) {
                value = (Map) entry.getValue();
            }
            return value.get(Util.stringEval(key)).toString();
        } else {
            String keyStr = " " + key.toString() + " ";
            if(map.get(keyStr) != null) {
               return stringEval(map.get(keyStr).toString());
            }
            return "";
        }
        
    }
    
    public static Set<Object> mapToSet(Map<Object, Object> map) {
        Set set = new HashSet();
        
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Map obj = new HashMap<>();
            Object key = entry.getKey();
            Object value = entry.getValue();
            obj.put(key, value);
            set.add(obj);
        }
        return set;
    }
    
    public static Object mapToEntity(Object clazz, Set<Map<Object, Object>> set) throws NoSuchFieldException {
        
        Map<Object, Object> entity = new HashMap<>();
        
        String key = null, value = null;
        
        for(Object field : clazz.getClass().getDeclaredFields()) {
            for (Map<Object, Object> map : set) {
                for(Map.Entry mapSet : map.entrySet()) {
                    
                    key = stringEval(mapSet.getKey().toString());
                    value = stringEval(mapSet.getValue().toString());
                    
                    if(field.equals(clazz.getClass().getDeclaredField(key))) {
                        if(mapSet.getValue() instanceof String) {
                            entity.put(key, value);
                        } else {
                            entity.put(key, mapSet.getValue());
                        }
                        
                        break;
                    } 
                }
                
            }
            
        }
        
        return entity;
    }
    
    public static Set<Map<Object, Object>> jsonMapper(String json, Set<Map<Object, Object>> set) {
       int len = json.length();
       String str = "";
       if(json.charAt(0) == '[' && json.charAt(len-1) == ']') {
           Map<Object, Object> omap = new HashMap<Object, Object>();
           Map<Object, Object> fmap = jsonObjectMapper(json, omap, set);
           if(set.size() > 0) {
               return set;
           } else {
               Map<Object, Object> mmap = jsonArrayMapper(json, new HashMap<>());
               if(mmap.size() > 0) {
                  for (Entry<Object, Object> entry : mmap.entrySet()) {
                    Map<Object, Object> mEntry = new HashMap<>();
                    mEntry.put(entry.getKey(), entry.getValue());
                    set.add(mEntry);
                  } 
               }
               return set;
           }
       }
       
       if(json.charAt(0) == '{' && json.charAt(len-1) == '}') {
           Map<Object, Object> omap = new HashMap<Object, Object>();
           Map<Object, Object> fmap = jsonObjectMapper(json, omap, set);
           if(set.size() == 0) {
               set.add(fmap);
               return set;
           }
       }
       return set;
    }
    
    public static void iterateMapper(int index, String json, String str, Map<Object, Object> map) {
        int len = json.length();
        for(int i=index; i < len; i++) {
            
            if(json.charAt(i) == '{') {
                if(str.length() > 0) {
                    return;
                }
                for(int j = i; j < len; j++) {
                    
                    if(json.charAt(j) == '}') {
                        str += json.substring(i, j+1);
                        break;
                    }
                }
                Map<Object, Object> omap = new HashMap<Object, Object>();
                Map<Object, Object> newMap = jsonObjectMapper(str, omap, null);
                map.put(map.size(), newMap);
                if(str.length()+2 < len) {
                    jsonArrayMapper(json.substring(str.length()+2, len), map);
                } else {
                    jsonArrayMapper(String.valueOf(']'), map);
                }
                
            }

        }
    }
    
    public static Map<Object, Object> jsonArrayMapper(String json, Map<Object, Object> map) {
        
        int len = json.length();
        String str = ""; 
        
        if(json.charAt(0) == '[') {
            iterateMapper(1, json, str, map);
        }
        
        if(json.charAt(0) == '{') {
            iterateMapper(0, json, str, map);
        }
        
        if((json.charAt(0) != '{' && json.charAt(0) != ']') || (json.charAt(0) != '{' && json.charAt(0) != '[')) {
            
            if(json.charAt(0) != '[' && json.charAt(0) != ']') {
                
                if(!json.contains("{]")) {
                    json = ("{").concat(json);
                    iterateMapper(0, json, str, map);
                }
                
            }
            
        }
        
        if(json.charAt(0) == ']') {
            iterateMapper(0, json, str, map);
            //return map;
        }
        
        
        return map;
    }
    
    public static Map<Object, Object> jsonObjectMapper(String json, Map<Object, Object> map, Object list) {
        int vlen = 0, dplen = 0;
        int flen = 0;
        int len = json.length();
        Integer alen = null;
        String key = "", strValue = "",  str = ""; 
        Object value = null;
        Map<Object, Object> imap = null;
        Set<Object> mapSet = new HashSet<>();
        
        for(int i = 0; i < len; i++) {
            if(json.charAt(i) == '{') {
                alen = i;
                continue;
            }
            
            if(p.matcher(String.valueOf(json.charAt(i))).find()) {
                continue;
            }
            
            if(json.charAt(i) == '}' && i == len - 1) {
                if(mapper.get(key) != strValue) {
                    if(key != "" && strValue != "") {
                        map.put(key, strValue);
                    }
                    
                }
                
            }
            
            if(json.charAt(i) == ']' && i == len - 1) {
                if(mapper.get(key) != strValue) {
                   if(key != "" && strValue != "") {
                        map.put(key, strValue);
                   }
                }
            }
            
            if(json.charAt(i) == ':') {
                dplen = i;
                if(alen != null) {
                    key += json.substring(alen + 1, dplen);
                } else {
                    key += json.substring(0, dplen);
                }
                
                System.out.println("property : "+key);
                
                if(json.charAt(dplen + 1) != '{') {
                    if(json.charAt(dplen + 1) != '[') {
                       for(int j = dplen + 1; j < len - 1; j++) {                       
                            if(str.length() > 0) {

                                return map;
                            } 
                            if(json.charAt(j) != ',') {

                                strValue += json.charAt(j);
                                i = j;
                            } else {
                                System.out.println("value : "+strValue);
                                vlen = j;
                                alen = null;
                                str += json.substring(vlen + 1, len);
                                System.out.println("updated string to evaluate : "+str);
                                if(mapper.get(key) != strValue) {
                                    map.put(key, strValue);
                                }
                                System.out.println(map.keySet());
                                System.out.println(json + " : formatted object result \n");
                                jsonObjectMapper(str, map, list);
                            }

                        } 
                    }
                    
                }
                
                if(json.charAt(dplen + 1) == '{') {
                    for(int j = dplen + 1; j < json.length(); j++) {
                        
                        if(str.length() > 0) {
                            
                            return map;
                        } 
                        
                        if(json.charAt(j) == '}') {
                            strValue += json.substring(dplen + 1, j+1);
                            str += json.substring(j+1, json.length());
                            System.out.println("updated string to evaluate : "+str);
                            Map<Object, Object> emap = new HashMap<Object, Object>();
                            Map<Object, Object> smap = jsonObjectMapper(strValue, emap, null);
                            if(mapper.get(key) != strValue) {
                                map.put(key, smap);
                            }
                            System.out.println(map.keySet());
                            System.out.println(json + " : formatted object result \n");
                            jsonObjectMapper(str, map, list);
                        }
                    }
                    
                }
                
                if(json.charAt(dplen + 1) == '[') {
                   for(int j = dplen + 1; j < json.length(); j++) {
                        if(str.length() > 0) {
                            if(list != null) {
                                map.put(map.size(), list);
                            }
                            return map;
                        } 
                        if(json.charAt(j) == ']') {
                            strValue += json.substring(dplen + 1, j+1);
                            str += json.substring(j+1, json.length());
                            
                            System.out.println("updated string to evaluate : "+str);
                            Map<Object, Object> emap = new HashMap<Object, Object>();
                            Map<Object, Object> smap = jsonArrayMapper(strValue, emap);
                            
                            if(mapper.get(key) != smap) {
                                map.put(key, smap);
                            }
                            if(map.containsValue(smap)) {
                                Map<Object, Object> amap = new HashMap<>(map);
                                if(list instanceof Set) {
                                    map.clear();
                                    ((Set) list).add(amap);
                                }
                            }
                            System.out.println(map.keySet());
                            System.out.println(json + " : formatted object result \n");
                            jsonObjectMapper(str, map, list);
                        } 
                    } 
                }
            }            
        }
        
        return map;
    }
}
