package trikita.obsqr;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import hugo.weaving.DebugLog;

public class ObsqrActivity extends Activity implements CameraPreview.OnQrDecodedListener {

	public final static int PERMISSIONS_REQUEST = 100;

	private CameraPreview mCameraPreview;
	private AlertDialog mDialog;

	private String mLastKnownContent = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mCameraPreview = (CameraPreview) findViewById(R.id.surface);

		mCameraPreview.setOnQrDecodedListener(this);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
				requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST);
			}
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		mDialog = builder.create();
	}

	@Override
	public void onQrDecoded(String s) {
		if (mDialog.isShowing()) {
			return;
		}
		mLastKnownContent = s;
		final QrContent mQrContent = QrContent.from(this, s);
		mDialog.setTitle(mQrContent.title);
		mDialog.setMessage(mQrContent.content);
		mDialog.setButton(AlertDialog.BUTTON_POSITIVE, mQrContent.action, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
							performAction(mQrContent);
					}
				});
		mDialog.setButton(AlertDialog.BUTTON_NEUTRAL, this.getString(R.string.dlg_alert_share_btn_caption), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						performShare(mQrContent);
					}
					});
		mDialog.setButton(AlertDialog.BUTTON_NEGATIVE, this.getString(R.string.dlg_alert_cancel_btn_caption), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing (just dismiss dialog)
					}
				});
		mDialog.show();
	}

	@Override
	public void onQrNotFound() {
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (!mDialog.isShowing()) {
			return super.dispatchKeyEvent(event);
		}
		// Pressing DPAD, Volume keys or Camera key would call the QR action
		switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_CAMERA:
			case KeyEvent.KEYCODE_VOLUME_UP:
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				performAction(QrContent.from(this, mLastKnownContent));
				return true;
			default:
				return super.dispatchKeyEvent(event);
		}
	}

	@DebugLog
	@Override
	protected void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
				return;
			}
		}
		boolean success = mCameraPreview.acquireCamera(getWindowManager()
			.getDefaultDisplay().getRotation());
		if (!success) {
			new AlertDialog.Builder(this)
					.setMessage(getString(R.string.dlg_alert_msg))
					.setCancelable(false)
					.setPositiveButton(getString(R.string.dlg_alert_ok_btn_caption),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									ObsqrActivity.this.finish();
									dialog.dismiss();
								}
							})
					.create().show();
		}
	}

	@DebugLog
	@Override
	protected void onPause() {
		if (mDialog.isShowing()) {
			mDialog.dismiss();
		}
		mCameraPreview.releaseCamera();
		super.onPause();
	}

	@DebugLog
	@Override
	public void onBackPressed() {
		if (mDialog.isShowing()) {
			mDialog.dismiss();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == PERMISSIONS_REQUEST &&
				grantResults.length == 1 &&
				grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			recreate();
			return;
		}
		finish();
	}

	private void performAction(QrContent content) {
		try {
			this.startActivity(content.getActionIntent(this));
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, this.getString(R.string.alert_msg_activity_not_found),
					Toast.LENGTH_SHORT).show();
		}
	}


	public void performShare(QrContent content) {
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, content.rawContent);
		this.startActivity(Intent.createChooser(intent, this.getString(R.string.intent_share_caption)));
	}
}
