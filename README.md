# CS307 Final Project  
- Andrei Cozma & Hunter Price  
- Trello: https://trello.com/b/ghb9XDRV/cs307-final-project  

## **INSTALLATION**
#### 1. COMPILATION DEPENDENCIES:
- Java 11 - Check version using `./mvnw --version`
```
> sudo add-apt-repository ppa:openjdk-r/ppa && sudo apt-get update && sudo apt-get install openjdk-11-jdk
```
- This is a link to where you can download the Java 11 Dependency
```
https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
```
#### 2. USE THE INSTALLATION SCRIPT:
```
> ./install
```
- This compiles and packages the program into a .JAR exacutable file in the `target` folder

#### 3. EXECUTION:
```
> ./execute
- Alternatively, double click the 307Proj.jar file generated in the 'target' folder
```

## **DETAILED MANUAL INSTALLATIONS STEPS**
### MAVEN:
- use the Maven Wrapper `./mvnw`  
Otherwise install Maven (```sudo apt-get install maven```)

### INSTALL INCLUDED PROJECT DEPENDENCIES:   
```
	> ./mvnw validate
```

### COMPILATION & INSTALLATION:  

#### OPTION 1:  
1. COMPILE  
```
	> ./mvnw clean // delete compiled files, optional  
	> ./mvnw compile // compile source code  
(or)  
	> ./mvnw clean compile // do both  
```
2. RUN 
```
	> ./mvnw javafx:run  
(or)  
	> ./mvnw exec:java  
```
#### OPTION 2:  
1. PACKAGE  
```
	> ./mvnw clean // delete compiled files, optional  
	> ./mvnw package // Compiles & Packages JAR inside "target" folder  
(or)  
	> ./mvnw clean package // do both  
```
2. RUN  
```
	> java -jar target/307Proj.jar  
```
#### OPTION 3:  
1. INSTALL  
```
	> ./mvnw install // Compiles & Packages & Install into Local Repository  
(or)  
	> ./mvnw clean install // Cleans and Installs  
```
2. RUN with either of the Run options described above  

#### MAVEN LIFECYCLE:  
Usage: `> ./mvnw command`  
1. process-resources
2. compile
3. process-test-resources
4. test-compile
5. test
6. package
7. install
8. deploy

### IMPORT INTO IntelliJ IDEA:  
1. Clone Repository: `git clone https://github.com/andreicozma1/CS307FinalProject`  
2. Open IntelliJ IDEA -> Import -> pom.xml  
3. All dependencies are loaded by the Maven Build System  
4. Click the Maven Tab on the right side of the IDE for options and commands  
5. Add Configuration -> Click + -> Select Maven -> Write "javafx:run" for Command Line   

