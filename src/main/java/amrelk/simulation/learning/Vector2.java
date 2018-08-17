package amrelk.simulation.learning;

public class Vector2 {

    public double
            x,
            y;

    public Vector2 (double x, double y) {
        this.x = x;
        this.y = y;
    }

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
}
