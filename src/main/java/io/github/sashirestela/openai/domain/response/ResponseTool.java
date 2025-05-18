package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.domain.assistant.RankingOption;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ResponseTool.FileSearchResponseTool.class, name = "file_search"),
        @JsonSubTypes.Type(value = ResponseTool.FunctionResponseTool.class, name = "function"),
        @JsonSubTypes.Type(value = ResponseTool.ComputerResponseTool.class, name = "computer_use_preview"),
        @JsonSubTypes.Type(value = ResponseTool.WebSearchResponseTool.class, name = "web_search_preview"),
})
@Getter
@Setter
public abstract class ResponseTool {

    protected ResponseToolType type;

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FileSearchResponseTool extends ResponseTool {

        @Required
        private List<String> vectorStoreIds;

        private Filter filters;

        @Range(min = 1, max = 50)
        private Integer maxNumResults;

        private RankingOption rankingOptions;

        @Builder
        public FileSearchResponseTool(List<String> vectorStoreIds, Filter filters, Integer maxNumResults,
                RankingOption rankingOptions) {
            this.vectorStoreIds = vectorStoreIds;
            this.filters = filters;
            this.maxNumResults = maxNumResults;
            this.rankingOptions = rankingOptions;
            this.type = ResponseToolType.FILE_SEARCH;
        }

        public static FileSearchResponseTool of(List<String> vectorStoreIds) {
            return FileSearchResponseTool.builder().vectorStoreIds(vectorStoreIds).build();
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FunctionResponseTool extends ResponseTool {

        @Required
        private String name;

        @Required
        private JsonNode parameters;

        @Required
        private Boolean strict;

        private String description;

        public static FunctionResponseTool function(FunctionDef funcDef) {
            var funcTool = new FunctionResponseTool(
                    funcDef.getName(),
                    funcDef.getSchemaConverter().convert(funcDef.getFunctionalClass()),
                    funcDef.getStrict(),
                    funcDef.getDescription());
            funcTool.setType(ResponseToolType.FUNCTION);
            return funcTool;
        }

        public static List<FunctionResponseTool> functions(List<FunctionDef> funcDefList) {
            return funcDefList.stream()
                    .map(FunctionResponseTool::function)
                    .collect(Collectors.toList());
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ComputerResponseTool extends ResponseTool {

        @Required
        private Double displayHeight;

        @Required
        private Double displayWidth;

        @Required
        private Environment environment;

        @Builder
        public ComputerResponseTool(Double displayHeight, Double displayWidth, Environment environment) {
            this.displayHeight = displayHeight;
            this.displayWidth = displayWidth;
            this.environment = environment;
            this.type = ResponseToolType.COMPUTER_USE_PREVIEW;
        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class WebSearchResponseTool extends ResponseTool {

        private ContextSize searchContextSize;

        private Location userLocation;

        @Builder
        public WebSearchResponseTool(ContextSize searchContextSize, Location userLocation) {
            this.searchContextSize = searchContextSize;
            this.userLocation = userLocation;
            this.type = ResponseToolType.WEB_SEARCH_PREVIEW;
        }

        public static WebSearchResponseTool of() {
            return WebSearchResponseTool.builder().build();
        }

    }

    public static interface Filter {

        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        class ComparisonFilter implements Filter {

            @Required
            private String key;

            @Required
            private ComparisonOperator type;

            @Required
            @ObjectType(baseClass = String.class)
            @ObjectType(baseClass = Double.class)
            @ObjectType(baseClass = Boolean.class)
            private Object value;

        }

        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        class CompoundFilter implements Filter {

            @Required
            @Singular
            private List<Filter> filters;

            @Required
            private LogicalOperator type;

        }

    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Location {

        private String type;
        private String city;
        private String country;
        private String region;
        private String timezone;

        @Builder
        public Location(String type, String city, String country, String region, String timezone) {
            this.type = "approximate";
            this.city = city;
            this.country = country;
            this.region = region;
            this.timezone = timezone;
        }

    }

    public enum ResponseToolType {

        @JsonProperty("file_search")
        FILE_SEARCH,

        @JsonProperty("function")
        FUNCTION,

        @JsonProperty("computer_use_preview")
        COMPUTER_USE_PREVIEW,

        @JsonProperty("web_search_preview")
        WEB_SEARCH_PREVIEW;

    }

    public enum ComparisonOperator {

        @JsonProperty("eq")
        EQUAL,

        @JsonProperty("ne")
        NOT_EQUAL,

        @JsonProperty("gt")
        GREATER_THAN,

        @JsonProperty("gte")
        GREATER_THAN_OR_EQUAL,

        @JsonProperty("lt")
        LESS_THAN,

        @JsonProperty("lte")
        LESS_THAN_OR_EQUAL;

    }

    public enum LogicalOperator {

        @JsonProperty("and")
        AND,

        @JsonProperty("or")
        OR;

    }

    public enum Environment {

        @JsonProperty("mac")
        MAC,

        @JsonProperty("windows")
        WINDOWS,

        @JsonProperty("ubuntu")
        UBUNTU,

        @JsonProperty("browser")
        BROWSER;

    }

    public enum ContextSize {

        @JsonProperty("low")
        LOW,

        @JsonProperty("medium")
        MEDIUM,

        @JsonProperty("high")
        HIGH;

    }

}
