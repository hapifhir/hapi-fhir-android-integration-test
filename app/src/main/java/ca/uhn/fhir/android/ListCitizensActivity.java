package ca.uhn.fhir.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ListCitizensActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_list_citizens);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        final TextView tv = (TextView) findViewById(R.id.textview);
//        tv.setTextSize(20);
//        tv.setText("Fetching data...");
//        AsyncTask<Void, Object, List<Patient>> as = new AsyncTask<Void, Object, List<Patient>>() {
//            @Override
//            protected List<Patient> doInBackground(Void... voids) {
//                PatientFhirHelper gcm = new PatientFhirHelper();
//                return gcm.getPatients();
//            }
//
//            @Override
//            protected void onPostExecute(List<Patient> patients) {
//                StringBuilder b = new StringBuilder("Found the following patients...").append('\n');
//                for (Patient patient : patients) {
//                    b.append(patient.getText().getDiv().getValueAsString()).append('\n');
//                }
//                tv.setText(Html.fromHtml("<html>"+b.toString()+"</html>"));
//            }
//        };
//        as.execute();
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
