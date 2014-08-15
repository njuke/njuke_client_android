package njuke.njukeclient.data;

/**
 * Represents a song (i.e. a title and an artist).
 */
public class Song implements Comparable<Song> {
    public String title;
    public String artist;
    public int voteCount;
    public boolean isVoted;

    public Song(String title, String artist, int voteCount) {
        this.title = title;
        this.artist = artist;
        this.isVoted = false;
        this.voteCount = voteCount;
    }

    public void toggleVoted() {
        isVoted = !isVoted;
        voteCount += isVoted ? 1 : -1;
    }

    @Override
    public String toString() {
        return title + " - " + artist;
    }

    @Override
    public int compareTo(Song another) {
        return this.voteCount - another.voteCount;
    }

    @Override
    public int hashCode() {
        // TODO: Implement a better hashCode(). Currently it calls Object.hashCode() since the songs
        // in the list (see SongListActivity) have the same hashCode otherwise.
        return title.hashCode() + artist.hashCode() + super.hashCode();
    }
}
