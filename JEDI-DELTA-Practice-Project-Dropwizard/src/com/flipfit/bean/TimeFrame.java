package com.flipfit.bean;

// TODO: Auto-generated Javadoc
/**
 * The Class TimeFrame.
 *
 * Represents a time frame for gym slots with start and end hours.
 *
 * @author JEDI-DELTA
 * @ClassName  "TimeFrame"
 */
public class TimeFrame {
    private int startHour;
    private int endHour;

    /**
     * Instantiates a new time frame.
     *
     * @param startHour the start hour (0-23)
     * @param endHour the end hour (0-23)
     */
    public TimeFrame(int startHour, int endHour) {
        this.startHour = startHour;
        this.endHour = endHour;
    }

    /**
     * Gets the start hour.
     *
     * @return the start hour
     */
    public int getStartHour() {
        return startHour;
    }

    /**
     * Sets the start hour.
     *
     * @param startHour the new start hour
     */
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    /**
     * Gets the end hour.
     *
     * @return the end hour
     */
    public int getEndHour() {
        return endHour;
    }

    /**
     * Sets the end hour.
     *
     * @param endHour the new end hour
     */
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    /**
     * Returns a string representation of the time frame.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return startHour + ":00 - " + endHour + ":00";
    }

    /**
     * Checks equality based on start and end hour.
     *
     * @param obj the object to compare
     * @return true if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TimeFrame timeFrame = (TimeFrame) obj;
        return startHour == timeFrame.startHour && endHour == timeFrame.endHour;
    }

    /**
     * Returns hash code for this time frame.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(startHour, endHour);
    }
}