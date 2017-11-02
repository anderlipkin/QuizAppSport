package com.quiz.quizappsport;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.questionQuiz.quizappsport.R;

import java.util.List;

/**
 * Created by anderlipkin on 10/24/17.
 */

class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizViewHolder> {

    private List<QuestionQuiz> questionQuiz;

    private Context context;

    static OnItemClickListener itemClickListener;

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

        return new QuizViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QuizViewHolder holder, int position) {

        holder.textQuestion.setText(questionQuiz.get(position).getQuestion());

    }

    static class QuizViewHolder extends RecyclerView.ViewHolder {

        TextView textQuestion;

        QuizViewHolder(View itemView){
            super(itemView);

            textQuestion = (TextView) itemView.findViewById(R.id.questionTitle);

            //itemView.findViewById(R.id.questionHolder).setOnClickListener(this);
        }

       /* @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(itemView, adapterPosition);
        }
        */
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        QuizListAdapter.itemClickListener = itemClickListener;
    }

    interface OnItemClickListener {

        void onItemClick(View view, int position);

    }

}
