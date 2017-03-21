package com.example.harelavikasis.rumpel.Chat.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.harelavikasis.rumpel.Listeners.OnAnswerClicked;
import com.example.harelavikasis.rumpel.Models.Question;

import butterknife.ButterKnife;

/**
 * Created by harelavikasis on 21/03/2017.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    protected Question openQuestion;
    protected OnAnswerClicked mlistener;


    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected void setHolder(TextView ansText, Question question, int index) {
        ansText.setText((index + 1) + ": " + question.getAnswers().get(index).getAnswerText());
        ansText.setClickable(true);
    }

}
