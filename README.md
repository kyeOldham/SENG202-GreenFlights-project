NOTE: All testing of this project was performed using JDK 11 and on the Linux operating system.



EXTRACTING THE ZIP FILE

To extract the zip file, open the directory where the zip file is stored in a terminal window and
run the following command: "jar xf seng202_2020_team4_phase3.zip". The contents of the zip file
will now be stored (unzipped) in the directory of the zip file itself.



LOADING THE SOURCE CODE INTO INTELLIJ IDEA and RUNNING THE PROGRAM

To load the source code into IntelliJ IDEA IDE, extract the following folder from the zip file to a
directory of your choosing: seng202_2020_team4_3. Once you have safely stored the file, open
IntelliJ IDEA IDE and select: (File > Open). Next, navigate to the directory of the folder, select
it and press "OK".

To run the program, first navigate to the pom.xml file in the main directory inside IntelliJ IDEA
IDE, right click it and select "Add as Maven Project" and refresh the Maven project with the
refresh symbol in the Maven menu on the right side of the IDE. Next, select "Add Configuration..."
at the top right of the IDE. When the new "Run/Debug Configurations" window appears, select the
plus (+) symbol at the top right, then select "Application". Now, enter "seng202.team4.model.Main"
in the Main class field and press OK. Finally, press the "Run" button (green play symbol) at the
top right of the IDE to run the program.



RUNNING THE JAR FILE

To run the jar file, firstly ensure that Java SE Development Kit 11 is installed on the machine,
and that the machine is running a Linux operating system. Next, extract the following JAR file from
the given zip file: "seng202_2020_team4_3.jar". Finally, run the following command in a terminal
window in the folder that the JAR file is stored in: "sudo java -jar seng202_2020_team4_3.jar".
(Note: sudo may not be required, depending on the permissions of the user account and the
installation directory).

Alternatively (and, if possible), open "seng202_2020_team4_3.jar" with Java Platform SE Binary.

Note: When running the application for the first time, a "databases" folder will be created in the
directory of the JAR file.

Note: To generate JavaDoc, the following command line parameter is required due to foreign
characters being used in the code for validation checking: "-encoding UTF-8".
