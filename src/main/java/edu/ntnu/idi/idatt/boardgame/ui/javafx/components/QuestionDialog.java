package edu.ntnu.idi.idatt.boardgame.ui.javafx.components;

import edu.ntnu.idi.idatt.boardgame.model.quiz.Question;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Header.HeaderType;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.enums.Weight;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class QuestionDialog {

  private static final double PADDING = 24;

  private final StackPane owner;
  private final Question question;
  private final Consumer<Integer> onAnswer;

  public QuestionDialog(StackPane owner, Question question, Consumer<Integer> onAnswer) {
    this.owner = owner;
    this.question = question;
    this.onAnswer = onAnswer;
  }

  public void show() {
    StackPane overlay = new StackPane();
    overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
    overlay.setPrefSize(owner.getWidth(), owner.getHeight());

    Card dialogCard = new Card();
    dialogCard.setPadding(new Insets(PADDING));
    dialogCard.setMaxWidth(Region.USE_PREF_SIZE);
    dialogCard.setMaxHeight(Region.USE_PREF_SIZE);

    Header header = new Header(question.getQuestion());
    header.withType(HeaderType.H2).withFontWeight(Weight.BOLD);
    header.setWrapText(true);

    VBox answersBox = new VBox(10);
    answersBox.setAlignment(Pos.CENTER_LEFT);
    ToggleGroup toggleGroup = new ToggleGroup();
    List<String> answers = question.getAnswers();

    IntStream.range(0, answers.size())
        .forEach(i -> {
          RadioButton rb = new RadioButton(answers.get(i));
          rb.setToggleGroup(toggleGroup);
          rb.setUserData(i);
          rb.setStyle("-fx-font-size: 14px;");
          answersBox.getChildren().add(rb);
        });

    Button confirm = new Button("Confirm");
    confirm.disableProperty().bind(Bindings.isNull(toggleGroup.selectedToggleProperty()));
    confirm.setOnAction(evt -> {
      Integer idx = (Integer) toggleGroup.getSelectedToggle().getUserData();
      onAnswer.accept(idx);
      owner.getChildren().remove(overlay);
    });

    VBox content = new VBox(20, header, answersBox, confirm);
    content.setAlignment(Pos.CENTER);
    dialogCard.setCenter(content);

    StackPane.setAlignment(dialogCard, Pos.CENTER);
    overlay.getChildren().add(dialogCard);

    owner.getChildren().add(overlay);
  }
}