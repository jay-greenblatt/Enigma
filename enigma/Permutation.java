package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
      _cycles += ( cycle + ")");
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char x = _alphabet.toChar(p);
        char y = permute(x);
        return _alphabet.toInt(y);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char x = _alphabet.toChar(c);
        char y = (char) invert(x);
        return _alphabet.toInt(y);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (!_cycles.contains(Character.toString(p))) {
          return p;
        }
        int i = _cycles.indexOf(p);
        String _next = Character.toString(_cycles.charAt(i + 1));
        String _last = Character.toString(_cycles.charAt(i));
        if (_next.equals(")")) {
          while (!_last.equals("(")) {
            i -= 1;
            _last = Character.toString(_cycles.charAt(i));
            }
        return _cycles.charAt(i + 1);
      }
        return _next.charAt(0);
}

    /** Return the result of applying the inverse of this permutation to C. */
    int invert(char c) {
      if (!_cycles.contains(Character.toString(c))) {
        return (int) c;
      }
      int i = _cycles.indexOf(c);
      String _last = Character.toString(_cycles.charAt(i - 1));
      String _next = Character.toString(_cycles.charAt(i));
      if (_last.equals("(")) {
        while (!_next.equals(")")) {
          i += 1;
          _next = Character.toString(_cycles.charAt(i));
          }
      return (int) _cycles.charAt(i - 1);
    }
      return (int) _last.charAt(0);
}

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
      if (_cycles.length() == 0) {
        return true;
      } else {
        return false;
      }
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles of this permutation */
    private String _cycles;
    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED
}
