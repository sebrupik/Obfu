package obfu;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Obfu {
    HashMap<String, ArrayList> swap;
    Pattern[] patterns;
    Matcher matcher;
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileInputStream input = new FileInputStream(args[0]);
        FileChannel channel = input.getChannel();
        
        ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int)channel.size());
        CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
        
        new Obfu(cbuf);
    }
    
    public Obfu(CharBuffer buff) {
        String match;
        ArrayList arr;
        swap =  new HashMap();
        patterns= new PatternBuilder("blah.com").create();
    
        for(int i=0; i<patterns.length; i++) {
            matcher = patterns[i].matcher(buff);
            
            while (matcher.find()) {
                match = matcher.group();
                
                arr = (swap.containsKey(PatternBuilder.type[i])) ? swap.get(PatternBuilder.type[i]) : new ArrayList();
                
                arr.add(new Item(new String[]{match, ""}, PatternBuilder.type[i]));
                swap.put(PatternBuilder.type[i], arr);
            }
            
            this.displayFinds(swap);
        }

    }
    
    private void displayFinds(HashMap h) {
        ArrayList<Item> a;
        Iterator it = h.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            a = (ArrayList)pair.getValue();
            for (Item a1 : a) {
                System.out.println(pair.getKey() + " :: " + a1.swap[0]);
            }
        }
    }
    
}