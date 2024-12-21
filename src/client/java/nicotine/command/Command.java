package nicotine.command;

public class Command {
    public String name;
    public String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void trigger(String[] splitCommand) {}
}
