package amrelk.simulation.learning;

public class Vector2 {

    public double
            x,
            y;

    /**
     * creates a vector with a magnitude of 1 at the specified angle
     * @param rot angle in radians
     */
    public Vector2 (double rot) {
        x = Math.cos(rot);
        y = Math.sin(rot);
    }

    /**
     * creates a vector with the specified magnitude at the specified angle
     * @param magnitude the magnitude!
     * @param rot angle in radians
     */
    public Vector2 (double magnitude, double rot) {
        x = magnitude * Math.cos(rot);
        y = magnitude * Math.sin(rot);
    }

    /**
     * creates a zero vector
     */
    public Vector2 () {
        x = 0;
        y = 0;
    }

    public static Vector2 add(Vector2 a, Vector2 b) {
        Vector2 result = new Vector2();
        result.x = a.x + b.x;
        result.y = a.y + b.y;
        return result;
    }

    public Vector2 add(Vector2 b) {
        Vector2 result = new Vector2();
        result.x = this.x + b.x;
        result.y = this.y - b.y;
        return result;
    }

    public static Vector2 sub(Vector2 a, Vector2 b) {
        Vector2 result = new Vector2();
        result.x = a.x - b.x;
        result.y = a.y - b.y;
        return result;
    }

    public Vector2 sub(Vector2 b) {
        Vector2 result = new Vector2();
        result.x = this.x - b.x;
        result.y = this.y - b.y;
        return result;
    }

    public static Vector2 mult(Vector2 a, double b) {
        Vector2 result = new Vector2();
        result.x = a.x * b;
        result.y = a.y * b;
        return result;
    }

    public Vector2 mult(double b) {
        Vector2 result = new Vector2();
        result.x = this.x * b;
        result.y = this.y * b;
        return result;
    }

    public void damp(double factor) {
        this.x = this.x / factor;
        this.y = this.y / factor;
    }

    public void limit(double limit) {
        double magnitude = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
        if (magnitude > limit) {
            this.damp(magnitude / limit);
        }
    }

    public void wrap(double minX, double maxX, double minY, double maxY) {
        while (this.x < minX) {
            this.x += maxX - minX;
        }

        while (this.x > maxX) {
            this.x += minX - maxX;
        }

        while (this.y < minY) {
            this.y += maxY - minY;
        }

        while (this.y > maxY) {
            this.y += minY - maxY;
        }
    }
}
