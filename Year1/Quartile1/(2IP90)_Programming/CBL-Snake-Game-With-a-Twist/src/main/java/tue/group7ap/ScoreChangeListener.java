package tue.group7ap;

/**
 * Listener for changes of the score.
 */
public interface ScoreChangeListener {
    void onVariableChange(int newValue);
    void booleanVariableChange(boolean value);
}
