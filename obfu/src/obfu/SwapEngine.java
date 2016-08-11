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
    
    public static String swapMacAddress(String macOri, int count) {
        String macNew ="";
        
        
        return macNew;
    }
}
