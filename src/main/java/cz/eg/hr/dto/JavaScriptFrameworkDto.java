package cz.eg.hr.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JavaScriptFrameworkDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Version is required")
    private String version;

    @NotNull(message = "End of support date is required")
    private LocalDate endOfSupportDate;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private BigDecimal rating;
}
