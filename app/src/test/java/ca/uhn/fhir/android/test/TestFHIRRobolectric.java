package ca.uhn.fhir.android.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.UUID;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.BundleUtil;

import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class TestFHIRRobolectric {

    @Test
    public void testHapiFHIRInitializationDSTU2() {
        PatientFhirHelper gcm = new PatientFhirHelper();

        Patient p = new Patient();
        p.setId("Patient/"+UUID.randomUUID().toString());
        p.setActive(true);
        p.addName().addFamily("HapiFhirAndroidTest").addGiven("Patient");
        Observation o = new Observation();
        o.setId("Observation/"+UUID.randomUUID().toString());
        o.addIdentifier().setValue(o.getId().getIdPart());
        o.setComments("HapiFhirAndroidTestObservation");
        o.setSubject(new ResourceReferenceDt(p));
        //Test transaction create
        gcm.getClient().update().resource(p).execute();
        gcm.getClient().update().resource(o).execute();
        //Test search
        Observation obs = getObservation(o.getId(), gcm.getClient());
        Assert.assertNotNull(obs);
        Assert.assertNotNull(obs.getSubject().getResource());
        Patient patient = ((Patient)obs.getSubject().getResource());
        Assert.assertEquals(p.getId().getIdPart(), patient.getId().getIdPart());
        Assert.assertEquals(o.getComments(), obs.getComments());
        Assert.assertEquals(o.getId().getIdPart(), obs.getId().getIdPart());
        //Test and use delete
        gcm.getClient().delete().resourceById(o.getId()).execute();
        gcm.getClient().delete().resourceById(p.getId()).execute();
        //Test the they are deleted.
        try {
            gcm.getClient().read().resource(Observation.class).withId(o.getId()).execute();
            fail();
        } catch (ResourceGoneException e) {
            // good
        }
    }

    private Observation getObservation(IdDt id, IGenericClient client) {
        Bundle bundle = client.search().forResource(Observation.class)
                .where(Observation.IDENTIFIER.exactly().identifier(id.getIdPart())).include(Observation.INCLUDE_PATIENT)
                .prettyPrint()
                .returnBundle(Bundle.class)
                .execute();
        FhirContext ctx = FhirContext.forDstu2();
        List<Observation> observations = BundleUtil.toListOfResourcesOfType(ctx, bundle, Observation.class);
        if (observations.isEmpty()) {
            return null;
        } else {
            return observations.iterator().next();
        }
    }
}

