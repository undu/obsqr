package trikita.obsqr.model;

import android.content.Context;
import android.text.Spannable;

import trikita.obsqr.R;

import static trikita.obsqr.Utils.spannable;

/** Web URL */
public class WebUrlContent extends QrContent {
    public final static String MATCH = android.util.Patterns.WEB_URL.pattern();
    public WebUrlContent(Context c, String s) {
        super(s, c.getString(R.string.title_url), c.getString(R.string.action_url), generateContent(s));
    }

    private static Spannable generateContent(String s) {
        if (!s.startsWith("http:") && !s.startsWith("https:") && !s.startsWith("ftp:")) {
            s = "http://" + s;
        }
        return spannable(s);
    }
}
