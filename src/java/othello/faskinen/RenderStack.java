package othello.faskinen;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class RenderStack {

    public HashMap<Model, ArrayList<Pair<Mat4, Integer>>> renderStack;

    public RenderStack() {
        this.renderStack = new HashMap<Model, ArrayList<Pair<Mat4, Integer>>>();
    }

    public void pushModel(Model model, Mat4 transform) {
        this.pushModel(model, transform, -1);
    }

    public void pushModel(Model model, Mat4 transform, int id) {
        ArrayList<Pair<Mat4, Integer>> queue = this.renderStack.get(model);

        if (queue == null) {
            queue = new ArrayList<Pair<Mat4, Integer>>();
            this.renderStack.put(model, queue);
        }

        queue.add(new Pair<Mat4, Integer>(transform, id));

    }
}
