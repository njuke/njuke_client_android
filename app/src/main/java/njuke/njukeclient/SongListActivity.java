package njuke.njukeclient;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import njuke.njukeclient.data.Song;
import njuke.njukeclient.ui.VoteButton;


public class SongListActivity extends ListActivity {

    private String[] songNames = new String[]{"Never Gonna Give You Up", "Never Gonna Give You Up",
                                              "Never Gonna Give You Up", "Never Gonna Give You Up",
                                              "Never Gonna Give You Up", "Never Gonna Give You Up",
                                              "Never Gonna Give You Up", "Never Gonna Give You Up",
                                              "Never Gonna Give You Up", "Never Gonna Give You Up",
                                              "Never Gonna Give You Up", "Never Gonna Give You Up"};
    private String artistName = "Rick Astley";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        ArrayList<Song> songs = new ArrayList<Song>(songNames.length);
        for (String song : songNames) {
            songs.add(new Song(song, artistName));
        }

        final SongAdapter adapter = new SongAdapter(songs);
        adapter.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VoteButton btn = (VoteButton) view;
                Toast.makeText(getApplicationContext(), "Item " + position,
                               Toast.LENGTH_SHORT).show();
                Song song = adapter.getItem(position);
                song.toggleVoted();
                btn.changeNumber(song.isVoted);
            }
        });
        setListAdapter(adapter);
    }

    private class SongAdapter extends BaseAdapter {
        private ArrayList<Song> songs;
        private AdapterView.OnItemClickListener itemClickListener;
        private final LayoutInflater inflater;

        private SongAdapter(ArrayList<Song> songs) {
            this.songs = songs;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        public void addOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public int getCount() {
            return songs.size();
        }

        @Override
        public Song getItem(int position) {
            return songs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View recycledView, ViewGroup parent) {
            ViewHolder vh;
            if (recycledView == null) {
                recycledView = inflater.inflate(R.layout.list_item, parent, false);
                vh = new ViewHolder();
                vh.title = (TextView) recycledView.findViewById(R.id.title);
                vh.artist = (TextView) recycledView.findViewById(R.id.artist);
                vh.voteUp = (VoteButton) recycledView.findViewById(R.id.voteup);
                recycledView.setTag(vh);
            } else {
                vh = (ViewHolder) recycledView.getTag();
            }

            final Song song = getItem(position);
            vh.voteUp.setState(song.isVoted);
            if (itemClickListener != null) {
                vh.voteUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(getListView(), v, position,
                                getItemId(position));
                    }
                });
            }
            vh.title.setText(song.title);
            vh.artist.setText(song.artist);

            return recycledView;
        }

        private class ViewHolder {
            public TextView title;
            public TextView artist;
            public VoteButton voteUp;
        }
    }
}
