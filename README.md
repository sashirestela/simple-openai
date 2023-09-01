# 📌 Simple-OpenAI

A Java library to use the OpenAI API.


## 💡 Description
**Simple-OpenAI** is a Java http client library for sending requests to and receiving responses from the [OpenAI Api](https://platform.openai.com/docs/api-reference).

It exposes a consistent interface across all the resources, yet as simple as you can find in other languages like Python or NodeJs.

It uses the standard Java library [HttpClient](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html) for http communication, [Jackson](https://github.com/FasterXML/jackson) for Json parsing, and [Lombok](https://projectlombok.org/) to minimize boilerplate code.


## ✅ Supported Services
Full support for all of the OpenAI services:

![Services](media/supported_services.png)
NOTE: All the responses are ```CompletableFuture<Response Object>```, so they are asynchronous.

## 🛠️ Installation
You can install Simple-OpenAI by adding the following dependency to your Maven project:

```xml
<dependency>
    <groupId>io.github.sashirestela</groupId>
    <artifactId>simple-openai</artifactId>
    <version>0.1.0</version>
</dependency>
```

Or alternatively using Gradle:

```groovy
dependencies {
    implementation 'io.github.sashirestela:simple-openai:0.1.0'
}
```


## 📘 Usage

![Demo](media/demo_chat_stream.gif)


## 📄 License
Simple-OpenAI is licensed under the MIT License. See the
[LICENSE](https://github.com/sashirestela/simple-openai/blob/main/LICENSE) file
for more information.