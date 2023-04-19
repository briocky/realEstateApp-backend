package pl.diminuen.propertysalessystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "real_estates_images")
@NoArgsConstructor
@Getter
@Setter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @Column(length = 300000)
    private byte[] pictureBytes;

    public Image(String name, String type, byte[] pictureBytes) {
        this.name = name;
        this.type = type;
        this.pictureBytes = pictureBytes;
    }
}
