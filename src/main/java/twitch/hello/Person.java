package twitch.hello;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Person(String name, String company, @JsonProperty("home_address") Address homeAddress,
                     @JsonProperty("favoraite_book") Book favoriteBook) {
}
