package pl.diminuen.propertysalessystem.dto.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.diminuen.propertysalessystem.models.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class OfferDto {
    private Long id;
    private String title;
    private EOfferType offerType;
    private EOfferStatus offerStatus;
    private LocalDateTime submissionDate;
    private LocalDateTime expirationDate;
    private Double price;
    private Integer viewsCount;
    private Double area;
    private ERealEstateType realEstateType;
    private String place;
    private Image image;
    private Integer roomCount;

    public static OfferDto build(Offer offer, Image offerMainImage) {
        return OfferDto.builder()
                .id(offer.getId())
                .offerStatus(offer.getOfferStatus())
                .offerType(offer.getOfferType())
                .title(offer.getTitle())
                .price(offer.getPrice())
                .expirationDate(offer.getExpirationDate())
                .submissionDate(offer.getSubmissionDate())
                .area(offer.getRealEstate().getArea())
                .realEstateType(offer.getRealEstate().getRealEstateType())
                .place(offer.getRealEstate().getAddress().getPlace())
                .image(offerMainImage)
                .roomCount(offer.getRealEstate().getRoomCount())
                .viewsCount(offer.getViewsCount())
                .build();
    }
}
