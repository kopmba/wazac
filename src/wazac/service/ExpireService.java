/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import wazac.util._FileRW;
import wazac.util.JsonObject;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class ExpireService {
    
    public static Map<Object, Object> getExpire() throws IOException {
        String jsonText = _FileRW.read("collections/expire");
        Map map = new HashMap();
        if(jsonText.equals("")) {
            map.put("expireId", jsonText);
            return map;
        }
        Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());
        
        for (Object object : (Set) set) {
            
            if(object instanceof Map) {
                Object[] val = ((Map) object).values().toArray();
                map.put("expireId", val[0]);
            }
        }
        return map;
    }
    
    public static void save(String value) throws IOException, FileNotFoundException, URISyntaxException {
        
        JsonObject.JsonBuilder sessionBuilder = new JsonObject.JsonBuilder();
        sessionBuilder.add("expireId", value);
        JsonObject sessionData = new JsonObject(sessionBuilder);
        //FileRW.remove("collections/session.txt");
        _FileRW.write("collections/expire", sessionData.build().toString());
        
    }
}
