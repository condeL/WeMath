package com.welearn.wemath;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/*Question interface for both LessongQuestion and QuizQuestion*/
public abstract class Question {

    private String mProblem = "";
    private List<Pair<String, Boolean>> mAnswers = new ArrayList<>();
    private boolean mMultipleChoice;
    private int mNbCorrectAnswers;
    private ArrayList<String> mChoices;
    private ArrayList<Boolean> mTruths;

    public Question(){};

    public Question(String problem, List<Pair<String, Boolean>> answers, boolean multipleChoice) {

        int nbCorrectAnswers = 0;
        mProblem = problem;
        int length = answers.size();
        if (length > 1 && length < 5) {
            for (int i = 0; i < length; i++) {
                mAnswers.add(answers.get(i));
                if (answers.get(i).second.booleanValue() == true) {
                    nbCorrectAnswers++;
                }
            }
        }
        mMultipleChoice = multipleChoice;
        mNbCorrectAnswers = nbCorrectAnswers;

        mTruths = new ArrayList<>();
        mChoices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mTruths.add(mAnswers.get(i).second);
        }
        for (int i = 0; i < 4; i++) {
            mChoices.add(mAnswers.get(i).first);
        }
    }

    public String getProblem() {
        return mProblem;
    }

    public void setProblem(String problem) {
        mProblem = problem;
    }

    /*public List<Pair<String, Boolean>> getAnswers() {
        return mAnswers;
    }*/

    public int getAnswersSize(){
        return mAnswers.size();
    }

    public String getAnswerText(int index){
        return mAnswers.get(index).first;
    }

    public boolean getAnswerValue(int index){
        return mAnswers.get(index).second;
    }

    public void setAnswers(ArrayList<Pair<String, Boolean>> answers) {
        mAnswers = answers;
    }

    public boolean isMultipleChoice() {
        return mMultipleChoice;
    }

    public void setMultipleChoice(boolean multipleChoice) {
        mMultipleChoice = multipleChoice;
    }

    public int getNbCorrectAnswers() {
        return mNbCorrectAnswers;
    }

    public void setNbCorrectAnswers(int nbCorrectAnswers) {
        mNbCorrectAnswers = nbCorrectAnswers;
    }

    public ArrayList<String> getChoices() {
        return mChoices;
    }

    public void setChoices(ArrayList<String> choices) {
        mChoices = choices;
    }

    public ArrayList<Boolean> getTruths() {
        return mTruths;
    }

    public void setTruths(ArrayList<Boolean> truths) {
        mTruths = truths;
    }
}
