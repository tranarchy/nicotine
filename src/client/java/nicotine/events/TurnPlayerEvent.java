package nicotine.events;

public class TurnPlayerEvent {
    public double xo;
    public double yo;

    public TurnPlayerEvent(double xo, double yo) {
        this.xo = xo;
        this.yo = yo;
    }
}
