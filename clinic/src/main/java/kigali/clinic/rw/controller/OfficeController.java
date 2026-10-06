package kigali.clinic.rw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.service.OfficeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping (value="/api/office")
public class OfficeController {

    @Autowired 
    private OfficeService offServe;
    



    @PostMapping(value = "/save")  
    public ResponseEntity<?> saveOffice(@RequestBody Office office){
        
       String returnedMessage =  offServe.saveOffice(office);

       if(returnedMessage.equals("Office is saved successfully")){
        return new ResponseEntity<>(returnedMessage,HttpStatus.OK);
       }else{
        return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
       }

    }

    @org.springframework.web.bind.annotation.GetMapping(value = "/all")
    public ResponseEntity<?> getAllOffices() {

        return new ResponseEntity<>(offServe.getAllOffices(), HttpStatus.OK);
    }

    @org.springframework.web.bind.annotation.GetMapping(value = "/{id}")
    public ResponseEntity<?> getOfficeById(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id) {

        java.util.Optional<Office> office = offServe.getOfficeById(id);

        if (office.isPresent()) {
            return new ResponseEntity<>(office.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Office with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @org.springframework.web.bind.annotation.PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateOffice(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id, @RequestBody Office office) {

        String returnedMessage = offServe.updateOffice(id, office);

        if (returnedMessage.equals("Office is updated successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    @org.springframework.web.bind.annotation.DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteOffice(@org.springframework.web.bind.annotation.PathVariable java.util.UUID id) {

        String returnedMessage = offServe.deleteOffice(id);

        if (returnedMessage.equals("Office is deleted successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }
    
}
