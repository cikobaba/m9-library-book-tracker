
package bootcamp.hibernate_practical.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateBookRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Genre is required")
    private String genre;

    @Min(value = 1, message = "Publication year must be positive")
    @Max(value = 2100, message = "Publication year must not exceed 2100")
    private int publicationYear;
}
