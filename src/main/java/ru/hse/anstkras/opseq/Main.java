package ru.hse.anstkras.opseq;

public class Main {
    private static final String ERROR_MESSAGE = "Incorrect input. Example of correct input: +10*2*3 +10*3*2";
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.out.println(ERROR_MESSAGE);
                return;
            }
            String firstOPs = args[0];
            String secondOps = args[1];
            if (OpsEquivalence.opsEqual(firstOPs, secondOps)) {
                System.out.println("true");
            } else {
                System.out.println("false");
            }
        } catch (Exception e) {
            System.out.println(ERROR_MESSAGE);
        }
    }
}
