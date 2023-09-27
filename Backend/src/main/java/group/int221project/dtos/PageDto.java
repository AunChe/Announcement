package group.int221project.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class PageDto<T> {
        private List<T> content;
        private Boolean last;
        private Boolean first;
        private Integer totalPages;
        private Integer totalElements;
        private Integer size;
        private Integer page;

        @JsonIgnore
        private Integer number;

    public Integer getNumber() {
            return number;
        }
    }
