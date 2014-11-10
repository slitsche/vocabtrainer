package de.biosoft.fxvocabtrainer;

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LearnItem implements Cloneable, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 4202034994556203618L;
    private StringProperty question = new SimpleStringProperty();
    private StringProperty answer = new SimpleStringProperty();
    private int score = 15;
    private boolean wasRepeated = false;

    public boolean isWasRepeated() {
        return wasRepeated;
    }

    public LearnItem() {
    }

    public LearnItem(String question) {
        this.setQuestion(question);
    }

    public LearnItem(String question, String answer) {
        this.setQuestion(question);
        this.setAnswer(answer);
    }

    public LearnItem(String q, String a, int s) {
        this(q, a);
        this.setScore(s);
    }

    public String getQuestion() {
        return question.get();
    }

    public void setQuestion(String question) {
        this.question.set(question);
    }

    public StringProperty getQuestionProperty() {
        return this.question;
    }

    public String getAnswer() {
        return answer.get();
    }

    public void setAnswer(String answer) {
        this.answer.set(answer);
    }

    public StringProperty getAnswerProperty() {
        return this.answer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LearnItem clone() {
        try {
            return (LearnItem) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }

    }

    public void success() {
        if (!this.wasRepeated) {
            this.setScore(this.score - 1);
        }
    }

    /**
     * In case of failure we want to move item into next higher bucket if there
     * is one. Increase happens only once per instance. Hence only once per
     * learn iteration;
     */
    public void failure() {
        if (!this.wasRepeated) {
            this.wasRepeated = true;
            int b = this.getBucket();
            if (b == 3 && score < 15) {
                this.setScore(this.score + 1);
            } else if (b < 3) {
                int s = (int) ((Math.pow(2, b + 1)) + 1);
                this.setScore(s);
            }
        }
    }

    public int getBucket() {
        if (getScore() > 8) {
            return 3;
        } else if (getScore() > 4) {
            return 2;
        } else if (getScore() > 2) {
            return 1;
        } else if (getScore() >= 1) {
            return 0;
        } else
            return -1;

    }
}
