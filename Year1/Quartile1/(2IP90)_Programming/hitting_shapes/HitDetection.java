import java.util.Scanner;

/**
 * This program detects whether a given point with coordinates (userPoint.x,
 * userPoint.y)
 * is inside of two given circles with centers (circle1.x, circle1.y) and
 * (circle2.x, circle2.y)
 * and their respective radii circle1.radius and cricle2.radius. The ways this
 * is done
 * is by comparing the distance from the center of either circle to the user's
 * point
 * and the radius of the respective circle. If the distance is less than or
 * equal to
 * the radius then the point hits the circle, otherwise, it doesn't. The
 * distance formula
 * that is used is sqrt((userPoint.x - circle1.x)^2+(userPoint.y-circle1.y)^2)
 * 
 * Usage:
 * In a single line enter 8 space-separated floating point numbers that
 * represent
 * the following information:
 * 1st number: x-coordinate of the first circle
 * 2nd number: y-coordinate of the first circle
 * 3rd number: radius of the first circle
 * 4th number: x-coordinate of the second circle
 * 5th number: y-coordinate of the second circle
 * 6th number: radius of the second circle
 * 7th number: x-coordinate of the user's point
 * 8th number: y-coordinate of the user's point
 * 
 * Example input: 0 0 3.1 0.25 0.13 1 2.1 2.03
 * 
 * @author Jiaqi Wang
 * @ID 1986619
 * @author Jazman Mohamad Ismail
 * @ID 1923072
 * 
 */
class HitDetection {

    Scanner scanner = new Scanner(System.in);

    public void run() {
        try {
            // First circle declaration
            Circle circle1 = new Circle(scanner);

            // Second Circle declaration
            Circle circle2 = new Circle(scanner);

            // User input declaration
            Point userPoint = new Point(scanner);

            // Print output
            printOutput(circle1.isPointWithin(userPoint), circle2.isPointWithin(userPoint));

        } catch (Exception e) {
            System.out.println("input error");
        }
    }

    public static void main(String[] args) {
        new HitDetection().run();
    }

    /**
     * Prints the necessary output depending on which circles does the userPoint
     * hit.
     * 
     * @param isWithinFirst  true if userPoint hits the first circle, otherwise,
     *                       false
     * @param isWithinSecond true if userPoint hits the second circle, otherwise,
     *                       false
     */
    void printOutput(boolean isWithinFirst, boolean isWithinSecond) {
        // The point is in both circles
        if (isWithinFirst && isWithinSecond) {
            System.out.println("The point hits both circles");
            return;
        }

        // The point is only in the first circle
        if (isWithinFirst) {
            System.out.println("The point hits the first circle");
            return;
        }

        // The point is only in the second circle
        if (isWithinSecond) {
            System.out.println("The point hits the second circle");
            return;
        }

        // The point is in neither circles
        System.out.println("The point does not hit either circle");
    }

}

/*
 * Class Point
 * -----------
 * 
 * x: double (the x coordinate of the point)
 * y: double (the y coordinate of the point)
 */
class Point {
    double x;
    double y;

    /**
     * Constructor method for class Point- reads two floating point numbers
     * from stdin and assigns them to Point.x and Point.y
     * 
     * @param scanner user to get user input
     */
    public Point(Scanner scanner) {
        this.x = scanner.nextDouble();
        this.y = scanner.nextDouble();
    }
}

/*
 * Class circle
 * ------------
 * 
 * center: Point
 * radius: double
 * 
 * Represents a circle with center "center" and radius "radius"
 */
class Circle {
    Point center;
    double radius;

    /**
     * 
     * Circle constructor - reads a 3 floating point numbers:
     * first to for the coordinates of the center and the third one for the radius.
     * 
     * @param scanner user to get user input
     */
    public Circle(Scanner scanner) {
        this.center = new Point(scanner);
        double r = scanner.nextDouble();
        if (r < 0) {
            throw new IllegalArgumentException();
        }
        this.radius = r;
    }

    /**
     * Caclulates whether userPoint is within the circle.
     * 
     * @param point userPoint
     * @return true if userPoint is in the circle, otherwise false
     */
    public boolean isPointWithin(Point point) {
        if (getDistance(point, this.center) <= radius) {
            return true;
        }
        return false;
    }

    /**
     * Calculates the distance between the points point1 and point2.
     * 
     * @param point1 is the first point
     * @param point2 is the second point
     * @return the distance between point a and point b (double)
     */
    private double getDistance(Point point1, Point point2) {
        return Math.sqrt(Math.pow(point2.x - point1.x, 2) + Math.pow(point2.y - point1.y, 2));
    }
}