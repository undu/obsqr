package trikita.obsqr.model;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;

import java.util.Map;

import trikita.obsqr.R;

import static trikita.obsqr.Utils.spannable;
import static trikita.obsqr.Utils.tokenize;

/** WiFi access point */
public class WifiContent extends QrContent {
    public final static String MATCH = "wifi:(.*)";
    private final static String[] FIELDS = new String[]{"T", "S", "P"};
    private final static int[] FIELD_NAMES = new int[]{
            R.string.wifi_qr_security_title,
            R.string.wifi_qr_ssid_title,
            R.string.wifi_qr_password_title,
    };

    WifiContent(Context c, String s) {
        super(s, c.getString(R.string.title_wifi), c.getString(R.string.action_wifi), generateContent(c, s));
    }

    @Override
    public Intent getActionIntent(Context context) {
        return new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
    }

    private static Spannable generateContent(Context context, String s) {
        StringBuilder res = new StringBuilder();
        Map<String, String> tokens = tokenize(s.substring(5), FIELDS);
        for (int i = 0; i < FIELDS.length; i++) {
            if (tokens.get(FIELDS[i]) != null) {
                res.append(context.getString(FIELD_NAMES[i])).append(": ")
                        .append(tokens.get(FIELDS[i])).append('\n');
            }
        }
        return spannable(res.toString());
    }
}
