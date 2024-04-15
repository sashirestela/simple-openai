# 📌 Simple-OpenAI
A Java library to use the OpenAI Api in the simplest possible way.

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sashirestela_simple-openai&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=sashirestela_simple-openai)
[![codecov](https://codecov.io/gh/sashirestela/simple-openai/graph/badge.svg?token=TYLE5788R3)](https://codecov.io/gh/sashirestela/simple-openai)
![Maven Central](https://img.shields.io/maven-central/v/io.github.sashirestela/simple-openai)
![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/sashirestela/simple-openai/build_java_maven.yml)


### Table of Contents
- [Description](#-description)
- [Support for Additional OpenAI Providers](#-support-for-additional-openai-providers)
  - [Azure OpenAI](#azure-openai)
  - [Anyscale](#anyscale)
- [Supported Services](#-supported-services)
- [Installation](#-installation)
- [Usage](#-usage)
  - [Creating a SimpleOpenAI Object](#creating-a-simpleopenai-object)
  - [Calling some SimpleOpenAI Services](#calling-some-simpleopenai-services)
    - [Audio Service](#audio-service)
    - [Image Service](#image-service)
    - [Chat Completion Service (blocking mode)](#chat-completion-service-blocking-mode)
    - [Chat Completion Service (streaming mode)](#chat-completion-service-streaming-mode)
    - [Chat Completion Service with Functions](#chat-completion-service-with-functions)
    - [Chat Completion Service with Vision](#chat-completion-service-with-vision)
- [Run Examples](#-run-examples)
- [Contributing](#-contributing)
- [License](#-license)


## 💡 Description
Simple-OpenAI is a Java http client library for sending requests to and receiving responses from the [OpenAI API](https://platform.openai.com/docs/api-reference). It exposes a consistent interface across all the services, yet as simple as you can find in other languages like Python or NodeJs. It's a _community-maintained_ library.

Simple-OpenAI uses the [CleverClient](https://github.com/sashirestela/cleverclient) library for http communication, [Jackson](https://github.com/FasterXML/jackson) for Json parsing, and [Lombok](https://projectlombok.org/) to minimize boilerplate code, among others libraries.


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
Currently we are supporting the `openai.chatCompletionService()` service only.

### Anyscale
[Anyscale](https://www.anyscale.com/endpoints) is suported by Simple-OpenAI. We can use the class `SimpleOpenAIAnyscale`, which extends the class `BaseSimpleOpenAI`, to start using this provider.
```java
var openai = SimpleOpenAIAnyscale.builder()
    .apiKey(System.getenv("ANYSCALE_API_KEY"))
    //.baseUrl(customUrl)             Optionally you could pass a custom baseUrl
    //.httpClient(customHttpClient)   Optionally you could pass a custom HttpClient
    .build();
```
Currently we are supporting the `openai.chatCompletionService()` service only. It was tested with the model _Mistral_.


## ✅ Supported Services
Full support for all of the OpenAI services:

* Text to speech (as part of Audio)
* Speech to text (as part of Audio)
* Text generation (as part of Chat)
* Function calling (as part of Chat)
* Image to text (as part of Chat)
* Text to image (as part of Image)
* Embeddings
* Fine tuning
* Assistants API (Beta)
* Assistant Stream Events (Beta) 📣

![OpenAI Services](media/openai_services.png)

![OpenAI Beta Services](media/openai_beta_services.png)

NOTE: All the responses are ```CompletableFuture<ResponseObject>```, which means they are asynchronous, but you can call the join() method to return the result value when complete.


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
var openai = SimpleOpenAI.builder()
    .apiKey(System.getenv("OPENAI_API_KEY"))
    .build();
```
Optionally you could pass your _OpenAI Organization Id_ ([See here](https://platform.openai.com/account/org-settings) for more details) in case you have multiple organizations and you want to identify usage by orgazanization. In the the following example we are getting the Organization Id from an environment variable called ```OPENAI_ORGANIZATION_ID``` which we have created to keep it:
```java
var openai = SimpleOpenAI.builder()
    .apiKey(System.getenv("OPENAI_API_KEY"))
    .organizationId(System.getenv("OPENAI_ORGANIZATION_ID"))
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

var openai = SimpleOpenAI.builder()
    .apiKey(System.getenv("OPENAI_API_KEY"))
    .httpClient(httpClient)
    .build();
```

### Calling some SimpleOpenAI Services
After you have created a SimpleOpenAI object, you are ready to call its services in order to communicate to OpenAI API. Let's see some examples.

#### Audio Service
Example to call the Audio service to transform text to audio. We are requesting to receive the audio in binary format (InputStream):
```java
var speechRequest = AudioSpeechRequest.builder()
        .model("tts-1")
        .input("Hello world, welcome to the AI universe!")
        .voice(Voice.ALLOY)
        .responseFormat(SpeechRespFmt.MP3)
        .speed(1.0)
        .build();
var futureSpeech = openAI.audios().speak(speechRequest);
var speechResponse = futureSpeech.join();
try {
    var audioFile = new FileOutputStream("response.mp3");
    audioFile.write(speechResponse.readAllBytes());
    System.out.println(audioFile.getChannel().size() + " bytes");
    audioFile.close();
} catch (Exception e) {
    e.printStackTrace();
}
```

Example to call the Audio service to transcribe an audio to text. We are requesting to receive the transcription in plain text format (see the name of the method):
```java
var audioRequest = AudioTranscribeRequest.builder()
    .file(Paths.get("hello_audio.mp3"))
    .model("whisper-1")
    .build();
var futureAudio = openai.audios().transcribePlain(audioRequest);
var audioResponse = futureAudio.join();
System.out.println(audioResponse);
```
#### Image Service
Example to call the Image service to generate two images in response to our prompt. We are requesting to receive the images' urls and we are printing out them in the console:
```java
var imageRequest = ImageRequest.builder()
    .prompt("A cartoon of a hummingbird that is flying around a flower.")
    .n(2)
    .size(Size.X256)
    .responseFormat(ImageRespFmt.URL)
    .model("dall-e-2")
    .build();
var futureImage = openai.images().create(imageRequest);
var imageResponse = futureImage.join();
imageResponse.stream().forEach(img -> System.out.println("\n" + img.getUrl()));
```
#### Chat Completion Service (blocking mode)
Example to call the Chat Completion service to ask a question and wait for a full answer. We are printing out it in the console:
```java
var chatRequest = ChatRequest.builder()
    .model("gpt-3.5-turbo-1106")
    .messages(List.of(
        new ChatMsgSystem("You are an expert in AI."),
        new ChatMsgUser("Write a technical article about ChatGPT, no more than 100 words.")))
    .temperature(0.0)
    .maxTokens(300)
    .build();
var futureChat = openai.chatCompletions().create(chatRequest);
var chatResponse = futureChat.join();
System.out.println(chatResponse.firstContent());
```
#### Chat Completion Service (streaming mode)
Example to call the Chat Completion service to ask a question and wait for an answer in partial message deltas. We are printing out it in the console as soon as each delta is arriving:
```java
var chatRequest = ChatRequest.builder()
    .model("gpt-3.5-turbo-1106")
    .messages(List.of(
        new ChatMsgSystem("You are an expert in AI."),
        new ChatMsgUser("Write a technical article about ChatGPT, no more than 100 words.")))
    .temperature(0.0)
    .maxTokens(300)
    .build();
var futureChat = openai.chatCompletions().createStream(chatRequest);
var chatResponse = futureChat.join();
chatResponse.filter(chatResp -> chatResp.firstContent() != null)
    .map(ChatResponse::firstContent)
    .forEach(System.out::print);
System.out.println();
```
#### Chat Completion Service with Functions
This functionality empowers the Chat Completion service to solve specific problems to our context. In this example we are setting three functions and we are entering a prompt that will require to call one of them (the function ```product```). For setting functions we are using additional classes which implements the interface ```Functional```. Those classes define a field by each function argument, annotating them to describe them and each class must override the ```execute``` method with the function's logic. Note that we are using the ```FunctionExecutor``` utility class to enroll the functions and to execute the function selected by the ```openai.chatCompletions()``` calling:
```java
public void demoCallChatWithFunctions() {
    var functionExecutor = new FunctionExecutor();
    functionExecutor.enrollFunction(
        ChatFunction.builder()
            .name("get_weather")
            .description("Get the current weather of a location")
            .functionalClass(Weather.class)
            .build());
    functionExecutor.enrollFunction(
        ChatFunction.builder()
            .name("product")
            .description("Get the product of two numbers")
            .functionalClass(Product.class)
            .build());
    functionExecutor.enrollFunction(
        ChatFunction.builder()
            .name("run_alarm")
            .description("Run an alarm")
            .functionalClass(RunAlarm.class)
            .build());
    var messages = new ArrayList<ChatMsg>();
    messages.add(new ChatMsgUser("What is the product of 123 and 456?"));
    chatRequest = ChatRequest.builder()
            .model("gpt-3.5-turbo-1106")
            .messages(messages)
            .tools(functionExecutor.getToolFunctions())
            .build();
    var futureChat = openAI.chatCompletions().create(chatRequest);
    var chatResponse = futureChat.join();
    var chatMessage = chatResponse.firstMessage();
    var chatToolCall = chatMessage.getToolCalls().get(0);
    var result = functionExecutor.execute(chatToolCall.getFunction());
    messages.add(chatMessage);
    messages.add(new ChatMsgTool(result.toString(), chatToolCall.getId()));
    chatRequest = ChatRequest.builder()
            .model("gpt-3.5-turbo-1106")
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
#### Chat Completion Service with Vision
Example to call the Chat Completion service to allow the model to take in external images and answer questions about them:
```java
var chatRequest = ChatRequest.builder()
    .model("gpt-4-vision-preview")
    .messages(List.of(
        new ChatMsgUser(List.of(
            new ContentPartText(
                "What do you see in the image? Give in details in no more than 100 words."),
            new ContentPartImage(new ImageUrl(
                "https://upload.wikimedia.org/wikipedia/commons/e/eb/Machu_Picchu%2C_Peru.jpg"))))))
    .temperature(0.0)
    .maxTokens(500)
    .build();
var chatResponse = openai.chatCompletions().createStream(chatRequest).join();
chatResponse.filter(chatResp -> chatResp.firstContent() != null)
    .map(chatResp -> chatResp.firstContent())
    .forEach(System.out::print);
System.out.println();
```
Example to call the Chat Completion service to allow the model to take in local images and answer questions about them:
```java
var chatRequest = ChatRequest.builder()
    .model("gpt-4-vision-preview")
    .messages(List.of(
        new ChatMsgUser(List.of(
            new ContentPartText(
                "What do you see in the image? Give in details in no more than 100 words."),
            new ContentPartImage(loadImageAsBase64("src/demo/resources/machupicchu.jpg"))))))
    .temperature(0.0)
    .maxTokens(500)
    .build();
var chatResponse = openai.chatCompletions().createStream(chatRequest).join();
chatResponse.filter(chatResp -> chatResp.firstContent() != null)
    .map(chatResp -> chatResp.firstContent())
    .forEach(System.out::print);
System.out.println();

private static ImageUrl loadImageAsBase64(String imagePath) {
    try {
        Path path = Paths.get(imagePath);
        byte[] imageBytes = Files.readAllBytes(path);
        String base64String = Base64.getEncoder().encodeToString(imageBytes);
        var extension = imagePath.substring(imagePath.lastIndexOf(".") + 1);
        var prefix = "data:image/" + extension + ";base64,";
        return new ImageUrl(prefix + base64String);
    } catch (Exception e) {
        return null;
    }
}
```

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
* Create environment variables for the Azure OpenAI demos

  Azure OpenAI requires a separate deployment for each model. The Azure OpenAI demos require 
  two models.   

  1. gpt-4-turbo (or similar) that supports:
     - /chat/completions (including tool calls)
     - /files
     - /assistants (beta)
     - /threads (beta)
  
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

  At the moment the simple-openai support for Azure OpenAI includes the following OpenAI endpoints:
  - /chat/completions (including tool calls)
  - /chat/completions (including images)
  - /files
  - /assistants (beta)
  - /threads (beta)
 
   In addition, streaming mode is not supported at the moment.

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
    * audio
    * chat
    * chatAnyscale
    * chatAzure
    * completion
    * embedding
    * file
    * finetuning
    * image
    * model
    * moderation
    * assistant
    * assistantAzure
  
  * ```[debug]``` Is optional and creates the ```demo.log``` file where you can see log details for each execution.
  * For example, to run the chat demo with a log file: ```./rundemo.sh chat debug```

## 💼 Contributing
Kindly read our [Contributing guide](CONTRIBUTING.md) to learn and understand how to contrinute to this project.

## 📄 License
Simple-OpenAI is licensed under the MIT License. See the
[LICENSE](https://github.com/sashirestela/simple-openai/blob/main/LICENSE) file
for more information.
