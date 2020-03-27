#CS307 Final Project
- Andrei Cozma & Hunter Price

##COMPILATION & INSTALLATION:

###OPTION 1
1. COMPILE
	> mvn clean // delete compiled files, optional
	> mvn compile // compile source code  
(or)  
	> mvn clean compile // do both
2. RUN:
	> mvn javafx:run  
(or)  
	> mvn exec:java

###OPTION 2
1. PACKAGE
	> mvn clean // delete compiled files, optional
	> mvn package // Compiles & Packages JAR inside "target" folder  
(or)  
	> mvn clean package // do both
3. RUN:
	> java -jar target/307Proj.jar

###OPTION 3
1. INSTALL 
	> mvn install // Compiles & Packages & Install into Local Repository  
(or)  
	> mvn clean install // Cleans and Installs
2. Run with either of the Run options described above


###MAVEN LIFECYCLE
Usage:  
	> mvn #### 
1. process-resources
2. compile
3. process-test-resources
4. test-compile
5. test
6. package
7. install
8. deploy

###IMPORT INTO IntelliJ IDEA
	> git clone https://github.com/andreicozma1/CS307FinalProject
	> cd CS307FinalProject
	Open IntelliJ IDEA -> Import -> pom.xml
	All dependencies are loaded by the Maven Build System
	Click the Maven Tab on the right side of the IDE for options and commands  
	Add Configuration -> Click + -> Select Maven -> Write "javafx:run" for Command Line

