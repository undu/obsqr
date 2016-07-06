package trikita.obsqr.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;

public abstract class QrContent {
    public final String rawContent;

    public final String title;
    public final String action;
    public final Spannable content;

    QrContent(String s, String title, String action, Spannable content) {
        this.rawContent = s;
        this.action = action;
        this.title = title;
        this.content = content;
    }

	public Intent getActionIntent(Context context) {
		return new Intent(Intent.ACTION_VIEW, Uri.parse(rawContent));
	}

	public static QrContent from(Context c, String s) {
		String m = s.toLowerCase();
		if (m.matches(GooglePlayContent.MATCH)) {
			return new GooglePlayContent(c, s);
		} else if (m.matches(WebUrlContent.MATCH)) {
			return new WebUrlContent(c, s);
		} else if (m.matches(EmailContent.MATCH)) {
			return new EmailContent(c, s);
		} else if (m.matches(PhoneNumberContent.MATCH)) {
			return new PhoneNumberContent(c, s);
		} else if (m.matches(SmsContent.MATCH)) {
			return new SmsContent(c, s);
		} else if (m.matches(ContactContent.MATCH)) {
			return new ContactContent(c, s);
		} else if (m.matches(GeoLocationContent.MATCH)) {
			return new GeoLocationContent(c, s);
		} else if (m.matches(WifiContent.MATCH)) {
			return new WifiContent(c, s);
		} else {
			return new QrMixedContent(c, s);
		}
	}
}
