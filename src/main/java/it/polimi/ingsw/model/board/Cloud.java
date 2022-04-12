package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.helpers.StudentGroup;

/**
 * Class corresponding to the Cloud entity in a Game
 */
public class Cloud {
    private final StudentGroup students = new StudentGroup();
    private boolean available;

    /**
     * Sole constructor for this class
     */
    public Cloud() {
    }

    /**
     * Returns the students contained in this <code>Cloud</code>
     * @return <code>StudentGroup</code> with the students contained in this <code>Cloud</code>
     */
    public StudentGroup getStudents() {
        return (StudentGroup) students.clone();
    }

    /**
     * Sets the available flag for this <code>Cloud</code>
     * @param available the flag to set
     */
    private void setAvailableTo(boolean available) {
        this.available = available;
    }

    /**
     * Returns whether students can be collected from this <code>Cloud</code>
     * @return Availability of this <code>Cloud</code>.
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Refills this <code>Cloud</code> with the specified students and makes it available.
     * @param toAdd the students to add to this <code>Cloud</code>
     */
    protected void refillCloud(StudentGroup toAdd) {
        toAdd.transferAllTo(students);
        setAvailableTo(true);
    }

    /**
     * Collects students contained in this <code>Cloud</code>.
     * @return <code>StudentGroup</code> containing this <code>Cloud</code>'s students.
     */
    protected StudentGroup collectStudents() {
        StudentGroup temp = new StudentGroup();
        students.transferAllTo(temp);

        setAvailableTo(false);

        return temp;
    }
}
