package pojo;

import lombok.Data;

import java.util.List;

@Data
public class Location {

    private Integer id;
    private String city;
    private String country;
    private List<Address> address = null;


}