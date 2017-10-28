package ca.uhn.fhir.android.test;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.UUID;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.BundleUtil;

import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class TestFHIRRobolectric {

    @Test
    public void testHapiFHIRInitializationR4() {
        PatientFhirHelper gcm = new PatientFhirHelper();

        Patient p = new Patient();
        p.setId("Patient/"+UUID.randomUUID().toString());
        p.setActive(true);
        p.addName().setFamily("HapiFhirAndroidTest").addGiven("Patient");
        Observation o = new Observation();
        o.setId("Observation/"+UUID.randomUUID().toString());
        o.addIdentifier().setValue(o.getIdElement().getIdPart());
        o.setComment("HapiFhirAndroidTestObservation");
        o.setSubject(new Reference(p));
        //Test transaction create
        gcm.getClient().update().resource(p).execute();
        gcm.getClient().update().resource(o).execute();
        //Test search
        Observation obs = getObservation(o.getIdElement(), gcm.getClient());
        Assert.assertNotNull(obs);
        Assert.assertNotNull(obs.getSubject().getResource());
        Patient patient = ((Patient)obs.getSubject().getResource());
        Assert.assertEquals(p.getIdElement().getIdPart(), patient.getIdElement().getIdPart());
        Assert.assertEquals(o.getComment(), obs.getComment());
        Assert.assertEquals(o.getIdElement().getIdPart(), obs.getIdElement().getIdPart());
        //Test and use delete
        gcm.getClient().delete().resourceById(o.getIdElement()).execute();
        gcm.getClient().delete().resourceById(p.getIdElement()).execute();
        //Test the they are deleted.
        try {
            gcm.getClient().read().resource(Observation.class).withId(o.getId()).execute();
            fail();
        } catch (ResourceGoneException e) {
            // good
        }
    }

    private Observation getObservation(IdType id, IGenericClient client) {
        Bundle bundle = client.search().forResource(Observation.class)
                .where(Observation.IDENTIFIER.exactly().identifier(id.getIdPart())).include(Observation.INCLUDE_PATIENT)
                .prettyPrint()
                .returnBundle(Bundle.class)
                .execute();
        FhirContext ctx = FhirContext.forR4();
        List<Observation> observations = BundleUtil.toListOfResourcesOfType(ctx, bundle, Observation.class);
        if (observations.isEmpty()) {
            return null;
        } else {
            return observations.iterator().next();
        }
    }
}

