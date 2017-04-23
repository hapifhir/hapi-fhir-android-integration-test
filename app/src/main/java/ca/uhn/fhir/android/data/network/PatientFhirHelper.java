package ca.uhn.fhir.android.data.network;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public class PatientFhirHelper {

    public static final String TAG = PatientFhirHelper.class.getSimpleName();

    private static final String HTML_OPEN = "<html>";
    private static final String HTML_CLOSE = "</html>";

    private String mUrl;


    public PatientFhirHelper(String serverUrl) {
        mUrl = serverUrl;
    }

    public static Spanned patientToPrettyPrint(Patient patient) {
        Spanned prettyPrintString = null;

        if (patient != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                prettyPrintString = Html.fromHtml(HTML_OPEN + patient.getText().getDiv().getValueAsString() + HTML_CLOSE, 0);
            } else {
                prettyPrintString = Html.fromHtml(HTML_OPEN + patient.getText().getDiv().getValueAsString() + HTML_CLOSE);
            }

        } else {
            throw new IllegalStateException("Can't syncReadingsWithinRange a null local FhirDataType");
        }

        return prettyPrintString;
    }

    public ArrayList<Patient> getPatients() {
        ArrayList<Patient> fetchedPatients = new ArrayList<>();

        List<ca.uhn.fhir.model.dstu2.resource.Bundle.Entry> remotePatientEntries
                = FhirNetworkHelper.fetchListOfFhirObjects(mUrl, Patient.class);

        for (ca.uhn.fhir.model.dstu2.resource.Bundle.Entry remoteEntry : remotePatientEntries) {
            fetchedPatients.add((Patient) remoteEntry.getResource());
        }

        return fetchedPatients;
    }

    public Spanned gimmySomePrettyPrint(String remoteKey, String url) {

        Patient remotePatient = null;
        Spanned prettyPrintString = null;

        try {
            if (remoteKey != null) {
                remotePatient = FhirNetworkHelper.getClient(url).read()
                        .resource(Patient.class)
                        .withId(remoteKey)
                        .prettyPrint()
                        .execute();

                prettyPrintString = patientToPrettyPrint(remotePatient);

            } else {
                throw new IllegalStateException("Remote key null");
            }

        } catch (BaseServerResponseException bse) {
            Log.e(TAG, "downloadSingleResource, exception syncing patient to FHIR server :: " + bse.getMessage());
        }

        return prettyPrintString;
    }
}
