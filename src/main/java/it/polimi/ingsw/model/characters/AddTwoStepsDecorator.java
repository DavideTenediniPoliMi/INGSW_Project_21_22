package it.polimi.ingsw.model.characters;

public class AddTwoStepsDecorator extends CharacterCardDecorator{
    public AddTwoStepsDecorator(CharacterCard card) {
        super(card);
    }

    @Override
    public void activate() {

    }

    public int getBoost() {
        return (card.isActive()) ? 2 : 0;
    }
}
