package twitch.favorite;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import twitch.db.entity.ItemEntity;
import twitch.model.TypeGroupedItemList;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/favorite")
    public TypeGroupedItemList getFavorites() {
        return favoriteService.getFavorites();
    }

    @PostMapping("/favorite")
    public ItemEntity addFavorite(@RequestBody ItemEntity item) {
        return favoriteService.addFavorite(item);
    }

    @DeleteMapping("/favorite/{twitchId}")
    public void removeFavorite(@PathVariable String twitchId) {
        favoriteService.removeFavorite(twitchId);
    }

    @DeleteMapping("/favorite")
    public void clearFavorites() {
        favoriteService.clearFavorites();
    }
}
