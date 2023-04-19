package pl.diminuen.propertysalessystem.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.diminuen.propertysalessystem.dto.AddOfferDto;
import pl.diminuen.propertysalessystem.exceptions.AddOfferException;
import pl.diminuen.propertysalessystem.models.*;
import pl.diminuen.propertysalessystem.repositories.*;
import pl.diminuen.propertysalessystem.security.SecurityUser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
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
        Set<Image> preparedImages = prepareImages(images);
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

    private Set<Image> prepareImages(MultipartFile[] requestImages) {
        final Set<String> allowedContentTypes = new HashSet<>(Arrays.asList("image/png", "image/jpeg"));
        Set<Image> images = new HashSet<>();
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
}
