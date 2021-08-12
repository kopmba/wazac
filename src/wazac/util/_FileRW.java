package wazac.util;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import wazac.Wazac;
import wazac.entity.Image;

public class _FileRW {

    private static final Logger LOG = Logger.getLogger(_FileRW.class.getName());
    
    public static void main(String[] args) {
        
        absolutePath("/home/mardets/Images/euizruie.png");
        
        FXMLLoader loader = new FXMLLoader();
        ClassLoader cLoader = ClassLoader.getSystemClassLoader();
        URL url = cLoader.getResource("/home/mardets/Images/pject.png");
        Path source = Paths.get("/home/mardets/Images/pject.png");
        StringBuffer buffer = new StringBuffer(url.getPath());
        int index = buffer.indexOf("dist/wazac");
        String str = buffer.substring(0, index) + "src/wazac/";
        
        createFile("collections/session.txt");
    }
    
    public static File createTmpFile(String filename) {
        String dir = fileDirectory("images");
        
        File file = null;
        FileOutputStream fos = null;
        try {
            file = new File(Paths.get(new URI(dir + filename)).toUri().getPath());
            if(file == null) {
                createFileW(filename);
            }
            Files.createFile(Paths.get(new URI(dir + filename)));
        } catch (FileAlreadyExistsException e) {
            System.err.format("file named %s" + "already exists %n", file);
        }
        catch (Exception e) {
                LOG.log(Level.SEVERE, null, e);
        }
        return file;
    } 
    
   public static File createFile(String filename) {
        
        URL url = Wazac.class.getResource("collections/history.txt");
        StringBuffer buffer = new StringBuffer(url.getPath());
        //String path = buffer.substring(0, buffer.indexOf("wazac/dist")) + "wazac/src/wazac/";
        String path = buffer.substring(0, buffer.indexOf("wazac/app")) + "wazac/";
        File file = null;
        FileOutputStream fos = null;
        try {
            file = new File(Paths.get(new URI(path + filename)).toUri().getPath());
            if(file == null) {
                createFileW(filename);
            }
            Files.createFile(Paths.get(new URI(path + filename)));
        } catch (FileAlreadyExistsException e) {
            System.err.format("file named %s" + "already exists %n", file);
        }
        catch (Exception e) {
                LOG.log(Level.SEVERE, null, e);
        }
        return file;
    }
   
    public static File createFileW(String filename) {
       
        URL url = Wazac.class.getResource("collections\\history.txt");
        StringBuffer buffer = new StringBuffer(url.getPath());
        //String path = buffer.substring(0, buffer.indexOf("wazac\\dist")) + "\\src\\wazac\\";
        String path = buffer.substring(0, buffer.indexOf("wazac\\app")) + "wazac\\";
        File file = null;
        FileOutputStream fos = null;
        try {
            file = new File(Paths.get(new URI(path + filename)).toUri().getPath());
            Files.createFile(Paths.get(new URI(path + filename)));
        } catch (FileAlreadyExistsException e) {
            System.err.format("file named %s" + "already exists %n", file);
        }
        catch (Exception e) {
                LOG.log(Level.SEVERE, null, e);
        }
        return file;
    }
    
    public static void write(String filename, String data) throws FileNotFoundException, IOException, URISyntaxException {        
        
        URL url = Wazac.class.getResource("collections/history.txt");
        StringBuffer buffer = new StringBuffer(url.getPath());
        //String indexer = "wazac/dist";
	String indexer = "wazac/app";
	//Comment because of install
        /*if(buffer.indexOf("wazac/dist") == -1) {
            indexer = "wazac/build";
        }*/
        //String path = buffer.substring(0, buffer.indexOf(indexer)) + "wazac/src/wazac/";
	String path = buffer.substring(0, buffer.indexOf(indexer)) + "wazac/";
        String uri = "file:".concat(path + filename);
	//Comment because of install
        /*if(indexer.equals("wazac/dist")) {
            uri = path.concat(filename);
        }*/
        if(indexer.equals("wazac/app")) {
            uri = path.concat(filename);
        }
        
        Files.deleteIfExists(Paths.get(new URI(uri)));
        
        try (OutputStream out = new BufferedOutputStream(
           Files.newOutputStream(Paths.get(new URI(uri)), CREATE, APPEND))) {
            out.write(data.getBytes(), 0, data.getBytes().length);
        } catch(IOException e) {
            System.err.println(e);
        }
        
        
    }
    
