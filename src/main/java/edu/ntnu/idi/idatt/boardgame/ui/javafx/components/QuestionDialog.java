package edu.ntnu.idi.idatt.boardgame.ui.javafx.components;


import edu.ntnu.idi.idatt.boardgame.model.quiz.Question;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Button.ButtonVariant;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.Header.HeaderType;
import java.util.List;
import java.util.function.Consumer;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * In‑scene dialog that blocks interaction with the rest of the UI until the user answers a
 * multiple‑choice {@link Question}. The dialog is centred inside the given {@link StackPane} and
 * cannot be dismissed without selecting an option.
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class QuestionDialog {

  private static final double MAX_WIDTH = 420;
  private static final double PADDING = 24;

  private final StackPane owner;
  private final Question question;
  private final Consumer<Integer> onAnswer;

  private final Rectangle overlay = new Rectangle();
  private final VBox dialogBox = new VBox(12);

  private ToggleGroup toggleGroup;

  public QuestionDialog(StackPane owner, Question question, Consumer<Integer> onAnswer) {
    this.owner = owner;
    this.question = question;
    this.onAnswer = onAnswer;
    buildUI();
  }

  private void buildUI() {
    overlay.setFill(Color.rgb(0, 0, 0, 0.5));
    overlay.setMouseTransparent(false);
    overlay.widthProperty().bind(owner.widthProperty());
    overlay.heightProperty().bind(owner.heightProperty());

    dialogBox.getStyleClass().add("question-dialog");
    dialogBox.setMaxWidth(MAX_WIDTH);
    dialogBox.setPadding(new Insets(PADDING));
    dialogBox.setAlignment(Pos.TOP_LEFT);
    StackPane.setAlignment(dialogBox, Pos.CENTER);

    Header header = new Header(question.getQuestion()).withType(HeaderType.H3);
    header.setWrapText(true);

    VBox answersBox = new VBox(6);
    toggleGroup = new ToggleGroup();
    List<String> answers = question.getAnswers();
    for (int i = 0; i < answers.size(); i++) {
      RadioButton rb = new RadioButton(answers.get(i));
      rb.setToggleGroup(toggleGroup);
      rb.setUserData(i);
      answersBox.getChildren().add(rb);
    }

    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
    VBox.setVgrow(buttonBox, Priority.ALWAYS);

    Button confirm = new Button("OK").withVariant(ButtonVariant.PRIMARY);
    confirm.disableProperty().bind(Bindings.isNull(toggleGroup.selectedToggleProperty()));
    confirm.setOnAction(e -> {
      Integer idx = (Integer) toggleGroup.getSelectedToggle().getUserData();
      onAnswer.accept(idx);
      close();
    });

    buttonBox.getChildren().add(confirm);
    dialogBox.getChildren().addAll(header, answersBox, buttonBox);
  }

  /** Adds overlay and dialog box to the owner pane. */
  public void show() {
    if (!owner.getChildren().contains(overlay)) {
      owner.getChildren().add(overlay);
    }
    if (!owner.getChildren().contains(dialogBox)) {
      owner.getChildren().add(dialogBox);
    }
  }

  private void close() {
    owner.getChildren().removeAll(overlay, dialogBox);
  }
}

