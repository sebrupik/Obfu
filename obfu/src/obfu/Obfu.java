package obfu;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class Obfu {
    BufferedReader buffer;
    HashMap<String, ArrayList> swap;
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file = new File(args[0]);
        //new Obfu(new BufferedReader(new FileReader(file)));
        
        FileInputStream input = new FileInputStream(args[0]);
        FileChannel channel = input.getChannel();
        
        ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int)channel.size());
        CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
    }
    
    public Obfu(CharBuffer buff) {
        String line;
    
        swap = new HashMap();
         
        while ((line = buffer.readLine()) != null) {
            
        }

    }
    
}
