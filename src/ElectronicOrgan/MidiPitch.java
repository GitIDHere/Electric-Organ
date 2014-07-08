package ElectronicOrgan;

/**
 * Student Number: 11020070
 * All the code in this class was made available to us by our lecturer.
 */
public class MidiPitch {
    
  /**
    Obtain the frequency from the notes' midi pitch number
  */
  public static double frequencyFromMidiPitch (int midiPitchNumber) {
    if ((midiPitchNumber < MIDI_LOW) || (midiPitchNumber > MIDI_HIGH)) 
      return 0;
    else
      return freqFromPitch [midiPitchNumber];
  } // frequencyFromMidiPitch ()
  
  /*
   Static initialiser for the freqFromPitch lookup table.
  */
  public static final int MIDI_LOW = 33;
  public static final int MIDI_HIGH = 96;
  private static double [] freqFromPitch = new double [MIDI_HIGH+1];
  static {
    freqFromPitch [33] = 55.00;     // A 55
    freqFromPitch [34] = 58.27;
    freqFromPitch [35] = 61.74;
    freqFromPitch [36] = 65.41;
    freqFromPitch [37] = 69.30;
    freqFromPitch [38] = 73.42;
    freqFromPitch [39] = 77.78;
    freqFromPitch [40] = 82.41;
    freqFromPitch [41] = 87.31;
    freqFromPitch [42] = 92.50;
    freqFromPitch [43] = 98.00;
    freqFromPitch [44] = 103.83;
    freqFromPitch [45] = 110.00;        // A 110
    freqFromPitch [46] = 116.54;
    freqFromPitch [47] = 123.47;
    freqFromPitch [48] = 130.81;
    freqFromPitch [49] = 138.59;
    freqFromPitch [50] = 146.83;
    freqFromPitch [51] = 155.56;
    freqFromPitch [52] = 164.81;
    freqFromPitch [53] = 174.61;
    freqFromPitch [54] = 185.00;
    freqFromPitch [55] = 196.00;
    freqFromPitch [56] = 207.65;
    freqFromPitch [57] = 220.00;        // A 220
    freqFromPitch [58] = 233.08;
    freqFromPitch [59] = 249.94;
    freqFromPitch [60] = 261.63;        // Mid C
    freqFromPitch [61] = 277.18;
    freqFromPitch [62] = 293.66;
    freqFromPitch [63] = 311.13;
    freqFromPitch [64] = 329.63;
    freqFromPitch [65] = 349.23;
    freqFromPitch [66] = 369.99;
    freqFromPitch [67] = 392.00;
    freqFromPitch [68] = 415.30;
    freqFromPitch [69] = 440.00;        // A 440
    freqFromPitch [70] = 466.16;
    freqFromPitch [71] = 493.88;
    freqFromPitch [72] = 523.25;
    freqFromPitch [73] = 554.37;
    freqFromPitch [74] = 587.33;
    freqFromPitch [75] = 622.77;
    freqFromPitch [76] = 659.26;
    freqFromPitch [77] = 698.46;
    freqFromPitch [78] = 739.99;
    freqFromPitch [79] = 783.99;
    freqFromPitch [80] = 830.61;
    freqFromPitch [81] = 880.00;        // A 880
    freqFromPitch [82] = 932.33;
    freqFromPitch [83] = 987.77;
    freqFromPitch [84] = 1046.50;
    freqFromPitch [85] = 1108.73;
    freqFromPitch [86] = 1174.66;
    freqFromPitch [87] = 1244.51;
    freqFromPitch [88] = 1318.51;
    freqFromPitch [89] = 1396.91;
    freqFromPitch [90] = 1479.98;
    freqFromPitch [91] = 1567.98;
    freqFromPitch [92] = 1661.22;
    freqFromPitch [93] = 1760.00;       // A 1760
    freqFromPitch [94] = 1864.66;
    freqFromPitch [95] = 1975.53;
    freqFromPitch [96] = 2093.00;
  } // static
    
}
