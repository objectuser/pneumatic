package com.surgingsystems.etl.utility;

/**
 * Utility for helping hashCode and equals implementations.
 */
public class Equality {

    /**
     * Is the given object applicable to target object in terms of equality?
     * 
     * @param o1
     *            The target object
     * @param o2
     *            The given object
     * @return o2 if the given object is a candidate for equality
     */
    @SuppressWarnings("unchecked")
    public static <T> T applicable(T o1, Object o2) {
        if (o2 == null || o1.hashCode() != o2.hashCode() || !o1.getClass().equals(o2.getClass())) {
            return null;
        } else {
            return (T) o2;
        }
    }

    /**
     * Compute a hash code value based on the class of the argument.
     */
    public static <T> int hashCode(T o1) {
        return o1.getClass().hashCode();
    }
}
