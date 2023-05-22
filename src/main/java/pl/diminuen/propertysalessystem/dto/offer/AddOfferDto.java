package pl.diminuen.propertysalessystem.dto.offer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.diminuen.propertysalessystem.models.EOfferType;
import pl.diminuen.propertysalessystem.models.RealEstate;

@AllArgsConstructor
@Getter
public class AddOfferDto {
    private String offerTitle;
    private EOfferType offerType;
    private Double offerPrice;
    private RealEstate realEstate;
}
