package trikita.obsqr.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import trikita.obsqr.R;

import static trikita.obsqr.Utils.spannable;

/** Phone number */
class PhoneNumberContent extends QrContent {
    public final static String MATCH = "tel:(.*)";

    public PhoneNumberContent(Context c, String s) {
        super(s, c.getString(R.string.title_phone), c.getString(R.string.action_phone), spannable(s.substring(4)));
    }

    public Intent getActionIntent(Context context) {
        return new Intent(Intent.ACTION_DIAL, Uri.parse(rawContent));
    }
}
