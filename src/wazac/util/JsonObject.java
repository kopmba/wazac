package wazac.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class JsonObject {
	
	private Map<Object, Object> jsonMap;
	private JsonBuilder builder;
	
	public JsonObject() {
	}
	
	public JsonObject(JsonBuilder builder) {
		this.builder = builder;
		jsonMap = builder.getJsonMapBuilder();
		
	}
	
	public Object build() {
		return builder.createBuilder();
	}
	
	public static class JsonBuilder {
		Map<Object, Object> jsonMapBuilder;
		
		
		public JsonBuilder() {
			jsonMapBuilder = new HashMap<Object, Object>();
		}
		
		public Map<Object, Object> getJsonMapBuilder() {
			return jsonMapBuilder;
		}
		
		public void setJsonMapBuilder(Map<Object, Object> jsonMapBuilder) {
			this.jsonMapBuilder = jsonMapBuilder;
		}
		
		public JsonBuilder add(Object key, Object data) {
			jsonMapBuilder.put(key.toString(), data);
			return this;
		}
		
		public Object jsonParser(Map<Object, Object> object) {
			StringJoiner sj1 = new StringJoiner(":", "{", "}");
			StringJoiner sj2 = new StringJoiner(",", "{", "}");
			System.out.println(jsonMapBuilder.keySet());
			Set<Object> keys = jsonMapBuilder.keySet();
			int i = 0;
			for (Object key : keys) {
				
				if(jsonMapBuilder.containsKey(key) && i < keys.size()) {
					Object data = jsonMapBuilder.get(key);
					sj2.merge(sj1.add("'" + key + "'").add("'" + data + "'"));
				} else {
					
				}
				i++;
				sj1 = new StringJoiner(":", "{", "}");
			}
			return sj2;
		}
		
		public Object createBuilder() {
			
			Object jsonParse = jsonParser(getJsonMapBuilder());
			
			String desiredString = jsonParse.toString();
			StringBuffer bCharOld = new StringBuffer().append("'");
			StringBuffer bCharNew = new StringBuffer().append('"');
			char old = bCharOld.charAt(0);
			char newC = bCharNew.charAt(0);
			String str = "";
			 
			System.out.println(desiredString);
			StringBuffer buffer = new StringBuffer(desiredString);
			 
			for(int j=0; j < buffer.length(); j++) {
				 
				if(String.valueOf(buffer.charAt(j)).contains(String.valueOf("'"))) {
					str = str + String.valueOf(newC);
				}
				
				if(!(String.valueOf(buffer.charAt(j)).contains(String.valueOf("'")) 
						 || String.valueOf(buffer.charAt(j)).contains(String.valueOf(' ')))) {
					str = str + String.valueOf(buffer.charAt(j)); 
				}
			}
			System.out.println(str);
			return str;
		}
		
		
	}
	
}