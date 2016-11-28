package trikita.obsqr.model;

import android.content.Context;
import android.text.Spannable;

import trikita.obsqr.R;

import static trikita.obsqr.Utils.spannable;

/** Geolocation */
public class GeoLocationContent extends QrContent {
    public final static String MATCH = "geo:(.*)";
    GeoLocationContent(Context c, String s) {
        super(s, c.getString(R.string.title_geo), c.getString(R.string.action_geo), generateContent(c, s));
    }
    private static Spannable generateContent(Context context, String s) {
        String[] tokens = s.substring(4).split("\\?q=");
        StringBuilder res = new StringBuilder();
        if (tokens.length == 2 && tokens[1].length() > 0) {
            res.append(context.getString(R.string.geo_qr_title_title))
                    .append(": ").append(tokens[1]).append("\n");
        }

        String[] params = tokens[0].split(",");

        try {
            if (params.length < 2 || params.length > 3) {
                throw new NumberFormatException();
             }

            float latitude = Float.parseFloat(params[0]);
            String southMark = context.getString(R.string.geo_qr_latitude_south);
            String northMark = context.getString(R.string.geo_qr_latitude_north);
            res.append(context.getString(R.string.geo_qr_latitude_title)).append(": ")
                    .append(Math.abs(latitude)).append("\u00b0 ")
                    .append(latitude < 0 ? southMark : northMark);
            float longitude = Float.parseFloat(params[1]);
            String westMark = context.getString(R.string.geo_qr_longitude_west);
            String eastMark = context.getString(R.string.geo_qr_longitude_east);
            res.append("\n").append(context.getString(R.string.geo_qr_longitude_title)).append(": ")
                    .append(Math.abs(longitude)).append("\u00b0 ")
                    .append(longitude < 0 ? westMark : eastMark);
            if (params.length == 3) {
                float altitude = Float.parseFloat(params[2]);
                res.append("\n").append(context.getString(R.string.geo_qr_altitude_title))
                        .append(" ").append(altitude).append(": ")
                        .append(context.getString(R.string.geo_qr_altitude_suffix));
            }
            return spannable(res.toString());
        } catch (NumberFormatException e) {
            res = new StringBuilder(context.getString(R.string.unsupported_data_text));
        }

        return spannable(res.toString());
    }
}
