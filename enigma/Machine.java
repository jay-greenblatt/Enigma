package enigma;

import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import static enigma.EnigmaException.*;
import java.util.HashSet;
/** Class that represents a complete enigma machine.
 *  @author
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _myRotors = new Rotor[_numRotors];
        _plugboard = new Permutation("", _alphabet);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        int index = 0;
        for (int i = 0; i < numRotors(); i++) {
          String _name = rotors[i];
          for (Rotor r : _allRotors) {
            String _rname = r.name().toUpperCase();
            if (_rname.equals(_name)) {
              _myRotors[index] = r;
              index += 1;
            }
          }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 0; i < setting.length(); i++) {
            char x = setting.charAt(i);
            _myRotors[i + 1].set(x);
        }

    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    int convert(int c) {
        HashSet<Rotor> adv = new HashSet<Rotor>();
        adv.add(_myRotors[numRotors() - 1]);
        int index = 0;
        while (index <= numRotors() - 2) {
          Rotor a = _myRotors[index];
          Rotor b = _myRotors[index + 1];
          if (b.rotates() && a.atNotch()) {
            adv.add(b);
            adv.add(a);
          }
          if (a.rotates() && b.atNotch()) {
            adv.add(b);
            adv.add(a);
          }
          index += 1;
        }
        for (Rotor r : adv) {
          r.advance();
        }
        int x = c;
        if (!_plugboard.derangement()) {
          x = _plugboard.permute(x);
        }

        for (int i = numRotors() - 1; i >= 0; i--) {
          x = _myRotors[i].convertForward(x);
        }

        for (int i = 1; i < numRotors(); i++) {
          x = _myRotors[i].convertBackward(x);
        }

        if (!_plugboard.derangement()) {
          x = _plugboard.invert(x);
        }

        return x;
      }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        int i = 0;
        String a = "";
        String message = msg.toUpperCase();
        while (i < message.length()) {
          char x = message.charAt(i);
          if (_alphabet.contains(x)) {
          int y = convert(_alphabet.toInt(x));
          a += Character.toString(_alphabet.toChar(y));
          }
          i += 1;
        }
        return a;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    private int _numRotors;
    private int _pawls;
    private Collection<Rotor> _allRotors;
    public Rotor[] _myRotors;
    private char[] _plugboardlist;
    private Permutation _plugboard;
}
