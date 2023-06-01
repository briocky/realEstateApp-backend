package pl.diminuen.propertysalessystem.controllers;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.diminuen.propertysalessystem.dto.offer.*;
import pl.diminuen.propertysalessystem.exceptions.OfferNotFoundException;
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
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/search")
    public ResponseEntity<?> searchOffers(@AuthenticationPrincipal SecurityUser securityUser,
                                          @RequestBody SearchOffersDto searchCriteria,
                                          @RequestParam("page") int pageNumber,
                                          @RequestParam("pageSize") int pageSize) {
        SearchOffersResponse offers = offerService.searchOffers(searchCriteria, securityUser, pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(offers);
    }

    @GetMapping(value = "/{offerId}")
    public ResponseEntity<?> getOfferDetails(@PathVariable long offerId) {
        return ResponseEntity.ok(offerService.getOfferDetails(offerId));
    }

    @GetMapping(value = "/my")
    public ResponseEntity<SearchOffersResponse> getAllMyOffers(@AuthenticationPrincipal SecurityUser securityUser,
                                                         @RequestParam("page") int pageNumber,
                                                         @RequestParam("pageSize") int pageSize ) {
        return ResponseEntity.ok(offerService.getAllMyOffers(securityUser, pageNumber, pageSize));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteOffer(@AuthenticationPrincipal SecurityUser securityUser,
                                         @PathVariable long id) {
        offerService.deleteOffer(securityUser, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/toEdit/{id}")
    public ResponseEntity<?> getOfferDetailsForEdit(@PathVariable(name = "id") long offerId) {
        return ResponseEntity.ok(offerService.getOfferDetailsForEdit(offerId));
    }

    @PutMapping(value = "/edit/{id}")
    public ResponseEntity<?> updateOfferDetails(@PathVariable long id,
                                                @RequestBody OfferDetailsEditDto detailsEditDto) {
        offerService.updateOfferDetails(id, detailsEditDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Offer updated successfully!");
    }

    @ExceptionHandler(value = {OfferNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleOfferNotFoundException(OfferNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
