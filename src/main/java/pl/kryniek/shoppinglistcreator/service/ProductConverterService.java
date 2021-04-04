package pl.kryniek.shoppinglistcreator.service;

import org.springframework.stereotype.Service;
import pl.kryniek.shoppinglistcreator.enums.ProductType;
import pl.kryniek.shoppinglistcreator.model.Product;

import static java.lang.String.format;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
public class ProductConverterService {
    public Product toProduct(String rawProduct) {
        String[] fields = getFields(rawProduct);
        String name = getName(rawProduct, fields);
        Integer quantity = getQuantity(rawProduct, fields);
        String measure = getMeasure(rawProduct, fields);

        return Product.builder()
                .name(name)
                .quantity(quantity)
                .measure(measure)
                .type(ProductType.parse(name))
                .build();
    }

    private String[] getFields(String rawProduct) {
        String[] fields = rawProduct.split(" - ");
        if (fields.length != 3) {
            throw new RuntimeException(format("Row: %s, can't be converted to product.", rawProduct));
        }
        return fields;
    }

    private String getName(String rawProduct, String[] fields) {
        String name = fields[1];
        if (isBlank(name)) {
            throw new RuntimeException(format("Name is blank, row: %s.", rawProduct));
        }
        return name;
    }

    private Integer getQuantity(String rawProduct, String[] fields) {
        String quantityAndMeasure = getQuantityAndMeasure(rawProduct, fields);
        String[] quantityAndMeasureFields = quantityAndMeasure.split(" ");
        if (quantityAndMeasureFields.length < 1) {
            throw new RuntimeException(format("Can't get quantity from row: %s.", rawProduct));
        }
        return Integer.valueOf(quantityAndMeasureFields[0]);
    }

    private String getMeasure(String rawProduct, String[] fields) {
        String quantityAndMeasure = getQuantityAndMeasure(rawProduct, fields);
        String[] quantityAndMeasureFields = quantityAndMeasure.split(" ");
        if (quantityAndMeasureFields.length < 2) {
            throw new RuntimeException(format("Can't get measure from row: %s.", rawProduct));
        }
        return quantityAndMeasureFields[1];
    }

    private String getQuantityAndMeasure(String rawProduct, String[] fields) {
        String quantityAndMeasure = fields[2];
        if (isBlank(quantityAndMeasure)) {
            throw new RuntimeException(format("Quantity and measure are blank, row: %s.", rawProduct));
        }
        return quantityAndMeasure;
    }
}
