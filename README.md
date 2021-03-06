![Game Screen Shot](/docs/img.png)

- **Homepage: www.andreicozma.com/307**
- Developers: Andrei Cozma & Hunter Price  (CS307 Final Project)   
# INFINIBUILDER:
### Download & Execution Instructions:
- Execution Requirements: *Java JDK 11+*
- Feature Run-Through Demo: https://youtu.be/TODO
- Execution Instructions + Video: https://github.com/andreicozma1/InfiniBuilder/blob/master/docs/README.md
- Download the Latest Release: https://github.com/andreicozma1/InfiniBuilder/releases  
### Development Tracker:
- *v0.1 Alpha -> v1.0.0 Public Release:*
- Full *Feature List, Bug List, Developer Contributions, and Time Log* are on our public Trello
- Link: https://trello.com/b/ghb9XDRV/cs307-final-project  
### Feature Clips:
- These are just a few of the main features present in InfiniBuilder
- Day-Night Cycle & Skybox Demo: https://youtu.be/4PrfSI-VXnw   
- Infinite Terrain Generation Demo: https://youtu.be/iRxEGaEeFj0   
- Falling and Swimming Physics Demo: https://youtu.be/jwQlcNfhsZA   
- Maze Generation Demo - https://youtu.be/ASkL21km2gs   
- Path Generation Demo - https://youtu.be/VD-9SN94J5o

# **BUILD FROM SOURCE**
#### 1. Compilation Dependencies:
- Java 11 - Check version using `java -version` or `./mvnw --version`
```
sudo add-apt-repository ppa:openjdk-r/ppa && sudo apt-get update && sudo apt-get install openjdk-11-jdk
```
- Java 11 Download link for Windows/Mac/Linux
```
https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
```
#### 2. Use the Installation Script:
```
./install
```
- This compiles and packages the program into a .JAR exacutable file in the `target` folder

#### 3. Execution:
```
./execute
```
- Alternatively, run `java -jar target/InfiniBuilder.jar` or double click the InfiniBuilder.jar file generated in the 'target' folder


# **BUILD FROM SOURCE (MANUAL)**
### Maven:
- use the Maven Wrapper `./mvnw`  
Otherwise install Maven (```sudo apt-get install maven```)

### Install Included Project Dependencies:   
```
./mvnw validate
```

### Compilation & Installation:  

#### Option 1:  
1. COMPILE  
```
./mvnw clean // delete compiled files, optional  
./mvnw compile // compile source code  
(or)  
./mvnw clean compile // do both  
```
2. RUN 
```
./mvnw exec:java  
```
#### Option 2:  
1. PACKAGE  
```
./mvnw clean // delete compiled files, optional  
./mvnw package // Compiles & Packages JAR inside "target" folder  
(or)  
./mvnw clean package // do both  
```
2. RUN  
```
java -jar target/InfiniBuilder.jar  
(or)
./execute
```
#### Option 3:  
1. INSTALL  
```
./mvnw install // Compiles & Packages & Install into Local Repository  
(or)  
./mvnw clean install // Cleans and Installs  
```
2. RUN with either of the Run options described above  

#### EXTRA: MAVEN LIFECYCLE  
Usage: `> ./mvnw command`  
1. process-resources
2. compile
3. process-test-resources
4. test-compile
5. test
6. package
7. install
8. deploy

# IMPORT INTO IntelliJ IDEA:  
1. **Clone Repository:** `git clone https://github.com/andreicozma1/CS307FinalProject`  
2. Open **IntelliJ IDEA -> Import -> pom.xml**  
3. All dependencies are loaded by the **Maven Build System**  
4. Click the Maven Tab on the right side of the IDE for options and commands  
5. **Add Configuration -> Click + -> Select Maven -> Write "javafx:run" for Command Line**   

