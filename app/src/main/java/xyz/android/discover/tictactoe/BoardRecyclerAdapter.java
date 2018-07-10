package xyz.android.discover.tictactoe;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.BoardItemViewHolder> {
    private GamePlayed mGamePlayed;
    private Context mContext;
    private int mMaxRowsColumns = Constants.MAX_ROW_COLUMN;
    private String mCurrentPlayer = Constants.KISS;
    private boolean isMoveAllowed = true;

    public BoardRecyclerAdapter(Context context) {
        this.mGamePlayed = (GamePlayed) context;
        this.mContext = context;
    }

    @Override
    public BoardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row, parent, false);
        adaptToScreen(itemView);
        return new BoardItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BoardItemViewHolder holder, final int position) {

        holder.gameActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMoveAllowed) {
                    holder.gameActionButton.setText(mCurrentPlayer);
                    if (mCurrentPlayer.equals(Constants.KISS)) {
                        mGamePlayed.moveMade(position, mCurrentPlayer);
                        mCurrentPlayer = Constants.HUG;
                    } else {
                        mGamePlayed.moveMade(position, mCurrentPlayer);
                        mCurrentPlayer = Constants.KISS;
                    }

                    holder.gameActionButton.setEnabled(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Constants.MAX_ROW_COLUMN * Constants.MAX_ROW_COLUMN;
    }


    public static class BoardItemViewHolder extends RecyclerView.ViewHolder {
        private TextView gameActionButton;

        public BoardItemViewHolder(View itemView) {
            super(itemView);
            gameActionButton = itemView.findViewById(R.id.game_view);
        }


    }

    public interface GamePlayed {
        public void moveMade(int position, String currentPlayer);
    }

    /**
     * This helps in fitting the layout to screen
     * @param convertView
     */
    private void adaptToScreen(View convertView) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        convertView.setMinimumHeight(metrics.heightPixels / (mMaxRowsColumns + 2));
    }

    /**
     * Update clicks on board
     * @param isMoveAllowed
     */
    public void updateBoard(boolean isMoveAllowed) {
        this.isMoveAllowed = isMoveAllowed;
    }

}
