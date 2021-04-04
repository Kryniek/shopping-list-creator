package pl.kryniek.shoppinglistcreator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kryniek.shoppinglistcreator.model.Product;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ShoppingListCreatorService {
    private final ProductConverterService productConverterService;
    private final ShoppingListConverterService shoppingListConverterService;

    public String create(String rawShoppingList) {
        List<Product> products = rawShoppingList
                .lines()
                .filter(row -> !row.contains("Do kupienia"))
                .map(productConverterService::toProduct)
                .collect(toList());
        List<Product> distinctProducts = getDistinctProducts(products);
        return shoppingListConverterService.toShoppingList(distinctProducts);
    }

    private List<Product> getDistinctProducts(List<Product> products) {
        return products
                .stream()
                .map(product -> {
                    Optional<Integer> sum = products
                            .stream()
                            .filter(p -> product.getName().equals(p.getName()))
                            .map(Product::getQuantity)
                            .reduce(Integer::sum);

                    if (sum.isEmpty()) {
                        throw new RuntimeException(format("Exception thrown during summarizing same products quantities, product: %s", product));
                    }

                    return Product.builder()
                            .name(product.getName())
                            .quantity(sum.get())
                            .measure(product.getMeasure())
                            .type(product.getType())
                            .build();
                })
                .distinct()
                .sorted(Comparator.comparing(Product::getName))
                .collect(toList());
    }
}
