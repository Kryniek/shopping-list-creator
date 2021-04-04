package pl.kryniek.shoppinglistcreator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kryniek.shoppinglistcreator.service.ShoppingListCreatorService;

import static org.apache.logging.log4j.util.Strings.isBlank;

@RestController
@RequestMapping("api/shopping-list-creators")
@RequiredArgsConstructor
public class ShoppingListCreatorController {
    private final ShoppingListCreatorService service;

    @PostMapping
    public String create(@RequestBody String rawShoppingList) {
        if (isBlank(rawShoppingList)) {
            throw new RuntimeException("You must add at least one product!");
        }
        return service.create(rawShoppingList);
    }
}
