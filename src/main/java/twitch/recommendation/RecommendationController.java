package twitch.recommendation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import twitch.db.entity.ItemEntity;
import twitch.external.TwitchService;
import twitch.favorite.FavoriteService;
import twitch.item.ItemService;
import twitch.model.TypeGroupedItemList;

import java.util.List;

@RestController
public class RecommendationController {

    private final FavoriteService favoriteService;
    private final ItemService itemService;
    private final TwitchService twitchService;

    public RecommendationController(FavoriteService favoriteService, ItemService itemService, TwitchService twitchService) {
        this.favoriteService = favoriteService;
        this.itemService = itemService;
        this.twitchService = twitchService;
    }

    @GetMapping("/recommendation")
    public TypeGroupedItemList getRecommendations() {
        List<ItemEntity> favorites = favoriteService.sortedFavorites();
        if (!favorites.isEmpty() && favorites.getFirst().gameId() != null) {
            return itemService.getItems(favorites.getFirst().gameId());
        }

        List<String> topGameIds = twitchService.getTopGameIds();
        return itemService.getItems(topGameIds.isEmpty() ? "33214" : topGameIds.getFirst());
    }
}
