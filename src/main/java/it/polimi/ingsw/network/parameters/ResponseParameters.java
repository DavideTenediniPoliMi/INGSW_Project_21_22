package it.polimi.ingsw.network.parameters;

import com.google.gson.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.MatchInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.characters.CharacterCard;
import it.polimi.ingsw.model.enumerations.Card;
import it.polimi.ingsw.model.enumerations.CharacterCards;
import it.polimi.ingsw.model.enumerations.GameStatus;
import it.polimi.ingsw.model.helpers.StudentBag;
import it.polimi.ingsw.utils.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a response from the server after any action
 */
public class ResponseParameters implements Serializable {
    private List<School> schools = new ArrayList<>();
    private List<CharacterCard> characterCards;
    private List<Cloud> clouds;
    private List<Island> islands;
    private boolean bagEmpty;
    private ProfessorTracker professors;
    private Player player;
    private int coinsLeft = -1;
    private boolean sendCards;
    private boolean sendMatchInfo;
    private boolean sendGame;
    private List<Player> players;
    private String error;

    /**
     * Gets the list of schools.
     *
     * @return the list of schools of this <code>ResponseParameters</code>.
     */
    public List<School> getSchools() {
        return schools;
    }

    /**
     * Sets the specified list of schools and returns this instance.
     *
     * @param schools the list of schools of this message.
     * @return this <code>ResponseParameters</code>
     */
    public ResponseParameters setSchools(List<School> schools) {
        this.schools = schools;
        return this;
    }

    /**
     * Sets the specified <code>School</code> and returns this instance.
     *
     * @param school the <code>School</code> of this message.
     * @return this <code>ResponseParameters</code>
     */
    public ResponseParameters addSchool(School school) {
        schools.add(school);
        return this;
    }

    /**
     * Gets the list of Character cards.
     *
     * @return the list of Character cards of this <code>ResponseParameters</code>.
     */
    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    /**
     * Sets the specified list of Character cards and returns this instance.
     *
     * @param characterCards the list of Character cards of this message.
     * @return this <code>ResponseParameters</code>
     */
    public ResponseParameters setCharacterCards(List<CharacterCard> characterCards) {
        this.characterCards = characterCards;
        return this;
    }

    /**
     * Gets the list of clouds.
     *
     * @return the list of clouds of this <code>ResponseParameters</code>.
     */
    public List<Cloud> getClouds() {
        return clouds;
    }

    /**
     * Sets the specified list of clouds and returns this instance.
     *
     * @param clouds the list of clouds of this message.
     * @return this <code>ResponseParameters</code>
     */
    public ResponseParameters setClouds(List<Cloud> clouds) {
        this.clouds = clouds;
        return this;
    }

    /**
     * Gets the list of schools.
     *
     * @return the list of schools of this <code>ResponseParameters</code>.
     */
    public List<Island> getIslands() {
        return islands;
    }

    /**
     * Sets the specified list of islands and returns this instance.
     *
     * @param islands the list of islands of this message.
     * @return this <code>ResponseParameters</code>
     */
    public ResponseParameters setIslands(List<Island> islands) {
        this.islands = islands;
        return this;
    }

    /**
     * Gets the flag specifying if the <code>StudentBag</code> is empty.
     *
     * @return if the <code>StudentBag</code> is set as empty in this <code>ResponseParameters</code>.
     */
    public boolean isBagEmpty() {
        return bagEmpty;
    }

    /**
     * Sets if the <code>StudentBag</code> is empty and returns this instance.
     *
     * @param bagEmpty the flag regarding the <code>StudentBag</code>.
     * @return this <code>ResponseParameters</code>.
     */
    public ResponseParameters setBagEmpty(boolean bagEmpty) {
        this.bagEmpty = bagEmpty;
        return this;
    }

    /**
     * Gets the <code>ProfessorTracker</code>.
     *
     * @return the <code>ProfessorTracker</code> of this <code>ResponseParameters</code>.
     */
    public ProfessorTracker getProfessors() {
        return professors;
    }

    /**
     * Sets the specified <code>ProfessorTracker</code> and returns this instance.
     *
     * @param professors the <code>ProfessorTracker</code> to set.
     * @return this <code>ResponseParameters</code>.
     */
    public ResponseParameters setProfessors(ProfessorTracker professors) {
        this.professors = professors;
        return this;
    }

    /**
     * Gets the amount of coins left in the <code>Board</code>.
     *
     * @return the amount of coins set in this <code>ResponseParameters</code>.
     */
    public int getCoinsLeft() {
        return coinsLeft;
    }

