package trikita.obsqr;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QrContentDialog extends FrameLayout {
	public final static int MAX_HORIZONTAL_BUTTON_TEXT_LENGTH = 12;

	private Runnable mCloseDialogRunnable = new Runnable() {
		public void run() {
			close();
		}
	};
	private QrContent mContent;

	private TextView mTitleText;
	private TextView mContentText;
	private TextView mCancelButton;
	private TextView mActionButton;
	private TextView mShareButton;

	public QrContentDialog(Context c) {
		this(c, null);
	}

	public QrContentDialog(Context c, AttributeSet attrs) {
		super(c, attrs, 0);
	}

	public QrContentDialog(Context c, AttributeSet attrs, int defStyle) {
		super(c, attrs, defStyle);
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		mTitleText = (TextView) findViewById(R.id.tv_title);
		mContentText = (TextView) findViewById(R.id.tv_content);
		mActionButton = (TextView) findViewById(R.id.btn_action);
		mActionButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				QrContentDialog.this.performAction();
			}
		});
		mCancelButton = (TextView) findViewById(R.id.btn_cancel);
		mCancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				QrContentDialog.this.close();
			}
		});
		mShareButton = (TextView) findViewById(R.id.btn_share);
		mShareButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				QrContentDialog.this.performShare();
			}
		});
	}

	public void open(QrContent content) {
		mContent = content;
		mTitleText.setText(mContent.title);
		mContentText.setText(mContent.content);
		mActionButton.setText(mContent.action);
		removeCallbacks(mCloseDialogRunnable);
		setVisibility(View.VISIBLE);
	}

	public boolean close() {
		if (mContent == null) {
			return false;
		}
		removeCallbacks(mCloseDialogRunnable);
		mContent = null;
		setVisibility(View.INVISIBLE);
		return true;
	}

	public void performAction() {
		if (mContent != null) {
			mContent.performAction();
		}
	}

	private void performShare() {
		if (mContent != null) {
			mContent.performShare();
		}
	}
}

