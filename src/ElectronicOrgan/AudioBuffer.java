package ElectronicOrgan;

/*
 * @author Student Number: 11020070
 */
public class AudioBuffer {

    // This method is used to fill the integer buffer passed in via one 
    // of its argument with the number of samples at the sine waveform frequency.
    public static void fillSineBuffer(int[] intBuffer, double samplesPerWave, double gain, int firstSampleNumber, int numberOfSamples) {

        // Acquire the factor to be used to generate the phase angle.
        double factor = 2.0 * Math.PI / samplesPerWave;

        //assign the firstSampleNumber to a local variable.
        int sampleNumber = firstSampleNumber;

        //Loop through each of the number of samples so that one complete wave cylce is mapped.
        for (int i = 0; i < numberOfSamples; i++) {

            // Get the phase angle of the sine waveform by multiplying the factor by the sample number.
            double phaseAngle = factor * sampleNumber;

            //Increment the sampleNumber.
            sampleNumber++;

            // Get the sample containing the sine waveform data by multiplying Math.sin(phaseAngle) by gain.
            int sample = (int) (gain * Math.sin(phaseAngle));

            //Inset the waveform data into the int buffer.
            intBuffer[i] = sample;
        }

    }

    public static void fillSquareBuffer(int[] intBuffer, double samplesPerWave, double gain, int firstSampleNumber, int numberOfSamples) {

        // Variable to hold the the number of sample passed in from the main loop.
        int SampleNumber;

        // Variable to hold the square wave sample audio data to be inserted into the int buffer.
        int sample;

        // Loop through each sample to output each audio data for every iteration through the loop.
        for (int i = 0; i < numberOfSamples; i++) {
            
            // Set up the the variable to use to identify the sample number in the loop.
            SampleNumber = firstSampleNumber + i;
            
            //Get the number of samples within the current wave.
            int samplesInWave = (int)(SampleNumber % samplesPerWave);

            // If samplesInWave is less than half the samplesPerWave, then force 
            // it down to a lower gain. Else, increase the Gain.
            if (samplesInWave < samplesPerWave / 2) {
                
                // Lower the gain to a negative an insert it into the sample variable 
                // ready to be put into the buffer.
                sample = (int) -gain;

            } else {
                
                //Increase the gain to a positive number and insert it into the sample variable.
                sample = (int) +gain;

            }
            
            //Inset the waveform data into the int buffer.
            intBuffer[i] = sample;
        }

    }

    // This method converts the audio data from the left and right initger buffers to a byte buffer.
    public static void convert16bitBigEndian(int[] leftChannel, int[] rightChannel, byte[] byteBuffer) {

        // The variable which will be used to denote the position within the buffer byte array.
        int b = 0;

        // Loop through the length length of one of the int arrays, since both of them are the same length.
        for (int i = 0; i < leftChannel.length; i++) {

            // Insert the content of leftChannel at position i into sample.
            int sample = leftChannel[i];

            // Bit shift the top 8 bits from sample to get 8 bits, and then bit mask it.
            byte msb = (byte) ((sample >> 8) & 0xFF);

            //Bit mask only the bottom 8 bits.
            byte lsb = (byte) (sample & 0xFF);

            // Store in the buffer
            byteBuffer[b] = msb;

            b++; // Big-endian
            byteBuffer[b] = lsb;

            b++;


            // Process next sample for RIGHT channel
            sample = rightChannel[i];

            // Bit shift the top 8 bits from sample to get 8 bits, and then bit mask it.
            msb = (byte) ((sample >> 8) & 0xFF);

            //Bit mask only the bottom 8 bits.
            lsb = (byte) (sample & 0xFF);

            // Store in the buffer
            byteBuffer[b] = msb;

            b++; // Big-endian
            byteBuffer[b] = lsb;

            b++;

        }
    }
}