    public static void writeNone(String filename) throws IOException, URISyntaxException {
        URL url = Wazac.class.getResource("collections/history.txt");
        StringBuffer buffer = new StringBuffer(url.getPath());
        //String indexer = "wazac/dist";
	String indexer = "wazac/app";
        /*if(buffer.indexOf("wazac/dist") == -1) {
            indexer = "wazac/build";
        }*/
        //String path = buffer.substring(0, buffer.indexOf(indexer)) + "wazac/src/wazac/";
	String path = buffer.substring(0, buffer.indexOf(indexer)) + "wazac/";
        String uri = "file:".concat(path + filename);
        //Comment because of install
        /*if(indexer.equals("wazac/dist")) {
            uri = path.concat(filename);
        }*/
        if(indexer.equals("wazac/app")) {
            uri = path.concat(filename);
        }
        Files.delete(Paths.get(new URI(uri)));
        try (OutputStream out = new BufferedOutputStream(
           Files.newOutputStream(Paths.get(new URI(uri)), CREATE, APPEND))) {
            out.write("".getBytes(), 0, "".getBytes().length);
        } catch(IOException e) {
            System.err.println(e);
        } catch (URISyntaxException ex) {
            Logger.getLogger(_FileRW.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void WriteImage(String filename, byte[] data) throws URISyntaxException, IOException {
        
        URL url = Wazac.class.getResource("images/avatar.png");
        StringBuffer buffer = new StringBuffer(url.getPath());
        //String indexer = "wazac/dist";
	String indexer = "wazac/app";
        /*if(buffer.indexOf("wazac/dist") == -1) {
            indexer = "wazac/build";
        }*/
        //String path = buffer.substring(0, buffer.indexOf(indexer)) + "wazac/src/wazac/";
	String path = buffer.substring(0, buffer.indexOf(indexer)) + "wazac/";
        String uri = "file:".concat(path + filename);
        //Comment because of install
        /*if(indexer.equals("wazac/dist")) {
            uri = path.concat(filename);
        }*/
        if(indexer.equals("wazac/app")) {
            uri = path.concat(filename);
        }
        
        
        Files.deleteIfExists(Paths.get(new URI(uri)));
        
        try (OutputStream out = new BufferedOutputStream(
           Files.newOutputStream(Paths.get(new URI(uri)), CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch(IOException e) {
            System.err.println(e);
        }
    }
    
    public static String readContent(String filename) throws URISyntaxException, IOException {
        URL url = Wazac.class.getResource("collections/history.txt");
        StringBuffer buffer = new StringBuffer(url.getPath());
        //String indexer = "wazac/dist";
	String indexer = "wazac/app";
        /*if(buffer.indexOf("wazac/dist") == -1) {
            indexer = "wazac/build";
        }*/
        //String path = buffer.substring(0, buffer.indexOf(indexer)) + "wazac/src/wazac/";
	String path = buffer.substring(0, buffer.indexOf(indexer)) + "wazac/";
        
        String uri = "file:".concat(path + filename);
        //Comment because of install
        /*if(indexer.equals("wazac/dist")) {
            uri = path.concat(filename);
        }*/
        if(indexer.equals("wazac/app")) {
            uri = path.concat(filename);
        }
        
        List<String> listStr = Files.readAllLines(Paths.get(new URI(uri)));
        String content = "";
        for (String string : listStr) {
            content += string;
        }
        return content;
    }

    public static String read(String file) throws IOException {
        //File file = new File(path);
        //FileInputStream fis = new FileInputStream(file);
        FXMLLoader loader = new FXMLLoader();
        InputStream fis = Wazac.class.getResourceAsStream(file);
        
       if(fis == null) {
           return "";
       }

        //Use the buffered reader
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
        } in.close();
        System.out.println(response.toString());
        return response.toString();

    }
    
    public static void remove(String filename) throws URISyntaxException, IOException {
        URL url = Wazac.class.getResource("collections/history.txt");
        StringBuffer buffer = new StringBuffer(url.getPath());
        //String path = buffer.substring(0, buffer.indexOf("wazac/dist")) + "wazac/src/wazac/";
        String path = buffer.substring(0, buffer.indexOf("wazac/app")) + "wazac/";
        Files.deleteIfExists(Paths.get(new URI(path + filename)));
    }
    
    public static String fileDirectory(String dir) {
        URL url = Wazac.class.getResource(dir + "/history.txt");
        StringBuffer buffer = new StringBuffer(url.getPath());
        //String path = buffer.substring(0, buffer.indexOf("wazac/dist")) + "wazac/src/wazac/";
        String path = buffer.substring(0, buffer.indexOf("wazac/app")) + "wazac/";
        return path;
    }
    
    public static String absolutePath(String filename) {
        URL url = Wazac.class.getResource(Util.stringEvalUrl(filename));
        StringBuffer buffer = new StringBuffer(url.getPath());
        String path = buffer.substring(0, buffer.indexOf("wazac/dist")) + "wazac/src/wazac/" + Util.stringEvalUrl(filename);
        return path;
    }
    
    
    
}
