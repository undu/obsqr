package trikita.obsqr;

import android.text.Spannable;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static Map<String, String> tokenize(String s, String... keys) {
        Map<String, String> tokens = new HashMap<>();

        int len = s.length();
        StringBuilder builder = new StringBuilder();
        boolean escaped = false;

        // treat a char sequence between two non-escaped semicolons
        // as a single token
        for (int i = 0; i < len; i++) {
            if (escaped) {
                builder.append(s.charAt(i));
                escaped = false;
            } else {
                if (s.charAt(i) == ';') {
                    String token = builder.toString();
                    for (String key : keys) {
                        if (token.startsWith(key+":")) {
                            tokens.put(key, token.substring(key.length()+1));
                        }
                    }
                    builder = new StringBuilder();
                } else if (s.charAt(i) == '\\') {
                    escaped = true;
                } else {
                    builder.append(s.charAt(i));
                }
            }
        }
        return tokens;
    }

    public static Spannable spannable(String s) {
        return Spannable.Factory.getInstance().newSpannable(s);
    }
}
