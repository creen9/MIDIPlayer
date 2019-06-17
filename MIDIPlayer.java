package synth;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class MIDIPlayer extends JFrame{
	
	Synthesizer synthesizer;
	MidiChannel kanal;
	int[] note = {51,48,46,53,62,54,55,56,67,57,58,59,50,49,68,69,60,63,52,64,66,47,61,45,44,65}; //Y=44, A=51, Q=60, 1=70

	public MIDIPlayer() {
		JPanel jp = new JPanel();
		jp.setLayout(new GridBagLayout());
		this.add(jp);
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel jl = new JLabel("Choose an instrument(0-127):");
		c.gridx = 0;
		c.gridy = 0;
		jp.add(jl, c);
		
		JTextField tf = new JTextField(10);
		c.gridx = 1;
		c.gridy = 0;
		jp.add(tf, c);
		
		JButton bt = new JButton("Change instrument");
		c.insets = new Insets(0,10,0,0);
		c.gridx = 2;
		c.gridy = 0;
		jp.add(bt, c);
		
		JLabel navodila = new JLabel("<html>Instructions: <br> You play with the keyboard by clicking the Play button. <br> Following keys represent these notes (C4 = Middle C): <br><br> Y: G#2 X: A2 C: A#2 V: B2 B: C3 N: C#3 M: D3 <br> A: D#3 S: E3 D: F3 F: F#3 G: G3 H: G#3 J: A3 K: A#3 L: B3 <br> Q: C4 W: C#4 E: D4 R: D#4 T: E4 Z: F4 U: F#4 I: G4 O: G#4 P: A4 <br> 1: A#4 2: B4 3: C5 4: C#5 5: D5 6: D#5 7: E5 8: F5 9: F#5 0: G5</html>");		
		c.insets = new Insets(20,0,0,0);
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 1;
		jp.add(navodila, c);
		
		
		JButton ja = new JButton("Play");
		c.insets = new Insets(20,20,0,0);
		c.gridx = 0;
		c.gridy = 2;
		jp.add(ja, c);

		
		try {
			synthesizer = MidiSystem.getSynthesizer();
			synthesizer.open();
			kanal = synthesizer.getChannels()[0];
			kanal.programChange(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ja.addKeyListener(new KAdapter());
		
		bt.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				int instrument;
				try {
					instrument = Integer.parseInt(tf.getText());
				} catch (Exception es) {
					instrument = 0;
				}
			    if(instrument >= 0 && instrument < 128) {
			    	kanal.programChange(instrument);
			    }
			 }          
	    });
		
	}

	public static void main(String[] args) {
		MIDIPlayer sintetizator = new MIDIPlayer();
		sintetizator.setSize(500, 400);
		sintetizator.setVisible(true);
		sintetizator.setLocationRelativeTo(null);
		sintetizator.setResizable(false);
		sintetizator.setTitle("MIDI Player");
	}
	
    private class KAdapter extends KeyAdapter {
    	boolean[] keysDown = new boolean[128];

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode(); //A-65 do Z-90  0-9 je 48-57

            if (key > 64 && key < 91) {
            	if (keysDown[key]) return;
                keysDown[key] = true;
            	int nota = note[key-65];
        		try {
        			kanal.noteOn(nota, 60);
        			
        		} catch (Exception ex) {
        			ex.printStackTrace();
        		}
            }
            
            if (key > 47 && key < 58) {
            	if (keysDown[key]) return;
                keysDown[key] = true;
            	int nota;
            	if (key == 48) nota = 79;
            	else nota = key + 21;
        		try {
        			kanal.noteOn(nota, 60);
        			
        		} catch (Exception ex) {
        			ex.printStackTrace();
        		}
            }
        }
        

		public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode(); //A-65 do Z-90  0-9 je 48-57
            
            if (key > 64 && key < 91) {
                keysDown[key] = false;
            	int nota = note[key-65];
        		try {
        			Thread.sleep(100);
        			kanal.noteOff(nota, 60);
        			
        		} catch (Exception ex) {
        			ex.printStackTrace();
        		}
            }
            
            if (key > 47 && key < 58) {
                keysDown[key] = false;
            	int nota;
            	if (key == 48) nota = 79;
            	else nota = key + 21;
        		try {
        			Thread.sleep(100);
        			kanal.noteOff(nota, 60);
        			
        		} catch (Exception ex) {
        			ex.printStackTrace();
        		}
            }
        }
    }

}
