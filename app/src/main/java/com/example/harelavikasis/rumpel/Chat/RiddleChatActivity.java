    package com.example.harelavikasis.rumpel.Chat;

    import android.support.design.widget.BottomSheetBehavior;
    import android.support.design.widget.FloatingActionButton;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.support.v7.widget.LinearLayoutManager;
    import android.support.v7.widget.RecyclerView;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageButton;

    import com.example.harelavikasis.rumpel.Managers.ChatManager;
    import com.example.harelavikasis.rumpel.Models.Answer;
    import com.example.harelavikasis.rumpel.Models.Chat;
    import com.example.harelavikasis.rumpel.Models.Question;
    import com.example.harelavikasis.rumpel.Managers.UserManger;
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

        private RecyclerChatAdapterList adapter;
        private ChatManager cManger;
        private Chat currentChat;
        private DatabaseReference mDatabase;
        private DatabaseReference globalDatabase;

        private RiddleChatActivity self = this;
        private BottomSheetBehavior mBottomSheetBehavior;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_riddle_chat);
            ButterKnife.bind(this);

            // TODO: need to be mooved and set in the loginpage
            UserManger.getInstance().setUserId("helloWorld1234");
            UserManger.getInstance().setUserName("Harel");

            initRecyclerView();

            mDatabase = FirebaseDatabase.getInstance().getReference();
            globalDatabase = FirebaseDatabase.getInstance().getReference();
            cManger = new ChatManager(this);

            //  TODO: to get from the intent the endpointUserId so we can
            // fetch the chat id from it and set the data
            setBottomSheet();
            setDatabseChatHistory("TODO:");
            fetchRiddleConversation();
        }

        private void setBottomSheet() {
            mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            mBottomSheetBehavior.setPeekHeight(0);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        mBottomSheetBehavior.setPeekHeight(0);
                        if (!currentChat.isThereOpenQuestion()) {
                            addQuestionButton.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onSlide(View bottomSheet, float slideOffset) {
                }
            });
        }

        private void initRecyclerView() {

            adapter = new RecyclerChatAdapterList(this);

            riddleList.setLayoutManager(new LinearLayoutManager(this));
            riddleList.setHasFixedSize(true);
            riddleList.setAdapter(adapter);
        }

        private void setDatabseChatHistory(String chatId) {
            String fakeChatId = "-Kfc-jVPq8wFpi9Iplmp";
            mDatabase = FirebaseDatabase.getInstance().getReference("chats").child(fakeChatId);
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
            if (currentChat.isThereOpenQuestion()){
                addQuestionButton.setVisibility(View.GONE);
            }
            else
            {
                addQuestionButton.setVisibility(View.VISIBLE);
            }
        }

        public void sendRiddleTapped(View v) {

    //        Log.d("uniNote", "send riddle: " + v.getId());
    //        Intent nextScreen = new Intent(getApplicationContext(), ScoresTableActivity.class);
    //        Intent nextScreen = new Intent(getApplicationContext(), ScoresTabActivity.class);

    //        startActivity(nextScreen);


//            List<Answer> answers = new ArrayList<>();
//            answers.add(new Answer("Misha", false));
//            answers.add(new Answer("Micail", false));
//            answers.add(new Answer("Michael", true));
//            answers.add(new Answer("MASHA", false));
//
//            String key1 = globalDatabase.child("questions").push().getKey();
//            Question q1 = new Question(key1 , "what is Misha real name?", answers);
//            globalDatabase.child("questions").child(q1.getId()).setValue(q1);
//            q1.setSenderId(UserManger.getInstance().getUserId());
//
//            if (currentChat.fetchTheOpenQuestion() != null)  currentChat.fetchTheOpenQuestion().closeQuestion();
//            currentChat.addQuestion(q1);
//            mDatabase.child("questions").setValue(currentChat.getQuestions());
//            mDatabase.child("thereOpenQuestion").setValue(true);

            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            v.setVisibility(View.GONE);
        }

        @Override
        public void notifyChatForQuestionsAnswer(Boolean isRight) {
            currentChat.fetchTheOpenQuestion().setIsRightAnswer(isRight);
            currentChat.fetchTheOpenQuestion().closeQuestion();
            mDatabase.child("questions").setValue(currentChat.getQuestions());
            mDatabase.child("thereOpenQuestion").setValue(false);
        }
    }
