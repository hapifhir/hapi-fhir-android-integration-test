package ca.uhn.fhir.android.ui.PatientActivity;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.uhn.fhir.android.test.R;

/**
 * Created by mark on 2017-04-21.
 */
public class PatientViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.card_view)
    public CardView mCardView;

    @BindView(R.id.citizen_index)
    public TextView mPatientIndex;

    @BindView(R.id.id_field)
    public TextView mIdField;

    @BindView(R.id.gender_field)
    public TextView mGenderField;

    @BindView(R.id.name_field)
    public TextView mGivenNameField;

    @BindView(R.id.birthday_field)
    public TextView mBirthdayField;

    @BindView(R.id.address_field)
    public TextView mAddressField;

    public PatientViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
