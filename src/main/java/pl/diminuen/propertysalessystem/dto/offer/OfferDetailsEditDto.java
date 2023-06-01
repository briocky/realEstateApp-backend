package pl.diminuen.propertysalessystem.dto.offer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.diminuen.propertysalessystem.models.ERealEstateType;
import pl.diminuen.propertysalessystem.models.Offer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferDetailsEditDto {
    private Long id;
    private String title;
    private String place;
    private String street;
    private String voivodeship;
    private String county;
    private String buildingNumber;
    private String apartmentNumber;
    private Double price;
    private Double area;
    private Integer roomCount;
    private String description;
    private ERealEstateType realEstateType;

    public static OfferDetailsEditDto build(Offer offer) {
        OfferDetailsEditDto offerDetails = new OfferDetailsEditDto();
        offerDetails.setId(offer.getId());
        offerDetails.setTitle(offer.getTitle());
        offerDetails.setPlace(offer.getRealEstate().getAddress().getPlace());
        offerDetails.setStreet(offer.getRealEstate().getAddress().getStreet());
        offerDetails.setVoivodeship(offer.getRealEstate().getAddress().getVoivodeship());
        offerDetails.setCounty(offer.getRealEstate().getAddress().getCounty());
        offerDetails.setBuildingNumber(offer.getRealEstate().getAddress().getBuildingNumber());
        offerDetails.setApartmentNumber(offer.getRealEstate().getAddress().getApartmentNumber());
        offerDetails.setDescription(offer.getRealEstate().getDescription());
        offerDetails.setPrice(offer.getPrice());
        offerDetails.setArea(offer.getRealEstate().getArea());
        offerDetails.setRoomCount(offer.getRealEstate().getRoomCount());
        offerDetails.setRealEstateType(offer.getRealEstate().getRealEstateType());

        return offerDetails;
    }
}
