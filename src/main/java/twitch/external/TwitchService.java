package twitch.external;

import twitch.external.model.Clip;
import twitch.external.model.Game;
import twitch.external.model.Stream;
import twitch.external.model.Video;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TwitchService {

   private final TwitchApiClient twitchApiClient;
   private final DemoTwitchData demoTwitchData;
   private final boolean demoMode;

   public TwitchService(
           TwitchApiClient twitchApiClient,
           DemoTwitchData demoTwitchData,
           @Value("${app.twitch.demo-mode:false}") boolean demoMode
   ) {
       this.twitchApiClient = twitchApiClient;
       this.demoTwitchData = demoTwitchData;
       this.demoMode = demoMode;
   }

   public List<Game> getTopGames() {
       if (demoMode) {
           return demoTwitchData.topGames();
       }
       return safeList(twitchApiClient.getTopGames().data());
   }

   public List<Game> getGames(String name) {
       if (demoMode) {
           return demoTwitchData.games(name);
       }
       return safeList(twitchApiClient.getGames(name).data());
   }

   public List<Stream> getStreams(List<String> gameIds, int first) {
       if (demoMode) {
           return demoTwitchData.streams(gameIds, first);
       }
       return safeList(twitchApiClient.getStreams(gameIds, first).data());
   }

   public List<Video> getVideos(String gameId, int first) {
       if (demoMode) {
           return demoTwitchData.videos(gameId, first);
       }
       return safeList(twitchApiClient.getVideos(gameId, first).data());
   }

   public List<Clip> getClips(String gameId, int first) {
       if (demoMode) {
           return demoTwitchData.clips(gameId, first);
       }
       return safeList(twitchApiClient.getClips(gameId, first).data());
   }

   public List<String> getTopGameIds() {
       List<String> topGameIds = new ArrayList<>();
       for (Game game : getTopGames()) {
           topGameIds.add(game.id());
       }
       return topGameIds;
   }

   private static <T> List<T> safeList(List<T> values) {
       return values == null ? List.of() : values;
   }
}
