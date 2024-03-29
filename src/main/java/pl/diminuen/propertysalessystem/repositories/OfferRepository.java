package pl.diminuen.propertysalessystem.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.diminuen.propertysalessystem.models.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {
    Page<Offer> findAllByUserId(Long id, Pageable pageable);
}
