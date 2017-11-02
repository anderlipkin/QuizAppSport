package com.quiz.quizappsport;

import java.util.List;

/**
 * Created by anderlipkin on 10/24/17.
 */

public class QuestionQuiz {

    private int id;
    private String question;
    private List<String> optionsAnswer;
    private String answer;

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


    /*

    private Map<String, ArrayList<String>> questionAnswersMap;

    private Map<String, ArrayList<String>> correctAnswerMap;

    public QuestionQuiz() {
        questionAnswersMap = new HashMap<String, ArrayList<String>>();
        correctAnswerMap = new HashMap<String, ArrayList<String>>();
    }

    public void addQuestionOptionalAnswers(String question, ArrayList<String> answers){
        questionAnswersMap.put(question, answers);
    }

    public void addAnswerToQuestion(String question, String answer){
        if (questionAnswersMap.get(question) == null)
            questionAnswersMap.put(question, new ArrayList<String>());

        questionAnswersMap.get(question).add(answer);
    }

    public void addCorrectAnswer(String question, String answer) {
        if (correctAnswerMap.get(question) == null)
            correctAnswerMap.put(question, new ArrayList<String>());

        correctAnswerMap.get(question).add(answer);
    }

    //public int checkCorrectAnswer() {

    //}

    public int numQuestions(){
        return questionAnswersMap.keySet().size();
    }

    public String[] getQuestions() {
        return questionAnswersMap.keySet().toArray(new String[numQuestions()]);
    }

*/



}
