package com.example.harelavikasis.rumpel.Chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.harelavikasis.rumpel.ViewHolders.BaseViewHolder;
import com.example.harelavikasis.rumpel.ViewHolders.MessageLeftHolder;
import com.example.harelavikasis.rumpel.ViewHolders.MessageRightHolder;
import com.example.harelavikasis.rumpel.Models.Question;
import com.example.harelavikasis.rumpel.Managers.UserManger;
import com.example.harelavikasis.rumpel.R;
import com.example.harelavikasis.rumpel.Listeners.OnAnswerClicked;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harelavikasis on 14/03/2017.
 */

public class RecyclerChatAdapterList extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int TYPE_ME = 1;
    private static final int TYPE_HIM = 2;
    private Context mContext;
    private List<Question> questions = new ArrayList<>();
    private Question openQuestion;
    private OnAnswerClicked mlistener;

    public RecyclerChatAdapterList(Context context) {
        this.mContext = context;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_HIM:
                return new MessageLeftHolder(inflater.inflate(R.layout.left_message_item, parent, false));

            default:
                // me
                return new MessageRightHolder(inflater.inflate(R.layout.right_message_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Question q = questions.get(position);
        switch (getItemViewType(position)) {
            case TYPE_HIM:
                ((MessageLeftHolder) holder).bind(q,mlistener);
                break;

            default:
                // me
                ((MessageRightHolder) holder).bind(q,mlistener);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public void setQuestions(List<Question> questions, final OnAnswerClicked listener) {
        this.questions = questions;
        this.mlistener = listener;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Question q = questions.get(position);
        return isMe(q) ? TYPE_ME : TYPE_HIM;
    }

    private boolean isMe(Question q) {
        return UserManger.getInstance().getUserId().equals(q.getSenderId());
    }
}
