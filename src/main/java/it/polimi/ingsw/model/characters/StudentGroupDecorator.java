package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.helpers.StudentGroup;

public class StudentGroupDecorator extends CharacterCardDecorator{
    private StudentGroup students;
    private boolean toIslands, toDiningRoom;

    public StudentGroupDecorator(GenericCard card, boolean toIslands, boolean toDiningRoom){
        super(card);
    }

    public void activate(){

    }

    public void transferTo(Object destination, StudentGroup[] selectedStudents){

    }

    private void refillStudents(){

    }
}
