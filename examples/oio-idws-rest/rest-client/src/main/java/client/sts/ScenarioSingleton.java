package client.sts;

/**
 * Use this
 */
public class ScenarioSingleton {

    public static ScenarioSingleton instance = new ScenarioSingleton();

    private String scenario = "https://signature.sts.nemlog-in.dk/";

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getScenario() {
        return scenario;
    }
}
