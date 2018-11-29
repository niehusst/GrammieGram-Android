package com.grammiegram.grammiegram_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grammiegram.grammiegram_android.POJO.Board;
import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.R;
import com.grammiegram.grammiegram_android.activities.BoardActivity;

import java.util.List;

public class BoardListRecyclerAdapter extends RecyclerView.Adapter<BoardListRecyclerAdapter.ViewHolder> {

    private List<Board> boardList;
    private Context mContext;

    public BoardListRecyclerAdapter(BoardListResponse response, Context context) {
        this.mContext = context;
        this.boardList = response.getBoardList();
    }


    @NonNull
    @Override
    public BoardListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View boardListItemView = inflater.inflate(R.layout.list_item_recycler, viewGroup, false);
        return new ViewHolder(boardListItemView);
    }

    /**
     * Put the correct data into viewHolder at index position
     *
     * @param viewHolder - list item view to set bound data for
     * @param position - index of list item
     */
    @Override
    public void onBindViewHolder(@NonNull BoardListRecyclerAdapter.ViewHolder viewHolder, int position) {
        Board board = this.boardList.get(position);

        String boardName = board.getBoardFirstName() + " " + board.getBoardLastName();
        viewHolder.boardNames.setText(boardName);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public Board getItem(int i) {
        return this.boardList.get(i);
    }

    @Override
    public int getItemCount() {
        return boardList == null ? 0 : boardList.size();
    }

    /**
     * Class to hold the inflated views for recycler view
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView explainText;
        TextView boardNames;

        /**
         * Set the views by their corresponding id
         * @param itemView - view to hold
         */
        public ViewHolder(View itemView) {
            super(itemView);

            boardNames = itemView.findViewById(R.id.rv_item_board_names);
            explainText = itemView.findViewById(R.id.rv_item_text);
        }

        /**
         * When a list item is clicked, we must launch the BoardActivity
         * @param view - The list item clicked
         */
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, BoardActivity.class);
            //stand in for finishing activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(intent);
        }
    }

}
