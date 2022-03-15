package it.polimi.ingsw;

public enum Card {
    CARD_1(1, 1),
    CARD_2(2, 1),
    CARD_3(3, 2),
    CARD_4(4, 2),
    CARD_5(5, 3),
    CARD_6(6, 3),
    CARD_7(7, 4),
    CARD_8(8, 4),
    CARD_9(9, 5),
    CARD_10(10, 5);

    public final int WEIGHT;
    public final int RANGE;
    private boolean used = false;

    Card(int weight, int range) {
        this.WEIGHT = weight;
        this.RANGE = range;
    }

    void use() {
        this.used = true;
    }

    void reset() {
        this.used = false;
    }
}
