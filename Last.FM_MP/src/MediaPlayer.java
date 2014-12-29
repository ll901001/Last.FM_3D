
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import de.umass.lastfm.Playlist;
import de.umass.lastfm.Track;



public class MediaPlayer extends JPanel {

	boolean isStop = true;// control thread
	boolean hasStop = true;// thread condition
	boolean setPause = false;
	boolean isStart = true;  // play starting music

	String startMusic; 
	String filepath;
	String filename;
	String urlStr;
	AudioInputStream audioInputStream;// Stream
	AudioFormat audioFormat;// 
	SourceDataLine sourceDataLine;// 

	List list;// list holding song names

	JMenuBar menubar;
	JMenu menufile;
	JButton opensong;
	JButton openURL;
	JPanel panelmain ;
	JPanel control;
	Container container;
	JPanel paneright;
	URL url;
	JButton play;
	JButton pause;
	JButton exit;
	PlayThread playThread;
	ImageIcon startPic = new ImageIcon("./src/start.gif");
	ImageIcon pausePic = new ImageIcon("./src/start.gif");
	ImageIcon exitPic = new ImageIcon("./src/start.gif");
	ArrayList<Track> tracks = new ArrayList<Track>();//holds all information for tracks
	JFrame window;
	JButton next;
	Boolean newPlaylist = true;



	public MediaPlayer(JFrame window) {
		this.window = window;
		this.setLayout(new BorderLayout());
		next = new JButton("Next Song");
		pause = new JButton("Play");
		this.setVisible(true);
		list = new List();

		pause.addMouseListener(new MouseAdapter() {   //play pause button
			public void mouseClicked(MouseEvent e) {
				if(hasStop) {//if no song loaded play the next song
					playNextSong();
				} else {//otherwise toggle play pause
					setPause = !setPause;
					setPauseText(setPause);
				}
			}
		});

		next.addMouseListener(new MouseAdapter() {   
			public void mouseClicked(MouseEvent e) {
				if(!isStop) { //As long as music is playing, go to the next song in the playlist
					boolean pauseState = setPause;
					playNextSong();
					if(pauseState) {
						setPause= true;
						setPauseText(true);
					}
				}

			}
		});

		//gui stuff
		panelmain = new JPanel(new GridLayout(2, 1));
		control = new JPanel(new GridLayout(1, 10));
		container = new Container();
		container.setLayout(new GridLayout(1, 1));
		this.add(panelmain, BorderLayout.NORTH);
		this.add(control, BorderLayout.SOUTH);
		this.add(container, BorderLayout.CENTER);
		control.add(pause);
		control.add(next);
		container.add(list);


		//for the option to play a song at starup - not in use
		// close
		//ini();

	}

	public void setPauseText(boolean paused) {//control text on pause button
		if(paused) {
			pause.setText("Play");
		} else {
			pause.setText("Pause");
		}

	}

	private void setTitle() {//set wndow title to now playing
		window.setTitle("Currently Playing: " + tracks.get(0).getArtist() + " - " + tracks.get(0).getName());
	}



	public void openURL(Track track){
		String str = track.getLocation();
		urlStr = str;
		try {
			url = new URL(str);
		} catch (Exception ex) {

		}
		if(!(str.endsWith("mp3")||str.endsWith("wav"))){
			JOptionPane.showMessageDialog(null, "mp3 or wav");
			System.exit(0);
		}   
		tracks.add(track);
		list.add(track.getArtist() + " - " + track.getName());
	}

	public void addPlaylist(Playlist playlist) {
                                System.out.println("Add Playlist");
		if(!tracks.isEmpty()) {
			tracks.clear();
			list.removeAll();
                                                System.out.println("empty");
		} 

		for(Track track : playlist.getTracks()) {
                                                System.out.println("Add Tracks");
			tracks.add(track);
			list.add(track.getArtist() + " - " + track.getName());
		}

	}



	private void playNextSong() {
		if(list.getItemCount() > 0) {
			playThread = null;
			filename = tracks.get(0).getLocation();
			urlStr = tracks.get(0).getLocation();
			setTitle();
			setPause = false;
			play();
			setPauseText(false);
			list.remove(0);
			tracks.remove(0);

		}

	}
	private void play() {

		try {
			isStop = true;// 
			//				
			while (!hasStop) {
				//System.out.print(".");
				try {
					Thread.sleep(10);
				} catch (Exception e) {

				}
			}			


			if (filepath!=null){
				File file = new File(filepath + filename);
				audioInputStream = AudioSystem.getAudioInputStream(file);
			}
			else{
				URL url = new URL(urlStr);  
				audioInputStream = AudioSystem.getAudioInputStream(url);

			}
			//}
			audioFormat = audioInputStream.getFormat();

			if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {

				audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
						audioFormat.getSampleRate(), 16,
						audioFormat.getChannels(),
						audioFormat.getChannels() * 2,
						audioFormat.getSampleRate(), false);

				audioInputStream = AudioSystem.getAudioInputStream(audioFormat,
						audioInputStream);
			}


			DataLine.Info dataLineInfo = new DataLine.Info(
					SourceDataLine.class, audioFormat,
					AudioSystem.NOT_SPECIFIED);
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start();


			isStop = false;
			playThread = new PlayThread();
			playThread.start();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void exit (PlayThread t) throws InterruptedException{
		System.out.println("exit");
		System.exit(0);              

	}  
	private void ini() {
		startMusic = "./src/pic/x.mp3";
		play();
	}

	class PlayThread extends Thread {
		byte tempBuffer[] = new byte[320];

		public void run() {
			try {
				int cnt;
				hasStop = false;
				// 
				while ((cnt = audioInputStream.read(tempBuffer, 0,tempBuffer.length)) != -1) {
					if (isStop)
						break;
					if (cnt > 0) {
						while(setPause)
						{
							this.sleep(1);
						}

						sourceDataLine.write(tempBuffer, 0, cnt);

					}
				}

				sourceDataLine.drain();
				sourceDataLine.close();
				hasStop = true;

			} catch (Exception e) {

				System.exit(0);
			}
		}

	}
}

