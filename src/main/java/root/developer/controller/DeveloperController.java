package root.developer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import root.developer.exception.DeveloperNotFoundException;
import root.developer.model.Developer;
import root.developer.service.DeveloperService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeveloperController {

    private final DeveloperService developerService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/developer/save", method = RequestMethod.POST)
    public ResponseEntity<?> save(@Valid @RequestBody Developer developer){
        Map<Validation,Developer> map = developerService.save(developer);
        final long count = map.entrySet().stream().count();
        Validation message = map.entrySet().stream().skip(count-1).findFirst().get().getKey();
        Developer developer1 = map.entrySet().stream().skip(count-1).findFirst().get().getValue();
        if(message == Validation.IDBUSY){
            return new ResponseEntity<>(
                    "Id already in use:  " + developer1.getId(),
                    HttpStatus.BAD_REQUEST);

        }else if(message == Validation.EMAILBUSYORINVALID){
            return new ResponseEntity<>(
                    "Cant save new developer with this email because it is busy or check valid email(dog@gmail.com):     " + developer1.getEmail(),
                    HttpStatus.BAD_REQUEST);
        }else if(message == Validation.NAMEINVALID){
            return new ResponseEntity<>(
                    "Cant save new developer with this name because name's length should be 50<name>2 or check valid name it should start with alphabet:   " + developer1.getName(),
                    HttpStatus.BAD_REQUEST);
        }  else
            return ResponseEntity.ok(developer1);
    }

    @RequestMapping(value = "/developer/findAll",method = RequestMethod.GET)
    public List<Developer> findAll(){
        return developerService.findAll();
    }

    @RequestMapping(value = "/developer/find/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable("id") Long id) throws DeveloperNotFoundException {
        Developer developer = developerService.findById(id);
        if (developer == null) {
            throw  new DeveloperNotFoundException("Developer must not be null");
        } else {
            return new ResponseEntity<>(developer, HttpStatus.OK);
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/developer/update", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@Valid @RequestBody Developer developer){
        Map<Validation,Developer> map = developerService.update(developer);
        final long count = map.entrySet().stream().count();
        Validation message = map.entrySet().stream().skip(count-1).findFirst().get().getKey();
        Developer developer1 = map.entrySet().stream().skip(count-1).findFirst().get().getValue();
        if(message == Validation.IDNULL){
            return new ResponseEntity<>(
                    "Id is null!!:  " + developer1.getId() + " Please write id!!",
                    HttpStatus.BAD_REQUEST);

        }else if(message == Validation.EMAILBUSYORINVALID){
            return new ResponseEntity<>(
                    "Cant save new developer with this email because it is busy or check valid email(dog@gmail.com):     " + developer1.getEmail(),
                    HttpStatus.BAD_REQUEST);
        }else if(message == Validation.NAMEINVALID){
            return new ResponseEntity<>(
                    "Cant save new developer with this name because name's length should be 50<name>2 or check valid name it should start with alphabet:   " + developer1.getName(),
                    HttpStatus.BAD_REQUEST);
        }  else
            return ResponseEntity.ok(developer1);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/developer/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDeveloper(@PathVariable Long id) {
        if(developerService.delete(id) == Validation.IDNULL){
            return new ResponseEntity<>(
                    "Id is null!!:  " + id + " Please write id!!",
                    HttpStatus.BAD_REQUEST);
        }else  return ResponseEntity.ok("Developer deleted!!");
    }
}
