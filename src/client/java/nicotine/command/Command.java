package nicotine.command;

public class Command {
    public String name;
    public String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;

        this.init();
    }

    public void trigger(String[] splitCommand) {}

    protected void init() {}
}
