package ElectronicOrgan;

import javax.sound.midi.MidiMessage;

/**
 * @author Student Number: 11020070
 */
public class Buffer {

    //A multidimentional byte buffer array storing 16 elements
    private byte[][] bufferArray = new byte[16][];
    
    //Create counter variables to keep track of the numbers of elements in the array.
    private int count = 0;
    private int getIndex = 0;
    private int putIndex = 0;
    
    // A function to get the MIDI message from the buffer
    public byte[] get() {


        // only progress if there is 1 or more elements in the array.
        if (count > 0) {
          
          /*  
           * Decriment the count variable since we are going to be taking the element
           * out of the buffer array.
           */ 
            count--;
            
            // Incriment the getIndex variable to get the next ellement in the array.
            getIndex++;
            
            /* 
             * Check if getIndex is greater than or equal to the bufferArray length. 
             * If it is then reset the value in getIndex to prevent ArrayOutOfBounds exception.
             */
            if (getIndex >= bufferArray.length) {

                getIndex = 0;

            }

            /*
             * Retreive the MIDI message byte array from bufferArray 
             * at position getIndex and store it in a temporary byte array variable.
             */
            byte[] returnedValue = bufferArray[getIndex];
            
            //Set the bufferArray at the position of getIndex to null to remove any stale data
            bufferArray[getIndex] = null;
            
            return returnedValue;
            
        } else {
            //Return null if the count of the elements in the array is 0.
            return null;
        }//else
    }

  // A function to put MIDI messages into the multi-dimentional buffer array.
    public void put(MidiMessage message) {
        
        /*
         * Check if count has not reached the size of the buffer array so that
         * we dont try to add elements into the array in which there is no space for it.
         */
        if (count < bufferArray.length) {
            
            // Increment the count variable since we are adding elements into the buffer array.
            count++;
            
            //Get the next available space in the bufferarray. 
            putIndex++;

            /*
             * Check if putIndex is not over the bufferArray size length to prevent.
             * ArrayOutOfBounds exeption.
             */
            if (putIndex >= bufferArray.length) {
                putIndex = 0;
            }
           
            //Insert the MIDI message byte array into the bufferArray at location of putIndex.
            bufferArray[putIndex] = message.getMessage();

        }
    }
}