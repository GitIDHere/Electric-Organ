package ElectronicOrgan;

/**
 * @author Student Number: 11020070
 */
public class panSource {
    
    
    /* This method fills the left and right int buffers with values representing the stereo 
       filed generated from the MIDI pan position value passed into it via one of its argument. */
    // This code has been adapted from the code presented in the Java for Sound and Music presentation slides jsm2-4Ax.
    public static void stereoPanBufferFill(int midiPanPosition, int[] source, int[] leftBuffer, int[] rightBuffer) {

        final double PAN_ANGLE_MULTIPLIER = Math.PI / (2.0 * 127);

        double panAngle = midiPanPosition * PAN_ANGLE_MULTIPLIER;

        double leftGain = Math.cos(panAngle);

        double rightGain = Math.sin(panAngle);

        // Loop to pan all the samples in the sound source
        for (int i = 0; i < source.length; i++) {
            
            int sample = source[i];
            
            leftBuffer[i] = (int) (leftGain * sample);
            
            rightBuffer[i] = (int) (rightGain * sample);
        }
    }
    
    /* This method calculates the MIDI pan position of the range between and 
     * inclusive of 0 - 127 from the MIDI note number passed into one of its argument. 
     */
    public static int getPanSourcePosition(int noteNumber, int lowestMidiNumber){
        
        /* Calculates the pan position by subtracting the note number by the lowest 
           MIDI number acceptable, multiplying the result of that by 127 and then divding it by 63.*/
        int panPosition =  ((noteNumber - lowestMidiNumber) * 127 / 63);
        
        return panPosition;
    }
    
}//panSource Class end