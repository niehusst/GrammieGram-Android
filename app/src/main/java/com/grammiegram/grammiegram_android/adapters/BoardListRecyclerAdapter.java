package com.grammiegram.grammiegram_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grammiegram.grammiegram_android.POJO.Board;
import com.grammiegram.grammiegram_android.POJO.BoardListResponse;
import com.grammiegram.grammiegram_android.R;
import com.grammiegram.grammiegram_android.activities.BoardActivity;
import com.grammiegram.grammiegram_android.interfaces.ItemClickListener;

import java.util.List;

public class BoardListRecyclerAdapter extends RecyclerView.Adapter<BoardListRecyclerAdapter.ViewHolder> {

    private List<Board> boardList;
    private Context mContext;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    public BoardListRecyclerAdapter(BoardListResponse response, Context context, ItemClickListener listerActivity) {
        this.mContext = context;
        this.boardList = response.getBoardList();
        this.inflater = LayoutInflater.from(this.mContext);
        this.clickListener = listerActivity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
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
    class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener {

        TextView explainText;
        TextView boardNames;

        /**
         * Set the views by their corresponding id
         * @param itemView - view to hold
         */
        ViewHolder(View itemView) {
            super(itemView);

            boardNames = itemView.findViewById(R.id.rv_item_board_names);
            explainText = itemView.findViewById(R.id.rv_item_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * When a list item is clicked, we must launch the BoardActivity
                 *
                 * @param view - The list item clicked
                 */
                @Override
                public void onClick(View view) {
                    if(clickListener != null) {
                        clickListener.onItemClick(view, getAdapterPosition());
                    } //else perform no action
                }
            });
        }
    }
}
