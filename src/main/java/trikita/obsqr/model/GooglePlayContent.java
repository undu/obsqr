package trikita.obsqr.model;

import android.content.Context;
import android.text.Spannable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import trikita.obsqr.R;

import static trikita.obsqr.Utils.spannable;

/** Google Play URL */
class GooglePlayContent extends QrContent {
    public final static String MATCH = "market://(details\\?id=)?(.*)";

    public GooglePlayContent(Context c, String s) {
        super(s, c.getString(R.string.title_market), c.getString(R.string.action_market), generateContent(s));
    }

    private static Spannable generateContent(String s) {
        Matcher m = Pattern.compile(MATCH).matcher(s);
        if (m.matches() && m.group(1) != null) {
            return spannable(m.group(2));
        } else {
            return spannable(s);
        }
    }
}
