package pl.kryniek.shoppinglistcreator.model;

import lombok.Builder;
import lombok.Data;
import pl.kryniek.shoppinglistcreator.enums.ProductType;

@Data
@Builder
public class Product {
    private String name;
    private Integer quantity;
    private String measure;
    private ProductType type;
}
