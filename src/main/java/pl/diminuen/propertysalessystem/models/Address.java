package pl.diminuen.propertysalessystem.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "real_estates_addresses")
@NoArgsConstructor
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String place;
    private String street;
    private String voivodeship;
    private String county;
    private String buildingNumber;
    private String apartmentNumber;

    @JsonCreator
    public Address(String place, String street, String voivodeship, String county, String buildingNumber, String apartmentNumber) {
        this.place = place;
        this.street = street;
        this.voivodeship = voivodeship;
        this.county = county;
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
    }
}
