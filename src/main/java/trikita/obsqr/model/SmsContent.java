package trikita.obsqr.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;

import trikita.obsqr.R;

import static trikita.obsqr.Utils.spannable;

/** SMS */
class SmsContent extends QrContent {
    public final static String MATCH = "smsto:(.*)";
    public SmsContent(Context c, String s) {
        super(s, c.getString(R.string.title_sms), c.getString(R.string.action_sms), generateContent(c, s));
    }

    @Override
    public Intent getActionIntent(Context context) {
        String[] s = rawContent.split(":");
        String uri= s[0] + ":" + s[1];
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
        intent.putExtra("compose_mode", true);
        if (s.length > 2) {
            intent.putExtra("sms_body", s[2]);
        }
        return intent;
    }

    private static Spannable generateContent(Context c, String raw) {
        StringBuilder res = new StringBuilder();
        String[] s = raw.split(":");
        res.append(c.getString(R.string.sms_qr_phone_title))
                .append(": ").append(s[1]);
        if (s.length > 2) {
            res.append("\n")
                    .append(c.getString(R.string.sms_qr_message_title))
                    .append(": ").append(s[2]);
        }
        return spannable(res.toString());
    }
}
