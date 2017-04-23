package ca.uhn.fhir.android.data.network;

/**
 * Created by miantorno on 3/29/17.
 */

public enum ServersEnum {

    /**
     * UHN FHIR Testing Server
     */
    UHN_FHIRTEST("UHN test server", false, "http://fhirtest.uhn.ca/baseDstu2"),

    /**
     * UHN FHIR Testing Server
     */
    GRAHAME_FHIRTEST("Grahame test server", false, "http://fhir2.healthintersections.com.au/"),

    /**
     * Furore development server.
     */
    FURORE_TEST("Furore test server", true, "http://spark.furore.com");

    private final static String TAG = ServersEnum.class.getSimpleName();

    private final String mEncodedValue;
    private final boolean mAuthenticationRequired;
    private final String mUrl;

    ServersEnum(String encodedValue, boolean authReq, String url) {
        mEncodedValue = encodedValue;
        mAuthenticationRequired = authReq;
        mUrl = url;
    }

    public boolean equals(ServersEnum otherValue) {
        return (otherValue == null) ? false : mEncodedValue.equals(otherValue.getEncodedValue());
    }

    public String getEncodedValue() {
        return mEncodedValue;
    }

    public boolean isAuthenticationRequired() {
        return mAuthenticationRequired;
    }

    public String getUrl() {
        return mUrl;
    }
}
