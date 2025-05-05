package io.github.sashirestela.openai.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.common.content.ImageDetail;
import io.github.sashirestela.openai.common.function.Functional;
import io.github.sashirestela.openai.domain.assistant.VectorStoreRequest;
import io.github.sashirestela.openai.domain.chat.ChatRequest.ReasoningEffort;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;
import io.github.sashirestela.openai.domain.file.FileResponse;
import io.github.sashirestela.openai.domain.response.*;
import io.github.sashirestela.openai.domain.response.Input.Content.ImageInputContent;
import io.github.sashirestela.openai.domain.response.Input.Content.TextInputContent;
import io.github.sashirestela.openai.domain.response.Input.InputMessage;
import io.github.sashirestela.openai.domain.response.Input.MessageRole;
import io.github.sashirestela.openai.domain.response.ResponseText.ResponseTextFormat;
import io.github.sashirestela.openai.domain.response.events.ResponseDelta;
import io.github.sashirestela.openai.service.ResponseServices;
import io.github.sashirestela.openai.support.Base64Util;
import io.github.sashirestela.openai.support.Base64Util.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.sashirestela.openai.domain.response.events.ResponseEventName.*;

public class ResponseDemo extends AbstractDemo {

    protected ResponseRequest responseRequest;
    protected String model;
    protected ResponseServices responseProvider;
    protected final ObjectMapper mapper = new ObjectMapper();

    // Conversation state management
    protected List<InputMessage> conversationHistory;
    protected boolean conversationMode;

    protected ResponseDemo(String model) {
        this("standard", model);
    }

    protected ResponseDemo(String provider, String model) {
        super(provider);
        this.model = model;
        this.conversationHistory = new ArrayList<>();
        this.conversationMode = false;

        // Create a client adapter with custom interceptors
        var clientAdapter = new io.github.sashirestela.cleverclient.client.JavaHttpClientAdapter();

        // Add request interceptor to log the JSON body
        clientAdapter.setRequestInterceptor(requestData -> {
            try {
                if (requestData.getBody() != null) {
                    System.out.println("\n======= REQUEST JSON =======");
                    System.out.println(mapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(requestData.getBody()));
                    System.out.println("===========================\n");
                }
            } catch (Exception e) {
                System.out.println("Error serializing request: " + e.getMessage());
            }
            return requestData;
        });

        // Add response interceptor to log the response body
        clientAdapter.setResponseInterceptor(responseData -> {
            try {
                if (responseData.getBody() != null) {
                    System.out.println("\n======= RESPONSE JSON =======");
                    System.out.println(mapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(mapper.readTree(responseData.getBody())));
                    System.out.println("============================\n");
                }
            } catch (Exception e) {
                System.out.println("Error serializing response: " + e.getMessage());
            }
            return responseData;
        });

        // Create OpenAI client with the configured client adapter
        this.openAI = new io.github.sashirestela.openai.SimpleOpenAI(
                System.getenv("OPENAI_API_KEY"), // Get API key from environment
                null, // No organization ID
                null, // No project ID
                null, // Use default base URL
                null, // No HTTP client (will be managed by client adapter)
                clientAdapter, // Our configured client adapter
                null, // Default retry config
                mapper, // Use our object mapper
                null  // No realtime config
        );
        this.responseProvider = this.openAI;

        responseRequest = ResponseRequest.builder()
                .input("Write a short technical description of how large language models work, in no more than 100 words.")
                .model(model)
                .temperature(0.0)
                .maxOutputTokens(300)
                .build();
    }

    public void demoCallResponseStreaming() {
        System.out.println("Sending request to the OpenAI Response API (streaming)...");
        var futureResponse = responseProvider.responses().createStreamEvent(responseRequest);
        var eventStream = futureResponse.join();

        System.out.println("Response:");
        eventStream.forEach(event -> {
            String eventType = event.getName();

            switch (eventType) {
                case RESPONSE_OUTPUT_TEXT_DELTA:
                    // For incremental text updates, print the delta
                    ResponseDelta deltaEvent = (ResponseDelta) event.getData();
                    System.out.print(deltaEvent.getDelta());
                    break;

                case RESPONSE_COMPLETED:
                    // When response is complete, print usage info
                    Response response = (Response) event.getData();
                    var usage = response.getUsage();
                    if (usage != null) {
                        System.out.println("\n");
                        System.out.println(usage);
                    }
                    break;

                // Other event types can be handled as needed
            }
        });
    }

    public void demoCallResponseBlocking() {
        System.out.println("Sending request to the OpenAI Response API (blocking)...");
        var futureResponse = responseProvider.responses().create(responseRequest);
        var response = futureResponse.join();
        System.out.println("Response:");
        System.out.println(extractTextFromResponse(response));

        var usage = response.getUsage();
        if (usage != null) {
            System.out.println();
            System.out.println(usage);
        }
    }

