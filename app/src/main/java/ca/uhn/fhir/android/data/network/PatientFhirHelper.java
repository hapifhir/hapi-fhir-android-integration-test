package ca.uhn.fhir.android.data.network;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.dstu2.resource.Patient;

public class PatientFhirHelper {

    private String mUrl;

    public PatientFhirHelper(String serverUrl) {
        mUrl = serverUrl;
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
}
