package nl.tudelft.oopp.qubo.controllers.helpers;

import javafx.scene.control.FocusModel;

public class NoFocusModel<T> extends FocusModel<T> {
    @Override
    protected int getItemCount() {
        return 0;
    }

    @Override
    protected T getModelItem(int index) {
        return null;
    }
}
