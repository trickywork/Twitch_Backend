package twitch.favorite;

import org.springframework.stereotype.Service;
import twitch.db.entity.ItemEntity;
import twitch.model.TypeGroupedItemList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class FavoriteService {

    private final ConcurrentMap<String, ItemEntity> favorites = new ConcurrentHashMap<>();

    public TypeGroupedItemList getFavorites() {
        return new TypeGroupedItemList(sortedFavorites());
    }

    public ItemEntity addFavorite(ItemEntity item) {
        if (item.twitchId() == null || item.twitchId().isBlank()) {
            throw new IllegalArgumentException("twitch_id is required");
        }

        favorites.put(item.twitchId(), item);
        return item;
    }

    public void removeFavorite(String twitchId) {
        favorites.remove(twitchId);
    }

    public void clearFavorites() {
        favorites.clear();
    }

    public List<ItemEntity> sortedFavorites() {
        return new ArrayList<>(favorites.values()).stream()
                .sorted(Comparator.comparing(ItemEntity::type).thenComparing(ItemEntity::title))
                .toList();
    }
}
