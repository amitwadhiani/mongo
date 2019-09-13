package co.arctern.api.provider.service;

import co.arctern.api.internal.api.emr.api.PatientEntityApi;
import co.arctern.api.internal.api.emr.model.ResourcesPatient;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DiagnosticOrderService {

    public final PatientEntityApi patientEntityApi;


    public DiagnosticOrderService(PatientEntityApi patientEntityApi) {
        this.patientEntityApi = patientEntityApi;
    }

    public  ResourcesPatient searchPatient(String phone, String name, Pageable pageable) {
        return patientEntityApi.findDistinctByNameContainingOrPhoneContainingPatientUsingGET(name, pageable.getPageNumber(), phone, pageable.getPageSize(), pageable.getSort().toString());
    }
}
