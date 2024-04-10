# Contributing
Thank you very much for your interest in contributing to the improvement of this project. Please, follow the next guidelines.

## General Guidelines
1. Keep the code compatible with Java 11.
1. Follow existing code styles present in the project.
1. Avoid adding new dependencies as much as possible.
1. Ensure to add Javadoc where necessary.
1. Provide unit tests for your code.
1. Large features should be discussed with maintainers before implementation. Use the [Discussion](https://github.com/sashirestela/simple-openai/discussions) section for this.

## Code Formatting
This project is configured with [Spotless](https://github.com/diffplug/spotless/tree/main/plugin-maven) to automatically check code formatting against a [custom code format](codestyle/spotless_java_eclipseformat.xml) specification of modern Java, as well as [import order](codestyle/spotless_java.importorder).

Spotless comes with two Maven commands that you can run at any moment, preferably before committing your changes:

- `mvn spotless:check`, to verify if code is formatted correctly.
- `mvn spotless:apply`, to rewrite the source code in place.

The remote build will check the code formatting, so the build will fail if the code isn't formatted correctly.

## Static Analysis
This project is configured to use [SonarCloud](https://sonarcloud.io) for static analysis. That analysis runs after every pull request to the `main` branch and the results can be examined in the project [dashboard](https://sonarcloud.io/project/overview?id=sashirestela_simple-openai).

It is highly recommended you install the free [SonarLint](https://sonarlint.io) extension in your favorite IDE, so you can analyze your code in advance before your pull request.

## Configure IDE (Optional)
You could set your favorite IDE to be aligned to the custom code format. See details below.

<details>

<summary><b>IntelliJ</b></summary>

1. Import the custom code format:

    - Settings > Editor > Code Style > Java > Scheme > ⚙ > Import Scheme > Eclipse XML Profile

2. Select the file on _codestyle/spotless_java_eclipseformat.xml_

3. In the _Imports_ tab for the previous scheme, change the values:
    - In _General_ section, mark the option _Use single class import_
    - In the field _Class count to use import with '*'_ put the value 99
    - In the field _Names count to use static import with '*'_ put the value 99

4. At the bottom part in the _Imports_ section, change the import statement order as:
    - **import** all other imports
    - **import** javax.*
    - **import** java.*
    - **import static** all other imports

</details>

<details>

<summary><b>VSCode</b></summary>

1. Install the extension _Language Support for Java by Red Hat_

2. Set the custom code format:
    
    - Settings > Workspace > Extensions > Language Support for Java(TM) by Red Hat > Formatting
    - Choose the box _Java/Format/Settings:Url_ and put the value _codestyle/spotless_java_eclipseformat.xml_

3. Set the import order:

    - Settings > Workspace > Extensions > Language Support for Java(TM) by Red Hat > Code Completion
    - Choose the box _Java/Completion:ImportOrder_ and click the link _Edit in settings.json_
    - In the editor, make sure to add the following entries:

      ```json
      "java.completion.importOrder": [
          "",
          "javax",
          "java",
          "*"
      ]
      ```

</details>

<details>

<summary><b>Eclipse</b></summary>

1. Import the custom code format:

    - Preferences > Java > Code Style > Formatter > Import...

2. Select the file on _codestyle/spotless_java_eclipseformat.xml_

3. Import the custom import statement order:

    - Preferences > Java > Code Style > Organize Imports > Import...

4. Select the file on _codestyle/spotless_java.importorder_

5. In the _Organize Imports_ section, make sure the values are set as:

    - Sorting order:
      - \* - all unmatched type imports
      - javax
      - java
      - \* - all unmatched static imports
    - Number of imports needed for .*: 99
    - Number of static imports needed for .*: 99

</details>