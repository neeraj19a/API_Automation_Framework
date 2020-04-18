package pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Posts {
    private String id;
    private String title;
    private String author;

    public Posts(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;

    }

    public Posts() {

    }
}
