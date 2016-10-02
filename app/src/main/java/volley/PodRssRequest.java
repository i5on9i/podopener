package volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.namh.podopener.parser.PodRssParser;
import com.namh.podopener.recyclerview.Pod;

import org.json.JSONException;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * A request for retrieving a {@link List<Pod>} response body at a given URL, allowing for an
 * optional {@link JSONObject} to be passed in as part of the request body.
 */

public class PodRssRequest extends JsonRequest<List<Pod>> {

    /**
     * Creates a new request.
     * @param method the HTTP method to use
     * @param url URL to fetch the JSON from
     * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
     *   indicates no parameters will be posted along with request.
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public PodRssRequest(int method, String url, JSONObject jsonRequest,
                         Response.Listener<List<Pod>> listener, Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);
    }

    /**
     * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
     * <code>null</code>, <code>POST</code> otherwise.
     *
     * @see #PodRssRequest(int, String, JSONObject</Pod>, Response.Listener, Response.ErrorListener)
     */
    public PodRssRequest(String url, JSONObject jsonRequest, Response.Listener<List<Pod>> listener,
                         Response.ErrorListener errorListener) {
        this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest,
                listener, errorListener);
    }

    @Override
    protected Response<List<Pod>> parseNetworkResponse(NetworkResponse response) {
        try {
            String rssString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));


            InputStream is = new ByteArrayInputStream(rssString.getBytes(StandardCharsets.UTF_8));
            PodRssParser podRssParser = new PodRssParser(20);
            List<Pod> podlist = (List<Pod>)podRssParser.parse(is);

            return Response.success(podlist,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (XmlPullParserException e) {
            return Response.error(new ParseError(e));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
    }

}

