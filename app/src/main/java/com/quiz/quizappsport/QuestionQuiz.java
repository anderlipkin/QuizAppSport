package com.quiz.quizappsport;

import java.util.List;

public class QuestionQuiz {

    private int id;
    private String question;
    private List<String> optionsAnswer;
    private String answer;

    /**
     * For using Json instead Gson
    public QuestionQuiz(JSONObject object) {
        try {
            this.question = object.getString("question");
            this.optionsAnswer = new ArrayList<>();
            this.answer = object.getString("answer");
            JSONArray options = object.getJSONArray("optionsAnswer");
            for (int i = 0; i < options.length(); i++) {
                this.optionsAnswer.add(options.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
     */

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptionsAnswer() {
        return optionsAnswer;
    }

    public void setOptionsAnswer(List<String> optionsAnswer) {
        this.optionsAnswer = optionsAnswer;
    }

    /**
     * For using Json instead Gson
    public static ArrayList<User> fromJson(JSONArray jsonObjects) {
        ArrayList<QuestionQuiz> questionsQuiz = new ArrayList<>();

        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                questionsQuiz.add(new QuestionQuiz(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
     */

}
