package twitch.external;

import org.springframework.stereotype.Component;
import twitch.external.model.Clip;
import twitch.external.model.Game;
import twitch.external.model.Stream;
import twitch.external.model.Video;

import java.util.List;
import java.util.Locale;

@Component
public class DemoTwitchData {

    private static final List<Game> GAMES = List.of(
            new Game("33214", "Fortnite", "https://static-cdn.jtvnw.net/ttv-boxart/33214-{width}x{height}.jpg"),
            new Game("509658", "Just Chatting", "https://static-cdn.jtvnw.net/ttv-boxart/509658-{width}x{height}.jpg"),
            new Game("27471", "Minecraft", "https://static-cdn.jtvnw.net/ttv-boxart/27471-{width}x{height}.jpg"),
            new Game("21779", "League of Legends", "https://static-cdn.jtvnw.net/ttv-boxart/21779-{width}x{height}.jpg"),
            new Game("516575", "VALORANT", "https://static-cdn.jtvnw.net/ttv-boxart/516575-{width}x{height}.jpg"),
            new Game("32982", "Grand Theft Auto V", "https://static-cdn.jtvnw.net/ttv-boxart/32982-{width}x{height}.jpg")
    );

    public List<Game> topGames() {
        return GAMES;
    }

    public List<Game> games(String name) {
        if (name == null || name.isBlank()) {
            return topGames();
        }

        String query = name.toLowerCase(Locale.ROOT);
        List<Game> matches = GAMES.stream()
                .filter(game -> game.name().toLowerCase(Locale.ROOT).contains(query))
                .toList();
        return matches.isEmpty() ? topGames() : matches;
    }

    public List<Stream> streams(List<String> gameIds, int first) {
        return topGames().stream()
                .filter(game -> gameIds == null || gameIds.isEmpty() || gameIds.contains(game.id()))
                .flatMap(game -> List.of(
                        new Stream("stream-" + game.id() + "-1", "user-" + game.id() + "-1", "junplays",
                                "JunPlays", game.id(), game.name(), "live",
                                "Ranked session and community queue", 18420, "2026-05-08T20:00:00Z",
                                "en", preview("junplays-" + game.id()), List.of(), false),
                        new Stream("stream-" + game.id() + "-2", "user-" + game.id() + "-2", "nightcast",
                                "NightCast", game.id(), game.name(), "live",
                                "Patch notes, viewer games, and highlights", 9320, "2026-05-08T21:10:00Z",
                                "en", preview("nightcast-" + game.id()), List.of(), false)
                ).stream())
                .limit(first)
                .toList();
    }

    public List<Video> videos(String gameId, int first) {
        Game game = findGame(gameId);
        return List.of(
                        new Video("video-" + game.id() + "-1", null, "video-user-1", "junplays", "JunPlays",
                                game.name() + " weekly recap", "Top plays and lessons from the week.",
                                "2026-05-08T18:00:00Z", "2026-05-08T18:30:00Z",
                                "https://www.twitch.tv/videos/123456789", preview("video-" + game.id() + "-1"),
                                "public", 42100, "en", "archive", "32m", List.of()),
                        new Video("video-" + game.id() + "-2", null, "video-user-2", "nightcast", "NightCast",
                                game.name() + " strategy breakdown", "A focused walkthrough for new players.",
                                "2026-05-07T19:00:00Z", "2026-05-07T19:25:00Z",
                                "https://www.twitch.tv/videos/987654321", preview("video-" + game.id() + "-2"),
                                "public", 23800, "en", "highlight", "18m", List.of())
                ).stream()
                .limit(first)
                .toList();
    }

    public List<Clip> clips(String gameId, int first) {
        Game game = findGame(gameId);
        return List.of(
                        new Clip("clip-" + game.id() + "-1", "https://www.twitch.tv/junplays/clip/example",
                                "https://clips.twitch.tv/embed?clip=example", "broadcaster-1", "JunPlays",
                                "creator-1", "Jun", null, game.id(), "en",
                                "Clean clutch in " + game.name(), 12800, "2026-05-08T22:00:00Z",
                                preview("clip-" + game.id() + "-1"), 29.0f, null),
                        new Clip("clip-" + game.id() + "-2", "https://www.twitch.tv/nightcast/clip/example",
                                "https://clips.twitch.tv/embed?clip=example-two", "broadcaster-2", "NightCast",
                                "creator-2", "Jun", null, game.id(), "en",
                                "Perfect timing moment", 7600, "2026-05-07T23:00:00Z",
                                preview("clip-" + game.id() + "-2"), 21.0f, null)
                ).stream()
                .limit(first)
                .toList();
    }

    private Game findGame(String gameId) {
        return GAMES.stream()
                .filter(game -> game.id().equals(gameId))
                .findFirst()
                .orElse(GAMES.getFirst());
    }

    private String preview(String seed) {
        return "https://picsum.photos/seed/twitch-" + seed + "/480/270";
    }
}
