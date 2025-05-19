package io.github.sashirestela.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.github.sashirestela.openai.common.ResponseFormat.ResponseFormatType;
import io.github.sashirestela.openai.support.DefaultSchemaConverter;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@ToString
@JsonInclude(Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseText {

    private ResponseTextFormat format;

    public static ResponseText text() {
        return new ResponseText(new ResponseTextFormat(ResponseFormatType.TEXT));
    }

    public static ResponseText jsonObject() {
        return new ResponseText(new ResponseTextFormat(ResponseFormatType.JSON_OBJECT));
    }

    public static ResponseText jsonSchema(ResponseTextFormat.ResponseTextFormatJsonSchema format) {
        return new ResponseText(format);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    @ToString
    @JsonInclude(Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ResponseTextFormat {

        protected ResponseFormatType type;

        @NoArgsConstructor
        @Getter
        @ToString
        @JsonInclude(Include.NON_EMPTY)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class ResponseTextFormatJsonSchema extends ResponseTextFormat {

            @Required
            @Size(max = 64)
            private String name;

            @Required
            private JsonNode schema;

            private String description;

            private Boolean strict;

            @Builder
            public ResponseTextFormatJsonSchema(String name, JsonNode schema, String description, Boolean strict,
                    Class<?> schemaClass) {
                this.type = ResponseFormatType.JSON_SCHEMA;
                this.name = name;
                this.description = description;
                this.strict = strict;
                if (schema != null) {
                    this.schema = schema;
                } else {
                    this.schema = schemaClass != null
                            ? new DefaultSchemaConverter(Boolean.TRUE).convert(schemaClass)
                            : null;
                }
            }

            public static ResponseTextFormatJsonSchema of(Class<?> schemaClass) {
                var name = schemaClass.getSimpleName();
                var description = schemaClass.isAnnotationPresent(JsonClassDescription.class)
                        ? schemaClass.getAnnotation(JsonClassDescription.class).value()
                        : "";
                var strict = Boolean.TRUE;
                return ResponseTextFormatJsonSchema.builder()
                        .name(name)
                        .description(description)
                        .strict(strict)
                        .schemaClass(schemaClass)
                        .build();
            }

        }

    }

}
