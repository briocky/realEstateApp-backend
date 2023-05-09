package pl.diminuen.propertysalessystem.repositories.specifications;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import pl.diminuen.propertysalessystem.models.EOfferType;
import pl.diminuen.propertysalessystem.models.ERealEstateType;
import pl.diminuen.propertysalessystem.models.Offer;
import pl.diminuen.propertysalessystem.models.RealEstate;

public abstract class OfferSpecifications {
    public static Specification<Offer> hasOfferType(EOfferType offerType) {
        return (root, query, criteriaBuilder) -> {
            if(offerType != null)
                return criteriaBuilder.equal(root.get("offerType"), offerType);
            return null;
        };
    }

    public static Specification<Offer> hasRealEstateType(ERealEstateType realEstateType) {
        return (root, query, criteriaBuilder) -> {
            if(realEstateType != null) {
                Join<RealEstate, Offer> offerRealEstate = root.join("realEstate");
                return criteriaBuilder.equal(offerRealEstate.get("realEstateType"), realEstateType);
            }
            return null;
        };
    }

    public static Specification<Offer> hasPlace(String place) {
        return (root, query, criteriaBuilder) -> {
            if(place != null) {
                Join<RealEstate, Offer> offerAddress = root.join("realEstate").join("address");
                return criteriaBuilder.equal(offerAddress.get("place"), place);
            }
            return null;
        };
    }

    public static Specification<Offer> hasRoomCount(Integer roomCount) {
        return (root, query, criteriaBuilder) -> {
            if(roomCount != null) {
                Join<RealEstate, Offer> offerRealEstate = root.join("realEstate");
                return criteriaBuilder.equal(offerRealEstate.get("roomCount"), roomCount);
            }
            return null;
        };
    }

    public static Specification<Offer> hasPriceMax(Double priceMax) {
        return (root, query, criteriaBuilder) -> {
            if(priceMax != null)
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceMax);
            return null;
        };
    }

    public static Specification<Offer> hasPriceMin(Double priceMin) {
        return (root, query, criteriaBuilder) -> {
            if(priceMin != null)
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceMin);
            return null;
        };
    }

    public static Specification<Offer> hasAreaMax(Double areaMax) {
        return (root, query, criteriaBuilder) -> {
            if(areaMax != null) {
                Join<RealEstate, Offer> offerRealEstate = root.join("realEstate");
                return criteriaBuilder.lessThanOrEqualTo(offerRealEstate.get("area"), areaMax);
            }
            return null;
        };
    }

    public static Specification<Offer> hasAreaMin(Double areaMin) {
        return (root, query, criteriaBuilder) -> {
            if(areaMin != null) {
                Join<RealEstate, Offer> offerRealEstate = root.join("realEstate");
                return criteriaBuilder.greaterThanOrEqualTo(offerRealEstate.get("area"), areaMin);
            }
            return null;
        };
    }
}
