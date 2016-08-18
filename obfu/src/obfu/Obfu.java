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
    HashMap<String, HashMap> swapHM;
    Pattern[] patterns;
    Matcher matcher;
    int[] typeCount;
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileInputStream input = new FileInputStream(args[0]);
        FileChannel channel = input.getChannel();
        
        ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int)channel.size());
        CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
        
        new Obfu(cbuf, args[0]);
    }
    
    public Obfu(CharBuffer buff, String filename) {
        String match, swapStr;
        typeCount = new int[PatternBuilder.type.length];
        swapHM =  new HashMap();
        HashMap<String, Item> typeHM;
        patterns= new PatternBuilder("blah.com").create();
    
        for(int i=0; i<patterns.length; i++) {
            matcher = patterns[i].matcher(buff);
            
            while (matcher.find()) {
                match = matcher.group();
                
                typeHM = (swapHM.containsKey(PatternBuilder.type[i])) ? swapHM.get(PatternBuilder.type[i]) : new HashMap();
                
                if(!typeHM.containsKey(match)) {
                    swapStr = swap(match, i);
                    typeHM.put(match, new Item(new String[]{match, swapStr}, PatternBuilder.type[i]));
                    
                    //Matcher m = Pattern.compile(match).matcher(buff);
                    //m.replaceAll(swapStr);
                    (Pattern.compile(match).matcher(buff)).replaceAll(swapStr);                    
                } 
                
                swapHM.put(PatternBuilder.type[i], typeHM);
            }  
        }
        System.out.println(this.displayFinds(swapHM));
        
        System.out.println(buff.toString());
        try {
            outputResult(buff, filename+"-modified");
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
                break;
            case 3:    //Layer3-IPv6_gua
                break;
            default:
                break;
        }
        
        return replaceStr;
    }
    
    private String displayFinds(HashMap h) {
        //System.out.println("swap hash size "+h.size());
        String output="";
        
        HashMap<String, Item> tHM;
        Iterator it = h.entrySet().iterator();
        Iterator it2;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            tHM = (HashMap)pair.getValue();
            it2 = tHM.entrySet().iterator();
            //System.out.println(pair.getKey()+"--");
            output += String.format(pair.getKey()+"-- %n");
            while(it2.hasNext()) {
                Map.Entry pair2 = (Map.Entry)it2.next();
                
                //System.out.println(pair2.getKey() + " :: " + ((Item)pair2.getValue()).swap[1]);
                output += String.format(pair2.getKey() + " :: " + ((Item)pair2.getValue()).swap[1]+"%n");
            }
        }
        return output;
    }
    
    private void outputResult(CharBuffer cBuff, String filename) throws FileNotFoundException, IOException {
        FileChannel fc = new FileOutputStream(filename).getChannel();
        ByteBuffer bBuff = ByteBuffer.allocate(2400000);
        
        bBuff.asCharBuffer().put(cBuff);
        fc.write(bBuff);
        fc.close();
    }
    
}