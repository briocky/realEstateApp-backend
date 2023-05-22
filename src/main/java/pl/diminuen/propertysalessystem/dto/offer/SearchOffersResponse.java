package pl.diminuen.propertysalessystem.dto.offer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class SearchOffersResponse {
    private int totalPages;
    private List<OfferDto> offers;
}