    /**
     * Sets the specified amount of coins left in the <code>Board</code> and returns this instance.
     *
     * @param coinsLeft the amount of coins.
     * @return this <code>ResponseParameters</code>.
     */
    public ResponseParameters setCoinsLeft(int coinsLeft) {
        this.coinsLeft = coinsLeft;
        return this;
    }

    /**
     * Gets the flag specifying if this message should send information about the assistant cards.
     *
     * @return whether this <code>ResponseParameters</code> should send information about the assistant cards.
     */
    public boolean shouldSendCards() {
        return sendCards;
    }

    /**
     * Sets whether this message should send information about the assistant cards and returns this instance.
     *
     * @param sendCards the flag specifying if the VirtualView should send info about Cards.
     * @return this <code>ResponseParameters</code>.
     */
    public ResponseParameters setSendCards(boolean sendCards) {
        this.sendCards = sendCards;
        return this;
    }

    /**
     * Gets the flag specifying if this message should send information about the Match.
     *
     * @return whether this <code>ResponseParameters</code> should send information about the Match.
     */
    public boolean shouldSendMatchInfo() {
        return sendMatchInfo;
    }

    /**
     * Sets whether this message should send information about this Match and returns this instance.
     *
     * @param sendMatchInfo the flag specifying if the VirtualView should send info about this Match.
     * @return this <code>ResponseParameters</code>.
     */
    public ResponseParameters setSendMatchInfo(boolean sendMatchInfo) {
        this.sendMatchInfo = sendMatchInfo;
        return this;
    }

    /**
     * Gets the flag specifying if this message should send information about the Game.
     *
     * @return whether this <code>ResponseParameters</code> should send information about the Game.
     */
    public boolean shouldSetGame() {
        return sendGame;
    }

    /**
     * Sets whether this message should send information about this Game and returns this instance.
     *
     * @param sendGame the flag specifying if the VirtualView should send info about this Game.
     * @return this <code>ResponseParameters</code>.
     */
    public ResponseParameters setSendGame(boolean sendGame) {
        this.sendGame = sendGame;
        return this;
    }

    /**
     * Gets the list of players of this <code>ResponseParameters</code>.
     *
     * @return the list of players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Sets the specified list of players and returns this instance.
     *
     * @param players the list of players for this message.
     * @return this <code>ResponseParameters</code>
     */
    public ResponseParameters setPlayers(List<Player> players) {
        this.players = players;
        return this;
    }

    /**
     * Gets the error message of this <code>ResponseParameters</code>.
     *
     * @return the error message.
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the specified error message and returns this instance.
     *
     * @param error the error message.
     * @return this <code>ResponseParameters</code>.
     */
    public ResponseParameters setError(String error) {
        this.error = error;
        return this;
    }

