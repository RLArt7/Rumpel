    package com.example.harelavikasis.rumpel.Chat;

    import android.content.Context;
    import android.content.Intent;
    import android.content.res.ColorStateList;
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
    import android.widget.EditText;
    import android.widget.RadioButton;

    import com.example.harelavikasis.rumpel.Login.MainLoginActivity;
    import com.example.harelavikasis.rumpel.Managers.ChatManager;
    import com.example.harelavikasis.rumpel.Models.Answer;
    import com.example.harelavikasis.rumpel.Models.Chat;
    import com.example.harelavikasis.rumpel.Models.Question;
    import com.example.harelavikasis.rumpel.Managers.UserManger;
    import com.example.harelavikasis.rumpel.Models.User;
    import com.example.harelavikasis.rumpel.PushNotifications.PushManager;
    import com.example.harelavikasis.rumpel.QuestionsPicker.QuestionsPickerView;
    import com.example.harelavikasis.rumpel.R;
    import com.example.harelavikasis.rumpel.Listeners.OnAnswerClicked;
    import com.example.harelavikasis.rumpel.Utils.Constants;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
    import com.google.gson.Gson;

    import java.util.ArrayList;
    import java.util.List;

    import butterknife.Bind;
    import butterknife.ButterKnife;
    import es.dmoral.prefs.Prefs;

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
        private User contact;

        private DatabaseReference mDatabase;
        private DatabaseReference globalDatabase;
        private DatabaseReference usersRef;
        private DatabaseReference contactRef;

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
            usersRef = FirebaseDatabase.getInstance().getReference();
            contactRef = FirebaseDatabase.getInstance().getReference();
            String facebookId = UserManger.getInstance().getFacebookId();
            if (facebookId == null || facebookId.isEmpty())
            {
                User user = new Gson().fromJson(Prefs.with(this).read(MainLoginActivity.KEY_USER),User.class);
                if (user != null)
                {
                    UserManger.getInstance().setWithUser(user);
                    facebookId = user.getFacebookId();
                }
            }
            usersRef = usersRef.child(Constants.FIREBASE_USERS).child(facebookId).child(Constants.FIREBASE_CHAT_ID_MAP);
            contactRef = contactRef.child(Constants.FIREBASE_USERS).child(contactId);
//            cManger = new ChatManager(this);

            fetchContactInfo();
            setBottomSheet();
            setDatabseChatHistory(contactId);
            fetchRiddleConversation();

        }

        private void fetchContactInfo() {
            contactRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    contact =  dataSnapshot.getValue(User.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
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
                        }
                    }
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        tempSlideOffset = 1;
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

            if (chatId == null && UserManger.getInstance().isChatMapSet())
            {
                chatId = globalDatabase.child(Constants.FIREBASE_CHATS).push().getKey();
                Chat chat = new Chat(endPoint,chatId);
                globalDatabase.child(Constants.FIREBASE_USERS).child(endPointId).child(Constants.FIREBASE_CHAT_ID_MAP).child(UserManger.getInstance().getFacebookId()).setValue(chatId);
                UserManger.getInstance().addChatId(chatId,endPoint);
                usersRef.setValue(UserManger.getInstance().getChatIdMap());

                globalDatabase.child(Constants.FIREBASE_CHATS).child(chatId).setValue(chat);
            }

            mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHATS).child(chatId);
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
            nextScreen.putExtra("chatId", currentChat.getId());
            nextScreen.putExtra("contactToken", contact.getUserToken());
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

            String key1 = globalDatabase.child(Constants.FIREBASE_QUESTIONS).push().getKey();
            Question q1 = new Question(key1 , questionText.getText().toString(), answers);
            globalDatabase.child(Constants.FIREBASE_QUESTIONS).child(q1.getId()).setValue(q1);
            q1.setSenderId(UserManger.getInstance().getUserId());
            q1.initCreationTime();
            if (currentChat.fetchTheOpenQuestion() != null)  currentChat.fetchTheOpenQuestion().closeQuestion();
            currentChat.addQuestion(q1);
            mDatabase.child(Constants.FIREBASE_QUESTIONS).setValue(currentChat.getQuestions());
            mDatabase.child(Constants.FIREBASE_OPEN_QUESTION).setValue(true);

            if (contact.getUserToken() != null ) {
                PushManager pmanager = new PushManager(contact.getUserToken(), UserManger.getInstance().getUserName() + " Sent you a Riddle");
                pmanager.sendPush();
            }
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
            mDatabase.child(Constants.FIREBASE_QUESTIONS).setValue(currentChat.getQuestions());
            mDatabase.child(Constants.FIREBASE_OPEN_QUESTION).setValue(false);
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
