package group.int221project.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailDto {
    private Integer id;
    private String announcementTitle;
    private String announcementDescription;
    private String publishDate;
    private String closeDate;
    private String announcementDisplay;
    @JsonIgnore
    private CategoryDto category;
    private Integer view;

    public String getAnnouncementCategory() {
        return category.getCategoryName();
    }

    public void setAnnouncementCategory(String announcementCategory) {
        this.category = new CategoryDto();
        this.category.setCategoryName(announcementCategory);
    }
}



