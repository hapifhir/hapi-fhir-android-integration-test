package ca.uhn.fhir.android.test;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.util.BundleUtil;

public class PatientFhirHelper {

    private static final String DSTU2_URL = BuildConfig.API_URL + "baseDstu2";

    private IGenericClient client;
    private FhirContext ctx;

    public PatientFhirHelper() {
        ctx = FhirContext.forDstu2();
        client = ctx.newRestfulGenericClient(DSTU2_URL);
    }

    public List<Patient> getPatients() {
        // Invoke the client
        Bundle bundle = client.search().forResource(Patient.class)
                .where(new TokenClientParam("gender").exactly().code("unknown"))
                .prettyPrint()
                .returnBundle(Bundle.class)
                .execute();
        return BundleUtil.toListOfResourcesOfType(ctx, bundle, Patient.class);
    }

    public IGenericClient getClient() {
        return client;
    }

}
