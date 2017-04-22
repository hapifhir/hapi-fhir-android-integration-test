package ca.uhn.fhir.android.data.network;

import android.util.Log;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

/**
 * Created by mark on 2017-04-21.
 */

public class FhirNetworkHelper {

    private final static String TAG = "FhirNetworkHelper";

    private static IGenericClient mGenericClient;
    private static String mUrl;

    public static IGenericClient getClient(String url) {
        if ((mGenericClient != null) && (mUrl.equals(url))) {
            return mGenericClient;
        } else {
            FhirContext ctx = FhirContext.forDstu2();
            return ctx.newRestfulGenericClient(url);
        }
    }

    public static List<Bundle.Entry> fetchListOfEntries(String url, Class<? extends IBaseResource> clazz) {

        List<ca.uhn.fhir.model.dstu2.resource.Bundle.Entry> returnEntries = new ArrayList<>();

        try {
            ca.uhn.fhir.model.dstu2.resource.Bundle returnBundle = getClient(url)
                    .search()
                    .forResource(clazz)
                    .returnBundle(Bundle.class)
                    .execute();

            if (returnBundle != null) {
                returnEntries = returnBundle.getEntry();
                Log.d(TAG, "Fetched " + returnEntries.size() + " possible items from the server.");
            }
        } catch (BaseServerResponseException bse) {
            Log.e(TAG, "downloadAll, exception downloading " + clazz.getSimpleName() + " from FHIR server :: " + bse.getMessage());
        }

        return returnEntries;

    }

    public static IBaseResource downloadSingleResource(String url, Class<? extends IBaseResource> clazz, String remoteKey) {

        IBaseResource remoteObs = null;

        try {
            if (remoteKey != null) {
                remoteObs = getClient(url).read()
                        .resource(clazz)
                        .withId(remoteKey)
                        .execute();
            } else {
                throw new IllegalStateException("Can't syncReadingsWithinRange a null local FhirDataType");
            }
        } catch (BaseServerResponseException bse) {
            Log.e(TAG, "downloadSingleResource, exception syncing patient to FHIR server :: " + bse.getMessage());
        }

        return remoteObs;
    }
}
