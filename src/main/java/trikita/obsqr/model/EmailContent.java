package trikita.obsqr.model;

import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.net.ParseException;
import android.text.Spannable;

import trikita.obsqr.R;

import static trikita.obsqr.Utils.spannable;

/** E-mail address */
class EmailContent extends QrContent {
    public final static String MATCH = "mailto:(.*)";
    public EmailContent(Context c, String s) {
        super(s, c.getString(R.string.title_email),
                c.getString(R.string.action_email), generateContent(s));
    }

    @Override
    public Intent getActionIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        try {
            MailTo uri = MailTo.parse("mailto:" + rawContent.substring(7));
            intent.putExtra(Intent.EXTRA_EMAIL, uri.getTo());
            intent.putExtra(Intent.EXTRA_SUBJECT, uri.getSubject());
            intent.putExtra(Intent.EXTRA_TEXT, uri.getBody());
        } catch (ParseException e) {
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{content.toString()});
        }
        String text = context.getString(R.string.email_qr_send_dlg_title);
        return Intent.createChooser(intent, text);
    }

    private static Spannable generateContent(String s) {
        try {
            MailTo uri = MailTo.parse("mailto:" + s.substring(7));
            System.out.println("" + uri.getTo());
            return spannable(uri.getTo());
        } catch (ParseException e) {
            e.printStackTrace();
            return spannable(s);
        }
    }
}
