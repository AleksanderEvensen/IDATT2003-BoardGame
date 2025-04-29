package edu.ntnu.idi.idatt.boardgame.ui.javafx.components;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import edu.ntnu.idi.idatt.boardgame.Utils;
import edu.ntnu.idi.idatt.boardgame.ui.javafx.components.enums.Weight;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class Header extends Label {
    public enum HeaderType {
        H1, H2, H3, H4, H5, H6;

        static Set<String> AllClasses = Arrays.stream(HeaderType.values())
                .map(HeaderType::getCssClass).collect(Collectors.toSet());

        public String getCssClass() {
            return String.format("Header-type-%s", name().toLowerCase());
        }
    }

    public Header(String text, Node graphic) {
        super(text, graphic);
    }

    public Header(String text) {
        this(text, null);
    }

    public Header withType(HeaderType type) {
        Utils.ensureOneOfClasses(this, type.getCssClass(), HeaderType.AllClasses);
        return this;
    }

    public Header withFontWeight(Weight weight) {
        Utils.ensureOneOfClasses(this, weight.prefixed("font-weight"),
                Weight.AllPrefixed("font-weight"));
        return this;
    }

    public Header addClasses(String... classes) {
        getStyleClass().addAll(classes);
        return this;
    }
}

