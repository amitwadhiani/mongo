package co.arctern.rider.api.sms;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * Class picked from patient-api gateway service
 */
public class SmsGateway {

    private static String api_key;
    private static String sender;
    private static String base_URL;

    /**
     * Initialize the SmsGateway environment.
     *
     * @param api_key  - API key generated from SMS Account.
     * @param sender   - Sender ID assigned to the account.
     * @param base_URL - BASE URL of SMS Service URL
     */
    public static void init(String api_key, String sender, String base_URL) {

        SmsGateway.setApiKey(api_key);
        SmsGateway.setSender(sender);
        SmsGateway.setBaseURL(base_URL);
    }

    /**
     * set api_key.
     *
     * @param api_key - API key generated from SMS Account.
     */
    public static void setApiKey(String api_key) {

        SmsGateway.api_key = api_key;
    }

    /**
     * set sender
     *
     * @param sender - Sender ID assigned to the account.
     */
    public static void setSender(String sender) {

        SmsGateway.sender = sender;
    }

    /**
     * set base_URL.
     *
     * @param base_URL - BASE URL of SMS Service URL.
     */
    public static void setBaseURL(String base_URL) {

        SmsGateway.base_URL = base_URL;
    }

    /**
     * function for sending sms.
     *
     * @param to  - Receiver numbers to which the message has to be sent.
     * @param msg - Message content.
     * @param map - Optional parameters.
     *            time - Date and time for scheduling an SMS.
     *            unicode    - Specify that the message to be sent is in unicode format(0/1).
     *            flash      - To specify that the message is to be sent in the flash format(0/1).
     *            dlrurl     - The encoded URL to receive delivery reports.
     *            custom     - Custom message ID
     *            format     - Format of the response(XML/PHP/JSON/JSONP)..
     *            callback   - Callback function for JSONP response format.
     *            port       - Port number to which SMS has to be sent.
     * @return response - response  in requested format.
     * @throws Exception [description]
     */
    public String sendSms(String to, String msg, Map<String, String> map) throws Exception {

        String URL;
        String encoded_msg = URLEncoder.encode(msg, "UTF-8");

        URL = base_URL + "&method=sms&api_key=" + api_key + "&sender=" + sender + "&to=" + to + "&message=" + encoded_msg;
        URL = optionalParameters(URL, map);

        String response = triggerURL(URL);
        return response;
    }

    /**
     * function for sending sms using xml api.
     *
     * @param xml -    XML data.
     * @param map -    Optional parameters.
     *            format     - Format of the response(XML/PHP/JSON/JSONP)..
     *            callback   - Callback function for JSONP response format.
     * @return response - response in requested format.
     * @throws Exception [description]
     */
    public String sendSmsUsingXmlApi(String xml, Map<String, String> map) throws Exception {

        String URL;
        String encoded_xml = URLEncoder.encode(xml, "UTF-8");

        URL = base_URL + "&method=sms.xml&api_key=" + api_key + "&xml=" + encoded_xml;
        URL = optionalParameters(URL, map);

        String response = triggerURL(URL);
        return response;
    }

    /**
     * function for sending sms using json api.
     *
     * @param json -    JSON data.
     * @param map  -    Optional parameters.
     *             format     - Format of the response(XML/PHP/JSON/JSONP).(XML/PHP/JSON/JSONP).
     *             callback   - Callback function for JSONP response format.
     * @return response -    response in requested format.
     * @throws Exception [description]
     */
    public String sendSmsUsingJsonApi(String json, Map<String, String> map) throws Exception {

        String URL;
        //encoding of xml codes
        String encoded_json = URLEncoder.encode(json, "UTF-8");

        URL = base_URL + "&method=sms.json&api_key=" + api_key + "&json=" + encoded_json;
        URL = optionalParameters(URL, map);

        String response = triggerURL(URL);
        return response;
    }

    /**
     * function for knowing sms staus.
     *
     * @param ids -     Message IDs
     * @param map -     Optional parameters.
     *            format     - Format of the response(XML/PHP/JSON/JSONP).(XML/PHP/JSON/JSONP).
     *            numberinfo - Flag to query service provider and location data(0/1).
     * @return response -     response in requested format
     * @throws Exception [description]
     */
    public String smsStatusPull(String ids, Map<String, String> map) throws Exception {

        String URL;

        URL = base_URL + "&method=sms.status&api_key=" + api_key + "&id=" + ids;
        URL = optionalParameters(URL, map);

        String response = triggerURL(URL);
        return response;
    }

