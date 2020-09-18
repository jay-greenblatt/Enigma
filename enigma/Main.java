package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.String;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine _enigma = readConfig();
        String settings = _input.nextLine();
        setUp(_enigma, settings);
        while (_input.hasNextLine()) {
            if (_input.hasNext("[*].*")) {
              settings = _input.nextLine();
              if (settings.equals("")) {
                settings = _input.nextLine();
              }
              setUp(_enigma, settings);
              _output.println();
            }
            if (!_input.hasNext("[*].*")) {

            String _message = _input.nextLine();
            String _convert = _enigma.convert(_message.toUpperCase());
            printMessageLine(_convert);
          }
          }
      }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String _c = _config.next();
            _alphabet = new CharacterRange(_c.charAt(0), _c.charAt(2));
            int _numrotors = Integer.parseInt(_config.next());
            int _numpawls = Integer.parseInt(_config.next());
            ArrayList<Rotor> rtrs = new ArrayList<Rotor>();
            while (_config.hasNext()) {
              Rotor _rotor = readRotor();
              rtrs.add(_rotor);
            }
            return new Machine(_alphabet, _numrotors, _numpawls, rtrs);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String _name = _config.next();
            String _type = _config.next();
            String _perm = "";
            while (_config.hasNext("[(].*")) {
              String x = _config.next();
              _perm += x;
            }
            Permutation _permutation = new Permutation(_perm, _alphabet);
            if (_type.charAt(0) == "M".charAt(0)) {
              String _notches = "";
              for (int i = 1; i < _type.length(); i++) {
                char _letter = _type.charAt(i);
                _notches += Character.toString(_letter);
              }
              return new MovingRotor(_name, _permutation, _notches);
          } else if (_type.charAt(0) == "R".charAt(0)) {
              return new Reflector(_name, _permutation);
          } else {
              return new FixedRotor(_name, _permutation);
            }

        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        int i = 2;
        int rtri = 0;
        String[] rotors = new String[M.numRotors()];
        String set = "";
        String _rotorname = "";
        while (rtri <= M.numRotors() - 1 && i <= settings.length() - 1) {
          char dummy = settings.charAt(i);
          if (Character.isWhitespace(dummy)) {
            rotors[rtri] = _rotorname;
            rtri += 1;
            _rotorname = "";
            i += 1;
        } else if (dummy != '*') {
          _rotorname += Character.toString(dummy);
          i += 1;
        }
      }
        M.insertRotors(rotors);
        if (i + M.numRotors() - 1 <= settings.length()) {
        set += settings.substring(i, i + M.numRotors() - 1);
        i += set.length();
        M.setRotors(set);
        }
        if (i + 2 <= settings.length() - 1) {
          i += 1;
          String _pb = "";
          _pb += settings.substring(i, settings.length());
          Permutation _plugboard = new Permutation(_pb, _alphabet);
          M.setPlugboard(_plugboard);
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String _msg = "";
        String _word = "";
        int index = 0;
        while (index < msg.length()) {
          char c = msg.charAt(index);
          if (_word.length() == 5) {
            _msg += _word + " ";
            _word = "";
          }
          if (!Character.isWhitespace(c)) {
            _word += Character.toString(c);
          }
          index += 1;
        }
        _msg += _word;
        _output.println(_msg);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
