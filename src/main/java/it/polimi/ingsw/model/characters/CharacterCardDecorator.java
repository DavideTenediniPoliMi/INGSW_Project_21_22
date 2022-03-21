package it.polimi.ingsw.model.characters;

public abstract class CharacterCardDecorator {
    protected CharacterCard card;

    public CharacterCardDecorator(CharacterCard card) {
        this.card = card;
    }

    public abstract void activate();
}