    /**
     * function for sms stauts push.
     *
     * @param to      - receiver's number on which message has to be sent.
     * @param msg     - message content.
     * @param dlr_url - The encoded URL to receive delivery reports.
     * @param map     - Optional parameters.
     *                format     - Format of the response(XML/PHP/JSON/JSONP).(XML/PHP/JSON/JSONP).
     * @return response - response in requested format.
     * @throws Exception [description]
     */
    public String smsStatusPush(String to, String msg, String dlr_url, Map<String, String> map) throws Exception {

        String URL;

        String encoded_msg = URLEncoder.encode(msg, "UTF-8");
        String encode_dlr_url = URLEncoder.encode(dlr_url, "UTF-8");

        URL = base_URL + "&method=sms&api_key=" + api_key + "&sender=" + sender + "&to=" + to + "&message=" + encoded_msg + "&dlr_url=" + encode_dlr_url;
        URL = optionalParameters(URL, map);

        String response = triggerURL(URL);
        return response;
    }

    /**
     * function for sendng sms to optin group.
     *
     * @param name - name of optin group.
     * @param msg  - message content.
     * @param map  - Optional parameters.
     *             time - Date and time for scheduling an SMS.
     *             unicode    - Specify that the message to be sent is in unicode format(0/1).
     *             flash      - To specify that the message is to be sent in the flash format(0/1).
     *             format     - Format of the response(XML/PHP/JSON/JSONP).
     *             callback   - Callback function for JSONP response format.
     * @return response -  response in requested format.
     * @throws Exception [description]
     */
    public String smsToOptinGroup(String msg, String name, String id, Map<String, String> map) throws Exception {

        String URL;
        String encoded_msg = URLEncoder.encode(msg, "UTF-8");

        URL = base_URL + "&method=optin&api_key=" + api_key + "&sender=" + sender + "&message=" + encoded_msg;
        URL = optionalParameters(URL, map);

        if (id != null) {
            URL = URL + "&id=" + id;
        } else if (name != null) {
            URL = URL + "&name=" + name;
        } else {
            return "wrong id/name";
        }

        String response = triggerURL(URL);
        return response;
    }

    /**
     * function for adding contacts to the group.
     *
     * @param number - mobile number of the contact.
     * @param name   - group name.
     * @param map    - Optional parameters.
     *               fullname - name of the contact to be added name of the contact.
     *               email    - email of the contact.
     *               action.  - Flag to specify the action(add/delete).
     *               format   - Format of the response(XML/PHP/JSON/JSONP).
     * @return response -  response in requested format.
     * @throws Exception [description]
     */
    public String addContactsToGroup(String name, String number, Map<String, String> map) throws Exception {

        String URL;

        URL = base_URL + "&method=groups.register&api_key=" + api_key + "&name=" + name + "&number=" + number;
        URL = optionalParameters(URL, map);

        String response = triggerURL(URL);
        return response;
    }

    /**
     * funtion for sending sms to group.
     *
     * @param name - group name.
     * @param msg  - message content.
     * @param map  - Optional parameters.
     * @param map  - Optional parameters.
     *             time - Date and time for scheduling an SMS.
     *             unicode    - Specify that the message to be sent is in unicode format(0/1).
     *             flash      - To specify that the message is to be sent in the flash format(0/1).
     *             format     - Format of the response(XML/PHP/JSON/JSONP)..
     *             callback   - Callback function for JSONP response format.
     * @return response - response in requested format.
     * @throws Exception [description]
     */
    public String sendMessageToGroup(String msg, String name, String group_id, Map<String, String> map) throws Exception {

        String URL;
        String encoded_msg = URLEncoder.encode(msg, "UTF-8");

        URL = base_URL + "&method=groups&api_key=" + api_key + "&name=" + name + "&id=" + group_id + "&sender=" + sender + "&message=" + encoded_msg;
        URL = optionalParameters(URL, map);

        String response = triggerURL(URL);
        return response;
    }

    /**
     * function for modifying the scheduled sms.
     *
     * @param group_id - group id that has to be deleted.
     * @param time     - Time to which the slot needs to be re-scheuled
     * @param map      - Optional parameters.
     *                 format     - Format of the response(XML/PHP/JSON/JSONP)..
     * @return response - response in requested format.
     * @throws Exception [description]
     */
    public String editSchedule(String time, String group_id, Map<String, String> map) throws Exception {

        String URL;

        URL = base_URL + "&method=sms.schedule&api_key=" + api_key + "&groupid=" + group_id + "&time=" + time + "&task=modify";
        URL = optionalParameters(URL, map);

        String response = triggerURL(URL);
        return response;
    }

    /**
     * function for deleting the scheduled sms.
     *
     * @param group_id - group id that has to be deleted.
     * @param map      - Optional parameters.
     *                 format     - Format of the response(XML/PHP/JSON/JSONP)..
     * @return response - response in requested format.
     * @throws Exception [description]
     */
    public String deleteScheduledSms(String group_id, Map<String, String> map) throws Exception {

        String URL;

        URL = base_URL + "&method=sms.schedule&api_key=" + api_key + "&groupid=" + group_id;
        URL = optionalParameters(URL, map);

        String response = triggerURL(URL);
        return response;
    }

