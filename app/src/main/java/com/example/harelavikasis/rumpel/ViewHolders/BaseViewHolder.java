package com.example.harelavikasis.rumpel.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.harelavikasis.rumpel.Listeners.OnAnswerClicked;
import com.example.harelavikasis.rumpel.Models.Question;

/**
 * Created by harelavikasis on 21/03/2017.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    protected Question openQuestion;
    protected OnAnswerClicked mlistener;

    protected static final int V_INDICATOR_UNICODE = 0x2714;
    protected static final int X_INDICATOR_UNICODE = 0x2716;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected void setHolder(TextView ansText, Question question, int index) {
        ansText.setText((index + 1) + ": " + question.getAnswers().get(index).getAnswerText());
        ansText.setClickable(true);
    }
    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

}
