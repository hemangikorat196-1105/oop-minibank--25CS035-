interface Switchable {
    void on();
    void off();

    default void toggle() {
        on();
    }
}

class Fan implements Switchable {
    private boolean isOn = false;

    public void on() {
        isOn = true;
        System.out.println("Fan is ON");
    }

    public void off() {
        isOn = false;
        System.out.println("Fan is OFF");
    }
}

class Light implements Switchable {
    private boolean isOn = false;

    public void on() {
        isOn = true;
        System.out.println("Light is ON");
    }

    public void off() {
        isOn = false;
        System.out.println("Light is OFF");
    }
}

interface SwitchPolicy {
    boolean maySwitchOn(Switchable device, int hour);
}

public class Remotecontrol {
    public static void main(String[] args) {

        Switchable fan = new Fan();
        Switchable light = new Light();

        Switchable[] devices = { fan, light };

        System.out.println("Toggling devices:");
        for (Switchable device : devices) {
            device.toggle();
        }

        SwitchPolicy daytimePolicy = new SwitchPolicy() {
            public boolean maySwitchOn(Switchable device, int hour) {
                return hour >= 6 && hour < 18;
            }
        };

        SwitchPolicy eveningPolicy =(device, hour) -> hour >= 18 && hour < 23;

        System.out.println("\nAnonymous class policy:");
        System.out.println("Fan at 10:00 -> "+ daytimePolicy.maySwitchOn(fan, 10));

        System.out.println("\nLambda policy:");
        System.out.println("Light at 20:00 -> "+ eveningPolicy.maySwitchOn(light, 20));
    }
}