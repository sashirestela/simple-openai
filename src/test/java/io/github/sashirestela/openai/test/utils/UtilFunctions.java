package io.github.sashirestela.openai.test.utils;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.common.function.Functional;

public class UtilFunctions {

    private UtilFunctions() {
    }

    @JsonClassDescription("Get the current temperature for a specific location")
    public static class CurrentTemperature implements Functional {

        @JsonPropertyDescription("The city and state, e.g., San Francisco, CA")
        @JsonProperty(required = true)
        public String location;

        @JsonPropertyDescription("The temperature unit to use. Infer this from the user's location.")
        @JsonProperty(required = true)
        public String unit;

        @Override
        public Object execute() {
            double centigrades = Math.random() * (40.0 - 10.0) + 10.0;
            double fahrenheit = centigrades * 9.0 / 5.0 + 32.0;
            String shortUnit = unit.substring(0, 1).toUpperCase();
            if (shortUnit.equals("C")) {
                return centigrades;
            } else if (shortUnit.equals("F")) {
                return fahrenheit;
            } else {
                return 0.0;
            }
        }

    }

    @JsonClassDescription("Get the probability of rain for a specific location")
    public static class RainProbability implements Functional {

        @JsonPropertyDescription("The city and state, e.g., San Francisco, CA")
        @JsonProperty(required = true)
        public String location;

        @Override
        public Object execute() {
            return Math.random() * 100;
        }

    }

}
