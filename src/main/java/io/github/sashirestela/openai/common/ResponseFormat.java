package io.github.sashirestela.openai.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.support.DefaultSchemaConverter;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseFormat {

    public static final ResponseFormat TEXT = new ResponseFormat(ResponseFormatType.TEXT, null);
    public static final ResponseFormat JSON_OBJECT = new ResponseFormat(ResponseFormatType.JSON_OBJECT, null);

    private ResponseFormatType type;
    private JsonSchema jsonSchema;

    public static ResponseFormat jsonSchema(JsonSchema jsonSchema) {
        return new ResponseFormat(ResponseFormatType.JSON_SCHEMA, jsonSchema);
    }

    public enum ResponseFormatType {

        @JsonProperty("text")
        TEXT,

        @JsonProperty("json_object")
        JSON_OBJECT,

        @JsonProperty("json_schema")
        JSON_SCHEMA;

    }

    @Getter
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class JsonSchema {

        private String description;

        @Required
        @Size(max = 64)
        private String name;

        private JsonNode schema;

        private Boolean strict;

        @Builder
        public JsonSchema(String description, @NonNull String name, Boolean strict, Class<?> schemaClass,
                JsonNode schema) {
            this.description = description;
            this.name = name;
            this.strict = strict;
            if (schema != null) {
                this.schema = schema;
            } else {
                this.schema = schemaClass != null
                        ? new DefaultSchemaConverter(Boolean.TRUE).convert(schemaClass)
                        : null;
            }
        }

    }

}
