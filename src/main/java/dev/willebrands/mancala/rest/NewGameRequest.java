package dev.willebrands.mancala.rest;

import java.util.List;
import lombok.Data;

@Data
public class NewGameRequest {
    String variant;
    List<String> playerNames;
}
