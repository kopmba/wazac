/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wazac.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import wazac.util._FileRW;
import wazac.util.JsonArray;
import wazac.util.JsonObject;
import wazac.util.Util;

/**
 *
 * @author mardets
 */
public class HistoryService {
    public static Map<Object, Object> getSession() throws IOException {
        String jsonText = _FileRW.read("collections/session.txt");
        Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());
        for (Object object : (Set) set) {
            
            return (Map<Object, Object>) object;
        }
        return null;
    }
    
    public static void save(String log, Date date) throws IOException, FileNotFoundException, URISyntaxException {
        
        //String timeValue = String.valueOf(date.getHours()) + "h:" + String.valueOf(date.getMinutes()) + "m:" + String.valueOf(date.getSeconds()) + "s";
        JsonObject.JsonBuilder hBuilder = new JsonObject.JsonBuilder();
        
        hBuilder.add("log", log)
                      .add("date", date.toString());
        JsonObject historyData = new JsonObject(hBuilder);
        
        String jsonText = _FileRW.read("collections/history.txt");
        if(!jsonText.equals("")) {
            Object set = Util.jsonMapper(jsonText.replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());

            Object newSet = Util.jsonMapper(historyData.build().toString().replace(String.valueOf('"'), String.valueOf(' ')), new HashSet<Map<Object,Object>>());
            JsonArray.JsonArrayBuilder jsonArrayBuilder = new JsonArray.JsonArrayBuilder();
            
            jsonArrayBuilder.getObject().add(set);
            jsonArrayBuilder.getObject().add(newSet);
            JsonArray jsonArray = new JsonArray(jsonArrayBuilder);
            _FileRW.write("collections/history.txt", historyData.build().toString());
        } else {
            _FileRW.write("collections/history.txt", historyData.build().toString());
        }
        
        
    }
    
    
}
