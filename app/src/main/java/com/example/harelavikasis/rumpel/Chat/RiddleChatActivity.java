    package com.example.harelavikasis.rumpel.Chat;

    import android.content.Context;
    import android.content.Intent;
    import android.content.res.ColorStateList;
    import android.graphics.Color;
    import android.os.Build;
    import android.support.annotation.RequiresApi;
    import android.support.design.widget.BottomSheetBehavior;
    import android.support.design.widget.FloatingActionButton;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.support.v7.widget.LinearLayoutManager;
    import android.support.v7.widget.RecyclerView;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.inputmethod.InputMethodManager;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.RadioButton;

    import com.example.harelavikasis.rumpel.Managers.ChatManager;
    import com.example.harelavikasis.rumpel.Models.Answer;
    import com.example.harelavikasis.rumpel.Models.Chat;
    import com.example.harelavikasis.rumpel.Models.Question;
    import com.example.harelavikasis.rumpel.Managers.UserManger;
    import com.example.harelavikasis.rumpel.Models.User;
    import com.example.harelavikasis.rumpel.QuestionsPicker.QuestionsPickerView;
    import com.example.harelavikasis.rumpel.R;
    import com.example.harelavikasis.rumpel.Listeners.OnAnswerClicked;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;
    import java.util.List;

    import butterknife.Bind;
    import butterknife.ButterKnife;

    public class RiddleChatActivity extends AppCompatActivity implements OnAnswerClicked {

        @Bind(R.id.riddle_list)
        RecyclerView riddleList;
        @Bind(R.id.add_question_button)
        FloatingActionButton addQuestionButton;
        @Bind(R.id.bottom_sheet)
        View bottomSheet;


        @Bind(R.id.edit_question)
        EditText questionText;

        @Bind(R.id.edit_answer1)
        EditText answer1Text;
        @Bind(R.id.edit_answer2)
        EditText answer2Text;
        @Bind(R.id.edit_answer3)
        EditText answer3Text;
        @Bind(R.id.edit_answer4)
        EditText answer4Text;

        @Bind(R.id.radio_button1)
        RadioButton radio1;
        @Bind(R.id.radio_button2)
        RadioButton radio2;
        @Bind(R.id.radio_button3)
        RadioButton radio3;
        @Bind(R.id.radio_button4)
        RadioButton radio4;

        private RecyclerChatAdapterList adapter;
        private ChatManager cManger;
        private Chat currentChat;

        private DatabaseReference mDatabase;
        private DatabaseReference globalDatabase;
        private DatabaseReference usersRef;

        private LinearLayoutManager llm = new LinearLayoutManager(this);
        private RiddleChatActivity self = this;
        private BottomSheetBehavior mBottomSheetBehavior;
        private float tempSlideOffset = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_riddle_chat);
            ButterKnife.bind(this);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Intent intent = getIntent();
            String contactName = intent.getStringExtra("name");
            String contactId = intent.getStringExtra("endPointId");
            setTitle(contactName);

            initRecyclerView();

            mDatabase = FirebaseDatabase.getInstance().getReference();
            globalDatabase = FirebaseDatabase.getInstance().getReference();
            usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(UserManger.getInstance().getFacebookId()).child("chatIdMap");
//            cManger = new ChatManager(this);

            //  TODO: to get from the intent the endpointUserId so we can
            // fetch the chat id from it and set the data
            setBottomSheet();
            setDatabseChatHistory(contactId);
            fetchRiddleConversation();
        }

        private void setBottomSheet() {
            mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            mBottomSheetBehavior.setPeekHeight(0);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onStateChanged(View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.PEEK_HEIGHT_AUTO) {

                    }
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        mBottomSheetBehavior.setPeekHeight(0);
                        tempSlideOffset = 0;
                        if (!currentChat.isThereOpenQuestion()) {
                            addQuestionButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.blue_800)));
//                            addQuestionButton.setVisibility(View.VISIBLE);
                        }
//                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(questionText.getWindowToken(), 0);
                    }
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                        answer4Text.setFocusableInTouchMode(true);
//                        answer4Text.requestFocus();
                        tempSlideOffset = 1;
