import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.umass.lastfm.Tag;


public class gui {
	static Color black = new Color(0,0,0);
	static Color grey = new Color(100,100,250);
	static Font font = new Font("Candara", Font.PLAIN, 18);
	static Font italic = new Font("Candara", Font.ITALIC, 18);
	static visualisation panel;
	static JTextField searchText;
	
	
		private static void createAndShowGUI() {
			JFrame f = new JFrame("hello :)");
                                                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                                f.setSize(640,480);
			
			//System.setProperty("proxyHost", "wwwcache-20.cs.nott.ac.uk");
			//System.setProperty("proxyPort", "3128");
			String key = "c16451e142b6902ae41ae727b50f6bf3"; 
			String secret = "fc8ebb1b3d9c16be60a488cc322ff048";
			String searchTag = "rock";
			Collection<Tag> tags = Tag.search(searchTag, key);
			
			
			MediaPlayer mp = new MediaPlayer(f);//make da media player - up here so can be passed to sphere
			
			panel = new visualisation(mp);
			f.add(panel, BorderLayout.NORTH);/////////////sphereeeeee
			
			
			
			JPanel search = new JPanel();//middle bar
			search.setBackground(black);			
			search.setLayout(new BoxLayout(search, BoxLayout.LINE_AXIS));
			
			
			JLabel searchLabel = new JLabel("Search: ");//search label
			searchLabel.setFont(font);
			searchLabel.setForeground(grey);

			
			searchText = new JTextField(20);//search entry
			searchText.setFont(italic);
			searchText.setBackground(black);
			searchText.setForeground(grey);
			searchText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			searchText.addActionListener(new ActionListener() {
			      public void actionPerformed(ActionEvent e) {
			    	  tagUpdate();
			      }
			});
			
			search.add(Box.createRigidArea(new Dimension(150, 0)));
			search.add(searchLabel);
			search.add(searchText);
			
			f.add(search, BorderLayout.CENTER);
			
			//media playerrrrrrrr add
			f.add(mp, BorderLayout.SOUTH);
			
			f.pack();
			f.setVisible(true);
			
			
		}
		
		static void tagUpdate() {
			if(!searchText.getText().trim().isEmpty()) {
				searchText.setText(searchText.getText().trim());
				panel.updateTags(searchText.getText());
			}
		}
		
		public static void main(String[] args) {
			createAndShowGUI();
		}
}



