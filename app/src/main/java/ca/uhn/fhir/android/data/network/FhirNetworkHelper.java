package ca.uhn.fhir.android.data.network;

import android.util.Log;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.rest.api.MethodOutcome;
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
            mGenericClient = ctx.newRestfulGenericClient(url);
            mUrl = url;
            return mGenericClient;
        }
    }

    public static List<Bundle.Entry> fetchListOfFhirObjects(String url, Class<? extends IBaseResource> clazz) {

        List<ca.uhn.fhir.model.dstu2.resource.Bundle.Entry> returnEntries = new ArrayList<>();

        try {
            ca.uhn.fhir.model.dstu2.resource.Bundle returnBundle = getClient(url)
                    .search()
                    .forResource(clazz)
                    .prettyPrint()
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

    public static IBaseResource downloadSingleResource(Class<? extends IBaseResource> clazz, String remoteKey, String url) {

        IBaseResource remoteObj = null;

        try {
            if (remoteKey != null) {
                remoteObj = getClient(url).read()
                        .resource(clazz)
                        .withId(remoteKey)
                        .prettyPrint()
                        .execute();
            } else {
                throw new IllegalStateException("Can't syncReadingsWithinRange a null local FhirDataType");
            }
        } catch (BaseServerResponseException bse) {
            Log.e(TAG, "downloadSingleResource, exception syncing patient to FHIR server :: " + bse.getMessage());
        }

        return remoteObj;
    }

    public static <U extends BaseResource> MethodOutcome uploadFhirObject(U curLocalFhirType, String url) {

        MethodOutcome returnedOutcome = null;

        if ((curLocalFhirType != null) && (url != null)) {

            try {
                if (curLocalFhirType.getId().getIdPart() != null) {
                    Log.d(TAG, "Stored remote fhir id :: " + curLocalFhirType.getId().getIdPart()
                            + "\n will push with existing ID, update()");
                    returnedOutcome = getClient(url).update().resource(curLocalFhirType).withId(curLocalFhirType.getId().toVersionless()).execute();
                } else {
                    Log.d(TAG, "No stored remote fhir id, create() will be called.");
                    returnedOutcome = getClient(url).create().resource(curLocalFhirType).execute();
                }
            } catch (BaseServerResponseException bse) {
                Log.e(TAG, "uploadFhirObject, exception uploading object to FHIR server :: " + bse.getMessage());
            }

        } else {
            Log.e(TAG, "uploadFhirObject, cannot work with null input values...");
        }

        return returnedOutcome;
    }

    public static <U extends BaseResource> void deleteFhirObject(U curLocalFhirType, String url) {

        if ((curLocalFhirType != null) && (url != null)) {

            try {
                if (curLocalFhirType.getId() != null) {
                    Log.d(TAG, "Stored remote fhir id :: " + curLocalFhirType.getId().getIdPart()
                            + "\n will delete with existing ID, deleteFhirObject()");
                    getClient(url).delete().resource(curLocalFhirType).execute();
                } else {
                    Log.d(TAG, "No stored remote fhir id, cannot delete...");
                }
            } catch (BaseServerResponseException bse) {
                Log.e(TAG, "deleteFhirObject, exception deleting object from FHIR server :: " + bse.getMessage());
            }

        } else {
            Log.e(TAG, "deleteFhirObject, cannot work with null input values...");
        }
    }
}








