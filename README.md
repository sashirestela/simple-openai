# 📌 Simple-OpenAI
A Java library to use the OpenAI Api in the simplest possible way.

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sashirestela_simple-openai&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=sashirestela_simple-openai)
[![codecov](https://codecov.io/gh/sashirestela/simple-openai/graph/badge.svg?token=TYLE5788R3)](https://codecov.io/gh/sashirestela/simple-openai)
![Maven Central](https://img.shields.io/maven-central/v/io.github.sashirestela/simple-openai)
![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/sashirestela/simple-openai/build_java_maven.yml)
[![javadoc](https://javadoc.io/badge2/io.github.sashirestela/simple-openai/javadoc.svg)](https://javadoc.io/doc/io.github.sashirestela/simple-openai)


### Table of Contents
- [Description](#-description)
- [Supported Services](#-supported-services)
- [Installation](#-installation)
- [Usage](#-usage)
  - [Creating a SimpleOpenAI Object](#creating-a-simpleopenai-object)
  - [Audio Example](#audio-example)
  - [Image Example](#image-example)
  - [Chat Completion Example](#chat-completion-example)
  - [Chat Completion with Streaming Example](#chat-completion-with-streaming-example)
  - [Chat Completion with Functions Example](#chat-completion-with-functions-example)
  - [Chat Completion with Vision Example](#chat-completion-with-vision-example)
  - [Chat Completion Conversation Example](#chat-completion-conversation-example) **New**
  - [Assistant v2 Conversation Example](#assistant-v2-conversation-example) **New**
- [Support for Additional OpenAI Providers](#-support-for-additional-openai-providers)
  - [Azure OpenAI](#azure-openai)
  - [Anyscale](#anyscale)
- [Run Examples](#-run-examples)
- [Contributing](#-contributing)
- [License](#-license)
- [Show Us Your Love](#-show-us-your-love)


## 💡 Description
Simple-OpenAI is a Java http client library for sending requests to and receiving responses from the [OpenAI API](https://platform.openai.com/docs/api-reference). It exposes a consistent interface across all the services, yet as simple as you can find in other languages like Python or NodeJs. It's an unofficial library.

Simple-OpenAI uses the [CleverClient](https://github.com/sashirestela/cleverclient) library for http communication, [Jackson](https://github.com/FasterXML/jackson) for Json parsing, and [Lombok](https://projectlombok.org/) to minimize boilerplate code, among others libraries.


## ✅ Supported Services
Simple-OpenAI seeks to stay up to date with the most recent changes in OpenAI. Currently, it supports all existing features until [Jun 6th, 2024](https://platform.openai.com/docs/changelog/jun-6th-2024) and will continue to update with future changes.

Full support for all of the OpenAI services:

* Audio (Speech, Transcription, Translation)
* Batch (Batches of Chat Completion)
* Chat Completion (Text Generation, Streaming, Function Calling, Vision)
* Completion (Legacy Text Generation)
* Embedding  (Vectoring Text)
* Files (Upload Files)
* Fine Tuning (Customize Models)
* Image (Generate, Edit, Variation)
* Models (List)
* Moderation (Check Harmful Text)
* Assistants Beta v2 (Assistants, Threads, Messages, Runs, Steps, Vector Stores, Streaming, Function Calling, Vision)

![OpenAI Services](media/openai_services.png)

![OpenAI Beta Services](media/openai_beta_services.png)

NOTES:
1. The methods's responses are `CompletableFuture<ResponseObject>`, which means they are asynchronous, but you can call the join() method to return the result value when complete.
1. Exceptions for the above point are the methods whose names end with the suffix `AndPoll()`. These methods are synchronous and block until a Predicate function that you provide returns false.


## ⚙ Installation
You can install Simple-OpenAI by adding the following dependency to your Maven project:

```xml
<dependency>
    <groupId>io.github.sashirestela</groupId>
    <artifactId>simple-openai</artifactId>
    <version>[latest version]</version>
</dependency>
```

Or alternatively using Gradle:

```groovy
dependencies {
    implementation 'io.github.sashirestela:simple-openai:[latest version]'
}
```


## 📘 Usage

### Creating a SimpleOpenAI Object
This is the first step you need to do before to use the services. You must provide at least your _OpenAI Api Key_ ([See here](https://platform.openai.com/docs/api-reference/authentication) for more details). In the following example we are getting the Api Key from an environment variable called ```OPENAI_API_KEY``` which we have created to keep it:
```java
var openAI = SimpleOpenAI.builder()
    .apiKey(System.getenv("OPENAI_API_KEY"))
    .build();
```
Optionally you could pass your _OpenAI Organization Id_ in case you have multiple organizations and you want to identify usage by organization and/or you could pass your _OpenAI Project Id_ in case you want to provides access to a single project. In the following example we are using environment variable for those Ids:
```java
var openAI = SimpleOpenAI.builder()
    .apiKey(System.getenv("OPENAI_API_KEY"))
    .organizationId(System.getenv("OPENAI_ORGANIZATION_ID"))
    .projectId(System.getenv("OPENAI_PROJECT_ID"))
    .build();
```
Optionally, as well, you could provide a custom Java HttpClient object if you want to have more options for the http connection, such as executors, proxy, timeout, cookies, etc. ([See here](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.Builder.html) for more details). In the following example we are providing a custom HttpClient:
```java
var httpClient = HttpClient.newBuilder()
    .version(Version.HTTP_1_1)
    .followRedirects(Redirect.NORMAL)
    .connectTimeout(Duration.ofSeconds(20))
    .executor(Executors.newFixedThreadPool(3))
    .proxy(ProxySelector.of(new InetSocketAddress("proxy.example.com", 80)))
    .build();

var openAI = SimpleOpenAI.builder()
    .apiKey(System.getenv("OPENAI_API_KEY"))
    .httpClient(httpClient)
    .build();
```

After you have created a SimpleOpenAI object, you are ready to call its services in order to communicate to OpenAI API. Let's see some examples.

### Audio Example
Example to call the Audio service to transform text to audio. We are requesting to receive the audio in binary format (InputStream):
```java
var speechRequest = SpeechRequest.builder()
        .model("tts-1")
        .input("Hello world, welcome to the AI universe!")
        .voice(Voice.ALLOY)
        .responseFormat(SpeechResponseFormat.MP3)
        .speed(1.0)
        .build();
var futureSpeech = openAI.audios().speak(speechRequest);
var speechResponse = futureSpeech.join();
try {
    var audioFile = new FileOutputStream(speechFileName);
    audioFile.write(speechResponse.readAllBytes());
    System.out.println(audioFile.getChannel().size() + " bytes");
    audioFile.close();
} catch (Exception e) {
    e.printStackTrace();
}
```

Example to call the Audio service to transcribe an audio to text. We are requesting to receive the transcription in plain text format (see the name of the method):
```java
var audioRequest = TranscriptionRequest.builder()
        .file(Paths.get("hello_audio.mp3"))
        .model("whisper-1")
        .responseFormat(AudioResponseFormat.VERBOSE_JSON)
        .temperature(0.2)
        .timestampGranularity(TimestampGranularity.WORD)
        .timestampGranularity(TimestampGranularity.SEGMENT)
        .build();
var futureAudio = openAI.audios().transcribe(audioRequest);
var audioResponse = futureAudio.join();
System.out.println(audioResponse);
```
### Image Example
Example to call the Image service to generate two images in response to our prompt. We are requesting to receive the images' urls and we are printing out them in the console:
```java
var imageRequest = ImageRequest.builder()
        .prompt("A cartoon of a hummingbird that is flying around a flower.")
        .n(2)
        .size(Size.X256)
        .responseFormat(ImageResponseFormat.URL)
        .model("dall-e-2")
        .build();
var futureImage = openAI.images().create(imageRequest);
var imageResponse = futureImage.join();
imageResponse.stream().forEach(img -> System.out.println("\n" + img.getUrl()));
```
### Chat Completion Example
Example to call the Chat Completion service to ask a question and wait for a full answer. We are printing out it in the console:
```java
var chatRequest = ChatRequest.builder()
        .model("gpt-3.5-turbo-1106")
        .message(SystemMessage.of("You are an expert in AI."))
        .message(UserMessage.of("Write a technical article about ChatGPT, no more than 100 words."))
        .temperature(0.0)
        .maxTokens(300)
        .build();
var futureChat = openAI.chatCompletions().create(chatRequest);
var chatResponse = futureChat.join();
System.out.println(chatResponse.firstContent());
```
### Chat Completion with Streaming Example
Example to call the Chat Completion service to ask a question and wait for an answer in partial message deltas. We are printing out it in the console as soon as each delta is arriving:
```java
var chatRequest = ChatRequest.builder()
        .model("gpt-3.5-turbo-1106")
        .message(SystemMessage.of("You are an expert in AI."))
        .message(UserMessage.of("Write a technical article about ChatGPT, no more than 100 words."))
        .temperature(0.0)
        .maxTokens(300)
        .build();
var futureChat = openAI.chatCompletions().createStream(chatRequest);
var chatResponse = futureChat.join();
chatResponse.filter(chatResp -> chatResp.getChoices().size() > 0 && chatResp.firstContent() != null)
        .map(Chat::firstContent)
        .forEach(System.out::print);
System.out.println();
```
### Chat Completion with Functions Example
This functionality empowers the Chat Completion service to solve specific problems to our context. In this example we are setting three functions and we are entering a prompt that will require to call one of them (the function ```product```). For setting functions we are using additional classes which implements the interface ```Functional```. Those classes define a field by each function argument, annotating them to describe them and each class must override the ```execute``` method with the function's logic. Note that we are using the ```FunctionExecutor``` utility class to enroll the functions and to execute the function selected by the ```openai.chatCompletions()``` calling:
```java
public void demoCallChatWithFunctions() {
    var functionExecutor = new FunctionExecutor();
    functionExecutor.enrollFunction(
            FunctionDef.builder()
                    .name("get_weather")
                    .description("Get the current weather of a location")
                    .functionalClass(Weather.class)
                    .build());
    functionExecutor.enrollFunction(
            FunctionDef.builder()
                    .name("product")
                    .description("Get the product of two numbers")
                    .functionalClass(Product.class)
                    .build());
    functionExecutor.enrollFunction(
            FunctionDef.builder()
                    .name("run_alarm")
                    .description("Run an alarm")
                    .functionalClass(RunAlarm.class)
                    .build());
    var messages = new ArrayList<ChatMessage>();
    messages.add(UserMessage.of("What is the product of 123 and 456?"));
    chatRequest = ChatRequest.builder()
            .model(modelIdToUse)
            .messages(messages)
            .tools(functionExecutor.getToolFunctions())
            .build();
    var futureChat = openAI.chatCompletions().create(chatRequest);
    var chatResponse = futureChat.join();
    var chatMessage = chatResponse.firstMessage();
    var chatToolCall = chatMessage.getToolCalls().get(0);
    var result = functionExecutor.execute(chatToolCall.getFunction());
    messages.add(chatMessage);
    messages.add(ToolMessage.of(result.toString(), chatToolCall.getId()));
    chatRequest = ChatRequest.builder()
            .model(modelIdToUse)
            .messages(messages)
            .tools(functionExecutor.getToolFunctions())
            .build();
    futureChat = openAI.chatCompletions().create(chatRequest);
    chatResponse = futureChat.join();
    System.out.println(chatResponse.firstContent());
}

public static class Weather implements Functional {

    @JsonPropertyDescription("City and state, for example: León, Guanajuato")
    public String location;

    @JsonPropertyDescription("The temperature unit, can be 'celsius' or 'fahrenheit'")
    @JsonProperty(required = true)
    public String unit;

    @Override
    public Object execute() {
        return Math.random() * 45;
    }

}

public static class Product implements Functional {

    @JsonPropertyDescription("The multiplicand part of a product")
    @JsonProperty(required = true)
    public double multiplicand;

    @JsonPropertyDescription("The multiplier part of a product")
    @JsonProperty(required = true)
    public double multiplier;

    @Override
    public Object execute() {
        return multiplicand * multiplier;
    }

}

public static class RunAlarm implements Functional {

    @Override
    public Object execute() {
        return "DONE";
    }

}
```
### Chat Completion with Vision Example
Example to call the Chat Completion service to allow the model to take in external images and answer questions about them:
```java
var chatRequest = ChatRequest.builder()
        .model("gpt-4-vision-preview")
        .messages(List.of(
                UserMessage.of(List.of(
                        ContentPartText.of(
                                "What do you see in the image? Give in details in no more than 100 words."),
                        ContentPartImageUrl.of(ImageUrl.of(
                                "https://upload.wikimedia.org/wikipedia/commons/e/eb/Machu_Picchu%2C_Peru.jpg"))))))
        .temperature(0.0)
        .maxTokens(500)
        .build();
var chatResponse = openAI.chatCompletions().createStream(chatRequest).join();
chatResponse.filter(chatResp -> chatResp.getChoices().size() > 0 && chatResp.firstContent() != null)
        .map(Chat::firstContent)
        .forEach(System.out::print);
System.out.println();
```
Example to call the Chat Completion service to allow the model to take in local images and answer questions about them:
```java
var chatRequest = ChatRequest.builder()
        .model("gpt-4-vision-preview")
        .messages(List.of(
                UserMessage.of(List.of(
                        ContentPartText.of(
                                "What do you see in the image? Give in details in no more than 100 words."),
                        ContentPartImageUrl.of(loadImageAsBase64("src/demo/resources/machupicchu.jpg"))))))
        .temperature(0.0)
        .maxTokens(500)
        .build();
var chatResponse = openAI.chatCompletions().createStream(chatRequest).join();
chatResponse.filter(chatResp -> chatResp.getChoices().size() > 0 && chatResp.firstContent() != null)
        .map(Chat::firstContent)
        .forEach(System.out::print);
System.out.println();

private static ImageUrl loadImageAsBase64(String imagePath) {
    try {
        Path path = Paths.get(imagePath);
        byte[] imageBytes = Files.readAllBytes(path);
        String base64String = Base64.getEncoder().encodeToString(imageBytes);
        var extension = imagePath.substring(imagePath.lastIndexOf('.') + 1);
        var prefix = "data:image/" + extension + ";base64,";
        return ImageUrl.of(prefix + base64String);
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
```
### Chat Completion Conversation Example
This example simulates a conversation chat by the command console and demonstrates the usage of ChatCompletion with streaming and call functions.

You can see the full demo code as well as the results from running the demo code:

<details>

<summary><b>Demo Code</b></summary>

```java
package io.github.sashirestela.openai.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import io.github.sashirestela.openai.common.function.Functional;
import io.github.sashirestela.openai.common.tool.ToolCall;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.Chat.Choice;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.AssistantMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.ResponseMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.ToolMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConversationDemo {

    private SimpleOpenAI openAI;
    private FunctionExecutor functionExecutor;

    private int indexTool;
    private StringBuilder content;
    private StringBuilder functionArgs;

    public ConversationDemo() {
        openAI = SimpleOpenAI.builder().apiKey(System.getenv("OPENAI_API_KEY")).build();
    }

    public void prepareConversation() {
        List<FunctionDef> functionList = new ArrayList<>();
        functionList.add(FunctionDef.builder()
                .name("getCurrentTemperature")
                .description("Get the current temperature for a specific location")
                .functionalClass(CurrentTemperature.class)
                .build());
        functionList.add(FunctionDef.builder()
                .name("getRainProbability")
                .description("Get the probability of rain for a specific location")
                .functionalClass(RainProbability.class)
                .build());
        functionExecutor = new FunctionExecutor(functionList);
    }

    public void runConversation() {
        List<ChatMessage> messages = new ArrayList<>();
        var myMessage = System.console().readLine("\nWelcome! Write any message: ");
        messages.add(UserMessage.of(myMessage));
        while (!myMessage.toLowerCase().equals("exit")) {
            var chatStream = openAI.chatCompletions()
                    .createStream(ChatRequest.builder()
                            .model("gpt-4o")
                            .messages(messages)
                            .tools(functionExecutor.getToolFunctions())
                            .temperature(0.2)
                            .stream(true)
                            .build())
                    .join();

            indexTool = -1;
            content = new StringBuilder();
            functionArgs = new StringBuilder();

            var response = getResponse(chatStream);

            if (response.getMessage().getContent() != null) {
                messages.add(AssistantMessage.of(response.getMessage().getContent()));
            }
            if (response.getFinishReason().equals("tool_calls")) {
                messages.add(response.getMessage());
                var toolCalls = response.getMessage().getToolCalls();
                var toolMessages = functionExecutor.executeAll(toolCalls,
                        (toolCallId, result) -> ToolMessage.of(result, toolCallId));
                messages.addAll(toolMessages);
            } else {
                myMessage = System.console().readLine("\n\nWrite any message (or write 'exit' to finish): ");
                messages.add(UserMessage.of(myMessage));
            }
        }
    }

    private Choice getResponse(Stream<Chat> chatStream) {
        var choice = new Choice();
        choice.setIndex(0);
        var chatMsgResponse = new ResponseMessage();
        List<ToolCall> toolCalls = new ArrayList<>();

        chatStream.forEach(responseChunk -> {
            var choices = responseChunk.getChoices();
            if (choices.size() > 0) {
                var innerChoice = choices.get(0);
                var delta = innerChoice.getMessage();
                if (delta.getRole() != null) {
                    chatMsgResponse.setRole(delta.getRole());
                } else if (delta.getContent() != null && !delta.getContent().isEmpty()) {
                    content.append(delta.getContent());
                    System.out.print(delta.getContent());
                } else if (delta.getToolCalls() != null) {
                    var toolCall = delta.getToolCalls().get(0);
                    if (toolCall.getIndex() != indexTool) {
                        if (toolCalls.size() > 0) {
                            toolCalls.get(toolCalls.size() - 1).getFunction().setArguments(functionArgs.toString());
                            functionArgs = new StringBuilder();
                        }
                        toolCalls.add(toolCall);
                        indexTool++;
                    } else {
                        functionArgs.append(toolCall.getFunction().getArguments());
                    }
                } else {
                    if (content.length() > 0) {
                        chatMsgResponse.setContent(content.toString());
                    }
                    if (toolCalls.size() > 0) {
                        toolCalls.get(toolCalls.size() - 1).getFunction().setArguments(functionArgs.toString());
                        chatMsgResponse.setToolCalls(toolCalls);
                    }
                    choice.setMessage(chatMsgResponse);
                    choice.setFinishReason(innerChoice.getFinishReason());
                }
            }
        });
        return choice;
    }

    public static void main(String[] args) {
        var demo = new ConversationDemo();
        demo.prepareConversation();
        demo.runConversation();
    }

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
            return shortUnit.equals("C") ? centigrades : (shortUnit.equals("F") ? fahrenheit : 0.0);
        }

    }

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
```
</details>

<details>

<summary><b>Demo Results</b></summary>

```txt
Welcome! Write any message: Hi, can you help me with some quetions about Lima, Peru?
Of course! What would you like to know about Lima, Peru?

Write any message (or write 'exit' to finish): Tell me something brief about Lima Peru, then tell me how's the weather there right now. Finally give me three tips to travel there.
### Brief About Lima, Peru
Lima, the capital city of Peru, is a bustling metropolis that blends modernity with rich historical heritage. Founded by Spanish conquistador Francisco Pizarro in 1535, Lima is known for its colonial architecture, vibrant culture, and delicious cuisine, particularly its world-renowned ceviche. The city is also a gateway to exploring Peru's diverse landscapes, from the coastal deserts to the Andean highlands and the Amazon rainforest.

### Current Weather in Lima, Peru
I'll check the current temperature and the probability of rain in Lima for you.### Current Weather in Lima, Peru
- **Temperature:** Approximately 11.8°C
- **Probability of Rain:** Approximately 97.8%

### Three Tips for Traveling to Lima, Peru
1. **Explore the Historic Center:**
   - Visit the Plaza Mayor, the Government Palace, and the Cathedral of Lima. These landmarks offer a glimpse into Lima's colonial past and are UNESCO World Heritage Sites.

2. **Savor the Local Cuisine:**
   - Don't miss out on trying ceviche, a traditional Peruvian dish made from fresh raw fish marinated in citrus juices. Also, explore the local markets and try other Peruvian delicacies.

3. **Visit the Coastal Districts:**
   - Head to Miraflores and Barranco for stunning ocean views, vibrant nightlife, and cultural experiences. These districts are known for their beautiful parks, cliffs, and bohemian atmosphere.

Enjoy your trip to Lima! If you have any more questions, feel free to ask.

Write any message (or write 'exit' to finish): exit
```
</details>

### Assistant v2 Conversation Example
This example simulates a conversation chat by the command console and demonstrates the usage of the latest Assistants API v2 features:
- _Vector Stores_ to upload files and incorporate it as new knowledge base.
- _Function Tools_ to use internal bussiness services to answer questions.
- _File Search Tools_ to use vectorized files to do semantic search.
- _Thread Run Streaming_ to answer with chunks of tokens in real time.

You can see the full demo code as well as the results from running the demo code:

<details>

<summary><b>Demo Code</b></summary>

```java
package io.github.sashirestela.openai.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.cleverclient.Event;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartTextAnnotation;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import io.github.sashirestela.openai.common.function.Functional;
import io.github.sashirestela.openai.domain.assistant.AssistantRequest;
import io.github.sashirestela.openai.domain.assistant.AssistantTool;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageDelta;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRole;
import io.github.sashirestela.openai.domain.assistant.ThreadRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRun;
import io.github.sashirestela.openai.domain.assistant.ThreadRun.RunStatus;
import io.github.sashirestela.openai.domain.assistant.ThreadRunRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRunSubmitOutputRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRunSubmitOutputRequest.ToolOutput;
import io.github.sashirestela.openai.domain.assistant.ToolResourceFull;
import io.github.sashirestela.openai.domain.assistant.ToolResourceFull.FileSearch;
import io.github.sashirestela.openai.domain.assistant.VectorStoreRequest;
import io.github.sashirestela.openai.domain.assistant.events.EventName;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConversationV2Demo {

    private SimpleOpenAI openAI;
    private String fileId;
    private String vectorStoreId;
    private FunctionExecutor functionExecutor;
    private String assistantId;
    private String threadId;

    public ConversationV2Demo() {
        openAI = SimpleOpenAI.builder().apiKey(System.getenv("OPENAI_API_KEY")).build();
    }

    public void prepareConversation() {
        List<FunctionDef> functionList = new ArrayList<>();
        functionList.add(FunctionDef.builder()
                .name("getCurrentTemperature")
                .description("Get the current temperature for a specific location")
                .functionalClass(CurrentTemperature.class)
                .build());
        functionList.add(FunctionDef.builder()
                .name("getRainProbability")
                .description("Get the probability of rain for a specific location")
                .functionalClass(RainProbability.class)
                .build());
        functionExecutor = new FunctionExecutor(functionList);

        var file = openAI.files()
                .create(FileRequest.builder()
                        .file(Paths.get("src/demo/resources/mistral-ai.txt"))
                        .purpose(PurposeType.ASSISTANTS)
                        .build())
                .join();
        fileId = file.getId();
        System.out.println("File was created with id: " + fileId);

        var vectorStore = openAI.vectorStores()
                .createAndPoll(VectorStoreRequest.builder()
                        .fileId(fileId)
                        .build());
        vectorStoreId = vectorStore.getId();
        System.out.println("Vector Store was created with id: " + vectorStoreId);

        var assistant = openAI.assistants()
                .create(AssistantRequest.builder()
                        .name("World Assistant")
                        .model("gpt-4o")
                        .instructions("You are a skilled tutor on geo-politic topics.")
                        .tools(functionExecutor.getToolFunctions())
                        .tool(AssistantTool.fileSearch())
                        .toolResources(ToolResourceFull.builder()
                                .fileSearch(FileSearch.builder()
                                        .vectorStoreId(vectorStoreId)
                                        .build())
                                .build())
                        .temperature(0.2)
                        .build())
                .join();
        assistantId = assistant.getId();
        System.out.println("Assistant was created with id: " + assistantId);

        var thread = openAI.threads().create(ThreadRequest.builder().build()).join();
        threadId = thread.getId();
        System.out.println("Thread was created with id: " + threadId);
        System.out.println();
    }

    public void runConversation() {
        var myMessage = System.console().readLine("\nWelcome! Write any message: ");
        while (!myMessage.toLowerCase().equals("exit")) {
            openAI.threadMessages()
                    .create(threadId, ThreadMessageRequest.builder()
                            .role(ThreadMessageRole.USER)
                            .content(myMessage)
                            .build())
                    .join();
            var runStream = openAI.threadRuns()
                    .createStream(threadId, ThreadRunRequest.builder()
                            .assistantId(assistantId)
                            .build())
                    .join();
            handleRunEvents(runStream);
            myMessage = System.console().readLine("\nWrite any message (or write 'exit' to finish): ");
        }
    }

    private void handleRunEvents(Stream<Event> runStream) {
        runStream.forEach(event -> {
            switch (event.getName()) {
                case EventName.THREAD_RUN_CREATED:
                case EventName.THREAD_RUN_COMPLETED:
                case EventName.THREAD_RUN_REQUIRES_ACTION:
                    var run = (ThreadRun) event.getData();
                    System.out.println("=====>> Thread Run: id=" + run.getId() + ", status=" + run.getStatus());
                    if (run.getStatus().equals(RunStatus.REQUIRES_ACTION)) {
                        var toolCalls = run.getRequiredAction().getSubmitToolOutputs().getToolCalls();
                        var toolOutputs = functionExecutor.executeAll(toolCalls,
                                (toolCallId, result) -> ToolOutput.builder()
                                        .toolCallId(toolCallId)
                                        .output(result)
                                        .build());
                        var runSubmitToolStream = openAI.threadRuns()
                                .submitToolOutputStream(threadId, run.getId(), ThreadRunSubmitOutputRequest.builder()
                                        .toolOutputs(toolOutputs)
                                        .stream(true)
                                        .build())
                                .join();
                        handleRunEvents(runSubmitToolStream);
                    }
                    break;
                case EventName.THREAD_MESSAGE_DELTA:
                    var msgDelta = (ThreadMessageDelta) event.getData();
                    var content = msgDelta.getDelta().getContent().get(0);
                    if (content instanceof ContentPartTextAnnotation) {
                        var textContent = (ContentPartTextAnnotation) content;
                        System.out.print(textContent.getText().getValue());
                    }
                    break;
                case EventName.THREAD_MESSAGE_COMPLETED:
                    System.out.println();
                    break;
                default:
                    break;
            }
        });
    }

    public void cleanConversation() {
        var deletedFile = openAI.files().delete(fileId).join();
        var deletedVectorStore = openAI.vectorStores().delete(vectorStoreId).join();
        var deletedAssistant = openAI.assistants().delete(assistantId).join();
        var deletedThread = openAI.threads().delete(threadId).join();

        System.out.println("File was deleted: " + deletedFile.getDeleted());
        System.out.println("Vector Store was deleted: " + deletedVectorStore.getDeleted());
        System.out.println("Assistant was deleted: " + deletedAssistant.getDeleted());
        System.out.println("Thread was deleted: " + deletedThread.getDeleted());
    }

    public static void main(String[] args) {
        var demo = new ConversationV2Demo();
        demo.prepareConversation();
        demo.runConversation();
        demo.cleanConversation();
    }

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
            return shortUnit.equals("C") ? centigrades : (shortUnit.equals("F") ? fahrenheit : 0.0);
        }

    }

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
```
</details>

<details>

<summary><b>Demo Results</b></summary>

```txt
File was created with id: file-oDFIF7o4SwuhpwBNnFIILhMK
Vector Store was created with id: vs_lG1oJmF2s5wLhqHUSeJpELMr
Assistant was created with id: asst_TYS5cZ05697tyn3yuhDrCCIv
Thread was created with id: thread_33n258gFVhZVIp88sQKuqMvg


Welcome! Write any message: Hello
=====>> Thread Run: id=run_nihN6dY0uyudsORg4xyUvQ5l, status=QUEUED
Hello! How can I assist you today?
=====>> Thread Run: id=run_nihN6dY0uyudsORg4xyUvQ5l, status=COMPLETED

Write any message (or write 'exit' to finish): Tell me something brief about Lima Peru, then tell me how's the weather there right now. Finally give me three tips to travel there.
=====>> Thread Run: id=run_QheimPyP5UK6FtmH5obon0fB, status=QUEUED
Lima, the capital city of Peru, is located on the country's arid Pacific coast. It's known for its vibrant culinary scene, rich history, and as a cultural hub with numerous museums, colonial architecture, and remnants of pre-Columbian civilizations. This bustling metropolis serves as a key gateway to visiting Peru’s more famous attractions, such as Machu Picchu and the Amazon rainforest.

Let me find the current weather conditions in Lima for you, and then I'll provide three travel tips.
=====>> Thread Run: id=run_QheimPyP5UK6FtmH5obon0fB, status=REQUIRES_ACTION
### Current Weather in Lima, Peru:
- **Temperature:** 12.8°C
- **Rain Probability:** 82.7%

### Three Travel Tips for Lima, Peru:
1. **Best Time to Visit:** Plan your trip during the dry season, from May to September, which offers clearer skies and milder temperatures. This period is particularly suitable for outdoor activities and exploring the city comfortably.

2. **Local Cuisine:** Don't miss out on tasting the local Peruvian dishes, particularly the ceviche, which is renowned worldwide. Lima is also known as the gastronomic capital of South America, so indulge in the wide variety of dishes available.

3. **Cultural Attractions:** Allocate enough time to visit Lima's rich array of museums, such as the Larco Museum, which showcases pre-Columbian art, and the historical center which is a UNESCO World Heritage Site. Moreover, exploring districts like Miraflores and Barranco can provide insights into the modern and bohemian sides of the city.

Enjoy planning your trip to Lima! If you need more information or help, feel free to ask.
=====>> Thread Run: id=run_QheimPyP5UK6FtmH5obon0fB, status=COMPLETED

Write any message (or write 'exit' to finish): Tell me something about the Mistral company
=====>> Thread Run: id=run_5u0t8kDQy87p5ouaTRXsCG8m, status=QUEUED
Mistral AI is a French company that specializes in selling artificial intelligence products. It was established in April 2023 by former employees of Meta Platforms and Google DeepMind. Notably, the company secured a significant amount of funding, raising €385 million in October 2023, and achieved a valuation exceeding $2 billion by December of the same year.

The prime focus of Mistral AI is on developing and producing open-source large language models. This approach underscores the foundational role of open-source software as a counter to proprietary models. As of March 2024, Mistral AI has published two models, which are available in terms of weights, while three more models—categorized as Small, Medium, and Large—are accessible only through an API[1].
=====>> Thread Run: id=run_5u0t8kDQy87p5ouaTRXsCG8m, status=COMPLETED

Write any message (or write 'exit' to finish): exit

File was deleted: true
Vector Store was deleted: true
Assistant was deleted: true
Thread was deleted: true
```
</details>


## ✴ Support for Additional OpenAI Providers
Simple-OpenAI can be used with additional providers that are compatible with the OpenAI API. At this moment, there is support for the following additional providers:

### Azure OpenAI
[Azure OpenIA](https://learn.microsoft.com/en-us/azure/ai-services/openai/reference) is supported by Simple-OpenAI. We can use the class `SimpleOpenAIAzure`, which extends the class `BaseSimpleOpenAI`, to start using this provider. 
```java
var openai = SimpleOpenAIAzure.builder()
    .apiKey(System.getenv("AZURE_OPENAI_API_KEY"))
    .baseUrl(System.getenv("AZURE_OPENAI_BASE_URL"))   // Including resourceName and deploymentId
    .apiVersion(System.getenv("AZURE_OPENAI_API_VERSION"))
    //.httpClient(customHttpClient)   Optionally you could pass a custom HttpClient
    .build();
```
Currently we are supporting the following services only:
- `chatCompletionService` (streaming mode is not supported)
- `fileService`

### Anyscale
[Anyscale](https://www.anyscale.com/endpoints) is suported by Simple-OpenAI. We can use the class `SimpleOpenAIAnyscale`, which extends the class `BaseSimpleOpenAI`, to start using this provider.
```java
var openai = SimpleOpenAIAnyscale.builder()
    .apiKey(System.getenv("ANYSCALE_API_KEY"))
    //.baseUrl(customUrl)             Optionally you could pass a custom baseUrl
    //.httpClient(customHttpClient)   Optionally you could pass a custom HttpClient
    .build();
```
Currently we are supporting the `chatCompletionService` service only. It was tested with the _Mistral_ model.


## ✳ Run Examples
Examples for each OpenAI service have been created in the folder [demo](https://github.com/sashirestela/simple-openai/tree/main/src/demo/java/io/github/sashirestela/openai/demo) and you can follow the next steps to execute them:
* Clone this repository:
  ```
  git clone https://github.com/sashirestela/simple-openai.git
  cd simple-openai
  ```
* Build the project:
  ```
  mvn clean install
  ```
* Create an environment variable for your OpenAI Api Key:
  ```
  export OPENAI_API_KEY=<here goes your api key>
  ```
* Grant execution permission to the script file:
  ```
  chmod +x rundemo.sh
  ```
* Run examples:
  ```
  ./rundemo.sh <demo> [debug]
  ```
  Where:

  * ```<demo>``` Is mandatory and must be one of the values:
    * Audio
    * Batch
    * Chat
    * Completion
    * Embedding
    * File
    * Finetuning
    * Image
    * Model
    * Moderation
    * Conversation
    * AssistantV2
    * ThreadV2
    * ThreadMessageV2
    * ThreadRunV2
    * ThreadRunStepV2
    * VectorStoreV2
    * VectorStoreFileV2
    * VectorStoreFileBatchV2
    * ConversationV2
    * ChatAnyscale
    * ChatAzure
  
  * ```[debug]``` Is optional and creates the ```demo.log``` file where you can see log details for each execution.
  * For example, to run the chat demo with a log file: ```./rundemo.sh Chat debug```

* Indications for Azure OpenAI demos

    Azure OpenAI requires a separate deployment for each model. The Azure OpenAI demos require 
    two models.   

    1. gpt-4-turbo (or similar) that supports:
        - /chat/completions (including tool calls)
        - /files
    
    3. gpt-4-vision-preview that supports:
        - /chat/completions (including images). 
    
    See the Azure OpenAI docs for more details: [Azure OpenAI documentation](https://learn.microsoft.com/en-us/azure/ai-services/openai/).
    Once you have the deployment URLs and the API keys, set the following environment variables:
    ```
    export AZURE_OPENAI_BASE_URL=<your gpt-4-turbo deployment endpoint URL>
    export AZURE_OPENAI_API_KEY=<here goes your regional API key>
    export AZURE_OPENAI_BASE_URL_VISION=<your gpt-4 vision preview deployment endpoint URL>
    export AZURE_OPENAI_API_KEY_VISION=<here goes your regional API key>
    export AZURE_OPENAI_API_VERSION=2024-02-15-preview
    ```
    Note that some models may not be available in all regions. If you have trouble finding a model, 
    try a different region. The API keys are regional (per cognitive account). If you provision 
    multiple models in the same region they will share the same API key (actually there are two keys
    per region to support alternate key rotation).


## 💼 Contributing
Kindly read our [Contributing guide](CONTRIBUTING.md) to learn and understand how to contribute to this project.

## 📄 License
Simple-OpenAI is licensed under the MIT License. See the
[LICENSE](https://github.com/sashirestela/simple-openai/blob/main/LICENSE) file
for more information.


## ❤ Show Us Your Love
Thanks for using **simple-openai**. If you find this project valuable there are a few ways you can show us your love, preferably all of them 🙂:

* Letting your friends know about this project 🗣📢.
* Writing a brief review on your social networks ✍🌐.
* Giving us a star on Github ⭐.
