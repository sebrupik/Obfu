package obfu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SwapEngine {
    HashMap<String, HashMap> swap;
    HashMap<String, Item> tmpHM;
    int[] count;
    
    public SwapEngine(HashMap<String, HashMap> swap) {
        this.swap = swap;
        this.count = new int[PatternBuilder.type.length];
        
    }
    
    public void swap() {
        Iterator it = swap.entrySet().iterator();
    
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            tmpHM = (HashMap)pair.getValue();
            
            if(pair.getKey().equals(PatternBuilder.type[0])) { //Layer2-MAC
                Item[] items = (Item[])tmpHM.values().toArray();
                
                for(int i=0; i<items.length; i++) {
                    
                }
                
                count[0]++;
            } else if(pair.getKey().equals(PatternBuilder.type[1])) { //Layer3-IPv4
                
            } else if(pair.getKey().equals(PatternBuilder.type[2])) { //Layer3-IPv6_ll
                
            } else if(pair.getKey().equals(PatternBuilder.type[3])) { //Layer3-IPv6_gua
                
            }
        }
    }
    
    public void writeToFile() {
        
    }
    
    public static String swapMacAddress(String macOri, int count, int replace) {
        String macNew = macCompress(macOri);
        
        String cStr = String.valueOf(count);
        String prefix ="";
        
        for(int i=0; i < (replace-cStr.length()); i++)
            prefix = prefix.concat("X");
        
        prefix = prefix.concat(cStr);
        
        macNew = prefix+(macNew.substring(prefix.length(), macNew.length()));
        
        return macNew;
    }
    
    private static String removeElements(String oriStr, String ele) {
        String newStr ="";
        
        String[] sArr = oriStr.split(ele);
        for (String s : sArr) 
            newStr = newStr.concat(s);
        
        return newStr;
    }
    
    private static String ipv6FullLength(String oriIPv6) {
        StringBuilder sb = new StringBuilder(oriIPv6.trim());
        int index = oriIPv6.indexOf("::");
        if(index!=-1) {
            int octets = (oriIPv6.split(":").length)-1;
            String insert="";
            for(int i=octets;i<8; i++) {
                insert+="0000";
                if(i<=6)
                    insert+=":";
            }
            sb.insert(index+1, insert);
        }
	
        String[] elements = sb.toString().split(":");
        String output ="";
        for(int i=0;i<elements.length; i++) {
            while(elements[i].length()!=4)
                elements[i] = "0"+elements[i];
	    if(i==0)
		output = elements[i];
	    else
               output = output+":"+elements[i];
        }
        return output;
    }
    
    private static String macCompress(String oriMAC) {
        String output="";
        String[] elements = null;
        if(oriMAC.contains(".")) {  // IOS
            elements = oriMAC.split("\\.");
        } else if(oriMAC.contains(":")) {  //linux
             elements = oriMAC.split(":");
        }
        if(elements!=null) {
            for(String element : elements) 
                output+=element;
        }

	if (output.length()!=12) {
	    System.out.println("something has gone wrong with the parse on "+oriMAC+" ("+output+"), returning null");
            output=null;
	}
        return output;
    }
}
