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

//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        Question question = questions.get(position);
//        holder.chatText.setText(question.getQuestionText());
//        if (question.getQuestionOpen()) {
//            this.openQuestion = question;
//            setHolder(holder.ansText1, question, 0);
//            holder.ansText1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mlistener.notifyChatForQuestionsAnswer(openQuestion.getAnswers().get(0).getIsRight());
//                }
//            });
//            setHolder(holder.ansText2, question, 1);
//            holder.ansText2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mlistener.notifyChatForQuestionsAnswer(openQuestion.getAnswers().get(1).getIsRight());
//                }
//            });
//            setHolder(holder.ansText3, question, 2);
//            holder.ansText3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mlistener.notifyChatForQuestionsAnswer(openQuestion.getAnswers().get(2).getIsRight());
//                }
//            });
//            setHolder(holder.ansText4, question, 3);
//            holder.ansText4.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mlistener.notifyChatForQuestionsAnswer(openQuestion.getAnswers().get(3).getIsRight());
//                }
//            });
//            holder.indicaorIV.setVisibility(View.GONE);
//        } else {
//            holder.ansText1.setVisibility(View.GONE);
//            holder.ansText2.setVisibility(View.GONE);
//            holder.ansText3.setVisibility(View.GONE);
//            holder.ansText4.setVisibility(View.GONE);
//            if (question.getIsRightAnswer()) {
//                holder.indicaorIV.setImageResource(R.drawable.ic_thumbs_up);
//            } else {
//                holder.indicaorIV.setImageResource(R.drawable.ic_thumbs_down);
//            }
//            //TODO: here we need to add the total time to answer the question and indicator if its right or worng
//        }
//        if (question.getSenderId().equals(UserManger.getInstance().getUserId())) {
//            // TODO: here we need to decide how to align the message row
//            holder.chatText.setBackgroundColor(Color.BLUE);
//        }
//    }

//    private void setHolder(TextView ansText, Question question, int index) {
//        ansText.setText((index + 1) + ": " + question.getAnswers().get(index).getAnswerText());
//        ansText.setClickable(true);
//    }

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

//    class ViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.chat_text)
//        TextView chatText;
//        @Bind(R.id.answer_1)
//        TextView ansText1;
//        @Bind(R.id.answer_2)
//        TextView ansText2;
//        @Bind(R.id.answer_3)
//        TextView ansText3;
//        @Bind(R.id.answer_4)
//        TextView ansText4;
//        @Bind(R.id.indicator_image_view)
//        ImageView indicaorIV;
//
//        ViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//    }
}
