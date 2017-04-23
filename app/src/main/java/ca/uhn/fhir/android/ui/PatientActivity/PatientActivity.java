package ca.uhn.fhir.android.ui.PatientActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.uhn.fhir.android.data.loaders.AsyncTaskTimeout;
import ca.uhn.fhir.android.data.network.PatientFhirHelper;
import ca.uhn.fhir.android.test.R;
import ca.uhn.fhir.model.dstu2.resource.Patient;

public class PatientActivity extends AppCompatActivity {

    public static final String BUNDLE_STRING_URL = "BUNDLE_STRING_URL";

    @BindView(R.id.patient_recycler_view)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.progress_bar_layout)
    protected RelativeLayout mLoadingScreen;

    protected String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if ((extras != null) && (extras.getString(BUNDLE_STRING_URL) != null)) {
            mUrl = extras.getString(BUNDLE_STRING_URL);
        } else {
            throw new IllegalStateException("Cannot start patient activity without valid server URL...");
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        loadPatients();
    }

    protected void loadPatients() {

        AsyncTask<Void, Void, ArrayList<Patient>> getPatientsTask = new AsyncTask<Void, Void, ArrayList<Patient>>() {

            @Override
            protected ArrayList<Patient> doInBackground(Void... voids) {
                PatientFhirHelper gcm = new PatientFhirHelper(mUrl);
                return gcm.getPatients();
            }

            @Override
            protected void onPostExecute(ArrayList<Patient> patients) {
                FhirPatientAdapter adapter = new FhirPatientAdapter(getApplicationContext(), patients);
                mRecyclerView.setAdapter(adapter);
                mLoadingScreen.setVisibility(View.GONE);
            }
        };

        getPatientsTask.execute();
        AsyncTaskTimeout checkAyscTask = new AsyncTaskTimeout(getPatientsTask);
        new Thread(checkAyscTask).start();
    }


}
