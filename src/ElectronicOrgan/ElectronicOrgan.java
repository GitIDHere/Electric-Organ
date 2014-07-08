package ElectronicOrgan;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import javax.swing.JOptionPane;
import javax.sound.sampled.*;


import javax.sound.midi.ShortMessage;

/*
 * @author Student Number: 11020070
 */
public class ElectronicOrgan {

    public static void main(String[] args) {


        //Initialise all the variables to be used within the program.

        //This variable holds the sampling rate of 48KHz.
        double samplingRate = 48000;

        //The gain is set to 30000.
        double gain = 30000;

        /*This variable will hold the numbers of samples calculated from the 
         * frequency of the pitch and the sampling rate. */
        double samplesPerWave = 0;

        //Variable will hold the desired buffer size which the user will input.
        int bufferSize = 0;

        /*This variable holds the position of from which to start sampling. 
         * Initially it is set to 0, but it will be incrimented by the number of samples generated. */
        int firstSampleNumber = 0;

        //This variable will hold the note number of the note that the user is pressing.
        int currentNoteNumber = 0;

        //This variable holds a number between 0 - 127 to denote the pan position of a midi pitch.
        int midiPitchPanPosition = 0;

        //Variable to hold the number to multiply the gain by to reduce its value and thus fading the sound from the speakers.
        double gainReductionVal = 0.96;

        // This is the boolean gain flag which will be used in an if statement to check if the value within the gain variable should 
        // be lowered if the user has not pressed a key. When a note off is received lowerGain will be set to true which will act as a 
        // flag to lower the value within the gain variable.
        boolean lowerGain = false;

        // Instantiate a new MidiMonitorWindow object to show the OrganDialoue pane.
        OrganDialogue organ = new OrganDialogue("Organ Message Monitor");

        // Aquire an array of all the devices available on the computer.
        MidiDevice.Info[] infolist = MidiSystem.getMidiDeviceInfo();

        //Retreive a list of transmitter  devices (input sources) after filtering the infolist array.
        MidiDevice.Info[] TransInfoList = RefineInput.filtertrans(infolist);

        //Display a JOptionPane to the user for them to choose a transmitter (input) device. 
        MidiDevice.Info Transinfo = (MidiDevice.Info) JOptionPane.showInputDialog(null, "Please select a keyboard",
                "Keyboard select", JOptionPane.QUESTION_MESSAGE, null, TransInfoList, null);


        //Display a JOptionPane to the user for them to input a buffer size to suit them.
        String userInputBuffer = JOptionPane.showInputDialog(null, "Please enter a buffer size",
                "Buffer Size Input", JOptionPane.QUESTION_MESSAGE);

        /*
         * This if statement checks if the user input is a number of not. if it is a number then assign that number to the 
         * buffer size, else use the default buffer size of 2000. The isInteger method is a custom method created which 
         * checks if the value passed into its argument is an Integer or not. If it is then true is returned, else false is returned. 
         */
        if (isInteger(userInputBuffer)) {

            //If the user entered number is a valid Integer, then assign it into the bufferSize variable.
            bufferSize = Integer.parseInt(userInputBuffer);

        } else {

            //else assign the bufferSize to a default value of 2000.
            bufferSize = 2000;

        }

        /*After the bufferSize has been initalised to an appropriate size either from the user 
         * input or defualt size, it is then passed into numberOfSamples which will be used to 
         * set the size of the int and byte buffers. */
        int numberOfSamples = bufferSize;


// Check if the uer has selected an input device, else exit the program.
if (Transinfo != null) {

    try {

        // Create a MIDI device from the tranmitter Info that the user has selected.
        MidiDevice TransDevice = MidiSystem.getMidiDevice(Transinfo);

        //Open the transmitter (input device).
        TransDevice.open();

        //Instantiate a Transmitter object from the TransDevice that was created.
        Transmitter transmitter = TransDevice.getTransmitter();

        // Instantiate a Buffer object to be passed into a custom receiver.
        Buffer buff = new Buffer();

        // Instantiate the custom receiver, passing in a buffer object so that 
        // all messages from the transmitter are stored within the buffer object that is passed into its argument.
        CustomReceiver customRec = new CustomReceiver(buff);

        /*Set the transmitter's receiver to be the custom recevier. When a note is played, the transmitter will pass 
        the note data into the custom receiver which will store the note data into the Buffer object that was created. */
        transmitter.setReceiver(customRec);

        // Instantiate an AudioFormat object to be used to open the SourceDataLine. This AudioFormat object is instantiated 
        // with having 2 numbers of channel for stereo output.
        AudioFormat audioFormat = new AudioFormat((float)samplingRate, 16, 2, true, true);

        // Create an int buffer object to store the data for the waveforms.
        int[] intBuffer = new int[numberOfSamples];

                /* Create the left and right int buffers to hold the data for stero pan positioning. The data from these buffers will 
                then be combined and convereted into a byte buffer to be inserted as an argument within the SourceDataLine. */
                int[] leftBuffer = new int[numberOfSamples];
                int[] rightBuffer = new int[numberOfSamples];

                // The byteBuffer which will be used to hold the data of the different waveforms. This buffer will then be passed 
                // into the SourceDataLine to generate a tone.
                byte[] byteBuffer = new byte[4 * numberOfSamples];

                // Create a SourceDataLine object which will be used to play the audio data from the byteBuffer.
                SourceDataLine sdl = AudioSystem.getSourceDataLine(audioFormat);

                // Open the SourceDataLine passing in the audioFormat object to outline the type of data which it will output. The 
                // length of the byteBuffer is also required to set up its internet queue length.
                sdl.open(audioFormat, byteBuffer.length);

                //Start the SourceDataLine for it to be ready for input.
                sdl.start();

                /* The main while loop which will run in an infinite loop, constantly checking for input from the transmitter and then 
                 * filling the int buffer with audio data generated from the frequency of the note played. The contents of the int buffer 
                 * is then converted and passed into a byte buffer ready to be inserted into the SourceDataLine. 
                 */
                while (true) {

                    // buff.get() will pass a MIDI message byte array into the byteMess byte array if it has received input from the transmitter, 
                    // i.e. note data such as note on/off, velocity, and status bytes. If no input is recieved from the transmitter, then buff.get() will pass null into byteMess. 
                    byte[] byteMess = buff.get();

                    // Check if the retrieved byte array is not empty.
                    if (byteMess != null) {

                        // Bitmask the first byte in the byte array to get the status byte.
                        int statusTopFourBits = byteMess[0] & 0xF0;

                        //The variables which will be used to hold the note number and the velocity of the key pressed by the user.
                        int noteNumber = 0;
                        int velocity = 0;

                        /*
                         * Check the length of the MIDI message byte array, and assign
                         * velocity and noteNumber accordingly. In some circumstances, the length
                         * of the Midi message byte array will vary, that is why we need this check 
                         * so that we do not assign null values.
                         */
                        if (byteMess.length >= 3) {
                            velocity = byteMess[2];
                        }

                        if (byteMess.length >= 2) {
                            noteNumber = byteMess[1];
                        }
                        
                        // The main switch statement which will be used to check if note on or note off has been recieved. 
                        // If note on has been received then appropriate variables are assigned values to allow for the audio 
                        // playback of MIDI pitch recieved. Else the gain is lowered.
                        switch (statusTopFourBits) {
                            
                            //Check if statusTopFourBits matches a note on message. 
                            case ShortMessage.NOTE_ON:
                                
                                /*  A note on message could be recieved with the velocity of 0 which is a note off message. 
                                    Which is why this if statement has been implimneted to check if the note on message is an actual 
                                    note on, and not a note off message with the velocity of 0. */
                                if (velocity > 0) {
                                    
                                    // Only process the MIDI that are between the MIDI key values of 33 and 96 inclusive.
                                    if (noteNumber >= MidiPitch.MIDI_LOW && noteNumber <= MidiPitch.MIDI_HIGH) {
                                        
                                        // Reset the frstSampleNumber everytime a new note on message is received so that
                                        // the waveform is caluclated from the beginning.
                                        firstSampleNumber = 0;
                                        
                                        //Set lowerGain to false so that the gain is not lowered.
                                        lowerGain = false;
                                        
                                        // Assign midiPitchPanPosition a value between 0 - 127 based on the MIDI 
                                        // note number recived from the note on message.
                                        midiPitchPanPosition = panSource.getPanSourcePosition(noteNumber,  MidiPitch.MIDI_LOW);
                                        
                                        // Assign currentNoteNumber the MIDI note number that was played so that the gain is 
                                        // only lowered when noteNumber and currentNoteNumber are equal.
                                        currentNoteNumber = noteNumber;
                                        
                                        //Reset the gain back to 30,000.
                                        gain = 30000;
                                        
                                        System.out.println(currentNoteNumber);
                                    }
                                    
                                // Check if the current velocity is equal to 0. If it is then a note on message was recevied 
                                // with the velocity of 0, which is infact a note off message. So the procedure of note off will take place.
                                }else if(velocity == 0){
                                        
                                        //Only lower the gain if the note number is same as the note stored in the currentNoteNumber variable
                                        if (noteNumber == currentNoteNumber) {
                                            
                                            //Set the lowerGain flag to be true to enable the lowerin of the gain.
                                            lowerGain = true;
                                        }
                                }

                                break;
                                
                           //This case checks for if a note off message has been recieved.     
                            case ShortMessage.NOTE_OFF:
                                
                                // If a note of message has been recieved, then check if the note number of the note played is the same as the 
                                // number stored in currentNoteNumber. If it is only then lower the gain.
                                if (noteNumber == currentNoteNumber) {
                                    
                                    //Set the lowerGain flag to be true to lower the gain value.
                                    lowerGain = true;
                                }
                                break;
                        }
                    }
                    
                    // The if statement which checks if the lowerGain flag is true or false. 
                    // If it is true then lower the gain, else ignore it.
                    if (lowerGain == true) {
                        
                        // Check if the gain is lower than 100. If it is then force it to 0, 
                        // otherwise it will go into negative numbers.
                        if (gain < 100) {
                            
                            gain = 0;

                        } else {
                            
                            //Reduce the gain by multiplying it by gainReductionVal which is set to 0.96.
                            gain = gain * gainReductionVal;
                        }
                    }

                    // get the samples per wave by dividing the sampling rate with the frequency of the MIDI pitch note.
                    // The samplesPerWave variable will then be used to fill the integer buffer with audio data.
                    samplesPerWave = samplingRate / MidiPitch.frequencyFromMidiPitch(currentNoteNumber);

                    // Switch statement that checks which waveform the organGialogue window is set to. It will run the appropriate 
                    // method to fill the integer buffer based on the waveform the dialogue window is set to.
                    switch (organ.getWaveform()) {
                        case 0:
                            // Fill intBuffer by running the static fillSineBuffer method of the AudioBuffer class. This method
                            // will insert sine waveform data into the intBuffer.
                            AudioBuffer.fillSineBuffer(intBuffer, samplesPerWave, gain, firstSampleNumber, numberOfSamples);
                            break;
                        case 1:
                            // Fill intBuffer by running the static fillSquareBuffer method of the AudioBuffer class. This method
                            // will insert square waveform data into the intBuffer.
                            AudioBuffer.fillSquareBuffer(intBuffer, samplesPerWave, gain, firstSampleNumber, numberOfSamples);
                            break;
                    }

                    // Incriment the firstSampleNumber by the number of samples that have been generated so far.
                    firstSampleNumber += numberOfSamples;
                    
                    /* The static method of panSource is called to fill the left and right in buffers with the appropriate values based 
                     * on the midiPitchPanPosition. Therefore, if the midiPitchPanPosition is 0 then sound will be played only from the left 
                     * speaker. If midiPitchPanPosition is 127 then sound will be placed from the right speaker only. Any other notes in between 
                     * will be played according to its pitch value*/
                    panSource.stereoPanBufferFill(midiPitchPanPosition, intBuffer, leftBuffer, rightBuffer);
                    
                    //Convert the left and right int buffers into a byte buffer ready to be inserted into the SourceDataLine to be played.
                    AudioBuffer.convert16bitBigEndian(leftBuffer, rightBuffer, byteBuffer);
                    
                    //Write to the SourceDataLine playing the audio data stored within byteBuffer.
                    sdl.write(byteBuffer, 0, byteBuffer.length);
                }
                
               // Catch the MidiUnavailableException thrown by SourceDataLine. 
            } catch (LineUnavailableException e) {

                System.out.println(e);
                
              // Catch the MidiUnavailableException thrown by getMidiDevice.  
            } catch (MidiUnavailableException e) {

                System.out.println(e);

            }
            
        } else {
            //This is only reached if the user has not selected a transmitter (an input device).
            System.exit(0);

        }

    }//main();
    
    //A method which is used to check if the string passed in can be converted into an integer or not.
     static boolean isInteger(String s) {
        
        try {
            //Tries to convert the string into an integer.
            Integer.parseInt(s);
          
          //If the string is unable to be converted, then an Exception is thrown and ends the method by returning fasle.  
        } catch (NumberFormatException e) {
            
            return false;
            
        }
        
        // If not Exception has been thrown then the convertion was a success, and thil method will return true.
        return true;
    }
}//class
