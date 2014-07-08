package ElectronicOrgan;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

/**
 * @author Student Number: 11020070
 */
public class CustomReceiver implements Receiver {

    // Create an object of type Buffer to store the byte array 
    // passed from the transmitter.
    private Buffer messBuffer;

    /*
     * Constructor is used to instantiate a Buffer
     * object to handle the MIDI mssage byte array.
     */
    public CustomReceiver(Buffer mess) {
        this.messBuffer = mess;
    }
    
    /*
     * The send method of the CustomReceiver is used to 
     * insert MIDI message byte array into the buffer.
     */
    @Override
    public void send(MidiMessage message, long l) {
        messBuffer.put(message);
    }

    // A method to close the CustomeReceiver.
    @Override
    public void close() {
    }
}
