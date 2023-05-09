package pl.diminuen.propertysalessystem.controllers;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.diminuen.propertysalessystem.dto.AddOfferDto;
import pl.diminuen.propertysalessystem.dto.SearchOffersDto;
import pl.diminuen.propertysalessystem.dto.SearchOffersResponse;
import pl.diminuen.propertysalessystem.security.SecurityUser;
import pl.diminuen.propertysalessystem.services.OfferService;


@RestController
@RequestMapping("/api/v1/offer")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;

    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> addOffer(@AuthenticationPrincipal SecurityUser securityUser,
                                      @RequestPart("offerData") AddOfferDto offerDto,
                                      @Nullable @RequestPart("picture") MultipartFile[] pictures) {
        offerService.addOffer(offerDto, pictures, securityUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/search")
    public ResponseEntity<?> searchOffers(@AuthenticationPrincipal SecurityUser securityUser,
                                          @RequestBody SearchOffersDto searchCriteria,
                                          @RequestParam("page") int pageNumber,
                                          @RequestParam("pageSize") int pageSize) {
        SearchOffersResponse offers = offerService.searchOffers(searchCriteria, securityUser, pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(offers);
    }

}
