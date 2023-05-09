package pl.diminuen.propertysalessystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "offers")
@NoArgsConstructor
@Getter
@Setter
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Size(min = 5, max = 100)
    private String title;
    @Enumerated(EnumType.STRING)
    private EOfferType offerType;
    private Boolean isPaid = true;
    @Enumerated(EnumType.STRING)
    private EOfferStatus offerStatus;
    private LocalDateTime submissionDate;
    private LocalDateTime expirationDate;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User user;
    @NotNull
    private Double price;
    private Integer viewsCount;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "real_estate_id")
    private RealEstate realEstate;

    public Offer(String title, EOfferType offerType) {
        this.title = title;
        this.offerType = offerType;
    }

    public Offer(String title, EOfferType offerType, Boolean isPaid, EOfferStatus offerStatus, LocalDateTime submissionDate, LocalDateTime expirationDate, User user, Double price, Integer viewsCount, RealEstate realEstate) {
        this.title = title;
        this.offerType = offerType;
        this.isPaid = isPaid;
        this.offerStatus = offerStatus;
        this.submissionDate = submissionDate;
        this.expirationDate = expirationDate;
        this.user = user;
        this.price = price;
        this.viewsCount = viewsCount;
        this.realEstate = realEstate;
    }
}
