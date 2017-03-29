package com.example.harelavikasis.rumpel.ViewHolders;

import android.graphics.Color;
import android.text.format.DateFormat;
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
public class MessageLeftHolder extends BaseViewHolder {
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

    public MessageLeftHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Question q ,final OnAnswerClicked listener) {
        Question question = q;
        this.mlistener = listener;
        chatText.setText(question.getQuestionText());
        if (question.getQuestionOpen()) {
            this.openQuestion = question;
            setHolder(ansText1, question, 0);
            setHolder(ansText2, question, 1);
            setHolder(ansText3, question, 2);
            setHolder(ansText4, question, 3);
            ansText1.setVisibility(View.VISIBLE);
            ansText2.setVisibility(View.VISIBLE);
            ansText3.setVisibility(View.VISIBLE);
            ansText4.setVisibility(View.VISIBLE);
//            if (question.getSenderId().equals(UserManger.getInstance().getUserId())) {
                ansText1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mlistener.notifyChatForQuestionsAnswer(openQuestion.getAnswers().get(0).getIsRight());
                    }
                });
                ansText2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mlistener.notifyChatForQuestionsAnswer(openQuestion.getAnswers().get(1).getIsRight());
                    }
                });
                ansText3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mlistener.notifyChatForQuestionsAnswer(openQuestion.getAnswers().get(2).getIsRight());
                    }
                });
                ansText4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mlistener.notifyChatForQuestionsAnswer(openQuestion.getAnswers().get(3).getIsRight());
                    }
                });
//            }
        } else {
            ansText1.setVisibility(View.GONE);
            ansText2.setVisibility(View.GONE);
            ansText3.setVisibility(View.GONE);
            ansText4.setVisibility(View.GONE);
            long timeToAnswer = question.getTimeToAnswer();
            String time = (String) DateFormat.format("mm:ss", timeToAnswer);
            if (question.getIsRightAnswer()) {
                chatText.setText(question.getQuestionText() +" "+ getEmojiByUnicode(V_INDICATOR_UNICODE) +" " +time);
//                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
//                        model.getMessageTime()));
            } else {
                chatText.setText(question.getQuestionText() +" "+ getEmojiByUnicode(X_INDICATOR_UNICODE) +" " + time);
            }
            //TODO: here we need to add the total time to answer the question and indicator if its right or worng
        }
        if (question.getSenderId().equals(UserManger.getInstance().getUserId())) {
            // TODO: here we need to decide how to align the message row
            chatText.setBackgroundColor(Color.BLUE);
        }
    }
}
