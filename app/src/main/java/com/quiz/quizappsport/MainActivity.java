package com.quiz.quizappsport;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.questionQuiz.quizappsport.R;

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


public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    QuizListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        QuestionQuiz startQuestionQuiz = new QuestionQuiz();

        new GetQuestions().execute();
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

            mAdapter = new QuizListAdapter(MainActivity.this, questionQuiz);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
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
