package uz.pdp.task2.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPropertiesDto {
    private String key;
    private String value;
    private Integer productId;
}
