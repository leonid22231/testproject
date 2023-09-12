package crosti.nuli.daimpre.fragments.game.models;

public class Answer {
    String Answer_text;
    Boolean right;
    Boolean checked = false;
    public String getAnswer_text() {
        return Answer_text;
    }

    public void setAnswer_text(String answer_text) {
        Answer_text = answer_text;
    }

    public Boolean getRight() {
        return right;
    }

    public void setRight(Boolean right) {
        this.right = right;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
