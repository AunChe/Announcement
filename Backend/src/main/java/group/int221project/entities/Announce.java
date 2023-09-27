package group.int221project.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import group.int221project.validators.ValidDisplay;
import group.int221project.validators.ValidPublishAndCloseDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "announces")
@ValidPublishAndCloseDate
@ValidDisplay
public class Announce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer announcementId;

    @NotBlank
    @Size(min = 1, max = 200)
    private String announcementTitle;

    @NotBlank
    @Size(min = 1, max = 10000)
    private String announcementDescription;

    private String publishDate;

    private String closeDate;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "categoryId")
    private Category category;

    @Column(columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String announcementDisplay = "N";

    @Column(insertable = false, updatable = false)
    private int categoryId;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer view;

}
