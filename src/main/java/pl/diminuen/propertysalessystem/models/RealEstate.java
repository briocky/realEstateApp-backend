package pl.diminuen.propertysalessystem.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "real_estates")
@NoArgsConstructor
@Getter
@Setter
public class RealEstate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 3000)
    private String description;
    private Double area;
    private Integer roomCount;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
    @Enumerated(EnumType.STRING)
    private ERealEstateType realEstateType;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "real_estate_id")
    private List<Image> images;

    @JsonCreator
    public RealEstate(String description, Double area, Integer roomCount, Address address, ERealEstateType realEstateType) {
        this.description = description;
        this.area = area;
        this.roomCount = roomCount;
        this.address = address;
        this.realEstateType = realEstateType;
    }
}
