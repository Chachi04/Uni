import java.util.*;

/**
 * This program receives as input first a number n.
 * Then n space-separated numbers representing the kilemoters
 * that each of the n cans can make the truck go.
 * Finally n-1 space-separated numbers are entered which
 * represent the locations where the truck must not stop.
 *
 * The program then outputs a sequence of cans that the
 * truck drivers should use in order to never stop at a
 * location that is in the list of no-stops.
 *
 * Example input:
 * 40 42 6 66 96 86 59 8 83 12
 * 18 90 26 98 9 48 55 84 24 49
 * 70 77 27 85 41 63 57 21 38
 * 22 14 11 94 73 47 7 82 93 64
 * 89 54 2062 1994 1967 1963
 * 1897 1872 1840 1821 1806
 * 1800 1754 1598 1576 1399
 * 1391 1263 1252 1229 1166
 * 1098 1001 997 973 770 744
 * 738 687 667 565 545 388
 * 356 320 274 235 177 173
 * 110 78
 *
 * Example output:
 * 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28
 * 29 30 31 32 33 34 35 36 37 38 39
 *
 * @author Sona Hyblova
 * @ID 2005050
 * @author Jiaqi Wang
 * @ID 1986619
 */
class MadTrucker {
    public Scanner scanner = new Scanner(System.in);
    public ArrayList<Can> cans = new ArrayList<Can>();
    public HashSet<Integer> noStops = new HashSet<Integer>();
    public ArrayList<Integer> solution = new ArrayList<Integer>();
    public int destination = 0;

    void run() {
        getInput();

        calculate(0, 0);

        printSolution();
    }

    /**
     * - reads input.
     * - stores the number of kilometers each can can fuel the truck in cans
     * (an ArrayList of Can object)
     * - calculates final destination as the sum kilometers the can produce together
     * - stores kilometers at which the truck cannot stop to noStops HashSet
     * we used HashSet because the time complexity of HashSet operation contains()
     * is O(1) and it is not neccessary to know the order of places where the truck
     * cannot stop
     */
    void getInput() {
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            cans.add(new Can(scanner.nextInt()));
            destination += cans.get(i).mileage;
        }
        for (int i = 0; i < n - 1; i++) {
            noStops.add(scanner.nextInt());
        }
    }

    /**
     * Find one solution of the order of fuel can usage
     * that would lead to the truck to the end destination
     * without stopping at a no-stop location.
     * We use a backtracking algorithm to find a solution.
     */
    void calculate(int currentPos, int counter) {
        if (currentPos == destination) {
            return;
        }
        int i = 0;
        while (((noStops.contains(currentPos + cans.get(i).mileage)) && !cans.get(i).used)
                || solution.contains(i)) {
            i++;
        }
        if (i < cans.size()) {
            solution.add(i);
            cans.get(i).used = true;
            calculate(currentPos + cans.get(i).mileage, counter++);
        } else {
            int lastAdded = solution.get(solution.size() - 1);
            solution.remove(lastAdded);
            cans.get(cans.size() - 1).used = false;
            calculate(currentPos - lastAdded, counter--);
        }
    }

    /**
     * prints solution.
     */
    void printSolution() {
        for (int n : solution) {
            System.out.print(n + " ");
        }
    }

    public static void main(String[] args) {
        MadTrucker program = new MadTrucker();
        program.run();

        // program.check();
    }

    /**
     * Checks whether the solution is correct.
     */
    public void check() {
        int position = 0;
        for (int i : solution) {
            if (noStops.contains(position + cans.get(i).mileage)) {
                System.out.println("Fail");
                return;
            }
            position += cans.get(i).mileage;
        }
        System.out.println("Success");
    }

    class Can {
        public int mileage;
        public boolean used;

        /**
         * Constructor method for the class Can.
         *
         * @param n : the mileage of a can
         */
        Can(int n) {
            mileage = n;
            used = false;
        }
    }
}