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
    
    /*public void swap() {
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
    }*/
    
    public void writeToFile() {
        
    }
    
    /**
     * Strip the formating (:, -) from the the supplied MAC address. Replace the number of characters
     * specified by the value of 'replace' with an X. Reformat the MAC address and return.
     * 
     * 
     * @param macOri   The original MAC address
     * @param count    A unique value to be used in the new obfuscated address
     * @param replace    The number of leading characters in the MAC address to obfuscate
     * @return 
     */
    public static String swapMacAddress(String macOri, int count, int replace) {
        System.out.println("SwapEngine/swapMacAddress - "+macOri+", "+count+", "+replace);
        String macNew = macCompress(macOri);
        String prefix = createPrefix(count, replace);
        
        macNew = prefix+(macNew.substring(prefix.length(), macNew.length()));
        macNew = addElements(macNew, ":", 2, 3, 15);
        
        //System.out.println("macNew "+macNew);
        return macNew.toUpperCase();
    }
    
    /**
     * 
     * 
     * @param addressOri
     * @param count
     * @param replace         number of octets to obfuscate
     * @return 
     */
    public static String swapIPv4Address(String addressOri, int count, int replace) {
        System.out.println("SwapEngine/swapIPv4Address - "+addressOri+", "+count+", "+replace);
        String[] sArr = addressOri.split("\\.");
        String prefix = createPrefix(count, replace*3);
        
        prefix = addElements(prefix, ".", 3, 4, 8);
        //System.out.println("prefix is :"+prefix);
        
        String addressNew = "";
        for(int i=replace; i<sArr.length; i++) {
            addressNew += sArr[i];
            if(i != sArr.length-1)
                addressNew += ".";
        }
        //System.out.println("addressNew is :"+addressNew);
        
        return prefix.concat(addressNew);
    }
    
    public static String swapIPv6LLAddress(String addressOri, int count, int replace) {
        System.out.println("SwapEngine/swapIPv6LLAddress - "+addressOri+", "+count+", "+replace);
        
        
        return addressOri+count;
    }
    
    public static String swapIPv6GUAddress(String addressOri, int count, int replace, HashMap ip6np) {
        System.out.println("SwapEngine/swapIPv6GUAddress - "+addressOri+", "+count+", "+replace);
        
        
        return addressOri+count;
    }
    
    private static String createPrefix(int count, int replace) {
        String cStr = String.valueOf(count);
        String prefix ="";
        
        for(int i=0; i < (replace-cStr.length()); i++)
            prefix = prefix.concat("X");
        
        return prefix.concat(cStr);
    }
    
    private static String removeElements(String oriStr, String ele) {
        String newStr ="";
        
        String[] sArr = oriStr.split(ele);
        for (String s : sArr) 
            newStr = newStr.concat(s);
        
        return newStr;
    }
    
    private static String addElements(String input, String element, int start, int space, int max) {
        for(int i=start;i<max;i+=space) 
            input = input.substring(0,i)+element+input.substring(i, input.length());
        
        return input;
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