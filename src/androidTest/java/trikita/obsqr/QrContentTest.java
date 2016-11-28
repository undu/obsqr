package trikita.obsqr;

import android.test.AndroidTestCase;

import java.util.HashMap;
import java.util.Map;

import trikita.obsqr.model.ContactContent;
import trikita.obsqr.model.EmailContent;
import trikita.obsqr.model.GeoLocationContent;
import trikita.obsqr.model.GooglePlayContent;
import trikita.obsqr.model.PhoneNumberContent;
import trikita.obsqr.model.QrContent;
import trikita.obsqr.model.SmsContent;
import trikita.obsqr.model.WebUrlContent;
import trikita.obsqr.model.WifiContent;

public class QrContentTest extends AndroidTestCase {

	public void testBrokenRegexp() {
		String s = "http://qrs.ly/z24icxy";
		assertFalse(s.matches(GooglePlayContent.MATCH));
		// For some reason, Moto G's String.match() caused ANR on this string
		s = "https://play.google.com/store/apps/details?id=com.mvl.ThunderValley";
		assertFalse(s.matches(GooglePlayContent.MATCH));
		assertTrue(s.matches(WebUrlContent.MATCH));
	}

	public void testGooglePlayMatcher() {
		for (Map.Entry<String, String> e : new HashMap<String, String>() {{
			put("market://details?id=com.example", "com.example");
			put("market://details?id=foo", "foo");
			put("market://details.id.foo", "market://details.id.foo");
			put("market://search?pub=trikita", "market://search?pub=trikita");
			put("market://search?q=Some+Query&c=apps", "market://search?q=Some+Query&c=apps");
			put("market://apps/collections/editors_choice", "market://apps/collections/editors_choice");
		}}.entrySet()) {
			QrContent qr = QrContent.from(getContext(), e.getKey());
			assertEquals(GooglePlayContent.class, qr.getClass());
			assertEquals(e.getValue(), qr.content.toString());
		}
	}

	public void testUrlMatcher() {
		for (Map.Entry<String, String> e : new HashMap<String, String>() {{
			put("http://example.com", "http://example.com");
			put("https://example.com", "https://example.com");
			put("ftp://example.com", null);
			put("example.com", "http://example.com");
			put("link to http://example.com", null);
		}}.entrySet()) {
			QrContent qr = QrContent.from(getContext(), e.getKey());
			if (e.getValue() != null) {
				assertEquals(WebUrlContent.class, qr.getClass());
				assertEquals(e.getValue(), qr.content.toString());
			} else {
				assertFalse(qr instanceof WebUrlContent);
			}
		}
	}

	public void testEmailMatcher() {
		for (Map.Entry<String, String> e : new HashMap<String, String>() {{
			put("mailto:johndoe@example.com", "johndoe@example.com");
			put("MAILTO:johndoe@example.com", "johndoe@example.com");
			put("MAILTO:johndoe@example.com?subject=Hello+world", "johndoe@example.com");
		}}.entrySet()) {
			QrContent qr = QrContent.from(getContext(), e.getKey());
			if (e.getValue() != null) {
				assertEquals(EmailContent.class, qr.getClass());
				assertEquals(e.getValue(), qr.content.toString());
			} else {
				assertFalse(qr instanceof EmailContent);
			}
		}
	}

	public void testSmsMatcher() {
		QrContent content;
		content = QrContent.from(getContext(), "smsto:+123456789");
		content = QrContent.from(getContext(), "smsto:+18554407400:I am interested in using Scanova");
		// SMSTO
		assertEquals(SmsContent.class, content.getClass());
	}

	public void testPhoneNumberMatcher() {
		QrContent content;
		content = QrContent.from(getContext(), "tel:+123456789");
		// TEL:
		assertTrue(content instanceof PhoneNumberContent);
	}

	public void testWifiMatcher() {
		QrContent content;
		content = QrContent.from(getContext(), "WIFI:S:Example;T:WPA;P:example123;;");
		assertEquals(WifiContent.class, content.getClass());
	}

	public void testContactMatcher() {
		QrContent content;
		// TODO lots of other examples
		content = QrContent.from(getContext(), "MECARD:N:John Doe;EMAIL:john@example.com;;");
		assertEquals(ContactContent.class, content.getClass());
	}

	public void testGeolocationMatcher() {
		QrContent content;
		content = QrContent.from(getContext(), "geo:0,0");
		assertEquals(GeoLocationContent.class, content.getClass());
	}
}
