package ca.uhn.fhir.android.ui.PatientActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ca.uhn.fhir.android.data.network.PatientFhirHelper;
import ca.uhn.fhir.android.test.R;
import ca.uhn.fhir.model.dstu2.resource.Patient;

/**
 * Created by mark on 2017-04-21.
 */

public class FhirPatientAdapter extends RecyclerView.Adapter<PatientViewHolder> {

    protected ArrayList<Patient> mPatients;

    public FhirPatientAdapter(Context context, ArrayList<Patient> patients) {
        this.mPatients = patients;
    }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_card, parent, false);
        return new PatientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PatientViewHolder holder, int position) {
        final Patient patientAtPosition = mPatients.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat(holder.mAddressField.getContext().getString(R.string.birthday_simple_format));

        String patientId = (patientAtPosition.getId() != null ? patientAtPosition.getId().getIdPart() : holder.mAddressField.getContext().getString(R.string.patient_id_none_set));
        String patientGender = (patientAtPosition.getGender() != null ? patientAtPosition.getGender() : holder.mGenderField.getContext().getString(R.string.patient_gender_none_set));
        String patientName = (patientAtPosition.getNameFirstRep() != null ? patientAtPosition.getNameFirstRep().getText() : holder.mGivenNameField.getContext().getString(R.string.patient_given_name_none_set));
        String patientBirthday = (patientAtPosition.getBirthDate() != null ? dateFormat.format(patientAtPosition.getBirthDate()) : holder.mBirthdayField.getContext().getString(R.string.patient_address_none_set));
        String patientAddress = (((patientAtPosition.getAddress().size() > 0) && (patientAtPosition.getAddress().get(0) != null)) ? patientAtPosition.getAddress().get(0).getText() : holder.mAddressField.getContext().getString(R.string.patient_address_none_set));

        holder.mPatientIndex.setText(String.valueOf(position + 1));
        holder.mIdField.setText(patientId);
        holder.mGenderField.setText(patientGender);
        holder.mGivenNameField.setText(patientName);
        holder.mBirthdayField.setText(patientBirthday);
        holder.mAddressField.setText(patientAddress);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientDetailFragment dialog = new PatientDetailFragment(v.getContext(),
                        PatientFhirHelper.patientToPrettyPrint(patientAtPosition),
                        patientAtPosition.getId().getIdPart());
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mPatients ? mPatients.size() : 0);
    }
}
