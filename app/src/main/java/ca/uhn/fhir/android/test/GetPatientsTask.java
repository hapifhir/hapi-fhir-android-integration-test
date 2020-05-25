package ca.uhn.fhir.android.test;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.android.ListCitizensActivity;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;

public class GetPatientsTask extends AsyncTask<Void, Object, List<Patient>> {

    private static final String TAG = "GetPatientsTask";

    private final PatientFhirHelper fhir = new PatientFhirHelper();
    private WeakReference<ListCitizensActivity> activity;

    public GetPatientsTask(ListCitizensActivity activity) {
        this.activity = new WeakReference<ListCitizensActivity>(activity);
    }

    @Override
    protected List<Patient> doInBackground(Void... args) {
        final List<Patient> patients = new ArrayList<Patient>();

        try {
            patients.addAll(fhir.getPatients());
        } catch (FhirClientConnectionException e) {
            String message = "Failed to connect to Hapi FHIR server";

            if (e.getMessage() != null) {
                message = e.getMessage();
            }

            Log.e(TAG, message, e);
        }

        return patients;
    }

    @Override
    protected void onPostExecute(List<Patient> patients) {
        StringBuilder b = new StringBuilder("Found the following patients...").append('\n');
        for (Patient patient : patients) {
            b.append(patient.getText().getDiv().getValueAsString()).append('\n');
        }

        if (activity.isEnqueued()) {
            final TextView tv = (TextView) activity.get().findViewById(R.id.textview);
            tv.setText(Html.fromHtml("<html>"+b.toString()+"</html>"));
        }
    }

}
