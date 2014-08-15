package njuke.njukeclient;

import android.animation.ObjectAnimator;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import njuke.njukeclient.data.Song;
import njuke.njukeclient.ui.VoteButton;


public class SongListActivity extends ListActivity {
    /* Debug tag. */
    @SuppressWarnings("UnusedDeclaration")
    public static final String TAG = SongListActivity.class.getSimpleName();

    private String[] songNames = new String[]{"Never Gonna Give You Up", "Never Gonna Give You Up",
                                              "Never Gonna Give You Up", "Never Gonna Give You Up",
                                              "Never Gonna Give You Up", "Never Gonna Give You Up",
                                              "Never Gonna Give You Up", "Never Gonna Give You Up",
                                              "Never Gonna Give You Up", "Never Gonna Give You Up",
                                              "Never Gonna Give You Up", "Never Gonna Give You Up"};
    private String artistName = "Rick Astley";
    private SongAdapter adapter;
    private int mAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        mAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime) + 100;

        ArrayList<Song> songs = new ArrayList<Song>(songNames.length);
        for (String song : songNames) {
            songs.add(new Song(song, artistName, 1));
        }

        adapter = new SongAdapter(this, songs);
        adapter.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SongAdapter adapter = (SongAdapter) parent.getAdapter();
                adapter.getItem(position).toggleVoted();
            }
        });
        setListAdapter(adapter);
    }

    private class SongAdapter extends BaseAdapter {
        /* Backing data store. */
        private ArrayList<Song> songs;
        /* OnItemClickListener to call when the vote button is clicked. */
        private AdapterView.OnItemClickListener itemClickListener;
        /* HashMap for saving state for the animations. */
        private HashMap<Long, Integer> mSavedState = new HashMap<Long, Integer>();
        /* Which interpolator to use when animating list changes. */
        private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
        /* The LayoutInflater to use when creating new list item views. */
        private final LayoutInflater inflater;

        private SongAdapter(Context context, ArrayList<Song> songs) {
            this.songs = songs;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
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
            return getItem(position).hashCode();
        }

        private class ViewHolder {
            public TextView title;
            public TextView artist;
            public VoteButton voteUp;
        }

        @Override
        public View getView(final int position, View recycledView, ViewGroup parent) {
            final ViewHolder vh;
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
            vh.voteUp.setText("+" + song.voteCount);
            if (itemClickListener != null) {
                vh.voteUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(getListView(), v, position,
                                getItemId(position));
                        saveState();
                        Collections.sort(songs, Collections.reverseOrder());
                        notifyDataSetChanged();
                    }
                });
            }
            vh.title.setText(song.title);
            vh.artist.setText(song.artist);

            return recycledView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            animateNewState();
        }

        /**
         * Save the state of the UI elements in the ListView. Should be called before
         * rearranging the list items to enable the animations.
         */
        public void saveState() {
            mSavedState.clear();
            ListView lv = getListView();
            int firstVisiblePos = lv.getFirstVisiblePosition();
            for(int i = 0; i < lv.getChildCount(); i++) {
                View v = lv.getChildAt(i);
                int top = v.getTop();
                long id = adapter.getItemId(firstVisiblePos + i);
                mSavedState.put(id, top);
            }
        }

        /**
         * Animate views into their new place in the list. Only animates if saveState has been
         * called before reordering the list.
         */
        private void animateNewState() {
            ListView lv = getListView();
            int first = lv.getFirstVisiblePosition();
            for(int i=0; i < lv.getChildCount(); i++) {
                long id = adapter.getItemId(first + i);
                if (mSavedState.containsKey(id)) {
                    View v = lv.getChildAt(i);
                    int top = v.getTop();
                    int oldTop = mSavedState.get(id);
                    // Create and start animation.
                    ObjectAnimator animation = ObjectAnimator.ofFloat(v, "y", oldTop, top);
                    animation.setDuration(mAnimationDuration);
                    animation.setInterpolator(mInterpolator);
                    animation.start();
                }
            }
        }
    }
}