    public void demoCallResponseWithFunctions() {
        var question = "What is the product of 5 and 7?";
        System.out.println("Question: " + question);

        try {
            // Create a multiplication function tool
            var functionSchema = "{"
                    + "\"type\": \"object\","
                    + "\"properties\": {"
                    + "  \"a\": {\"type\": \"number\", \"description\": \"The first number\"},"
                    + "  \"b\": {\"type\": \"number\", \"description\": \"The second number\"}"
                    + "},"
                    + "\"required\": [\"a\", \"b\"]"
                    + "}";

            var multiplyTool = ResponseTool.builder()
                    .type(ResponseTool.ResponseToolType.FUNCTION)
                    .name("multiply")
                    .description("Multiply two numbers")
                    .parameters(mapper.readTree(functionSchema))
                    .build();

            // Create the request with our tool
            var request = ResponseRequest.builder()
                    .input(question)
                    .model(model)
                    .tool(multiplyTool)
                    .toolChoice(ResponseToolChoice.required()) // Force function usage
                    .build();

            System.out.println("Sending request to the OpenAI Response API...");
            var response = responseProvider.responses().create(request).join();
            System.out.println("Response received from OpenAI");

            // Process the function call
            if (response.getOutput() != null) {
                for (Object output : response.getOutput()) {
                    if (output instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> outputMap = (Map<String, Object>) output;

                        if ("function_call".equals(outputMap.get("type"))) {
                            var functionName = (String) outputMap.get("name");
                            var functionArguments = (String) outputMap.get("arguments");

                            System.out.println("\nFunction call detected:");
                            System.out.println("Function: " + functionName);
                            System.out.println("Arguments: " + functionArguments);

                            // Execute the function
                            if ("multiply".equals(functionName)) {
                                var multiply = mapper.readValue(functionArguments, Multiply.class);
                                var result = multiply.execute();

                                System.out.println("\nFunction execution result: 5 × 7 = " + result);

                                // Send a simple follow-up question with the result
                                System.out.println("Sending follow-up request to the model...");
                                var followUpInput = "The multiply function calculated that 5 × 7 = " + result +
                                        ". Can you explain this calculation to a child and give some examples of where this multiplication might be used in everyday life?";

                                var followUpRequest = ResponseRequest.builder()
                                        .input(followUpInput)
                                        .model(model)
                                        .build();

                                System.out.println("Waiting for model's response...");
                                // Get the model's final response using streaming for better user experience
                                var finalResponseEventStream = responseProvider.responses()
                                        .createStreamEvent(followUpRequest)
                                        .join();
                                System.out.println("\nModel's final answer:");
                                finalResponseEventStream.forEach(event -> {
                                    if (RESPONSE_OUTPUT_TEXT_DELTA.equals(event.getName())) {
                                        ResponseDelta deltaEvent = (ResponseDelta) event.getData();
                                        System.out.print(deltaEvent.getDelta());
                                    }
                                });
                                System.out.println();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void demoCallResponseWithWebSearch() {
        var question = "What are the latest developments in quantum computing as of 2025?";
        System.out.println("Question: " + question);

        // Create the request with web search tool
        var request = ResponseRequest.builder()
                .input(question)
                .model(model)
                .tool(ResponseTool.webSearchTool())
                .build();

        System.out.println("Sending request with web search capability...");
        var futureResponse = responseProvider.responses().createStreamEvent(request);
        var eventStream = futureResponse.join();

        System.out.println("Response:");
        eventStream.forEach(event -> {
            String eventType = event.getName();

            switch (eventType) {
                case RESPONSE_OUTPUT_TEXT_DELTA:
                    // For incremental text updates, print the delta
                    ResponseDelta deltaEvent = (ResponseDelta) event.getData();
                    System.out.print(deltaEvent.getDelta());
                    break;

                case "response.web_search_call.in_progress":
                    System.out.println("Web search in progress...");
                    break;

                case "response.web_search_call.searching":
                    System.out.println("Searching the web...");
                    break;

                case "response.web_search_call.completed":
                    System.out.println("Web search completed.");
                    break;

                case RESPONSE_COMPLETED:
                    // When response is complete, print usage info
                    Response response = (Response) event.getData();
                    var usage = response.getUsage();
                    if (usage != null) {
                        System.out.println("\n");
                        System.out.println(usage);
                    }
                    break;

                default:
                    // Print any other events for debugging
                    System.out.println("\nEvent: " + eventType);
                    break;
            }
        });
    }

    public void demoCallResponseWithExternalImage() {
        System.out.println("Sending request with external image to the OpenAI Response API...");

        // Create a list of input content
        var inputContents = List.of(
                TextInputContent.of("What do you see in this image? Describe it in detail in no more than 100 words."),
                ImageInputContent.builder()
                        .imageUrl("https://upload.wikimedia.org/wikipedia/commons/e/eb/Machu_Picchu%2C_Peru.jpg")
                        .detail(ImageDetail.HIGH)
                        .build());

        // Create a message with the input contents
        var message = InputMessage.of(inputContents, MessageRole.USER);

        // Provide the message as part of a collection
        var request = ResponseRequest.builder()
                .input(List.of(message))
                .model(model)
                .build();

        var futureResponse = responseProvider.responses().createStreamEvent(request);
        var eventStream = futureResponse.join();

        System.out.println("Response:");
        eventStream.forEach(event -> {
            String eventType = event.getName();

            switch (eventType) {
                case RESPONSE_OUTPUT_TEXT_DELTA:
                    // For incremental text updates, print the delta
                    ResponseDelta deltaEvent = (ResponseDelta) event.getData();
                    System.out.print(deltaEvent.getDelta());
                    break;

                case RESPONSE_COMPLETED:
                    // When response is complete, print usage info
                    Response response = (Response) event.getData();
                    var usage = response.getUsage();
                    if (usage != null) {
                        System.out.println("\n");
                        System.out.println(usage);
                    }
                    break;
            }
        });
    }

    public void demoCallResponseWithLocalImage() {
        System.out.println("Sending request with local image to the OpenAI Response API...");

        // Create a list of input content with text and base64-encoded local image
        var inputContents = List.of(
                TextInputContent.of("What do you see in this image? Describe it in detail in no more than 100 words."),
                ImageInputContent.builder()
                        .imageUrl(Base64Util.encode("src/demo/resources/machupicchu.jpg", MediaType.IMAGE))
                        .detail(ImageDetail.HIGH)
                        .build());

        // Create a message with the input contents
        var message = InputMessage.of(inputContents, MessageRole.USER);

        // Provide the message as part of a collection
        var request = ResponseRequest.builder()
                .input(List.of(message))
                .model(model)
                .build();

        var futureResponse = responseProvider.responses().createStreamEvent(request);
        var eventStream = futureResponse.join();

        System.out.println("Response:");
        eventStream.forEach(event -> {
            String eventType = event.getName();

            switch (eventType) {
                case RESPONSE_OUTPUT_TEXT_DELTA:
                    // For incremental text updates, print the delta
                    ResponseDelta deltaEvent = (ResponseDelta) event.getData();
                    System.out.print(deltaEvent.getDelta());
                    break;

                case RESPONSE_COMPLETED:
                    // When response is complete, print usage info
                    Response response = (Response) event.getData();
                    var usage = response.getUsage();
                    if (usage != null) {
                        System.out.println("\n");
                        System.out.println(usage);
                    }
                    break;
            }
        });
    }

    public void demoCallResponseWithFileSearch() {
        System.out.println("Sending request with file search capability to the OpenAI Response API...");

        try {
            // First, upload a text file to be searched
            FileResponse fileResponse = null;
            String vectorStoreId = null;

            try {
                // Create a file upload request
                var fileRequest = FileRequest.builder()
                        .purpose(PurposeType.ASSISTANTS)
                        .file(java.nio.file.Path.of("src/demo/resources/mistral-ai.txt"))
                        .build();

                System.out.println("Uploading file to OpenAI...");
                fileResponse = openAI.files().create(fileRequest).join();
                System.out.println("File uploaded successfully with ID: " + fileResponse.getId());

                // Wait a moment for the file to be processed
                try {
                    System.out.println("Waiting for file to be processed...");
                    Thread.sleep(3000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

                // Create a vector store to hold the file
                var vectorStoreRequest = VectorStoreRequest.builder()
                        .name("MistralInfo")
                        .build();

                System.out.println("Creating vector store...");
                var vectorStore = openAI.vectorStores().create(vectorStoreRequest).join();
                vectorStoreId = vectorStore.getId();
                System.out.println("Vector store created with ID: " + vectorStoreId);

                // Add the file to the vector store
                System.out.println("Adding file to vector store...");
                openAI.vectorStoreFiles().create(vectorStoreId, fileResponse.getId()).join();
                System.out.println("File added to vector store.");

                // Wait for vector store to process
                try {
                    System.out.println("Waiting for vector store to process...");
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

                // Create a prompt to search the uploaded file
                var prompt = "Analyze the information about Mistral AI and summarize the key points in 3-5 bullet points.";

                // Create the specialized file search tool
                var fileSearchTool = ResponseTool.fileSearchTool(List.of(vectorStoreId));

                // Create request with tools as a raw JSON node
                var request = ResponseRequest.builder()
                        .input(prompt)
                        .model(model)
                        .tools(List.of(fileSearchTool)) // Use the custom tools array with proper structure
                        .toolChoice("auto")
                        .build();

                try {
                    // Print the tool for debugging
                    System.out.println("\nUsing file search tool with parameters:");
                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fileSearchTool));
                } catch (Exception e) {
                    System.out.println("Error printing tool: " + e.getMessage());
                }

                System.out.println("Sending request with file search...");
                System.out.println("Using improved ResponseTool.FileSearchTool implementation...");
                var futureResponse = responseProvider.responses().createStreamEvent(request);
                var eventStream = futureResponse.join();

                System.out.println("Response:");
                eventStream.forEach(event -> {
                    String eventType = event.getName();

                    switch (eventType) {
                        case RESPONSE_OUTPUT_TEXT_DELTA:
                            // For incremental text updates, print the delta
                            ResponseDelta deltaEvent = (ResponseDelta) event.getData();
                            System.out.print(deltaEvent.getDelta());
                            break;

                        case "response.file_search_call.in_progress":
                            System.out.println("File search in progress...");
                            break;

                        case "response.file_search_call.searching":
                            System.out.println("Searching through files...");
                            break;

                        case "response.file_search_call.completed":
                            System.out.println("File search completed.");
                            break;

                        case RESPONSE_COMPLETED:
                            // When response is complete, print usage info
                            Object dataObj = event.getData();
                            if (dataObj instanceof Response) {
                                Response response = (Response) dataObj;
                                var usage = response.getUsage();
                                if (usage != null) {
                                    System.out.println("\n");
                                    System.out.println(usage);
                                }
                            }
                            break;

                        default:
                            // Print any other events for debugging
                            System.out.println("\nEvent: " + eventType);
                            break;
                    }
                });

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
                throw e;
            } finally {
                // Clean up vector store
                if (vectorStoreId != null) {
                    try {
                        System.out.println("Cleaning up - deleting vector store...");
                        openAI.vectorStores().delete(vectorStoreId).join();
                        System.out.println("Vector store deleted successfully.");
                    } catch (Exception e) {
                        System.out.println("Warning: Could not delete vector store: " + e.getMessage());
                    }
                }

                // Clean up - delete the file when done
                if (fileResponse != null) {
                    try {
                        System.out.println("Cleaning up - deleting uploaded file...");
                        openAI.files().delete(fileResponse.getId()).join();
                        System.out.println("File deleted successfully.");
                    } catch (Exception e) {
                        System.out.println("Warning: Could not delete file: " + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Extracts the text content from a Response object.
     */
    private String extractTextFromResponse(Response response) {
        if (response.getOutput() == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();

        for (Object output : response.getOutput()) {
            if (!(output instanceof Map)) {
                continue;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> outputMap = (Map<String, Object>) output;
            if (!"message".equals(outputMap.get("type"))) {
                continue;
            }

            Object contentObj = outputMap.get("content");
            if (!(contentObj instanceof List)) {
                continue;
            }

            @SuppressWarnings("unchecked")
            List<?> contentList = (List<?>) contentObj;
            for (Object contentItem : contentList) {
                if (!(contentItem instanceof Map)) {
                    continue;
                }

                @SuppressWarnings("unchecked")
                Map<String, Object> contentItemMap = (Map<String, Object>) contentItem;
                if (!"output_text".equals(contentItemMap.get("type"))) {
                    continue;
                }

                result.append((String) contentItemMap.get("text"));
            }
        }

        return result.toString();
    }

    public static class Multiply implements Functional {

        @JsonPropertyDescription("The first number")
        @JsonProperty(required = true)
        public double a;

        @JsonPropertyDescription("The second number")
        @JsonProperty(required = true)
        public double b;

        public Multiply() {
        }

        public Multiply(double a, double b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public Object execute() {
            return a * b;
        }

    }

    /**
     * Demonstrates using the Response API with structured JSON output format. This method shows how to
     * request responses in a specific JSON structure defined by a JSON Schema.
     */
    public void demoCallResponseWithStructuredOutput() {
        System.out.println("Sending request with structured JSON output format...");

        try {
            // Define JSON schema for the response
            String schema = "{"
                    + "  \"type\": \"object\","
                    + "  \"additionalProperties\": false,"
                    + "  \"properties\": {"
                    + "    \"name\": {\"type\": \"string\", \"description\": \"The character's full name\"},"
                    + "    \"age\": {\"type\": \"number\", \"description\": \"The character's age\"},"
                    + "    \"occupation\": {\"type\": \"string\", \"description\": \"The character's job title\"},"
                    + "    \"company\": {\"type\": \"string\", \"description\": \"Where the character works\"},"
                    + "    \"skills\": {"
                    + "      \"type\": \"array\","
                    + "      \"description\": \"Programming languages and technologies the character knows\","
                    + "      \"items\": {\"type\": \"string\"}"
                    + "    },"
                    + "    \"background\": {\"type\": \"string\", \"description\": \"Short biography of the character\"}"
                    + "  },"
                    + "  \"required\": [\"name\", \"age\", \"occupation\", \"company\", \"skills\", \"background\"]"
                    + "}";

            // Create a prompt that works well with the schema
            var prompt = "Create a fictional character profile for an experienced software developer. Include their name, age, job title, company, tech skills, and a brief background story.";

            // Create request with JSON schema format
            var schemaNode = mapper.readTree(schema);
            var formatNode = mapper.createObjectNode();
            formatNode.put("type", "json_schema");
            formatNode.set("schema", schemaNode);

            var textNode = mapper.createObjectNode();
            textNode.set("format", formatNode);

            var format = ResponseTextFormat.ResponseTextFormatJsonSchema.builder()
                    .name("character_profile")
                    .schema(schemaNode)
                    .description("A fictional character profile")
                    .strict(true)
                    .build();
            var text = ResponseText.jsonSchema(format);
            var request = ResponseRequest.builder()
                    .input(prompt)
                    .model(model)
                    .text(text)
                    .build();

            System.out.println("JSON Schema being used:");
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schemaNode));

            // Make the request and process response
            System.out.println("\nSending request...");
            var futureResponse = responseProvider.responses().create(request);
            var response = futureResponse.join();

            System.out.println("Structured JSON Response:");

            // Extract the JSON from the response
            if (response.getOutput() != null) {
                for (Object outputItem : response.getOutput()) {
                    if (outputItem instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> outputMap = (Map<String, Object>) outputItem;
                        if (outputMap.containsKey("content")) {
                            System.out.println(
                                    mapper.writerWithDefaultPrettyPrinter()
                                            .writeValueAsString(outputMap.get("content")));
                        }
                    }
                }
            }

            // Display usage information
            var usage = response.getUsage();
            if (usage != null) {
                System.out.println("\nToken Usage:");
                System.out.println(usage);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates using the Response API with instructions parameter. This method shows how to provide
     * system-level instructions to guide the model's response.
     */
    public void demoCallResponseWithInstructions() {
        System.out.println("Sending request with instructions to the OpenAI Response API...");

        try {
            // Create a prompt that requires reasoning
            var prompt = "What would happen if the moon suddenly disappeared? Explain the effects on Earth's environment, tides, and other astronomical impacts.";

            // Create request with detailed instructions for the model
            var request = ResponseRequest.builder()
                    .input(prompt)
                    .model(model)
                    .instructions("You are an expert astrophysicist with detailed knowledge of planetary systems. " +
                            "Provide scientifically accurate explanations using clear, structured reasoning. " +
                            "Include references to physical laws where appropriate and organize your response " +
                            "into distinct impact categories (e.g., gravitational, environmental, astronomical).")
                    .build();

            System.out.println("Sending request with detailed instructions...");
            var futureResponse = responseProvider.responses().createStreamEvent(request);
            var eventStream = futureResponse.join();

            System.out.println("Response with instructions:");
            eventStream.forEach(event -> {
                String eventType = event.getName();

                switch (eventType) {
                    case RESPONSE_OUTPUT_TEXT_DELTA:
                        // For incremental text updates, print the delta
                        ResponseDelta deltaEvent = (ResponseDelta) event.getData();
                        System.out.print(deltaEvent.getDelta());
                        break;

                    case RESPONSE_COMPLETED:
                        // When response is complete, print usage info
                        Response response = (Response) event.getData();
                        var usage = response.getUsage();
                        if (usage != null) {
                            System.out.println("\n");
                            System.out.println(usage);
                        }
                        break;

                    default:
                        // Print any other events for debugging
                        System.out.println("\nEvent: " + eventType);
                        break;
                }
            });

            // Now demonstrate without instructions for comparison
            System.out.println("\n\nNow sending a similar request without instructions for comparison...");

            var basicRequest = ResponseRequest.builder()
                    .input(prompt)
                    .model(model)
                    .build();

            var basicFutureResponse = responseProvider.responses().createStreamEvent(basicRequest);
            var basicEventStream = basicFutureResponse.join();

            System.out.println("Response without instructions:");
            basicEventStream.forEach(event -> {
                if (RESPONSE_OUTPUT_TEXT_DELTA.equals(event.getName())) {
                    ResponseDelta deltaEvent = (ResponseDelta) event.getData();
                    System.out.print(deltaEvent.getDelta());
                } else if (RESPONSE_COMPLETED.equals(event.getName())) {
                    Response response = (Response) event.getData();
                    var usage = response.getUsage();
                    if (usage != null) {
                        System.out.println("\n");
                        System.out.println(usage);
                    }
                }
            });

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates using the Response API with reasoning capabilities. This method shows how to control
     * the model's reasoning process and get more detailed explanations for complex questions.
     */
    public void demoCallResponseWithReasoning() {
        System.out.println("Sending request with reasoning controls to the OpenAI Response API...");

        try {
            // Create a prompt that requires reasoning
            var prompt = "What would happen if the moon suddenly disappeared? Explain the effects on Earth's environment, tides, and other astronomical impacts.";

            // We need to use a special constructor to set a different model just for this demo
            var demoWithReasoningModel = new ResponseDemo("o4-mini");

            // Create request with reasoning controls
            var request = ResponseRequest.builder()
                    .input(prompt)
                    .model("o4-mini") // Using o4-mini which supports reasoning
                    // Use HIGH for maximum reasoning capability
                    .reasoning(Reasoning.of(
                            ReasoningEffort.HIGH,  // Options: LOW, MEDIUM, HIGH
                            Reasoning.Summary.DETAILED  // Options: AUTO, CONCISE, DETAILED
                    ))
                    .build();

            System.out.println("Sending request with HIGH reasoning effort to GPT-4o-mini...");
            var futureResponse = responseProvider.responses().createStreamEvent(request);
            var eventStream = futureResponse.join();

            System.out.println("Response with detailed reasoning:");
            eventStream.forEach(event -> {
                String eventType = event.getName();

                switch (eventType) {
                    case RESPONSE_OUTPUT_TEXT_DELTA:
                        // For incremental text updates, print the delta
                        ResponseDelta deltaEvent = (ResponseDelta) event.getData();
                        System.out.print(deltaEvent.getDelta());
                        break;

                    case "response.reasoning.in_progress":
                        System.out.println("\n[Reasoning in progress...]\n");
                        break;

                    case "response.reasoning.completed":
                        System.out.println("\n[Reasoning completed]\n");
                        break;

                    case RESPONSE_COMPLETED:
                        // When response is complete, print usage info
                        Response response = (Response) event.getData();
                        var usage = response.getUsage();
                        if (usage != null) {
                            System.out.println("\n");
                            System.out.println(usage);
                        }
                        break;

                    default:
                        // Print any other reasoning-related events
                        if (eventType.startsWith("response.reasoning")) {
                            System.out.println("\nEvent: " + eventType);
                        }
                        break;
                }
            });

            // Now demonstrate with minimal reasoning for comparison
            System.out.println("\n\nNow sending a similar request with LOW reasoning effort for comparison...");

            var minimalRequest = ResponseRequest.builder()
                    .input(prompt)
                    .model("gpt-4o-mini")
                    .reasoning(Reasoning.of(ReasoningEffort.LOW, null))
                    .build();

            var minimalFutureResponse = demoWithReasoningModel.responseProvider.responses()
                    .createStreamEvent(minimalRequest);
            var minimalEventStream = minimalFutureResponse.join();

            System.out.println("Response with minimal reasoning:");
            minimalEventStream.forEach(event -> {
                if (RESPONSE_OUTPUT_TEXT_DELTA.equals(event.getName())) {
                    ResponseDelta deltaEvent = (ResponseDelta) event.getData();
                    System.out.print(deltaEvent.getDelta());
                } else if (RESPONSE_COMPLETED.equals(event.getName())) {
                    Response response = (Response) event.getData();
                    var usage = response.getUsage();
                    if (usage != null) {
                        System.out.println("\n");
                        System.out.println(usage);
                    }
                }
            });

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates using the Response API with the simple JSON object output format. This method shows
     * how to request responses in a generic JSON format without specifying a schema.
     */
    public void demoCallResponseWithJsonObject() {
        System.out.println("Sending request with simple JSON object output format...");

        try {
            // Create a prompt that works well with JSON output
            var prompt = "List 3 popular programming languages with their main use cases and key features. Format as JSON.";

            // Create request with JSON object format
            var request = ResponseRequest.builder()
                    .input(prompt)
                    .model(model)
                    .text(ResponseText.jsonObject())  // Use the simple JSON object format
                    .build();

            System.out.println("Sending request with JSON object format...");
            var futureResponse = responseProvider.responses().create(request);
            var response = futureResponse.join();

            System.out.println("JSON Object Response:");

            // Extract the JSON from the response
            if (response.getOutput() != null) {
                for (Object outputItem : response.getOutput()) {
                    if (outputItem instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> outputMap = (Map<String, Object>) outputItem;
                        if (outputMap.containsKey("content")) {
                            // Pretty print the JSON content
                            System.out.println(
                                    mapper.writerWithDefaultPrettyPrinter()
                                            .writeValueAsString(outputMap.get("content")));
                        }
                    }
                }
            }

            // Display usage information
            var usage = response.getUsage();
            if (usage != null) {
                System.out.println("\nToken Usage:");
                System.out.println(usage);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates an interactive conversation with the OpenAI Response API
     * using the built-in conversation state management with previous_response_id.
     * This approach lets OpenAI handle the conversation history automatically.
     */
    public void demoInteractiveConversation() {
        System.out.println("Starting an interactive conversation with the OpenAI Response API...");
        System.out.println("Using built-in conversation state management with previous_response_id");
        System.out.println("Type 'exit' to end the conversation");

        // Set conversation mode to true
        this.conversationMode = true;

        var console = System.console();
        if (console == null) {
            System.out.println("No console available. Running in non-interactive mode.");
            System.out.println("\nTo run this demo interactively, use a terminal and execute:");
            System.out.println("java -cp target/classes io.github.sashirestela.openai.demo.ResponseDemo");
            return;
        }

        // Create a non-interactive example first
        System.out.println("\nDemonstration of previous_response_id with a joke example:");

        // First request - tell a joke
        var jokeRequest = ResponseRequest.builder()
                .input("Tell me a joke")
                .model(model)
                .temperature(0.7)
                .build();

        System.out.println("User: Tell me a joke");
        System.out.println("\nAssistant: ");

        var jokeResponse = responseProvider.responses().create(jokeRequest).join();
        String jokeText = extractTextFromResponse(jokeResponse);
        System.out.println(jokeText);

        // Second request - using previous_response_id to ask for explanation
        var explainRequest = ResponseRequest.builder()
                .input("Explain why this is funny")
                .model(model)
                .previousResponseId(jokeResponse.getId())  // Key feature: using previous_response_id
                .temperature(0.7)
                .build();

        System.out.println("\nUser: Explain why this is funny");
        System.out.println("\nAssistant: ");

        var explainResponse = responseProvider.responses().create(explainRequest).join();
        String explainText = extractTextFromResponse(explainResponse);
        System.out.println(explainText);

        System.out.println("\n---------------------------------------------");
        System.out.println("Now starting interactive conversation...");
        System.out.println("Each response will use the previous_response_id to maintain context");
        System.out.println("---------------------------------------------\n");

        // Start interactive conversation
        String userInput = console.readLine("\nYou: ");
        String previousResponseId = null;

        while (!userInput.equalsIgnoreCase("exit")) {
            // Create request with or without previous_response_id
            ResponseRequest request;

            // Add previous_response_id if we have one from a previous turn
            if (previousResponseId != null) {
                request = ResponseRequest.builder()
                        .input(userInput)
                        .model(model)
                        .temperature(0.7)
                        .previousResponseId(previousResponseId)
                        .build();
            } else {
                request = ResponseRequest.builder()
                        .input(userInput)
                        .model(model)
                        .temperature(0.7)
                        .build();
            }

            System.out.println("\nAssistant: ");

            // Process response using streaming for better user experience
            var futureResponse = responseProvider.responses().createStreamEvent(request);
            var eventStream = futureResponse.join();

            Response finalResponse = null;

            eventStream.forEach(event -> {
                String eventType = event.getName();

                switch (eventType) {
                    case RESPONSE_OUTPUT_TEXT_DELTA:
                        // For incremental text updates, print the delta
                        ResponseDelta deltaEvent = (ResponseDelta) event.getData();
                        System.out.print(deltaEvent.getDelta());
                        break;

                    case RESPONSE_COMPLETED:
                        // When response is complete, capture it to get its ID
                        break;
                }
            });

            // Get the complete response to extract the ID for the next turn
            finalResponse = responseProvider.responses().retrieve(responseProvider.responses()
                .create(request).join().getId()).join();

            // Save the response ID for the next turn
            previousResponseId = finalResponse.getId();

            // Get next user input
            userInput = console.readLine("\n\nYou: ");
        }

        System.out.println("Conversation ended.");
        System.out.println("\nNote: The conversation history is automatically managed by OpenAI");
        System.out.println("through the previous_response_id parameter and is saved on their servers");
        System.out.println("for 30 days unless the store parameter is set to false.");
    }

    /**
     * Demonstrates an interactive conversation with the OpenAI Response API with function calling capabilities.
     * This method uses previous_response_id for state management and supports function execution.
     */
    public void demoInteractiveConversationWithFunctions() {
        System.out.println("Starting an interactive conversation with the OpenAI Response API with function capabilities...");
        System.out.println("Using built-in conversation state management with previous_response_id");
        System.out.println("You can ask about weather conditions for any location.");
        System.out.println("Type 'exit' to end the conversation");

        // Set conversation mode to true
        this.conversationMode = true;

        // Create weather functions
        var weatherSchema = "{" +
                "\"type\": \"object\"," +
                "\"properties\": {" +
                "  \"location\": {\"type\": \"string\", \"description\": \"The city and state, e.g., San Francisco, CA\"}," +
                "  \"unit\": {\"type\": \"string\", \"enum\": [\"celsius\", \"fahrenheit\"], \"description\": \"The temperature unit to use\"}" +
                "}," +
                "\"required\": [\"location\", \"unit\"]" +
                "}";

        var rainSchema = "{" +
                "\"type\": \"object\"," +
                "\"properties\": {" +
                "  \"location\": {\"type\": \"string\", \"description\": \"The city and state, e.g., San Francisco, CA\"}" +
                "}," +
                "\"required\": [\"location\"]" +
                "}";

        try {
            var getWeatherTool = ResponseTool.builder()
                    .type(ResponseTool.ResponseToolType.FUNCTION)
                    .name("getCurrentWeather")
                    .description("Get the current weather conditions for a specific location")
                    .parameters(mapper.readTree(weatherSchema))
                    .build();

            var getRainTool = ResponseTool.builder()
                    .type(ResponseTool.ResponseToolType.FUNCTION)
                    .name("getRainProbability")
                    .description("Get the probability of rain for a specific location")
                    .parameters(mapper.readTree(rainSchema))
                    .build();

            var console = System.console();
            if (console == null) {
                System.out.println("No console available. Running in non-interactive mode.");
                System.out.println("\nTo run this demo interactively, use a terminal and execute:");
                System.out.println("java -cp target/classes io.github.sashirestela.openai.demo.ResponseDemo");
                return;
            }

            // Start with a demonstration example
            System.out.println("\nDemonstration of function calling with previous_response_id:");

            // First request - ask about weather
            var weatherRequest = ResponseRequest.builder()
                    .input("What's the weather like in San Francisco?")
                    .model(model)
                    .tools(List.of(getWeatherTool, getRainTool))
                    .toolChoice("auto")
                    .temperature(0.7)
                    .build();

            System.out.println("User: What's the weather like in San Francisco?");
            System.out.println("\nAssistant: ");

            // Process first request and check for function calls
            var weatherResponse = responseProvider.responses().create(weatherRequest).join();
            String responseId = weatherResponse.getId();
            boolean functionWasCalled = false;
            String functionName = "";
            String functionArguments = "";
            String functionResult = "";

            // Check if there are any function calls in the response
            if (weatherResponse.getOutput() != null) {
                for (Object output : weatherResponse.getOutput()) {
                    if (output instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> outputMap = (Map<String, Object>) output;

                        if ("function_call".equals(outputMap.get("type"))) {
                            functionWasCalled = true;
                            functionName = (String) outputMap.get("name");
                            functionArguments = (String) outputMap.get("arguments");

                            System.out.println("[Function call detected: " + functionName + "]");

                            // Execute the function
                            if ("getCurrentWeather".equals(functionName)) {
                                try {
                                    var weather = mapper.readValue(functionArguments, WeatherInfo.class);
                                    double temp = executeGetWeather(weather.location, weather.unit);
                                    functionResult = "The current temperature in " + weather.location + " is " +
                                            String.format("%.1f", temp) + "° " +
                                            (weather.unit.startsWith("c") ? "Celsius" : "Fahrenheit");
                                } catch (Exception e) {
                                    functionResult = "Error getting weather data: " + e.getMessage();
                                }
                            } else if ("getRainProbability".equals(functionName)) {
                                try {
                                    var rainInfo = mapper.readValue(functionArguments, RainInfo.class);
                                    double probability = executeGetRainProbability(rainInfo.location);
                                    functionResult = "The probability of rain in " + rainInfo.location +
                                            " is " + String.format("%.1f", probability) + "%";
                                } catch (Exception e) {
                                    functionResult = "Error getting rain probability: " + e.getMessage();
                                }
                            }

                            // Print function result
                            System.out.println("[Function result: " + functionResult + "]");
                            break;
                        }
                    }
                }
            }

            if (!functionWasCalled) {
                // If no function was called, just print the text response
                String responseText = extractTextFromResponse(weatherResponse);
                System.out.println(responseText);
            } else {
                // If a function was called, submit the result with the previous_response_id
                var functionResultRequest = ResponseRequest.builder()
                        .input(functionResult)
                        .model(model)
                        .previousResponseId(responseId)  // Using previous_response_id to maintain context
                        .temperature(0.7)
                        .build();

                System.out.println("\nAssistant (after function call): ");

                var functionResultResponse = responseProvider.responses().create(functionResultRequest).join();
                String finalResponseText = extractTextFromResponse(functionResultResponse);
                System.out.println(finalResponseText);

                // Save the response ID for the next turn
                responseId = functionResultResponse.getId();
            }

            System.out.println("\n---------------------------------------------");
            System.out.println("Now starting interactive conversation...");
            System.out.println("Each response will use the previous_response_id to maintain context");
            System.out.println("---------------------------------------------\n");

            // Start interactive conversation
            String userInput = console.readLine("\nYou: ");
            String previousResponseId = responseId;  // Start with the last response ID from the demo

            while (!userInput.equalsIgnoreCase("exit")) {
                // Create request with tools and previous_response_id if available
                ResponseRequest request;

                // Add previous_response_id if we have one
                if (previousResponseId != null) {
                    request = ResponseRequest.builder()
                            .input(userInput)
                            .model(model)
                            .tools(List.of(getWeatherTool, getRainTool))
                            .toolChoice("auto")
                            .temperature(0.7)
                            .previousResponseId(previousResponseId)
                            .build();
                } else {
                    request = ResponseRequest.builder()
                            .input(userInput)
                            .model(model)
                            .tools(List.of(getWeatherTool, getRainTool))
                            .toolChoice("auto")
                            .temperature(0.7)
                            .build();
                }

                System.out.println("\nAssistant: ");

                // Process response
                var response = responseProvider.responses().create(request).join();
                previousResponseId = response.getId();  // Save response ID for next turn

                // Check for function calls in the response
                functionWasCalled = false;

                if (response.getOutput() != null) {
                    for (Object output : response.getOutput()) {
                        if (output instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> outputMap = (Map<String, Object>) output;

                            if ("function_call".equals(outputMap.get("type"))) {
                                functionWasCalled = true;
                                functionName = (String) outputMap.get("name");
                                functionArguments = (String) outputMap.get("arguments");

                                System.out.println("[Function call: " + functionName + "]");

                                // Execute the function
                                functionResult = "";
                                if ("getCurrentWeather".equals(functionName)) {
                                    try {
                                        var weather = mapper.readValue(functionArguments, WeatherInfo.class);
                                        double temp = executeGetWeather(weather.location, weather.unit);
                                        functionResult = "The current temperature in " + weather.location + " is " +
                                                String.format("%.1f", temp) + "° " +
                                                (weather.unit.startsWith("c") ? "Celsius" : "Fahrenheit");
                                    } catch (Exception e) {
                                        functionResult = "Error getting weather data: " + e.getMessage();
                                    }
                                } else if ("getRainProbability".equals(functionName)) {
                                    try {
                                        var rainInfo = mapper.readValue(functionArguments, RainInfo.class);
                                        double probability = executeGetRainProbability(rainInfo.location);
                                        functionResult = "The probability of rain in " + rainInfo.location +
                                                " is " + String.format("%.1f", probability) + "%";
                                    } catch (Exception e) {
                                        functionResult = "Error getting rain probability: " + e.getMessage();
                                    }
                                }

                                // Print function result
                                System.out.println("[Function result: " + functionResult + "]");

                                // Submit the function result with the previous_response_id
                                var functionResultRequest = ResponseRequest.builder()
                                        .input(functionResult)
                                        .model(model)
                                        .previousResponseId(previousResponseId)  // Using previous_response_id to maintain context
                                        .temperature(0.7)
                                        .build();

                                System.out.println("\nAssistant (after function call): ");

                                // Use streaming for better user experience
                                var functionResultStream = responseProvider.responses().createStreamEvent(functionResultRequest).join();
                                functionResultStream.forEach(event -> {
                                    if (RESPONSE_OUTPUT_TEXT_DELTA.equals(event.getName())) {
                                        ResponseDelta deltaEvent = (ResponseDelta) event.getData();
                                        System.out.print(deltaEvent.getDelta());
                                    }
                                });

                                // Get the complete response to extract the ID for the next turn
                                var completeFunctionResponse = responseProvider.responses()
                                        .create(functionResultRequest).join();
                                previousResponseId = completeFunctionResponse.getId();

                                break;
                            }
                        }
                    }

                    // If no function was called, extract and print the text content
                    if (!functionWasCalled) {
                        String assistantResponseText = extractTextFromResponse(response);
                        System.out.println(assistantResponseText);
                    }
                }

                // Get next user input
                userInput = console.readLine("\n\nYou: ");
            }

            System.out.println("Conversation ended.");
            System.out.println("\nNote: The conversation history is automatically managed by OpenAI");
            System.out.println("through the previous_response_id parameter and is saved on their servers");
            System.out.println("for 30 days unless the store parameter is set to false.");
        } catch (Exception e) {
            System.out.println("Error in conversation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper classes for function calling
    private static class WeatherInfo {
        public String location;
        public String unit;
    }

    private static class RainInfo {
        public String location;
    }

    // Weather function implementations
    private double executeGetWeather(String location, String unit) {
        // Simulate getting weather data - in a real app, you would call a weather API
        double centigrades = Math.random() * (40.0 - 10.0) + 10.0;
        if (unit.startsWith("f")) {
            return centigrades * 9.0 / 5.0 + 32.0;
        } else {
            return centigrades;
        }
    }

    private double executeGetRainProbability(String location) {
        // Simulate getting rain probability - in a real app, you would call a weather API
        return Math.random() * 100;
    }


    /**
     * Demonstrates using previous_response_id for conversation state management.
     */
    public void demoConversationStateManagement() {
        System.out.println("Demonstrating OpenAI conversation state management with previous_response_id");
        System.out.println("This example shows how to chain responses using previous_response_id.\n");

        // First request - tell a joke
        System.out.println("Step 1: First request - Tell a joke");
        var jokeRequest = ResponseRequest.builder()
                .input("tell me a joke")
                .model(model)
                .build();

        System.out.println("User: tell me a joke");
        System.out.println("Assistant: ");

        var jokeResponse = responseProvider.responses().create(jokeRequest).join();
        String jokeText = extractTextFromResponse(jokeResponse);
        System.out.println(jokeText);

        // Store the response ID for the next request
        String responseId = jokeResponse.getId();
        System.out.println("\nResponse ID: " + responseId);

        // Second request - using previous_response_id to ask for explanation
        System.out.println("\nStep 2: Second request - Ask to explain why it's funny");
        var explainRequest = ResponseRequest.builder()
                .input("explain why this is funny")
                .model(model)
                .previousResponseId(responseId)  // Key feature: using previous_response_id
                .build();

        System.out.println("User: explain why this is funny");
        System.out.println("Assistant: ");

        var explainResponse = responseProvider.responses().create(explainRequest).join();
        String explainText = extractTextFromResponse(explainResponse);
        System.out.println(explainText);

        System.out.println("\nNote: The second request didn't need to include the joke content.");
        System.out.println("The context was maintained through the previous_response_id parameter.");
        System.out.println("This demonstrates OpenAI's built-in conversation state management.");
    }

    public static void main(String[] args) {
        // Default model for all demos, may be overridden in individual methods
        var demo = new ResponseDemo("gpt-4o");

        demo.addTitleAction("Call Response API (Streaming Approach)", demo::demoCallResponseStreaming);
        demo.addTitleAction("Call Response API (Blocking Approach)", demo::demoCallResponseBlocking);
        demo.addTitleAction("Call Response API with Functions", demo::demoCallResponseWithFunctions);
        demo.addTitleAction("Call Response API with Web Search", demo::demoCallResponseWithWebSearch);
        demo.addTitleAction("Call Response API with External Image", demo::demoCallResponseWithExternalImage);
        demo.addTitleAction("Call Response API with Local Image", demo::demoCallResponseWithLocalImage);
        demo.addTitleAction("Call Response API with File Search", demo::demoCallResponseWithFileSearch);
        demo.addTitleAction("Call Response API with Structured JSON Output", demo::demoCallResponseWithStructuredOutput);
        demo.addTitleAction("Call Response API with JSON Object Format", demo::demoCallResponseWithJsonObject);
        demo.addTitleAction("Call Response API with Instructions", demo::demoCallResponseWithInstructions);
        demo.addTitleAction("Call Response API with Conversation Management", demo::demoConversationStateManagement);
        // Requires organization verification. See https://help.openai.com/en/articles/10910291-api-organization-verification
        //demo.addTitleAction("Call Response API with Reasoning Controls (using o3-mini)", demo::demoCallResponseWithReasoning);

        demo.run();
    }

}
