package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _setting = 0;
    }


    @Override
    void advance() {
        if (setting() == 25) {
          super.set(0);
        } else {
        super.set(setting() + 1);
      }
    }

    @Override
    boolean atNotch() {
      char s = alphabet().toChar(setting());
      if (_notches.contains(Character.toString(s))) {
        return true;
      } else {
        return false;
      }
    }

    @Override
    boolean rotates() {
      return true;
    }

    @Override
    String notches() {
      return _notches;
    }

    public String _notches;
    private int _setting;
}
