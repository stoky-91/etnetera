package cz.eg.hr.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
public class JavaScriptFramework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Version is required")
    private String version;

    @Column(nullable = false)
    @NotNull(message = "End of support date is required")
    private LocalDate endOfSupportDate;

    @Column
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private BigDecimal rating;

    public JavaScriptFramework() {
    }

    @Override
    public String toString() {
        return "JavaScriptFramework{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", version='" + version + '\'' +
            ", endOfSupportDate=" + endOfSupportDate +
            ", rating=" + rating +
            '}';
    }
}
