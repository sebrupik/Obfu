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
    HashMap<String, HashMap> swap;
    Pattern[] patterns;
    Matcher matcher;
    int[] typeCount;
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileInputStream input = new FileInputStream(args[0]);
        FileChannel channel = input.getChannel();
        
        ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int)channel.size());
        CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
        
        new Obfu(cbuf);
    }
    
    public Obfu(CharBuffer buff) {
        String match;
        typeCount = new int[PatternBuilder.type.length];
        swap =  new HashMap();
        HashMap<String, Item> typeHM;
        patterns= new PatternBuilder("blah.com").create();
    
        for(int i=0; i<patterns.length; i++) {
            matcher = patterns[i].matcher(buff);
            
            while (matcher.find()) {
                match = matcher.group();
                
                typeHM = (swap.containsKey(PatternBuilder.type[i])) ? swap.get(PatternBuilder.type[i]) : new HashMap();
                
                if(!typeHM.containsKey(match)) {
                    typeHM.put(match, new Item(new String[]{match, swap(match, i)}, PatternBuilder.type[i]));
                } 
                
                swap.put(PatternBuilder.type[i], typeHM);
            }  
        }
        this.displayFinds(swap);
    }
    
    private String swap(String ori, int type) {
        typeCount[type]++;
        String replaceStr = "";
        
        switch (type) {
            case 0:    //Layer2-MAC
                replaceStr = SwapEngine.swapMacAddress(ori, typeCount[type], 4);
                break;
            case 1:    //Layer3-IPv4
                break;
            case 2:    //Layer3-IPv6_ll
                break;
            case 3:    //Layer3-IPv6_gua
                break;
            default:
                break;
        }
        
        return replaceStr;
    }
    
    private void displayFinds(HashMap h) {
        //System.out.println("swap hash size "+h.size());
        HashMap<String, Item> tHM;
        Iterator it = h.entrySet().iterator();
        Iterator it2;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            tHM = (HashMap)pair.getValue();
            it2 = tHM.entrySet().iterator();
            System.out.println(pair.getKey()+"--");
            while(it2.hasNext()) {
                Map.Entry pair2 = (Map.Entry)it2.next();
                
                System.out.println(pair2.getKey() + " :: " + ((Item)pair2.getValue()).swap[1]);
            }
        }
    }
    
}