//                        final InputMethodManager inputMethodManager = (InputMethodManager) self.getSystemService(Context.INPUT_METHOD_SERVICE);
//                        inputMethodManager.showSoftInput(answer4Text, InputMethodManager.SHOW_IMPLICIT);

                    }
                }

                @Override
                public void onSlide(View bottomSheet, float slideOffset) {
                    if (slideOffset < tempSlideOffset ){
                        tempSlideOffset = 0;
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(questionText.getWindowToken(), 0);
                    }
                }
            });
        }

        private void initRecyclerView() {

            adapter = new RecyclerChatAdapterList(this);
            llm.setStackFromEnd(true);
            riddleList.setLayoutManager(llm);
            riddleList.setHasFixedSize(true);
            riddleList.setAdapter(adapter);
        }

        private void setDatabseChatHistory(String endPointId) {
//            String fakeChatId = "-Kfc-jVPq8wFpi9Iplmp";
            String endPoint = endPointId;
            String chatId = UserManger.getInstance().getChatIdWithendPointUserId(endPoint);

            if (chatId == null)
            {
                chatId = globalDatabase.child("chats").push().getKey();
                globalDatabase.child("users").child(endPointId).child("chatIdMap").child(UserManger.getInstance().getFacebookId()).setValue(chatId);
                UserManger.getInstance().addChatId(chatId,endPoint);
                usersRef.setValue(UserManger.getInstance().getChatIdMap());
            }

            mDatabase = FirebaseDatabase.getInstance().getReference("chats").child(chatId);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            ButterKnife.unbind(this);
        }

        private void fetchRiddleConversation() {
            // here we call to firebase for data
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentChat =  dataSnapshot.getValue(Chat.class);
                    adapter.setQuestions(currentChat.getQuestions() , self);
                    llm.scrollToPosition(currentChat.getQuestions().size() - 1);
                    checkForOpenQuestion();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
    //        fetchFakeChatHistory();
        }

        private void fetchFakeChatHistory() {
            Chat c = new Chat("111");
            if (c == cManger.fetchConversationByEndPoint(c)){
                Answer b = new Answer("Washington", true);

                List<Answer> answers = new ArrayList<>();
                answers.add(b);
                answers.add(new Answer("New York", false));
                answers.add(new Answer("Los angles", false));
                answers.add(new Answer("Jerusalem", false));

                String key1 = globalDatabase.child("questions").push().getKey();
                Question q1 = new Question(key1 , "where whitehouse?", answers);
                globalDatabase.child("questions").child(q1.getId()).setValue(q1);
                q1.setSenderId(UserManger.getInstance().getUserId());

                String key2 = globalDatabase.child("questions").push().getKey();
                Question q2 = new Question(key2, "where New york?", answers);
                q2.closeQuestion();
                q2.setSenderId("jsdcjals");

                globalDatabase.child("questions").child(q2.getId()).setValue(q2);

                c.addQuestion(q2);
                c.addQuestion(q1);
                String key = globalDatabase.child("chats").push().getKey();
                c.setId(key);
                cManger.updateChatRecord(c);
            }else{
                c = cManger.fetchConversationByEndPoint(c);
            }
            currentChat = c;
            globalDatabase.child("chats").child(currentChat.getId()).setValue(currentChat);
            adapter.setQuestions(currentChat.getQuestions() , this);

            checkForOpenQuestion();
        }

        private void checkForOpenQuestion() {
            if (addQuestionButton != null) {
                if (currentChat.isThereOpenQuestion()) {

                    addQuestionButton.setVisibility(View.GONE);
                } else {
                    addQuestionButton.setVisibility(View.VISIBLE);
                }
            }
        }

        public void onRadioButtonClicked(View v) {

        }
        public void getQuestionFromPool(View v) {
            Intent nextScreen = new Intent(getApplicationContext(), QuestionsPickerView.class);
            nextScreen.putExtra("chatId", currentChat.getId()); //Optional parameters
            startActivity(nextScreen);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void sendRiddleTapped(View v) {

    //        Log.d("uniNote", "send riddle: " + v.getId());
//            Intent nextScreen = new Intent(getApplicationContext(), ScoresTableActivity.class);
//            Intent nextScreen = new Intent(getApplicationContext(), ScoresTabActivity.class);
//
//            startActivity(nextScreen);

            if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                addQuestionButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green_600)));


            }else if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            {
                if (checkQuestionsAndAnswer()) {
                    setNewQuestion();
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        }

        private Boolean checkQuestionsAndAnswer() {
            Boolean isGoodToGo = true;
            if (questionText.getText().toString().trim().equals("")){
                questionText.setError( "Question is required!" );
                isGoodToGo = false;
            }else {
                if (answer1Text.getText().toString().trim().equals("")) {
                    answer1Text.setError( "All answers are required!" );
                    isGoodToGo = false;
                }
                if (answer2Text.getText().toString().trim().equals("")) {
                    answer2Text.setError( "All answers are required!" );
                    isGoodToGo = false;
                }
                if (answer3Text.getText().toString().trim().equals("")) {
                    answer3Text.setError( "All answers are required!" );
                    isGoodToGo = false;
                }
                if (answer4Text.getText().toString().trim().equals("")) {
                    answer4Text.setError( "All answers are required!" );
                    isGoodToGo = false;
                }
            }
            return isGoodToGo;
        }

        private void setNewQuestion() {

            List<Answer> answers = new ArrayList<>();
            answers.add(new Answer(answer1Text.getText().toString(), radio1.isChecked()));
            answers.add(new Answer(answer2Text.getText().toString(), radio2.isChecked()));
            answers.add(new Answer(answer3Text.getText().toString(), radio3.isChecked()));
            answers.add(new Answer(answer4Text.getText().toString(), radio4.isChecked()));

            String key1 = globalDatabase.child("questions").push().getKey();
            Question q1 = new Question(key1 , questionText.getText().toString(), answers);
            globalDatabase.child("questions").child(q1.getId()).setValue(q1);
            q1.setSenderId(UserManger.getInstance().getUserId());

            if (currentChat.fetchTheOpenQuestion() != null)  currentChat.fetchTheOpenQuestion().closeQuestion();
            currentChat.addQuestion(q1);
            mDatabase.child("questions").setValue(currentChat.getQuestions());
            mDatabase.child("thereOpenQuestion").setValue(true);
            emptyAllFields();
        }

        private void emptyAllFields() {
            questionText.setText("");
            answer1Text.setText("");
            answer2Text.setText("");
            answer3Text.setText("");
            answer4Text.setText("");
        }

        @Override
        public void notifyChatForQuestionsAnswer(Boolean isRight) {
            currentChat.fetchTheOpenQuestion().setIsRightAnswer(isRight);
            currentChat.fetchTheOpenQuestion().closeQuestion();
            mDatabase.child("questions").setValue(currentChat.getQuestions());
            mDatabase.child("thereOpenQuestion").setValue(false);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    // app icon in action bar clicked; goto parent activity.
                    this.finish();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }
