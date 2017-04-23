package ca.uhn.fhir.android.ui.PatientActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Spanned;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.uhn.fhir.android.test.R;

/**
 * Created by mark on 2017-04-23.
 */

public class PatientDetailFragment extends Dialog {

    @BindView(R.id.pretty_print_view)
    protected TextView mPrettyPrintView;

    @BindView(R.id.dismiss_button)
    protected Button mDismissButton;

    @BindView(R.id.title_view)
    protected TextView mTitleView;

    protected String mPatientId = null;
    protected Spanned mPrettyPrintString = null;

    public PatientDetailFragment(@NonNull Context context, Spanned prettyPrintString, String patientId) {
        super(context);
        if ((prettyPrintString != null) && (patientId != null)) {
            mPrettyPrintString = prettyPrintString;
            mPatientId = patientId;
        } else {
            throw new IllegalStateException("Null inputs passed to patient dialog...");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.patient_dialog_layout);
        ButterKnife.bind(this);

        mTitleView.setText(String.format(getContext().getString(R.string.patient_dialog_title), mPatientId));
        mPrettyPrintView.setText(mPrettyPrintString);
    }

    @OnClick(R.id.dismiss_button)
    public void fetchLoginInfo() {
        dismiss();
    }
}
