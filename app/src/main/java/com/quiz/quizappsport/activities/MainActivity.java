package com.quiz.quizappsport.activities;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.questionQuiz.quizappsport.R;
import com.quiz.quizappsport.QuestionQuiz;
import com.quiz.quizappsport.adapters.QuizListAdapter;
import com.quiz.quizappsport.Session;
import com.quiz.quizappsport.User;
import com.quiz.quizappsport.database.UsersDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    private QuizListAdapter quizAdapter;

    private Session session;

    private User user;

    private UsersDbHelper usersDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initObjects();

        session.checkLogin();

        bindViews();
        setViewActions();

        usersDbHelper = UsersDbHelper.getInstance(getApplication());

        int idUser = session.getUserId();

        user = usersDbHelper.getUser(idUser);

        new GetQuestions().execute();

    }

    private void initObjects() {
        session = new Session(getApplicationContext());
    }

    private void bindViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
    }

    private void setViewActions() {
        fab.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miLogout:
                session.logoutUser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                int score = quizAdapter.getUserPoint();
                user.setScore(score);
                viewResultScore();
                saveUserScoreToDB(score);
                break;
        }
    }

    private void saveUserScoreToDB(int userPoint) {
        if (userPoint > user.getBestScore()) {
            usersDbHelper.updateBestScore(user.getId(), userPoint);
            user.setBestScore(userPoint);
        }
    }

    public void viewResultScore() {
        int bestScore = user.getBestScore();
        int score = user.getScore();

        StringBuilder resultMessage = new StringBuilder();
        resultMessage
                .append(getString(R.string.score_message, score, quizAdapter.getItemCount()))
                .append("\n" + getString(R.string.best_score_msg, bestScore));

        if (user.getBestScore() > user.getScore()) {
            resultMessage.append("\n" + getString(R.string.less_points_msg, score));
        } else {
            resultMessage.append("\n" + getString(R.string.new_record_msg, score));
        }

        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(getString(R.string.completed_quiz, user.getNickName().toUpperCase()))
                .setMessage(resultMessage.toString())
                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        refreshQuizAdapter();
                    }
                }).show();
    }


    private void refreshQuizAdapter() {
        mRecyclerView.setAdapter(null);
        mRecyclerView.setLayoutManager(null);
        mRecyclerView.setAdapter(quizAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        quizAdapter.resetUserPoint();
    }

    private class GetQuestions extends AsyncTask<String, Void, List<QuestionQuiz>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected List<QuestionQuiz> doInBackground(String... strings) {
            List<QuestionQuiz> resultQuiz = null;
            Document html = getHtml();
            JSONObject quizJson = htmlToJson(html);

            try {
                resultQuiz = jsonToQuestionsQuiz(quizJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultQuiz;
        }

        @Override
        protected void onPostExecute(List<QuestionQuiz> questionQuiz) {
            super.onPostExecute(questionQuiz);


            quizAdapter = new QuizListAdapter(MainActivity.this, questionQuiz);
            mRecyclerView.setAdapter(quizAdapter);
            mLayoutManager = new LinearLayoutManager(MainActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
        }

        private Document getHtml(){
            try {
                return Jsoup.connect("http://www.knowledgepublisher.com/article-897.html").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private List<QuestionQuiz> jsonToQuestionsQuiz(JSONObject jsonObject) throws JSONException{
            Gson gson = new Gson();
            List<QuestionQuiz> questionList = new ArrayList<>();

            JSONArray jsonArray = jsonObject.getJSONArray("quiz");

            for (int i = 0; i < jsonArray.length(); i++) {
                questionList.add(gson.fromJson(jsonArray.getString(i), QuestionQuiz.class));
            }

            return questionList;
        }

        private JSONObject htmlToJson(Document doc) {
            JSONObject resultJson = new JSONObject();
            JSONArray quizJsonArray = new JSONArray();
            List<String> optionsAnswer = new ArrayList<String>();
            int countQuestion = 0;
            optionsAnswer.add("A");
            optionsAnswer.add("B");
            optionsAnswer.add("C");
            optionsAnswer.add("D");

            for (Element quiz : doc.select(".article-body")) {
                for (Element rowQuestion: quiz.getElementsByTag("p")) {
                    String question = rowQuestion.text();

                    if (question.matches("^\\d+\\..*")){
                        JSONObject itemQuiz = new JSONObject();
                        JSONObject jsonQuestion = new JSONObject();
                        JSONArray listOptions = new JSONArray();

                        Elements rowOptions = rowQuestion.nextElementSibling().getElementsByTag("li");
                        Elements rowAnswers = rowQuestion.lastElementSibling().getElementsByTag("li");

                        try {
                            listOptions.put(rowOptions.get(0).text());
                            listOptions.put(rowOptions.get(1).text());
                            listOptions.put(rowOptions.get(2).text());
                            listOptions.put(rowOptions.get(3).text());

                            String answer = listOptions.getString(
                                    optionsAnswer.indexOf(rowAnswers.get(countQuestion).text()));

                            jsonQuestion.put("id", countQuestion);
                            jsonQuestion.put("question", question);
                            jsonQuestion.put("optionsAnswer", listOptions);
                            jsonQuestion.put("answer", answer);
                            quizJsonArray.put(jsonQuestion);
                            resultJson.put("quiz", quizJsonArray);

                            countQuestion++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }

            return resultJson;
        }
    }
}
