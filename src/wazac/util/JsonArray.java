package wazac.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import wazac.entity.Category;
import wazac.service.CategoryService;
import wazac.util.JsonObject.JsonBuilder;

public class JsonArray {
		
	private JsonArrayBuilder builder;
	private Set<Object> object;
	
	public JsonArray() {
	}
	
	public JsonArray(JsonArrayBuilder builder) {
		this.builder = builder;
		object = builder.getObject();
		
	}
	
	public Object build() {
		return builder.createBuilder();
	}
	
	public Set<Object> getObject() {
		return object;
	}
	
	
	public static class JsonArrayBuilder {
		Set<Object> object;
		
		
		public JsonArrayBuilder() {
			object = new HashSet<Object>();
		}
		
		public Set<Object> getObject() {
			return object;
		}
		
		public void setObject(Set<Object> object) {
			this.object = object;
		}
		
		public JsonArrayBuilder add(JsonObject json) {
			object.add(json.build());
			return this;
		}
		
		public Object createBuilder() {
                    JsonArrayBuilder builder = new JsonArrayBuilder();
                    for (Object obj : object) {
                        if(obj instanceof Set) {
                            for(Object o : (Set)obj) {
                                if(o instanceof Map) {
                                    JsonBuilder jsonBuilder = new JsonObject.JsonBuilder();
                                    jsonBuilder.setJsonMapBuilder((Map<Object, Object>) o);
                                    builder.add(new JsonObject(jsonBuilder));
                                } else {
                                    if(o instanceof File) {
                                        JsonObject.JsonBuilder imBuilder = new JsonObject.JsonBuilder();
                                        imBuilder.add("url", ((File) o).getPath()).createBuilder();
                                        builder.add(new JsonObject(imBuilder));
                                    }
                                    
                                }
                            }
                            
                        } else {
                            builder.getObject().add(obj);
                        }
                    }
                    System.out.println(builder.getObject().toString());
                    return builder.getObject().toString();
		}
                
                
		
	}
	
}