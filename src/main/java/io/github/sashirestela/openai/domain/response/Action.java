package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Action.ClickAction.class, name = "click"),
        @JsonSubTypes.Type(value = Action.DoubleClickAction.class, name = "double_click"),
        @JsonSubTypes.Type(value = Action.DragAction.class, name = "drag"),
        @JsonSubTypes.Type(value = Action.KeyPressAction.class, name = "keypress"),
        @JsonSubTypes.Type(value = Action.MoveAction.class, name = "move"),
        @JsonSubTypes.Type(value = Action.ScreenshotAction.class, name = "screenshot"),
        @JsonSubTypes.Type(value = Action.ScrollAction.class, name = "scroll"),
        @JsonSubTypes.Type(value = Action.TypeAction.class, name = "type"),
        @JsonSubTypes.Type(value = Action.WaitAction.class, name = "wait"),
})
@Getter
@Setter
public abstract class Action {

    protected ActionType type;

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ClickAction extends Action {

        @Required
        private MouseButton button;

        @Required
        private Integer x;

        @Required
        private Integer y;

        private ClickAction(MouseButton button, Integer x, Integer y) {
            this.button = button;
            this.x = x;
            this.y = y;
            this.type = ActionType.CLICK;
        }

        public static ClickAction of(MouseButton button, Integer x, Integer y) {
            return new ClickAction(button, x, y);
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DoubleClickAction extends Action {

        @Required
        private Integer x;

        @Required
        private Integer y;

        private DoubleClickAction(Integer x, Integer y) {
            this.x = x;
            this.y = y;
            this.type = ActionType.DOUBLE_CLICK;
        }

        public static DoubleClickAction of(Integer x, Integer y) {
            return new DoubleClickAction(x, y);
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DragAction extends Action {

        @Required
        private List<Coord> path;

        private DragAction(List<Coord> path) {
            this.path = path;
            this.type = ActionType.DRAG;
        }

        public static DragAction of(List<Coord> path) {
            return new DragAction(path);
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KeyPressAction extends Action {

        @Required
        private List<String> keys;

        private KeyPressAction(List<String> keys) {
            this.keys = keys;
            this.type = ActionType.KEYPRESS;
        }

        public static KeyPressAction of(List<String> keys) {
            return new KeyPressAction(keys);
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MoveAction extends Action {

        @Required
        private Integer x;

        @Required
        private Integer y;

        private MoveAction(Integer x, Integer y) {
            this.x = x;
            this.y = y;
            this.type = ActionType.MOVE;
        }

        public static MoveAction of(Integer x, Integer y) {
            return new MoveAction(x, y);
        }

    }

    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ScreenshotAction extends Action {

        private ScreenshotAction() {
            this.type = ActionType.SCREENSHOT;
        }

        public static ScreenshotAction of() {
            return new ScreenshotAction();
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ScrollAction extends Action {

        @Required
        private Integer scrollX;

        @Required
        private Integer scrollY;

        @Required
        private Integer x;

        @Required
        private Integer y;

        private ScrollAction(Integer scrollX, Integer scrollY, Integer x, Integer y) {
            this.scrollX = scrollX;
            this.scrollY = scrollY;
            this.x = x;
            this.y = y;
            this.type = ActionType.SCROLL;
        }

        public static ScrollAction of(Integer scrollX, Integer scrollY, Integer x, Integer y) {
            return new ScrollAction(scrollX, scrollY, x, y);
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TypeAction extends Action {

        @Required
        private String text;

        private TypeAction(String text) {
            this.text = text;
            this.type = ActionType.TYPE;
        }

        public static TypeAction of(String text) {
            return new TypeAction(text);
        }

    }

    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class WaitAction extends Action {

        private WaitAction() {
            this.type = ActionType.WAIT;
        }

        public static WaitAction of() {
            return new WaitAction();
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Coord {

        @Required
        private Integer x;

        @Required
        private Integer y;

        private Coord(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }

        public static Coord of(Integer x, Integer y) {
            return new Coord(x, y);
        }

    }

    public enum ActionType {

        @JsonProperty("click")
        CLICK,

        @JsonProperty("double_click")
        DOUBLE_CLICK,

        @JsonProperty("drag")
        DRAG,

        @JsonProperty("keypress")
        KEYPRESS,

        @JsonProperty("move")
        MOVE,

        @JsonProperty("screenshot")
        SCREENSHOT,

        @JsonProperty("scroll")
        SCROLL,

        @JsonProperty("type")
        TYPE,

        @JsonProperty("wait")
        WAIT;

    }

    public enum MouseButton {

        @JsonProperty("left")
        LEFT,

        @JsonProperty("right")
        RIGHT,

        @JsonProperty("wheel")
        WHEEL,

        @JsonProperty("back")
        BACK,

        @JsonProperty("forward")
        FORWARD;

    }

}
