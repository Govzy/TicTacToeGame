package xyz.android.discover.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements BoardRecyclerAdapter.GamePlayed {


    private static final String TAG = GameActivity.class.getSimpleName();
    //View Declaration
    private Button mNewGameButton;
    private RecyclerView mGameBoardRecyclerView;
    private BoardRecyclerAdapter mBoardRecyclerAdapter;
    private TextView mCurrentPlayerText;
    //Variable declaration
    private int mMaxRowColumn = Constants.MAX_ROW_COLUMN;
    private String[][] mBoardArray = new String[Constants.MAX_ROW_COLUMN][Constants.MAX_ROW_COLUMN];
    private int mResultCheckCondition = (Constants.MAX_ROW_COLUMN * 2) - 1;
    private int mCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //Initialize Views
        mGameBoardRecyclerView = findViewById(R.id.recycler_view);
        mCurrentPlayerText = findViewById(R.id.current_player_text);
        mBoardRecyclerAdapter = new BoardRecyclerAdapter(this);
        mNewGameButton = findViewById(R.id.new_game_button);
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }
        });
        setUpGameBoard();
    }

    /**
     * Use this method to setup game board
     */
    private void setUpGameBoard() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, mMaxRowColumn);
        mGameBoardRecyclerView.setLayoutManager(gridLayoutManager);
        mGameBoardRecyclerView.setAdapter(mBoardRecyclerAdapter);
        mCurrentPlayerText.setText(getResources().getString(R.string.current_player) + Constants.KISS);
        mBoardRecyclerAdapter.updateBoard(true);
    }

    /**
     * Interface call back when a move is made
     *
     * @param position      - position of move made
     * @param currentPlayer - current player
     */
    @Override
    public void moveMade(int position, String currentPlayer) {
        mCurrentPlayerText.setText(getResources().getString(R.string.current_player) + currentPlayer);
        //Get row and column of selected move
        int row = position / Constants.MAX_ROW_COLUMN;
        int column = position % Constants.MAX_ROW_COLUMN;
        //Populate game board array
        mBoardArray[row][column] = currentPlayer;
        mCounter++;
        //Check for result only after minimum number of required moves are made
        if (mCounter >= mResultCheckCondition) {
            if (checkRowResult(row, column, currentPlayer)) {
                announceResult(currentPlayer);
            } else if (checkColumnResult(row, column, currentPlayer)) {
                announceResult(currentPlayer);
            } else if (checkDiagonal(row, column, currentPlayer)) {
                announceResult(currentPlayer);
            } else if (checkAntiDiagonal(row, column, currentPlayer)) {
                announceResult(currentPlayer);
            } else if (check2x2Result(position, currentPlayer)) {
                announceResult(currentPlayer);
            } else if (checkCorners(currentPlayer)) {
                announceResult(currentPlayer);
            }
        }
    }

    private void announceResult(String currentPlayer) {
        Log.d(TAG, "Winner is " + currentPlayer);
        stopFurtherMoves();
        mCurrentPlayerText.setText(getResources().getString(R.string.result_message) + currentPlayer);
    }

    /**
     * Use this method to check corners
     * @param currentPlayer
     * @return true if current player won if not false
     */
    private boolean checkCorners(String currentPlayer) {
        if (TextUtils.equals(mBoardArray[0][0], currentPlayer) && TextUtils
                .equals(mBoardArray[0][mMaxRowColumn - 1], currentPlayer)
                && TextUtils.equals(mBoardArray[mMaxRowColumn - 1][0], currentPlayer) &&
                TextUtils.equals(mBoardArray[mMaxRowColumn - 1][mMaxRowColumn - 1], (currentPlayer))) {
            return true;

        } else {
            return false;
        }
    }

    /**
     * Use this method to check anti-diagonal
     * @param row - current row
     * @param column - current column
     * @param currentPlayer - current player
     * @return
     */
    private boolean checkAntiDiagonal(int row, int column, String currentPlayer) {
        if (row + column == mMaxRowColumn - 1) {
            for (int i = 0; i < mMaxRowColumn; i++) {
                if (mBoardArray[i][(mMaxRowColumn - 1) - i] == null || !mBoardArray[i][(mMaxRowColumn - 1) - i]
                        .equals(currentPlayer)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Use this method to check diagonal
     * @param row - current row
     * @param column - current columns
     * @param currentPlayer - current player
     * @return
     */
    private boolean checkDiagonal(int row, int column, String currentPlayer) {
        if (row == column) {
            for (int i = 0; i < mMaxRowColumn; i++) {
                if (mBoardArray[i][i] == null || !mBoardArray[i][i].equals(currentPlayer)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Use this method to check row
     * @param row - current row
     * @param column - current column
     * @param currentPlayer - current player
     * @return
     */
    private boolean checkRowResult(int row, int column, String currentPlayer) {
        //Iteration can be made by only using columns and by keeping rows as constant but just
        // adding rows here for readability and clarity
        for (int i = row; i < row + 1; i++) {
            for (int j = 0; j < mMaxRowColumn; j++) {
                if (mBoardArray[i][j] == null || !mBoardArray[i][j].equals(currentPlayer)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Use this method to check column result
     * @param row - current row
     * @param column - current column
     * @param currentPlayer - current player
     * @return
     */
    private boolean checkColumnResult(int row, int column, String currentPlayer) {
        //Iteration can be made by only using rows and by keeping columns as constant but just
        // adding columns here for readability and clarity
        for (int i = 0; i < mMaxRowColumn; i++) {
            for (int j = column; j < column + 1; j++) {
                if (mBoardArray[i][j] == null || !mBoardArray[i][j].equals(currentPlayer)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Use this method to check 2X2 squares
     * @param position - current position
     * @param currentPlayer - current player
     * @return
     */
    private boolean check2x2Result(int position, String currentPlayer) {
        int startRowIndex = position - 5;
        int startColumnIndex = position - 5;
        if (startRowIndex < 0) {
            startRowIndex = 0;
        }
        if (startColumnIndex < 0) {
            startColumnIndex = 0;
        }
        int endRowIndex = position + 5;
        int endColumnIndex = position + 5;
        if (endRowIndex > 15) {
            endRowIndex = 15;
        }
        if (endColumnIndex > 15) {
            endColumnIndex = 15;
        }
        for (int i = startRowIndex / mMaxRowColumn; i < endRowIndex / mMaxRowColumn; i++) {
            for (int j = startColumnIndex % mMaxRowColumn; j < endColumnIndex % mMaxRowColumn; j++) {
                int sumX = 0;
                for (int p = i; p < 2 + i; p++) {
                    for (int q = j; q < 2 + j; q++) {
                        if (mBoardArray[p][q] == null) {

                        } else if (mBoardArray[p][q].equals(currentPlayer)) {
                            sumX++;
                        }
                    }
                }
                if (sumX == 4) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Stop further moves and disable on clicks
     */
    private void stopFurtherMoves() {
        mBoardRecyclerAdapter.updateBoard(false);
    }

}
