import java.lang.Math;

public class Vector3 {
    private final double x;
    private final double y;
    private final double z;
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 add(Vector3 other) {
        return new Vector3(
                this.x+other.x,
                this.y+other.y,
                this.z+other.z
        );
    }

    public Vector3 subtract(Vector3 other) {
        return new Vector3(
                this.x-other.x,
                this.y-other.y,
                this.z-other.z
        );
    }

    public Vector3 scale(double scalar) {
        return new Vector3(
                this.x*scalar,
                this.y*scalar,
                this.z*scalar
        );
    }

    public double dot(Vector3 other) {
        return this.x*other.x + this.y*other.y + this.z*other.z;
    }

    public Vector3 cross(Vector3 other) {
        return new Vector3(
                (this.y*other.z) - (this.z*other.y),
                -((this.x*other.z) - (this.z*other.y)),
                (this.x*other.y) - (this.y*other.x)
        );
    }

    public double magnitude() {
        return Math.sqrt(Math.pow(this.x, 2)
                + Math.pow(this.y, 2)
                + Math.pow(this.z, 2)
        );
    }

    public Vector3 normalize() {
        double magnitude = this.magnitude();
        if (magnitude > 0) {
            return new Vector3(
                    this.x/magnitude,
                    this.y/magnitude,
                    this.z/magnitude
            );
        } else {
            return this;
            /*
            mathematically incorrect solution;
            normalizing a zero vector is undefined
            returning "this" is just a design choice
             */
        }

    }

    public double angleBetween(Vector3 other) {
        double mag1 = this.magnitude();
        double mag2 = other.magnitude();
        if (mag1 == 0 || mag2 == 0) {return Double.NaN;}
        return Math.acos(this.dot(other) / (mag1 * mag2));
        // acos may occasionally end up outside [-1, 1] due to drift
    }

    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public double getZ() {
        return this.z;
    }

    @Override
    public String toString() {
        return "<" + x + ", " + y + ", " + z + ">";
    }
}
