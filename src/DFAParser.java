
/**
 * @title  Deterministic Finite Automata parser.
 *
 * @course CSC - 652 - Theory of Computation
 *
 * @date   UNCG Fall 2019
 *
 * @author Joshua Crotts
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class DFAParser {

    private static BufferedReader reader;
    private static Scanner keyboard;

    public static void main (String[] args) throws IOException {

        DFAParser.keyboard = new Scanner(System.in);

        DFA dfa = DFAParser.initializeDFA("dfa.txt");

        System.out.println("Alphabet is: " + dfa.getAlphabet());
        System.out.print("Enter String: ");

        String inputString = DFAParser.keyboard.nextLine();

        System.out.println(DFAParser.wasStringAccepted(inputString, dfa));
    }

    /**
     * Creates the DFA object by loading in the file and parsing through its
     * information.
     *
     * @param txtFile
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static DFA initializeDFA (String txtFile) throws FileNotFoundException, IOException {
        DFAParser.reader = new BufferedReader(new FileReader(txtFile));
        String inputBuffer = "";

        //  Read in the number of states.
        int numberOfStates = Utilities.intParseColon(DFAParser.reader.readLine());

        //  Read in alphabet
        inputBuffer = DFAParser.reader.readLine();
        char[] alphabet = Utilities.splitAlphabetStr(inputBuffer.substring(inputBuffer.indexOf(":") + 1));

        //  Instantiate the DFA with the number of states, which creates
        //  n state objects, and the alphabet.
        DFA dfa = new DFA(numberOfStates, alphabet);

        //  Transition begin
        inputBuffer = DFAParser.reader.readLine();

        //  Loop until we hit the end of the transition function
        while (!((inputBuffer = reader.readLine()).contains("Transitions end"))) {

            //
            //  Parse the input from the .txt file.
            //
            int stateName = DFA.parseStateName(inputBuffer);
            char transitionSymbol = DFA.parseTransitionSymbol(inputBuffer);
            int transitionState = DFA.parseTransitionState(inputBuffer);

            State state = dfa.getState(stateName);
            state.addTransition(transitionSymbol, dfa.getState(transitionState));
            dfa.addTransition(state);
        }

        //
        //  Read in the start state specified by the file, and store it.
        //
        int startState = Utilities.intParseColon(DFAParser.reader.readLine());

        //
        //  Read in the final states, split from the string after the colon,
        //  then split them into an integer array.
        //
        inputBuffer = DFAParser.reader.readLine();
        inputBuffer = Utilities.strParseColon(inputBuffer);
        int[] finalStates = Utilities.splitIntegerStr(inputBuffer, " ");

        dfa.setStartState(startState);

        //
        //  Iterates over the final states array, and defines them as final
        //  in the DFA object.
        //
        for (int i = 0 ; i < finalStates.length ; i++) {
            dfa.addFinalState(finalStates[i]);
        }

        return dfa;
    }

    /**
     * Return string representation/explanation of if the string was accepted by
     * the DFA or not.
     *
     * @param str
     * @return
     */
    private static String wasStringAccepted (String str, DFA dfa) {
        return "The string " + str + " was " + (dfa.parseDFA(str) ? "" : "not ") + "accepted.";
    }
}
