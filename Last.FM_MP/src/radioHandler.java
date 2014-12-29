import java.util.Collection;

import de.umass.lastfm.Authenticator;
import de.umass.lastfm.Playlist;
import de.umass.lastfm.Radio;
import de.umass.lastfm.Session;
import de.umass.lastfm.Track;

public class radioHandler {
	
	String key = "c16451e142b6902ae41ae727b50f6bf3"; 
	String secret = "fc8ebb1b3d9c16be60a488cc322ff048";
	
	
	
	
	public Playlist getRadio(String tag) {
                        String token = Authenticator.getToken(key);
                        Session session = Authenticator.getMobileSession("ll901001", "tt910208", key, secret);//.getSession(token, key, secret);
                  
                        System.out.println(session.isSubscriber());
                        Radio radio = Radio.tune(Radio.RadioStation.tagged(tag), session);   
                        System.out.println("radio = null");
                        if(radio != null) {
                                System.out.println("radio != null");
                                Playlist playlist = radio.getPlaylist();
                                return playlist;
                        }      
                           return null;    

                }


}
