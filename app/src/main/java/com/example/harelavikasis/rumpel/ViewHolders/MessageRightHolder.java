package com.example.harelavikasis.rumpel.ViewHolders;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harelavikasis.rumpel.Listeners.OnAnswerClicked;
import com.example.harelavikasis.rumpel.Managers.UserManger;
import com.example.harelavikasis.rumpel.Models.Question;
import com.example.harelavikasis.rumpel.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by harelavikasis on 21/03/2017.
 */
public class MessageRightHolder extends BaseViewHolder{
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

    public MessageRightHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bind(Question q ,final OnAnswerClicked listener) {
        Question question = q;
        this.mlistener = listener;
        chatText.setText(question.getQuestionText());
        if (question.getQuestionOpen()) {
            this.openQuestion = question;
            setHolder(ansText1, question, 0);
            ansText1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.notifyChatForQuestionsAnswer(openQuestion.getAnswers().get(0).getIsRight());
                }
            });
            setHolder(ansText2, question, 1);
            ansText2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.notifyChatForQuestionsAnswer(openQuestion.getAnswers().get(1).getIsRight());
                }
            });
            setHolder(ansText3, question, 2);
            ansText3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.notifyChatForQuestionsAnswer(openQuestion.getAnswers().get(2).getIsRight());
                }
            });
            setHolder(ansText4, question, 3);
            ansText4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.notifyChatForQuestionsAnswer(openQuestion.getAnswers().get(3).getIsRight());
                }
            });
            indicaorIV.setVisibility(View.GONE);
        } else {
            ansText1.setVisibility(View.GONE);
            ansText2.setVisibility(View.GONE);
            ansText3.setVisibility(View.GONE);
            ansText4.setVisibility(View.GONE);
            if (question.getIsRightAnswer()) {
                indicaorIV.setImageResource(R.drawable.ic_thumbs_up);
            } else {
                indicaorIV.setImageResource(R.drawable.ic_thumbs_down);
            }
            //TODO: here we need to add the total time to answer the question and indicator if its right or worng
        }
        if (question.getSenderId().equals(UserManger.getInstance().getUserId())) {
            // TODO: here we need to decide how to align the message row
            chatText.setBackgroundColor(Color.BLUE);
        }
    }
}
