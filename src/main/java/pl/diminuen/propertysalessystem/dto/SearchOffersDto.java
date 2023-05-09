package pl.diminuen.propertysalessystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.diminuen.propertysalessystem.models.EOfferType;
import pl.diminuen.propertysalessystem.models.ERealEstateType;

@AllArgsConstructor
@Getter
@Setter
public class SearchOffersDto {
    private ERealEstateType realEstateType;
    private EOfferType offerType;
    private Integer roomCount;
    private String place;
    private Double priceMin;
    private Double priceMax;
    private Double areaMin;
    private Double areaMax;
}