    @Override
    public JsonObject serialize() {
        JsonObject jsonObject = new JsonObject();

        if(schools != null &&  schools.size() > 0) {
            JsonArray jsonArray = new JsonArray();
            for(School s : schools) {
                jsonArray.add(s.serialize());
            }
            jsonObject.add("schools", jsonArray);
        }

        if(characterCards != null &&  characterCards.size() > 0) {
            JsonArray jsonArray = new JsonArray();
            for(CharacterCard c : characterCards) {
                jsonArray.add(c.serialize());
            }
            jsonObject.add("characterCards", jsonArray);
        }

        if(clouds != null &&  clouds.size() > 0) {
            JsonArray jsonArray = new JsonArray();
            for(Cloud c : clouds) {
                jsonArray.add(c.serialize());
            }
            jsonObject.add("clouds", jsonArray);
        }

        if(islands != null &&  islands.size() > 0) {
            JsonArray jsonArray = new JsonArray();
            for(Island i : islands) {
                jsonArray.add(i.serialize());
            }
            jsonObject.add("islands", jsonArray);
        }

        if(bagEmpty)
            jsonObject.add("bagEmpty", new JsonPrimitive(bagEmpty));

        if(professors != null)
            jsonObject.add("professors", professors.serialize());

        if(players != null && players.size() > 0) {
            JsonArray jsonArray = new JsonArray();
            for(Player p : players) {
                jsonArray.add(p.serialize());
            }
            jsonObject.add("players", jsonArray);
        }

        if(coinsLeft != -1) {
            jsonObject.add("coinsLeft", new JsonPrimitive(coinsLeft));
        }

        if(sendCards)
            jsonObject.add("cards", Card.serializeAll());

        if(sendMatchInfo)
            jsonObject.add("matchInfo", MatchInfo.getInstance().serialize());

        if(sendGame)
            jsonObject.add("game", Game.getInstance().serialize());

        if(error != null && error.length() > 0)
            jsonObject.add("error", new JsonPrimitive(error));

        return jsonObject;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        Game game = Game.getInstance();

        if(jsonObject.has("game"))
            Game.getInstance().deserialize(jsonObject.getAsJsonObject("game"));

        if (jsonObject.has("schools")) {
            schools = new ArrayList<>();
            JsonArray jsonArray = jsonObject.get("schools").getAsJsonArray();

            if(jsonArray.size() == MatchInfo.getInstance().getSelectedNumPlayer()) {
                for (JsonElement je : jsonArray) {
                    School s = new School(new Player(-1, ""));
                    s.deserialize(je.getAsJsonObject());
                    schools.add(s);
                }
                game.getBoard().setSchools(schools);
            } else {
                for (JsonElement je : jsonArray) {
                    School s = game.getBoard().getSchoolByPlayerID(je.getAsJsonObject().get("ownerID").getAsInt());
                    if(s != null) {
                        s.deserialize(je.getAsJsonObject());
                        schools.add(s);
                    }
                }
            }
        }

        if (jsonObject.has("characterCards")) {
            characterCards = new ArrayList<>();
            JsonArray jsonArray = jsonObject.get("characterCards").getAsJsonArray();
            for (JsonElement je : jsonArray) {
                String name = je.getAsJsonObject().get("name").getAsString();
                CharacterCard c = CharacterCards.valueOf(name).instantiate();
                c.deserialize(je.getAsJsonObject());
                characterCards.add(c);
            }
            game.setCharacterCards(characterCards);
        }

        if (jsonObject.has("clouds")) {
            clouds = new ArrayList<>();
            JsonArray jsonArray = jsonObject.get("clouds").getAsJsonArray();
            for (JsonElement je : jsonArray) {
                Cloud c = new Cloud();
                c.deserialize(je.getAsJsonObject());
                clouds.add(c);
            }
            game.getBoard().setClouds(clouds);
        }

        if (jsonObject.has("islands")) {
            islands = new ArrayList<>();
            JsonArray jsonArray = jsonObject.get("islands").getAsJsonArray();
            for (JsonElement je : jsonArray) {
                if (je.isJsonArray()) {
                    MultiIsland multiIsland = new MultiIsland(new SimpleIsland(), new SimpleIsland());
                    multiIsland.deserialize(je.getAsJsonObject());
                    islands.add(multiIsland);
                } else {
                    SimpleIsland simpleIsland = new SimpleIsland();
                    simpleIsland.deserialize(je.getAsJsonObject());
                    islands.add(simpleIsland);
                }
            }
            game.getBoard().setIslands(islands);
        }

        if (jsonObject.has("bagEmpty")) {
            bagEmpty = jsonObject.get("bagEmpty").getAsBoolean();
            if(bagEmpty)
                game.setStudentBag(new StudentBag(0));
        }

        if (jsonObject.has("professors")) {
            professors.deserialize(jsonObject.get("professors").getAsJsonObject());
            game.getBoard().setProfessorOwners(professors);
        }

        if(jsonObject.has("coinsLeft")) {
            coinsLeft = jsonObject.get("coinsLeft").getAsInt();
            game.getBoard().setNumCoinsLeft(coinsLeft);
        }

        if(jsonObject.has("cards"))
            Card.deserializeAll(jsonObject.getAsJsonObject("cards"));

        if(jsonObject.has("matchInfo"))
            MatchInfo.getInstance().deserialize(jsonObject.getAsJsonObject("matchInfo"));

        if (jsonObject.has("players")) {
            players = new ArrayList<>();
            JsonArray jsonArray = jsonObject.get("players").getAsJsonArray();
            for (JsonElement je : jsonArray) {
                Player p = new Player(0, "");
                p.deserialize(je.getAsJsonObject());
                players.add(p);
            }
            if(GameStatus.IN_GAME.equals(MatchInfo.getInstance().getGameStatus())) {
                game.setPlayers(players);
                for(School s: game.getBoard().getSchools()) {
                    s.updateOwner();
                }
            } else {
                Lobby.getLobby().setPlayers(players);
            }
        }

        if(jsonObject.has("error"))
            error = jsonObject.get("error").getAsString();
    }
}
