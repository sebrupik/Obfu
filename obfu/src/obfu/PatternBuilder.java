package obfu;

import java.util.regex.Pattern;

public class PatternBuilder {
    String domain;
    
    String[] patterns = new String[]{"([a-fA-F0-9]{2}):([a-fA-F0-9]{2}):([a-fA-F0-9]{2}):([a-fA-F0-9]{2}):([a-fA-F0-9]{2}):([a-fA-F0-9]{2})",
                                     "([0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3})",
                                     "fe80::\\w+:\\w+:\\w+:\\w+",
                                     "2001(?::\\w+|:){1,7}"
    };
    public static String[] type = new String[]{"Layer2-MAC",
                                 "Layer3-IPv4",
                                 "Layer3-IPv6_ll",
                                 "Layer3-IPv6_gua"
    };
    
    public PatternBuilder(String domain) {
        this.domain = domain;
    }
    
    public Pattern[] create() {
        Pattern[] p = new Pattern[patterns.length];
        
        for (int i=0; i<p.length;i++) {
            p[i] = Pattern.compile(patterns[i]);
        }
        
        return p;
    }
}