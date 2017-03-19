package com.example.harelavikasis.rumpel.Chat;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harelavikasis.rumpel.Models.Question;
import com.example.harelavikasis.rumpel.Models.UserManger;
import com.example.harelavikasis.rumpel.R;
import com.example.harelavikasis.rumpel.listeners.OnAnswerClicked;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by harelavikasis on 14/03/2017.
 */

public class RecyclerChatAdapterList extends RecyclerView.Adapter<RecyclerChatAdapterList.ViewHolder> {

    private Context mContext;
    private List<Question> questions = new ArrayList<>();
    private Question openQuestion;
    private OnAnswerClicked mlistener;
    private int tempIndex = 0;

    public RecyclerChatAdapterList(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.chatText.setText(question.getQuestionText());
        if (question.getQuestionOpen()){
            this.openQuestion = question;
            setHolder(holder.ansText1, question,0);
            setHolder(holder.ansText2, question,1);
            setHolder(holder.ansText3, question,2);
            setHolder(holder.ansText4, question,3);
            holder.indicaorIV.setVisibility(View.GONE);
        }
        else{
            holder.ansText1.setVisibility(View.GONE);
            holder.ansText2.setVisibility(View.GONE);
            holder.ansText3.setVisibility(View.GONE);
            holder.ansText4.setVisibility(View.GONE);
            if (question.getIsRightAnswer()) {
                holder.indicaorIV.setImageResource(R.drawable.ic_thumbs_up);
            }else{
                holder.indicaorIV.setImageResource(R.drawable.ic_thumbs_down);
            }
            //TODO: here we need to add the total time to answer the question and indicator if its right or worng
        }
        if(question.getSenderId().equals(UserManger.getInstance().getUserId())) {
            // TODO: here we need to decide how to align the message row
            holder.chatText.setBackgroundColor(Color.BLUE);
        }
    }

    private void setHolder(TextView ansText, Question question,int index) {
        tempIndex = index;
        ansText.setText((tempIndex+1) + ": " + question.getAnswers().get(tempIndex).getAnswerText());
        ansText.setClickable(true);
        ansText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mlistener.notifyChatForQuestionsAnswer(openQuestion.getAnswers().get(tempIndex).getIsRight());
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public void setQuestions(List<Question> questions , final OnAnswerClicked listener ) {
        this.questions = questions;
        this.mlistener = listener;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.chat_text)
        TextView chatText;
        @Bind(R.id.answer_1)
        TextView ansText1;
        @Bind(R.id.answer_2)
        TextView ansText2;
        @Bind(R.id.answer_3)
        TextView ansText3;
        @Bind(R.id.answer_4)
        TextView ansText4;
        @Bind(R.id.indicator_image_view)
        ImageView indicaorIV;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
