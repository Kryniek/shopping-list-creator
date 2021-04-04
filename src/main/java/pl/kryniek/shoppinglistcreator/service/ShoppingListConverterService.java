package pl.kryniek.shoppinglistcreator.service;

import org.springframework.stereotype.Service;
import pl.kryniek.shoppinglistcreator.enums.ProductType;
import pl.kryniek.shoppinglistcreator.model.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.groupingBy;
import static org.springframework.util.CollectionUtils.isEmpty;
import static pl.kryniek.shoppinglistcreator.enums.ProductType.NONE;

@Service
public class ShoppingListConverterService {
    public String toShoppingList(List<Product> products) {
        StringBuilder shoppingList = new StringBuilder(format("Lista zakupów: %tF \n\n", LocalDateTime.now()));
        Map<ProductType, List<Product>> typesToProducts = products
                .stream()
                .collect(groupingBy(Product::getType));

        typesToProducts
                .entrySet()
                .stream()
                .sorted(comparingByKey())
                .forEach(productTypeToProducts -> {
                    ProductType productType = productTypeToProducts.getKey();
                    if (productType.equals(NONE)) {
                        return;
                    }
                    appendTypeProducts(shoppingList, productType, productTypeToProducts.getValue());
                });
        List<Product> noneProducts = typesToProducts.get(NONE);
        if (!isEmpty(noneProducts)) {
            appendTypeProducts(shoppingList, NONE, noneProducts);
        }

        return shoppingList.toString();
    }

    private void appendTypeProducts(StringBuilder shoppingList, ProductType productType, List<Product> groupedProducts) {
        shoppingList.append(format("%s:\n", productType.getDescription()));
        groupedProducts.forEach(product -> shoppingList.append(format(" - %s - %d %s\n", product.getName(), product.getQuantity(), product.getMeasure())));
    }
}
