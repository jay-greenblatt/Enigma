package enigma;

import static enigma.EnigmaException.*;
import org.junit.Test;
import java.util.Collection;
import static enigma.TestUtils.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
public class enigmatesting {

@Test
  public void trivalencode() {

    String x = "testing/correct/default.conf";
    String y = "testing/correct/trivial.inp";
    String z = "testing/correct/trivial.out";

    Main.main(x, y, z);
  }

@Test
  public void caroll2() {

    String x = "testing/correct/default.conf";
    String y = "testing/correct/caroll2.inp";
    String z = "testing/correct/enigmatest.out";

    Main.main(x, y, z);
  }

@Test
  public void caroll3() {

    String x = "testing/correct/caroll3.conf";
    String y = "testing/correct/caroll3.inp";
    String z = "testing/correct/caroll3.out";

    Main.main(x, y, z);
  }
@Test
  public void hiawatha() {

    String x = "testing/correct/default.conf";
    String y = "testing/correct/hiawatha.inp";
    String z = "testing/correct/hiawatha.out";

    Main.main(x, y, z);

  }

  private Alphabet alpha = UPPER;
  @Test
  public void createmachinetest() {
    ArrayList<Rotor> _rotors = new ArrayList<Rotor>();
    Permutation _permutation = new Permutation("(ABCD)", alpha);
    _rotors.add(new Reflector("1", _permutation));
    _rotors.add(new MovingRotor("2", _permutation, "C"));
    _rotors.add(new MovingRotor("3", _permutation, "C"));
    Machine m = new Machine(alpha, 2, 1, _rotors);
    String[] rtrs = new String[2];
    rtrs[0] = "1";
    rtrs[1] = "2";
    m.insertRotors(rtrs);
    assertEquals(m._myRotors[0].name(), "1");
  }

  @Test
  public void basicmachineconvert() {
    ArrayList<Rotor> _rotors = new ArrayList<Rotor>();
    Permutation _permutation = new Permutation("(ABCD)", alpha);
    _rotors.add(new Reflector("1", _permutation));
    _rotors.add(new MovingRotor("2", _permutation, "C"));
    _rotors.add(new MovingRotor("3", _permutation, "C"));
    Machine m = new Machine(alpha, 3, 2, _rotors);
    String[] rtrs = new String[3];
    rtrs[0] = "1";
    rtrs[1] = "2";
    rtrs[2] = "3";
    m.insertRotors(rtrs);
    assertEquals(m.convert(0), 1);
  }

  @Test
  public void helloworld() {
    ArrayList<Rotor> _rotors = new ArrayList<Rotor>();
    Rotor B = new Reflector("B", new Permutation("(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP)" +
              "(RX) (SZ) (TV)", alpha));
    _rotors.add(B);
    Rotor Beta = new FixedRotor("BETA", new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", alpha));
    _rotors.add(Beta);
    Rotor I = new MovingRotor("I", new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", alpha), "Q");
    _rotors.add(I);
    Rotor II = new MovingRotor("II", new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)", alpha), "E");
    _rotors.add(II);
    Rotor III = new MovingRotor("III", new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", alpha), "V");
    _rotors.add(III);
    Machine m = new Machine(alpha, 5, 3, _rotors);
    String[] rtrs = new String[5];
    rtrs[0] = "B";
    rtrs[1] = "BETA";
    rtrs[2] = "I";
    rtrs[3] = "II";
    rtrs[4] = "III";
    m.insertRotors(rtrs);
    assertEquals(m.convert("Hello world"), "ILBDAAMTAZ");
  }
}
