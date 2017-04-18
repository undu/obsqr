package trikita.obsqr;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class QrContentTest {

	@Test
	public void testBrokenRegexp() {
		String s = "http://qrs.ly/z24icxy";
		assertFalse(s.matches(GooglePlayContent.MATCH));
		// For some reason, Moto G's String.match() caused ANR on this string
		s = "https://play.google.com/store/apps/details?id=com.mvl.ThunderValley";
		assertFalse(s.matches(GooglePlayContent.MATCH));
		assertTrue(s.matches(WebUrlContent.MATCH));
	}

	@Test
	public void testGooglePlayMatcher() {
		for (Map.Entry<String, String> e : new HashMap<String, String>() {{
			put("market://details?id=com.example", "com.example");
			put("market://details?id=foo", "foo");
			put("market://details.id.foo", "market://details.id.foo");
			put("market://search?pub=trikita", "market://search?pub=trikita");
			put("market://search?q=Some+Query&c=apps", "market://search?q=Some+Query&c=apps");
			put("market://apps/collections/editors_choice", "market://apps/collections/editors_choice");
		}}.entrySet()) {
			QrContent qr = QrContent.from(InstrumentationRegistry.getTargetContext(), e.getKey());
			assertEquals(GooglePlayContent.class, qr.getClass());
			assertEquals(e.getValue(), qr.content.toString());
		}
	}

	@Test
	public void testUrlMatcher() {
		for (Map.Entry<String, String> e : new HashMap<String, String>() {{
			put("http://example.com", "http://example.com");
			put("https://example.com", "https://example.com");
			put("ftp://example.com", null);
			put("example.com", "http://example.com");
			put("link to http://example.com", null);
		}}.entrySet()) {
			QrContent qr = QrContent.from(InstrumentationRegistry.getTargetContext(), e.getKey());
			if (e.getValue() != null) {
				assertEquals(WebUrlContent.class, qr.getClass());
				assertEquals(e.getValue(), qr.content.toString());
			} else {
				assertFalse(qr instanceof WebUrlContent);
			}
		}
	}

	@Test
	public void testEmailMatcher() {
		for (Map.Entry<String, String> e : new HashMap<String, String>() {{
			put("mailto:johndoe@example.com", "johndoe@example.com");
			put("MAILTO:johndoe@example.com", "johndoe@example.com");
			put("MAILTO:johndoe@example.com?subject=Hello+world", "johndoe@example.com");
		}}.entrySet()) {
			QrContent qr = QrContent.from(InstrumentationRegistry.getTargetContext(), e.getKey());
			if (e.getValue() != null) {
				assertEquals(EmailContent.class, qr.getClass());
				assertEquals(e.getValue(), qr.content.toString());
			} else {
				assertFalse(qr instanceof EmailContent);
			}
		}
	}

	@Test
	public void testSmsMatcher() {
		QrContent content;
		content = QrContent.from(InstrumentationRegistry.getTargetContext(), "smsto:+123456789");
		content = QrContent.from(InstrumentationRegistry.getTargetContext(), "smsto:+18554407400:I am interested in using Scanova");
		// SMSTO
		assertEquals(SmsContent.class, content.getClass());
	}

	@Test
	public void testPhoneNumberMatcher() {
		QrContent content;
		content = QrContent.from(InstrumentationRegistry.getTargetContext(), "tel:+123456789");
		// TEL:
		assertTrue(content instanceof PhoneNumberContent);
	}

	@Test
	public void testWifiMatcher() {
		QrContent content;
		content = QrContent.from(InstrumentationRegistry.getTargetContext(), "WIFI:S:Example;T:WPA;P:example123;;");
		assertEquals(WifiContent.class, content.getClass());
	}

	@Test
	public void testContactMatcher() {
		QrContent content;
		// TODO lots of other examples
		content = QrContent.from(InstrumentationRegistry.getTargetContext(), "MECARD:N:John Doe;EMAIL:john@example.com;;");
		assertEquals(ContactContent.class, content.getClass());
	}

	@Test
	public void testGeolocationMatcher() {
		QrContent content;
		content = QrContent.from(InstrumentationRegistry.getTargetContext(), "geo:0,0");
		assertEquals(GeoLocationContent.class, content.getClass());
	}
}
