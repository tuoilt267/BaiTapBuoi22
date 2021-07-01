package vn.techmaster.peopledata.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.techmaster.peopledata.model.Person;
import vn.techmaster.peopledata.repository.PersonRepository;

@RestController
@RequestMapping("/")
public class PeopleController {
  @Autowired private PersonRepository personRepo;


  @GetMapping("devhanoisaigoshanghai")
  public ResponseEntity<Map<String, List<Person>>> devhanoisaigoshanghai() {
    return ResponseEntity.ok().body(personRepo.devhanoisaigoshanghai());
  }

  @GetMapping("youngestdevs")
  public ResponseEntity<List<Map.Entry<String,Double>>> cityHasYoungDev() {
    return ResponseEntity.ok().body(personRepo.cityHasYoungDev());
  }

  @GetMapping("malefemaleratio")
  public ResponseEntity <Map<String, Double>> maleFemaleByCity () {
    return ResponseEntity.ok().body(personRepo.maleFemaleByCity());
  }

  @GetMapping("avgsalarypeopleabove30")
  public ResponseEntity <Double> averageSalaryByAge() {
    return ResponseEntity.ok().body(personRepo.averageSalaryByAge());
  }
}
