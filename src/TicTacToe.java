import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TicTacToe {
    private static final int DEFAULT_SQUARES = 9;

    private enum Symbol {
        INVALID,
        X,
        O;

        Symbol nextMove() {
            switch (this) {
            case X:
                return O;

            case O:
                return X;

            case INVALID:
            default:
                return INVALID;
            }
        }
    }

    private static class Move {
        Symbol symbol;
        int location;

    }

    public static void main(String[] args) {
        List<Move> moves = null;
        Symbol first = Symbol.X;
        int numSquares = DEFAULT_SQUARES;
        for (int i = 0; i < args.length; i++) {
            switch (i) {
            case 0:
                moves = loadMoves(args[i]);
                for(Move mv : moves){
                }
                break;

            case 1:
                first = parseSymbol(args[i]);
                break;

            case 2:
                numSquares = parseInt(args[i]);
                break;
            }
        }
        if (!validateData(moves, first, numSquares)) {
            System.err.println("Failed");
            System.exit(-1);
        }
        // Teach the AI program using the data
    }

    private static List<Move> loadMoves(String filename) {
        File moveFile = new File(filename);
        List<Move> moves = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(moveFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            String move;
            try {
                if ((move = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String split[] = move.split("\\s+");
            Symbol sym = (parseSymbol(split[0]));
            int locations = parseInt(split[1]);
            Move movess = new Move();
            movess.symbol = sym;
            movess.location = locations;
            moves.add(movess);
        }
        return moves;
    }

    private static int parseInt(String num) {
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException ignored) {}
        return 0;
    }

    private static Symbol parseSymbol(String symbol) {
        // If you give me garbage, I'll give it back
        if (symbol == null) {
            return Symbol.INVALID;
        }

        switch (symbol.toUpperCase().trim()) {
        case "X":
            return Symbol.X;

        case "O":
            return Symbol.O;

        default:
            return Symbol.INVALID;
        }
    }

    private static boolean validateData(List<Move> moves, Symbol first, int numSquares) {
        // Validate the number of squares
        boolean success = true;
        double squareOfNumSquares = Math.sqrt(numSquares);
        double remainder = squareOfNumSquares - (int)squareOfNumSquares;
        if (numSquares < DEFAULT_SQUARES || remainder > 0) {
            System.err.println("Error: Invalid number of squares (" + numSquares + ")");
            success = false;
        }

        // Validate the first move
        if (first == Symbol.INVALID) {
            System.err.println("Error: Invalid start Symbol");
            success = false;
        }

        // Validate the number of Moves. Bail early because all the rest
        // of the moves validation relies on having a non-null moves list.
        if (moves == null) {
            System.err.println("Error: No moves loaded");
            return false;
        }

        // Continue validation on number of moves
        if (moves.size() != numSquares) {
            System.err.println("Error: Number of moves does not match number of squares");
            success = false;
        }

        // Validate the actual Moves
        final Set<Integer> usedLocations = new HashSet<>();
        Symbol nextMove = first;
        for (Move m : moves) {
            if (m.symbol == Symbol.INVALID) {
                System.err.println("Error: At least one of the moves has an invalid Symbol");
                success = false;
            }
            if (usedLocations.contains(m.location)) {
                System.err.println("Error: The same location (" + m.location + ") was used multiple times");
                success = false;
            } else {
                usedLocations.add(m.location);
            }
            if (m.symbol != nextMove) {
                System.err.println("Error: Moves did not alternate");
                success = false;
            }
            nextMove = m.symbol.nextMove();
        }

        return success;
    }
}
