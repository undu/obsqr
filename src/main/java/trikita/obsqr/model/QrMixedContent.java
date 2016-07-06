package trikita.obsqr.model;

import android.content.Context;
import android.content.Intent;

import trikita.obsqr.CopyToClipboardActivity;
import trikita.obsqr.R;

import static trikita.obsqr.Utils.spannable;

/** Mixed content: plain text that may contain some URLs, emails etc */
class QrMixedContent extends QrContent {
    public QrMixedContent(Context c, String s) {
        super(s, c.getString(R.string.title_text), c.getString(R.string.action_text), spannable(s));
    }

    @Override
    public Intent getActionIntent(Context c) {
        Intent copyIntent = new Intent(c, CopyToClipboardActivity.class);
        copyIntent.putExtra(Intent.EXTRA_TEXT, rawContent);
        return copyIntent;
    }
}
