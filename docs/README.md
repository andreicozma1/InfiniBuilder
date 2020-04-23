# InfiniBuilder Public Release  - Execution Instructions 
- by Andrei Cozma & Hunter Price.  
#### Website: www.andreicozma.com/307
#### Installation Run-Through Video: https://youtu.be/ggAXd36MmdI
## System Requirements:
- Linux, MacOS, or Windows
- *Java 11+ (Download link on www.andreicozma.com/307)*
- Recommended 4GB of RAM

## Linux, MacOS:
- Install Oracle Java JDK 11+ (Or OpenJDK 11+)
- Ensure the files are set as executable: `chmod +x InfiniBuilder.jar execute.sh`
- Run ./execute.sh to run with VM options
- Alternatively, double click InfiniBuilder.jar to run without VM Options
## Windows:
- Install Oracle Java JDK 11+ (Or OpenJDK 11+)
- Ensure the .jar is set as executable
- Double click on InfiniBuilder.jar to run without VM Options

## Extra:
### ** For the best possible performance, run with Java VM Options:
> java -jar -Xmx8192m -Xms2048m -Dprism.order=es2,es1,sw,j2d -Dprism.targetvram=2G -Dprism.maxTextureSize=1024 -Dprism.dirtyopts=true -Dquantum.multithreaded=true InfiniBuilder.jar

### If running into stability issues, tweak the -Xmx and -Xms to match your system configuration.
- Xmx is the maximum amount of RAM that can be allocated to the Java virtual machine
- Xms is the inital amount of RAM that should be allocated to the Java virtual machine on startup


