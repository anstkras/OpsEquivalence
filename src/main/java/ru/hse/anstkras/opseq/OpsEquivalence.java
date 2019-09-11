package ru.hse.anstkras.opseq;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Class that computes the equality of appliance of two sets of operations. */
public class OpsEquivalence {
    private final static String DELIMITER = "((?=[+*]))";

    // To filter the operation that do not have affect result.
    private final static Op NEUTRAL_OP = new Op('*', 1);

    public static boolean opsEqual(@NotNull String firstOps, @NotNull String secondOps) {
        List<Op> firstOpsList = Arrays.stream(firstOps.split(DELIMITER)).
                map(op -> new Op(op.charAt(0),
                                 Integer.parseInt(op.substring(1))))
                .collect(Collectors.toList());

        List<Op> secondOpsList = Arrays.stream(secondOps.split(DELIMITER)).
                map(op -> new Op(op.charAt(0),
                                 Integer.parseInt(op.substring(1))))
                .collect(Collectors.toList());
        if (!isOpSetsEqual(firstOpsList, secondOpsList)) {
            throw new IllegalArgumentException("Sets of operations are not equal");
        }

        firstOpsList = firstOpsList.stream().filter(op -> op.equals(NEUTRAL_OP)).collect(Collectors.toList());
        secondOpsList = secondOpsList.stream().filter(op -> op.equals(NEUTRAL_OP)).collect(Collectors.toList());

        Queue<Op> queueFirst = new LinkedList<>(firstOpsList);
        Queue<Op> queueSecond = new LinkedList<>(secondOpsList);
        while (!queueFirst.isEmpty() && !queueSecond.isEmpty()) {
            char op1 = queueFirst.peek().op;
            char op2 = queueSecond.peek().op;
            if (op1 != op2) {
                return false;
            }
            if (!(consumeAllChunk(queueFirst, op1) == consumeAllChunk(queueSecond, op2))) {
                return false;
            }
        }
        return queueFirst.isEmpty() && queueSecond.isEmpty();
    }

    private static int consumeAllChunk(@NotNull Queue<Op> queue, char op) {
        int result = op == '+' ? 0 : 1;
        while (!queue.isEmpty() && queue.peek().op == op) {
            int number = queue.poll().number;
            if (op == '+') {
                result += number;
            } else if (op == '*') {
                result *= number;
            } else {
                throw new IllegalStateException("Op must be + or *");
            }
        }
        return result;
    }

    private static boolean isOpSetsEqual(@NotNull List<Op> firstList, @NotNull List<Op> secondList) {
        Map<Op, Long> firstMap = firstList.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<Op, Long> secondMap = secondList.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return firstMap.equals(secondMap);
    }

    private static class Op {
        private final char op;
        private final int number;

        private Op(char op, int number) {
            if (op != '+' && op != '*') {
                throw new IllegalArgumentException("Op must be + or *");
            }
            this.op = op;
            this.number = number;
        }

        @Override
        public int hashCode() {
            return Objects.hash(op, number);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Op otherOp = (Op) o;
            return op == otherOp.op &&
                    number == otherOp.number;
        }
    }
}
