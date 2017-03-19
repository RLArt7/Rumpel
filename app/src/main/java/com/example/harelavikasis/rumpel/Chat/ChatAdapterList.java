package com.example.harelavikasis.rumpel.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.harelavikasis.rumpel.Models.Answer;
import com.example.harelavikasis.rumpel.Models.Question;
import com.example.harelavikasis.rumpel.R;

import java.util.ArrayList;

/**
 * Created by harelavikasis on 14/03/2017.
 */

public class ChatAdapterList extends BaseAdapter {

    private Context mContext;
    private ArrayList<Question> questions;
    private static LayoutInflater inflater = null;

    public ChatAdapterList(Context context, ArrayList<Question> questionsArrayList) {
        this.mContext = context;
        this.questions = questionsArrayList;

    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if (vi == null)
            vi = inflater.inflate(R.layout.chat_row, null);
        TextView text = (TextView) vi.findViewById(R.id.chat_text);
        TextView ansText1 = (TextView) vi.findViewById(R.id.answer_1);
        TextView ansText2 = (TextView) vi.findViewById(R.id.answer_2);
        TextView ansText3 = (TextView) vi.findViewById(R.id.answer_3);
        TextView ansText4 = (TextView) vi.findViewById(R.id.answer_4);
        if (questions.get(i).getQuestionOpen())
        {
//            for (int j = 0 ; j < 4 ; j ++)
//            {
//                Answer ans = questions.get(i).getAnswers()[j];

//                ansText1.setText(questions.get(i).getAnswers().get(0).getContext());
//                ansText2.setText(questions.get(i).getAnswers().get(1).getContext());
//                ansText3.setText(questions.get(i).getAnswers().get(2).getContext());
//                ansText4.setText(questions.get(i).getAnswers().get(3).getContext());
//            }
        }
        else{

            ansText1.setVisibility(View.GONE);
            ansText2.setVisibility(View.GONE);
            ansText3.setVisibility(View.GONE);
            ansText4.setVisibility(View.GONE);
        }
        text.setText(questions.get(i).toString());
        return vi;
    }
}
