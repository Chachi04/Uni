import java.util.Locale;
import java.util.Scanner;

/**
 * Reads a list of numbers, and can reconstruct the corresponding list of
 * Palindromes,
 * produce the size of the largest magic set, and the content of that magic set.
 * 
 * Usage:
 * 1. First enter the number of the task you want to performe.
 * - task 1: fix the palindrome list and print it on the console.
 * - task 2: print the numbers of elements of the largest magic set formed from
 * the corrected palindrome list.
 * - task 3: print the elements of the largest magic set formed from the
 * corrected palindrome list in ascending order.
 * 
 * 2. Then on a new line enter a number N which is the number of palindromes you
 * are going to input.
 * 
 * 3. Lastly on the third line input input N space-separated numbers.
 * 
 * Example input:
 * 3
 * 5
 * 489 123 14890 2148909 789
 * 
 * This will perform task 3 on the palindrome list
 * [489, 123, 14890, 2148909, 789]
 * 
 * @author Sona Hyblova
 * @ID 2005050
 * @author Jiaqi Wang
 * @ID 1986619
 * 
 */
class KingsPalindromeList {
    public Scanner scanner = new Scanner(System.in).useLocale(Locale.ENGLISH);
    public int taskNumber;
    public long[] correctPalindromeList;
    public long[] maxMagicSet;
    public int maxMagicSetLength = 0;
    public int maxMagicSetIndex = 0;

    /**
     * KingsPalindromeList constructor:
     * read all necessary input and does the necessary calculations.
     */
    KingsPalindromeList() {
        taskNumber = Integer.valueOf(scanner.nextLine());
        int numberOfPalindromes = Integer.valueOf(scanner.nextLine());
        long[] arrayOfPalindromes = new long[numberOfPalindromes];
        for (int i = 0; i < numberOfPalindromes; i++) {
            arrayOfPalindromes[i] = scanner.nextLong();
        }

        // Fix arrayOfPalindromes inplace and store a copy in
        // correctPalindromeList (used for task 1).
        correctPalindromeList = fixPalindromeList(arrayOfPalindromes).clone();

        calculateMagicSets(arrayOfPalindromes, numberOfPalindromes);
    }

    /**
     * Finds the largest magic set.
     */
    void calculateMagicSets(long[] arrayOfPalindromes, int numberOfPalindromes) {
        // sort arrayOfPalindromes in a decending order
        quickSort(arrayOfPalindromes, 0, numberOfPalindromes - 1);
        if (numberOfPalindromes == 1) {
            maxMagicSet = correctPalindromeList;
            maxMagicSetLength = 1;
        } else {
            for (int i = 0; i < numberOfPalindromes - 1; i++) {
                calculateMaxMagicSet(arrayOfPalindromes.clone(), i);
            }
        }
    }

    /** returns an array of digits of a given number. */
    public int[] getDigits(long number) {
        long temp = number; // temporary variable to store the number in
        int numberOfDigits = 0; // counter of the digits of a number
        long[] maxDigits = new long[17]; // has length of the maximum number of digits

        /**
         * using integer division the last digit of a number is
         * separated and inserted into a long enough array of integers
         */
        while (temp > 0) {
            maxDigits[numberOfDigits] = temp - (temp / 10) * 10;
            temp = temp / 10;
            numberOfDigits++;
        }
        int[] digits = new int[numberOfDigits];

        /**
         * digits from int[] maxDigits are inserted into int[] digits in the right order
         * and without unused spaces
         */
        for (int i = 0; i < digits.length; i++) {
            digits[i] = (int) maxDigits[digits.length - 1 - i];
        }
        return digits;
    }

