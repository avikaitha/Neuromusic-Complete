package ark.neuromusic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ark.neuromusic.R;

/**
 * Created by avinash on 4/8/16.
 */
public class SCTrackAdapter extends BaseAdapter {

    private Context mContext;
    private List<Track> mTracks;

    public SCTrackAdapter(Context context, List<Track> tracks) {
        mContext = context;
        mTracks = tracks;
    }

    @Override
    public int getCount() {
        return mTracks.size();
    }

    @Override
    public Track getItem(int position) {
        return mTracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Track track = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.track_list_row, parent, false);
            holder = new ViewHolder();
            holder.trackImageView = (ImageView) convertView.findViewById(R.id.track_image);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.track_title);
            holder.moodIcon = (ImageView)convertView.findViewById(R.id.mood_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleTextView.setText(track.getTitle());

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(mContext)
                .load(track.getArtworkURL())
                .error(R.drawable.placeholder_track_drawable)
                .placeholder(R.drawable.placeholder_track_drawable)
                .into(holder.trackImageView);
        switch (track.getMood()) {
            case "happy" :
                holder.moodIcon.setImageResource(R.drawable.happy_icon);
                break;
            case "angry" :
                holder.moodIcon.setImageResource(R.drawable.angry_icon);
                break;
            case "sad" :
                holder.moodIcon.setImageResource(R.drawable.sad_icon);
                break;
            case "calm" :
                holder.moodIcon.setImageResource(R.drawable.relaxed_icon);
                break;
        }


        return convertView;
    }

    static class ViewHolder {
        ImageView trackImageView;
        TextView titleTextView;
        ImageView moodIcon;
    }

}
