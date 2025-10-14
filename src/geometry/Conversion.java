package geometry;

public final class Conversion {

    private Conversion() {
        throw new UnsupportedOperationException();
    }

    /** Convert the angle value into radians
     * @param a angle in degrees
     * @return the radian convertion of this angle
     */
    public static double toRad(double angle) {
        return angle*Math.PI / 180;
    }
}
