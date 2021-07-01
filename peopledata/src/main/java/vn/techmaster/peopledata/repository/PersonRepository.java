package vn.techmaster.peopledata.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import vn.techmaster.peopledata.model.Person;

@Repository
public class PersonRepository {
  private List<Person> people = new ArrayList<Person>();

  public PersonRepository() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    File file;
    try {
      file = ResourceUtils.getFile("classpath:static/person.json");
      people.addAll(mapper.readValue(file, new TypeReference<List<Person>>() {
      }));
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Double maleFemaleRatio1(List<Person> people) {
    Long maleCount = people
            .stream()
            .filter(p -> p.getGender().equals("Male"))
            .collect(Collectors.counting());

    return (double) maleCount / (double) (people.size() - maleCount);
  }

  public List<Person> getAll() {
    return people;
  }

//  1: Trả về danh sách 'developer' ở các thành phố Hanoi, Saigon, Shanghai

  public Map<String, List<Person>> devhanoisaigoshanghai() {
    return people
            .stream()
            .filter(p -> p.getJob().equals("developer"))
            .filter(p -> p.getCity().equals("Hanoi") || p.getCity().equals("Saigon") || p.getCity().equals("Shanghai"))
            .collect(Collectors.groupingBy(Person::getCity));
  }

  //  2: Tìm ra thành phố có độ tuổi trung bình của lập trình viên trẻ nhất
  public List<Map.Entry<String, Double>> cityHasYoungDev() {
    return people
            .stream()
            .filter(p -> p.getJob().equals("developer"))
//            .collect(Collectors.groupingBy(Person::getCity, //nhóm người theo thành phố
//            Collectors.maxBy(Comparator.comparing(Person::getAge))))
            .collect(Collectors.groupingBy(Person::getCity, Collectors.averagingInt(Person::getAge)))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
            .limit(1)
            .collect(Collectors.toList());
  }

  //  3: Liệt kê tỷ lệ nam/nữ ở từng thành phố

  public Map<String, Double> maleFemaleByCity() {
    return people
            .stream()
            .collect(Collectors.groupingBy(Person::getCity))
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                    entry -> entry.getKey(),
                    entry -> maleFemaleRatio1(entry.getValue())
            ));
  }

//  4. Tính mức lương trung bình của tất cả những người trên 30 tuổi

  public Double averageSalaryByAge() {
    return people
            .stream()
            .filter(p-> p.getAge()>30)
            .collect(Collectors.averagingInt(Person::getSalary));
  }
}
