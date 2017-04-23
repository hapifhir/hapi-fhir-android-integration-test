package ca.uhn.fhir.android.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import ca.uhn.fhir.android.data.network.FhirNetworkHelper;
import ca.uhn.fhir.android.data.network.ServersEnum;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class TestFHIRRobolectric {

    @Test
    public void testHapiFHIRInitializationDSTU2() {

        String serverUrl = ServersEnum.UHN_FHIRTEST.getUrl();

        Patient createdPatient = createPatient();
        MethodOutcome uploadPatientOutcome = FhirNetworkHelper.uploadFhirObject(createdPatient, serverUrl);
        createdPatient.setId(uploadPatientOutcome.getId());

        Observation createdObservation = createObservation(createdPatient);
        MethodOutcome uploadObservationOutcome = FhirNetworkHelper.uploadFhirObject(createdObservation, serverUrl);
        createdObservation.setId(uploadObservationOutcome.getId());

        //Test search
        Observation obs = (Observation) FhirNetworkHelper.downloadSingleResource(Observation.class, createdObservation.getId().getIdPart(), serverUrl);
        Assert.assertNotNull(obs);
        Assert.assertNotNull(obs.getSubject().getReference());

        Patient patient = (Patient) FhirNetworkHelper.downloadSingleResource(Patient.class, createdPatient.getId().getIdPart(), serverUrl);
        Assert.assertEquals(createdPatient.getId().getIdPart(), patient.getId().getIdPart());
        Assert.assertEquals(createdObservation.getComments(), obs.getComments());
        Assert.assertEquals(createdObservation.getId().getIdPart(), obs.getId().getIdPart());

        //Test and use delete
        FhirNetworkHelper.deleteFhirObject(createdObservation, serverUrl);
        FhirNetworkHelper.deleteFhirObject(createdPatient, serverUrl);

        //Test the they are deleted.
        Assert.assertNull(FhirNetworkHelper.downloadSingleResource(Observation.class, createdObservation.getId().getIdPart(), serverUrl));
    }

    private Patient createPatient() {

        Patient patient = new Patient();
        patient.setActive(true);
        patient.addName().addFamily("HapiFhirAndroidTest").addGiven("Patient");

        return patient;
    }

    private Observation createObservation(Patient referencePatient) {

        Observation createdObservation = new Observation();
        createdObservation.setComments("HapiFhirAndroidTestObservation");

        if (referencePatient != null) {
            createdObservation.setSubject(new ResourceReferenceDt(referencePatient));
        }

        return createdObservation;
    }

    //TODO nuke this
    private Observation getObservation(IdDt id, IGenericClient client) {
        Bundle bundle = client.search().forResource(Observation.class)
                .where(Observation.IDENTIFIER.exactly().identifier(id.getIdPart())).include(Observation.INCLUDE_PATIENT)
                .prettyPrint()
                .execute();
        List<Observation> observations = bundle.getResources(Observation.class);
        if (observations.isEmpty()) {
            return null;
        } else {
            return observations.iterator().next();
        }
    }
}

