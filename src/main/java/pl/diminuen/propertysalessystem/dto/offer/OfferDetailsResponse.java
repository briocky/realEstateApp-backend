package pl.diminuen.propertysalessystem.dto.offer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.diminuen.propertysalessystem.models.EOfferType;
import pl.diminuen.propertysalessystem.models.Offer;
import pl.diminuen.propertysalessystem.models.RealEstate;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class OfferDetailsResponse {
    private Long id;
    private String title;
    private EOfferType offerType;
    private Double price;
    private Integer viewsCount;
    private LocalDateTime submissionDate;
    private OfferOwnerDto owner;
    private RealEstate realEstate;

    public static OfferDetailsResponse build(Offer offer) {
        return new OfferDetailsResponse(
                offer.getId(),
                offer.getTitle(),
                offer.getOfferType(),
                offer.getPrice(),
                offer.getViewsCount(),
                offer.getSubmissionDate(),
                OfferOwnerDto.build(offer.getUser()),
                offer.getRealEstate()
        );
    }
}
