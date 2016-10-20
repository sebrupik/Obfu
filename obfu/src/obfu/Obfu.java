package obfu;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Obfu {
    HashMap<String, HashMap> swapHM;
    Pattern[] patterns;
    Matcher matcher;
    int[] typeCount;
    
    private final static String _ip6np = "IPv6_network_prifix";
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileInputStream input = new FileInputStream(args[1]);
        FileChannel channel = input.getChannel();
        
        ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int)channel.size());
        CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
        
        if(args.length!=2) {
            System.out.println("Unexpected arguments. Require <domain> <input_file>");
            System.exit(0);
        } else {
            new Obfu(cbuf, args[0], args[1]);
        }
    }
    
    public Obfu(CharBuffer buff, String domain, String filename) {
        String match, swapStr;
        typeCount = new int[PatternBuilder.type.length];
        swapHM =  new HashMap();
        HashMap<String, Item> typeHM;
        patterns= new PatternBuilder(domain).create();
        
        swapHM.put(_ip6np, new HashMap<>());
    
        for(int i=0; i<patterns.length; i++) {
            matcher = patterns[i].matcher(buff);
            
            while (matcher.find()) {
                match = matcher.group();
                
                typeHM = (swapHM.containsKey(PatternBuilder.type[i])) ? swapHM.get(PatternBuilder.type[i]) : new HashMap();
                
                if(!typeHM.containsKey(match)) {
                    swapStr = swap(match, i);
                    typeHM.put(match, new Item(new String[]{match, swapStr}, PatternBuilder.type[i]));
                    
                    try {
                        buff = Charset.forName("8859_1").newDecoder().decode( ByteBuffer.wrap(((Pattern.compile(match).matcher(buff)).replaceAll(swapStr)).getBytes()) );
                    } catch(CharacterCodingException cce) { System.out.println("Obfu/Obfu(main) - "+cce); }
                } 
                
                swapHM.put(PatternBuilder.type[i], typeHM);
            }  
        }
        
        try {
            outputResult(buff, swapHM, filename+"-modified");
        } catch(FileNotFoundException fnfe) {
            System.out.println("FNFE "+fnfe);
        } catch(IOException ioe) {
            System.out.println("IOE "+ioe);
        }
    }
    
    private String swap(String ori, int type) {
        typeCount[type]++;
        String replaceStr = "";
        
        switch (type) {
            case 0:    //Layer2-MAC
                replaceStr = SwapEngine.swapMacAddress(ori, typeCount[type], 4);
                break;
            case 1:    //Layer3-IPv4
                replaceStr = SwapEngine.swapIPv4Address(ori, typeCount[type], 2);
                break;
            case 2:    //Layer3-IPv6_ll
                replaceStr = SwapEngine.swapIPv6LLAddress(ori, typeCount[type], 2);
                break;
            case 3:    //Layer3-IPv6_gua
                replaceStr = SwapEngine.swapIPv6GUAddress(ori, typeCount[type], 2, swapHM.get(_ip6np));
                break;
            default:
                break;
        }
        
        return replaceStr;
    }
    
    private String displayFinds(HashMap h) {
        String output="";
        
        HashMap<String, Item> tHM;
        Iterator it = h.entrySet().iterator();
        Iterator it2;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            tHM = (HashMap)pair.getValue();
            it2 = tHM.entrySet().iterator();
            output += String.format(pair.getKey()+"-- %n");
            while(it2.hasNext()) {
                Map.Entry pair2 = (Map.Entry)it2.next();
                
                output += String.format(pair2.getKey() + " :: " + ((Item)pair2.getValue()).swap[1]+"%n");
            }
        }
        return output;
    }
    
    private void outputResult(CharBuffer cBuff, HashMap h, String filename) throws FileNotFoundException, IOException {
        try(PrintWriter out = new PrintWriter(filename)) {
            out.println( cBuff.toString() );
        }
        try(PrintWriter out = new PrintWriter(filename+"-swaps")) {
            out.println( this.displayFinds(h) );
        }
    }
    
}