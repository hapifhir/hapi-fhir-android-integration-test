package ca.uhn.fhir.android.test;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.TokenClientParam;

public class PatientFhirHelper {

    private IGenericClient client;

    public PatientFhirHelper() {
        FhirContext ctx = FhirContext.forDstu2();
        client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");
    }

    public List<Patient> getPatients() {
        // Invoke the client
        Bundle bundle = client.search().forResource(Patient.class)
                .where(new TokenClientParam("gender").exactly().code("unknown"))
                .prettyPrint()
                .execute();
        return bundle.getResources(Patient.class);
    }

    public IGenericClient getClient() {
        return client;
    }
}
