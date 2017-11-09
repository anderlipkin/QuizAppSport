package com.quiz.quizappsport.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.questionQuiz.quizappsport.R;
import com.quiz.quizappsport.QuestionQuiz;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizViewHolder> {

    private List<QuestionQuiz> questionQuiz;

    private Context context;

    private static int userPoint = 0;

    public QuizListAdapter(Context context, List<QuestionQuiz> questionQuiz) {
        this.questionQuiz = questionQuiz;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return questionQuiz.size();
    }

    @Override
    public QuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_questions, parent, false);

        return new QuizViewHolder(itemView, context, questionQuiz);
    }

    @Override
    public void onBindViewHolder(QuizViewHolder holder, int position) {
        holder.textQuestion.setText(questionQuiz.get(position).getQuestion());
    }


    public int getUserPoint() {
        return userPoint;
    }

    public void resetUserPoint() {
        userPoint = 0;
    }

    static class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int countClick = 0;
        int selectedPosition = -1;
        TextView textQuestion;
        List<QuestionQuiz> questionList;
        Context context;

        QuizViewHolder(View itemView, Context context, List<QuestionQuiz> questionList){
            super(itemView);
            this.questionList = questionList;
            this.context = context;

            textQuestion = (TextView) itemView.findViewById(R.id.questionTitle);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            final QuestionQuiz quiz = questionList.get(position);

            new AlertDialog.Builder(context)
                    .setTitle(quiz.getQuestion())
                    .setSingleChoiceItems(quiz.getOptionsAnswer().toArray(new String[4]), selectedPosition, null)
                    .setPositiveButton(R.string.ok_button_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            countClick++;
                            selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                            if (checkCorrectAnswer(quiz, selectedPosition)) {
                                if (countClick == 1)
                                    userPoint++;
                            } else if (countClick > 1) {
                                userPoint--;
                                countClick = 0;
                            }

                        }
                    })
                    .setNegativeButton(R.string.cancel_button_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        private boolean checkCorrectAnswer(QuestionQuiz quiz, int selectedPosition) {
            return quiz.getAnswer().equals(quiz.getOptionsAnswer().get(selectedPosition));
        }
    }

}