    /** finds the smallest pallindrome greater than given number. */
    long lowestGreaterPalindrome(long number) {
        int[] digits = getDigits(number); // stores digits of a number
        long newNumber = 0; // new palindrome that will be outputted
        /**
         * chceck and changes digits of the second half of a number to be
         * corresponding to the reverse order of digits from the first half
         */
        for (int i = 0; i < (digits.length / 2); i++) {
            if (digits[i] < digits[digits.length - 1 - i]) {
                digits[digits.length - 1 - i] = digits[i];
                digits[digits.length - 2 - i]++;
            } else {
                digits[digits.length - 1 - i] = digits[i];
            }
        }
        /**
         * if the middle digit is increased to 10 during the process of changing
         * digits, the first half of the number has to be incremented, and the digits
         * of the second half adjusted accordingly
         */
        if (digits[digits.length / 2] == 10) {
            long halfNumber = 0; // stores the first half of digits as a number
            for (int i = 0; i < (digits.length / 2); i++) {
                halfNumber += digits[i] * Math.pow(10, (digits.length / 2 - 1 - i));
            }
            halfNumber++;
            int[] firstHalf = getDigits(halfNumber);
            for (int i = 0; i < digits.length / 2; i++) {
                digits[digits.length - 1 - i] = firstHalf[i];
            }

        }
        /** digits are converted back to a number */
        for (int i = 0; i < digits.length; i++) {
            newNumber += digits[i] * (long) Math.pow(10, (digits.length - 1 - i));
        }
        return newNumber;
    }

    /** reconstructs the corresponding list of Palindromes. */
    long[] fixPalindromeList(long[] numbers) {
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = lowestGreaterPalindrome(numbers[i]);
        }
        return numbers;
    }

    /** removes first and last digit of a number and returns this new number. */
    int removeFirstAndLastDigit(long number) {
        int[] digits = getDigits(number);
        int newNumber = 0;
        for (int i = 1; i < digits.length - 1; i++) {
            newNumber += digits[i] * Math.pow(10, (digits.length - 2 - i));
        }
        return newNumber;
    }

    /**
     * Returns the corrected list of palindromes.
     */
    void task1() {
        for (long n : correctPalindromeList) {
            System.out.print(n + " ");
        }
    }

    /**
     * The number of elements of the largest magic set that can be obtained from
     * the correct list.
     */
    void task2() {
        System.out.println(maxMagicSetLength);
    }

    /** prints out the magic set with the highest number of palindromes. */
    void task3() {
        for (int i = maxMagicSetLength - 1; i >= 0; i--) {
            System.out.print(maxMagicSet[i] + " ");
        }
    }

    /**
     * finds a magic set in an array.
     * takes an int[] array, from which elements should the magic set consist, and a
     * pointer
     * int left which serves as a base to which other elements are compared
     */
    void calculateMaxMagicSet(long[] array, int left) {
        int arrayLength = 1; // counts the number of palindromes in this magic set
        int right = left + 1;
        if (array.length == 1) {
            if (maxMagicSet[0] < array[0]) {
                maxMagicSet[0] = array[0];
                maxMagicSetLength = 1;
            }
            return;
        }
        long[] magicSet = new long[array.length];
        int pointer = 0;
        magicSet[pointer] = array[left];
        // while (array[left] / 10 != 0) {
        while (array[left] / 10 != 0) {
            if (array[right] == removeFirstAndLastDigit(array[left])) {
                magicSet[pointer + 1] = array[right];
                pointer++;
                left = right;
                arrayLength++;
            }
            right++;
            if (right == array.length) {
                array[left] = removeFirstAndLastDigit(array[left]);
                right = left + 1;
            }
        }
        if (maxMagicSetLength < arrayLength) {
            maxMagicSetLength = arrayLength;
            maxMagicSet = magicSet;
        }
        // }
    }

    /** sorts an array in descending order. */
    public void quickSort(long[] arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    private int partition(long[] arr, int begin, int end) {
        long pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j] >= pivot) {
                i++;

                long swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }

        long swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    public static void main(String[] args) {
        KingsPalindromeList kingsPalindromeList = new KingsPalindromeList();
        if (kingsPalindromeList.taskNumber == 1) {
            kingsPalindromeList.task1();
        }
        if (kingsPalindromeList.taskNumber == 2) {
            kingsPalindromeList.task2();
        }
        if (kingsPalindromeList.taskNumber == 3) {
            kingsPalindromeList.task3();
        }
    }
}