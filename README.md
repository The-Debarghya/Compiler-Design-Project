# Compiler Design Project

A lexical and syntactic analyzer done as a part of **Compiler Design** coursework group project.

## Grammar Used:

A `C` like language parser has been implemented which has the following conditions:
- Datatypes include **int**, **char**, **float** and **string**.
- Statements end with a semicolon.
- Program will have only one function that is **main**, must end with **return** statement.
- Loop constructs include only **for**.
- Assignment operator: `=`, supports only addition operator: `+` and comparison operators: `.gt.` (_for greater than_), `.lt.` (_for lesser than_)
- The Grammar input file can be found [here](app/src/main/java/cd_project/parser/inp.txt).
- A sample code file can be found [here](app/src/main/java/cd_project/input.c).

## Testing and Output:

Requirements: `Java17` and `Gradle` for compiling yourself; alternatively you can download from assets.

- Compile using: `./gradlew build`
- The Jarfile will be generated inside `app/build/libs/app.jar`.
- Run the Jarfile using: `java -jar <path_to_jar> <path_to_input_code>`.
- The analysis will be shown in stdout, the following files generated:
  - Canonical States: [here](app/src/main/java/cd_project/parser/parser.states.txt)
  - GoTo Table: [here](app/src/main/java/cd_project/parser/parser.goto.txt)
  - Action Table: [here](app/src/main/java/cd_project/parser/parser.action.txt)
- In the end, it'll display the code is syntactically correct or not.