package pl.diminuen.propertysalessystem.dto;

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
}
