package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.helpers.StudentGroup;

public class Cloud {

    private final StudentGroup students;
    private boolean available = true;

    public Cloud(){
        students = new StudentGroup();
    }
}
