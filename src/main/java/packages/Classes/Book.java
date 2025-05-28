package packages.Classes;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    private String imagePath;
    private String title;
    private String author;
    private String publisher;
    private int book_id;
    private int stock;
    private int total_rent_count;
}

