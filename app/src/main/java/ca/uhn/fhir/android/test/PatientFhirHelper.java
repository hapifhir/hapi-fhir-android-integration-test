package ca.uhn.fhir.android.test;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.util.BundleUtil;

public class PatientFhirHelper {

    private IGenericClient client;
    private FhirContext ctx;

    public PatientFhirHelper() {
        ctx = FhirContext.forDstu2();
        client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");
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
