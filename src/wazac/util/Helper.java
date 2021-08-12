package main.wewaz.auth.helper;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;

public class Helper {
	
	public static void serialize(Object data, String filePath) throws FileNotFoundException {
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            XMLEncoder encode = new XMLEncoder(fos);
            encode.writeObject(data);
            encode.close();
	}
	
	public static Object deserialize(byte[] content) {
            InputStream is = new ByteArrayInputStream(content);
            XMLDecoder decode = new XMLDecoder(is);
            Object data = decode.readObject();
            return data;
	}
	
	public static Object deserialize(URI uri) throws FileNotFoundException {
            InputStream is = null;
		if(uri != null) {
			is = new FileInputStream(new File(Paths.get(uri).toUri().getPath()));
		} else {
			is = new FileInputStream(new File("storage"));
		}
		
            XMLDecoder decode = new XMLDecoder(is);
            Object data = decode.readObject();
            return data;
	}
	
}
