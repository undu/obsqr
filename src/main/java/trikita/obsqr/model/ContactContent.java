package trikita.obsqr.model;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.text.Spannable;

import java.util.Map;

import trikita.obsqr.R;

import static trikita.obsqr.Utils.spannable;
import static trikita.obsqr.Utils.tokenize;

/** Contact information */
public class ContactContent extends QrContent {
    public final static String MATCH = "mecard:(.*)";
    private static String FIELDS[] = new String[]{"N", "TEL", "ADR", "EMAIL", "ORG"};
    private static int FIELD_NAMES[] = new int[]{
            R.string.contact_qr_name_title,
            R.string.contact_qr_phone_title,
            R.string.contact_qr_address_title,
            R.string.contact_qr_email_title,
            R.string.contact_qr_company_title,
    };

    private static String INTENT_FIELDS[] = new String[]{
            ContactsContract.Intents.Insert.NAME,
            ContactsContract.Intents.Insert.PHONE,
            ContactsContract.Intents.Insert.POSTAL,
            ContactsContract.Intents.Insert.EMAIL,
            ContactsContract.Intents.Insert.COMPANY,
    };

    ContactContent(Context c, String s) {
        super(s, c.getString(R.string.title_contact), c.getString(R.string.action_contact), generateContent(c, s));
    }

    @Override
    public Intent getActionIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        Map<String, String> tokens = tokenize(rawContent.substring(7), FIELDS);
        for (int i = 0; i < FIELDS.length; i++) {
            if (tokens.get(FIELDS[i]) != null) {
                intent.putExtra(INTENT_FIELDS[i], tokens.get(FIELDS[i]));
            }
        }
        return intent;
    }

    private static Spannable generateContent(Context c, String s) {
        StringBuilder res = new StringBuilder();
        Map<String, String> tokens = tokenize(s.substring(7), FIELDS);
        for (int i = 0; i < FIELDS.length; i++) {
            if (tokens.get(FIELDS[i]) != null) {
                res.append(c.getString(FIELD_NAMES[i])).append(": ")
                        .append(tokens.get(FIELDS[i])).append('\n');
            }
        }
        return spannable(res.toString());
    }
}
