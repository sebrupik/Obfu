package obfu;

import java.util.HashMap;

public class SwapEngine {
    HashMap<String, HashMap> swap;
    HashMap<String, Item> tmpHM;
    int[] count;
    
    public SwapEngine(HashMap<String, HashMap> swap) {
        this.swap = swap;
        this.count = new int[PatternBuilder.type.length];
        
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
        
        String addressNew = "";
        for(int i=replace; i<sArr.length; i++) {
            addressNew += sArr[i];
            if(i != sArr.length-1)
                addressNew += ".";
        }
        
        return prefix.concat(addressNew);
    }
    
    /*public static String swapIPv6LLAddress(String addressOri, int count, int replace) {
        System.out.println("SwapEngine/swapIPv6LLAddress - "+addressOri+", "+count+", "+replace);
        
        String addressExt = ipv6FullLength(addressOri);
        String[] addressParts = new String[]{addressExt.substring(0,20), addressExt.substring(21,addressExt.length())};
                
        return ipv6FullLength(addressParts[0]+intToHexPrefix(null, false, count));
    }*/
    
    public static String swapIPv6Address(String addressOri, int count, int replace, HashMap ip6np) {
        System.out.println("SwapEngine/swapIPv6GUAddress - "+addressOri+", "+count+", "+replace);
        String output;
        String addressExt = ipv6FullLength(addressOri);
        String[] addressParts = new String[]{addressExt.substring(0,20), addressExt.substring(21,addressExt.length())};
        
        if(ip6np != null) {
            if(!ip6np.containsKey(addressParts[0])) {
                ip6np.put(addressParts[0], new Item(new String[]{addressParts[0], intToHexPrefix(addressParts[0].substring(0,addressParts[0].indexOf(":")), true, ip6np.size()+1)}, Obfu._ip6np));
            }
            output = ipv6FullLength(((Item)ip6np.get(addressParts[0])).swap[1]+":"+intToHexPrefix(null, false, count));
        } else {
            output = ipv6FullLength(addressParts[0]+intToHexPrefix(null, false, count));
        }
        
        return output;
    }
    
    /**
     * Take an int, convert to hex, incrementing the address group to the left each time 
     * the value of FFFF is reached, then returning to 0000 to begin incrementing again.
     * 
     * @param prefix  The network prefix from which will be reused in the returned value
     * @param count
     * @return 
     */
    private static String intToHexPrefix(String prefixStr, boolean prefix, int count) {
        String[] output;
        String returnStr = "";
        String binaryStr = Integer.toBinaryString(count);
       
        if(prefix) {
            output = new String[]{"0","0","0"};
            binaryStr = String.format("%48s", binaryStr).replace(' ', '0');
        } else {
            output = new String[]{"0","0","0","0"};
            binaryStr = String.format("%64s", binaryStr).replace(' ', '0');
        }
        
        //Convert each byte into hex
        for(int i=0; i<output.length; i++) {
            output[i] = Integer.toString(Integer.parseInt(binaryStr.substring(0+(16*i),16+(16*i)), 2), 16);
        }
        
        if(prefix)
            returnStr = prefixStr+":"+output[0]+":"+output[1]+":"+output[2];
        else
            returnStr = output[0]+":"+output[1]+":"+output[2]+":"+output[3];
        
        return returnStr;
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
        } else if(oriMAC.contains("-")) {  //linux
             elements = oriMAC.split("-");
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