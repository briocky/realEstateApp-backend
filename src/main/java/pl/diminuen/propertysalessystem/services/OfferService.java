package pl.diminuen.propertysalessystem.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.diminuen.propertysalessystem.dto.offer.OfferDetailsEditDto;
import pl.diminuen.propertysalessystem.dto.offer.*;
import pl.diminuen.propertysalessystem.exceptions.AddOfferException;
import pl.diminuen.propertysalessystem.exceptions.OfferDeletionException;
import pl.diminuen.propertysalessystem.exceptions.OfferNotFoundException;
import pl.diminuen.propertysalessystem.models.*;
import pl.diminuen.propertysalessystem.repositories.*;
import pl.diminuen.propertysalessystem.security.SecurityUser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static pl.diminuen.propertysalessystem.repositories.specifications.OfferSpecifications.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferService {
    @Value("${app.offer.active.duration}")
    private long offerActiveDuration;
    private final int initalViewsCount = 0;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    public void addOffer(AddOfferDto offerDto, MultipartFile[] images, SecurityUser securityUser) {
        User user = userRepository.findByEmail(securityUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + securityUser.getUsername() + " cannot be found!"));
        boolean isPaid = true; //TODO: future feature - offer's fee
        List<Image> preparedImages = prepareImages(images);
        offerDto.getRealEstate().setImages(preparedImages);
        Offer offer = new Offer(
                offerDto.getOfferTitle(),
                offerDto.getOfferType(),
                isPaid,
                EOfferStatus.ACTIVE,
                LocalDateTime.now(ZoneId.systemDefault()),
                LocalDateTime.now(ZoneId.systemDefault()).plusDays(offerActiveDuration),
                user,
                offerDto.getOfferPrice(),
                initalViewsCount,
                offerDto.getRealEstate()
        );

        offerRepository.save(offer);
    }

    private List<Image> prepareImages(MultipartFile[] requestImages) {
        final Set<String> allowedContentTypes = new HashSet<>(Arrays.asList("image/png", "image/jpeg"));
        List<Image> images = new ArrayList<>();
        Image img;

        if(requestImages != null) {
            for(MultipartFile image:requestImages) {
                if(!allowedContentTypes.contains(image.getContentType())){
                    throw new AddOfferException("Image type: " + image.getContentType() + " is unsupported!");
                }
                try {
                    img = new Image(
                            image.getOriginalFilename(),
                            image.getContentType(),
                            image.getBytes()
                    );
                } catch (IOException e) {
                    throw new AddOfferException("A problem with storing a file has occured!");
                }
                images.add(img);
            }
        }

        return images;
    }

    public SearchOffersResponse searchOffers(SearchOffersDto searchCriteria, SecurityUser securityUser,
                                             int pageNumber, int pageSize) {
        if(securityUser != null) {
            User user = userRepository.findByEmail(securityUser.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User with email: " + securityUser.getUsername() + " cannot be found!"));
        }

        Pageable pageData = PageRequest.of(pageNumber,pageSize, Sort.by("submissionDate").descending());

        Page<Offer> offers = offerRepository.findAll(
                hasOfferType(searchCriteria.getOfferType())
                        .and(hasRealEstateType(searchCriteria.getRealEstateType()))
                        .and(hasPlace(searchCriteria.getPlace()))
                        .and(hasPriceMin(searchCriteria.getPriceMin()))
                        .and(hasPriceMax(searchCriteria.getPriceMax()))
                        .and(hasAreaMax(searchCriteria.getAreaMax()))
                        .and(hasAreaMin(searchCriteria.getAreaMin()))
                        .and(hasRoomCount(searchCriteria.getRoomCount())),pageData
        );

        int totalPages = offers.getTotalPages();
        List<OfferDto> offersDto = mapOffersToOfferDto(offers.getContent());

        return new SearchOffersResponse(totalPages, offersDto);
    }

    private List<OfferDto> mapOffersToOfferDto(List<Offer> offers) {
        return offers.stream().map((offer) -> {
            Image offerMainImage = offer.getRealEstate().getImages().size() != 0 ?
                    offer.getRealEstate().getImages().get(0) : null;

            return OfferDto.build(offer, offerMainImage);
        }).toList();
    }

    public OfferDetailsResponse getOfferDetails(long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found with id=" + offerId));

        return OfferDetailsResponse.build(offer);
    }

    public SearchOffersResponse getAllMyOffers(SecurityUser securityUser, int pageNumber, int pageSize) {
        Pageable pageData = PageRequest.of(pageNumber,pageSize, Sort.by("submissionDate").descending());
        Page<Offer> offers = offerRepository.findAllByUserId(securityUser.getId(),pageData);

        return new SearchOffersResponse(offers.getTotalPages(), mapOffersToOfferDto(offers.getContent()));
    }

    public void deleteOffer(SecurityUser securityUser, long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException(
                        "The offer cannot be found with id " + id
                ));
        if(offer.getUser().getId().equals(securityUser.getId())) {
            offerRepository.deleteById(id);
        } else {
            log.info("An attempt to delete someone else's offer has been detected!");
            throw new OfferDeletionException("Offer delete operation not permitted!");
        }
    }

    public OfferDetailsEditDto getOfferDetailsForEdit(long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found with id=" + offerId));

        return OfferDetailsEditDto.build(offer);
    }

    public void updateOfferDetails(long offerId, OfferDetailsEditDto detailsEditDto) {
        log.info("Update offer details with id={} has started!", offerId);
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found with id=" + offerId));

        RealEstate realEstate = offer.getRealEstate();
        Address address = realEstate.getAddress();

        address.setPlace(detailsEditDto.getPlace());
        address.setStreet(detailsEditDto.getStreet());
        address.setVoivodeship(detailsEditDto.getVoivodeship());
        address.setCounty(detailsEditDto.getCounty());
        address.setBuildingNumber(detailsEditDto.getBuildingNumber());
        address.setApartmentNumber(detailsEditDto.getApartmentNumber());

        realEstate.setAddress(address);
        realEstate.setArea(detailsEditDto.getArea());
        realEstate.setRoomCount(detailsEditDto.getRoomCount());
        realEstate.setDescription(detailsEditDto.getDescription());

        offer.setTitle(detailsEditDto.getTitle());
        offer.setRealEstate(realEstate);
        offer.setPrice(detailsEditDto.getPrice());

        offerRepository.save(offer);
        log.info("Offer details with id={} has finished!", offerId);
    }
}
