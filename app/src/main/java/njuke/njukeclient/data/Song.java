package njuke.njukeclient.data;

/**
 * Represents a song (i.e. a title and an artist).
 */
public class Song {
    public String title;
    public String artist;
    public boolean isVoted;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
        this.isVoted = false;
    }

    public void toggleVoted() {
        isVoted = !isVoted;
    }

    public String toString() {
        return title + " - " + artist;
    }
}