    /**
     * function for checking the credits available in account.
     *
     * @param map - Optional parameters.
     *            format     - Format of the response(XML/PHP/JSON/JSONP).
     * @return response - response in requested format.
     * @throws Exception [description]
     */
    public String creditAvailabilityCheck(Map<String, String> map) throws Exception {

        String URL;

        URL = base_URL + "&method=account.credits&api_key=" + api_key;
        URL = optionalParameters(URL, map);

        String response = triggerURL(URL);
        return response;
    }

    /**
     * function for SI Lookup.
     *
     * @param to  - Receiver's number on which message has to be sent.
     * @param map - Optional parameters.
     *            format     - Format of the response(XML/PHP/JSON/JSONP).
     * @return response - response in requested format.
     * @throws Exception [description]
     */
    public String SILookup(String to, Map<String, String> map) throws Exception {

        String URL;

        URL = base_URL + "&method=hlr&api_key=" + api_key + "&to=" + to;
        URL = optionalParameters(URL, map);

        String response = triggerURL(URL);
        return response;
    }

    /**
     * functino for creating txtly.
     *
     * @param txtly_URL - URL that requires to be shortened and tracked
     * @param map       - Optional parameters.
     *                  format     - Format of the response(XML/PHP/JSON/JSONP).
     *                  token      - Customized word unique for each txtly.
     *                  title      - A significant title to the txtly.
     *                  advanced   - Advanced analytics gives an option to track who (Recipient mobile numbers) visited.
     *                  track      - Location Track gives the city and state details of URL visitor(0/1).
     *                  attach     - Media file that requires to be compressed to a short link.
     * @return response - response in requested format.
     * @throws Exception [description]
     */
    public String createTxtly(String txtly_URL, Map<String, String> map) throws Exception {

        String URL;

        URL = base_URL + "&method=txtly.create&api_key=" + api_key + "&url=" + txtly_URL;
        URL = optionalParameters(URL, map);

        String response = triggerURL(URL);
        return response;
    }

    /**
     * function for deleting the txtly.
     *
     * @param id  - Id of the txtly.
     * @param map - Optional parameters.
     *            format     - Format of the response(XML/PHP/JSON/JSONP).
     * @return response - response in requested format.
     * @throws Exception [description]
     */
    public String deleteTxtly(String id, Map<String, String> map) throws Exception {

        String URL;

        URL = base_URL + "&method=txtly" + "&api_key=" + api_key + "&task=delete&id=" + id + "&app=1";
        URL = optionalParameters(URL, map);

        String response = triggerURL(URL);
        return response;
    }

    /**
     * function for getting textly report.
     *
     * @param map - Optional parameters.
     *            format     - Format of the response(XML/PHP/JSON/JSONP).
     * @return response - response in requested format.
     * @throws Exception [description]
     */
    public String textlyReportExtract(Map<String, String> map) throws Exception {

        String URL;

        URL = base_URL + "&method=txtly&api_key=" + api_key + "&app=1";
        URL = optionalParameters(URL, map);

        String response = triggerURL(URL);
        return response;
    }

    /**
     * funcition for getting log about individual txtly.
     *
     * @param id  - Id of the txtly.
     * @param map - Optional parameters.
     *            format     - Format of the response(XML/PHP/JSON/JSONP).
     * @return response - response in requested format.
     * @throws Exception [description]
     */
    public String pullLogForIndividualTxtly(String id, Map<String, String> map) throws Exception {

        String URL;

        URL = base_URL + "&method=txtly.logs&api_key=" + api_key + "&id=" + id + "&app=1";
        URL = optionalParameters(URL, map);

        String response = triggerURL(URL);
        return response;
    }

    /**
     * function for adding optional parameters.
     *
     * @param URL - URL without optional parameters.
     * @param map - optional parameters.
     * @return URL - URL with optional parameters included.
     */
    public String optionalParameters(String URL, Map<String, String> map) {

        if (!(map.isEmpty())) {
            for (Map.Entry m : map.entrySet()) {
                URL = URL + "&" + m.getKey() + "=" + m.getValue();
            }
        }
        return URL;
    }

    /**
     * function for triggering url.
     *
     * @param request_URL - url
     * @return response - return response back to the caller in string format.
     * @throws Exception [description]
     */
    public String triggerURL(String request_URL) throws IOException, Exception {

        addSslCertificate();
        // initiating url
        URL URL = new URL(request_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) URL.openConnection();

        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode != 200) {
            return Integer.toString(responseCode);
        }
        //reading data from input stream.
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String response = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response = response + inputLine;
        }
        in.close();
        return response;
    }

    /**
     * function for adding ssl certificate.
     */
    public void addSslCertificate() throws NoSuchAlgorithmException, KeyManagementException {

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}