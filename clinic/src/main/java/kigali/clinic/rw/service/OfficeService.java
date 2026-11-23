package kigali.clinic.rw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.repository.OfficeRepository;

@Service 
public class OfficeService {

    @Autowired 
    private OfficeRepository offRepo;


    

    public String saveOffice(Office office){
        
         offRepo.save(office);
        return "Office is saved successfully";
    }

    public java.util.List<Office> getFreeOffices() {
        return offRepo.findFreeOffices();
    }

    public java.util.List<Office> getAllOffices() {

        return offRepo.findAll();
    }

    public java.util.Optional<Office> getOfficeById(java.util.UUID id) {

        return offRepo.findById(id);
    }

    public String updateOffice(java.util.UUID id, Office officeDetails) {

        java.util.Optional<Office> existingOffice = offRepo.findById(id);

        if (existingOffice.isEmpty()) {
            return "Office with id " + id + " not found";
        }

        Office office = existingOffice.get();
        office.setName(officeDetails.getName());
        office.setOfficeNumber(officeDetails.getOfficeNumber());

        offRepo.save(office);

        return "Office is updated successfully";
    }

    public String deleteOffice(java.util.UUID id) {

        if (!offRepo.existsById(id)) {
            return "Office with id " + id + " not found";
        }

        offRepo.deleteById(id);

        return "Office is deleted successfully";
    }
}
