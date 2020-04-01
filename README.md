# CS307 Final Project  
- Andrei Cozma & Hunter Price  
- Trello: https://trello.com/b/ghb9XDRV/cs307-final-project  

### DEPENDENCIES:
If you want to use mvn command install Maven
- Maven (```sudo apt-get install maven```)  
Otherwise use the Maven Wrapper ./mvnw 

### INSTALL COMPILATION PRE-REQUISITES:   
```
	> ./mvnw validate
```

## COMPILATION & INSTALLATION:  

### OPTION 1:  
- COMPILE  
```
	> ./mvnw clean // delete compiled files, optional  
	> ./mvnw compile // compile source code  
(or)  
	> ./mvnw clean compile // do both  
```
- RUN 
```
	> ./mvnw javafx:run  
(or)  
	> ./mvnw exec:java  
```
### OPTION 2:  
- PACKAGE  
```
	> ./mvnw clean // delete compiled files, optional  
	> ./mvnw package // Compiles & Packages JAR inside "target" folder  
(or)  
	> ./mvnw clean package // do both  
```
- RUN  
```
	> java -jar target/307Proj.jar  
```
### OPTION 3:  
- INSTALL  
```
	> ./mvnw install // Compiles & Packages & Install into Local Repository  
(or)  
	> ./mvnw clean install // Cleans and Installs  
```
- Run with either of the Run options described above  

### MAVEN LIFECYCLE:  
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

