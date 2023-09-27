package group.int221project.dtos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnounceDto {
    private Integer id;
    private String announcementTitle;
    private String publishDate;
    private String closeDate;
    private String announcementDisplay;
    @JsonIgnore
    private CategoryDto category;

    public String getAnnouncementCategory() {
        return category.getCategoryName();
    }

}
