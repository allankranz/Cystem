# Cystem

The Cystem class is a replacement for System in a Java GUI program.

public final class Cystem
extends java.lang.Object
The Cystem class is a replacement for System in a Java program. It cannot be instantiated

The Cystem class provides a standard input stream, a standard output stream, and a console for simple presentation control of text and screen colors. Using the Cystem class closely mimics using java.lang.System. It does not provide an error stream or access to externally defined properties and environment variables; a means of loading files and libraries; and a utility method for quickly copying a portion of an array.

The output stream out can be used to print to the Cystem console by simply by calling Cystem.out.println("Message"); and all the types supported by System.out are supported by Cystem.out.

```
// Cystem.in is meant to used exactly like System.in, typically use is with java.util.Scanner as follows; 
Scanner cin = new Scanner(Cystem.in); 
String input = cin.nextLine(); 

// To control the console colors 
Cystem.console.setTextColor(Color c); 
Cystem.console.setScreenColor(Color c); 

// No attempt is made to check for colors that will not be visible. The default color is bright green
// for text and black for the screen. Typical use looks like 
Cystem.console.setScreenColor(Color.DARK_GRAY); 
Cystem.console.setTextColor(Color.ORANGE); 
Cystem.console.makeVisible(); 

Cystem.out.println("Starting Cystem console..."); 
Scanner cin = new Scanner(Cystem.in); 
Cystem.out.println("Enter your age:"); 
int x = cin.nextInt(); 
Cystem.out.printf("%s %d\n","Your age is ", x); 
cin.nextLine(); 
Cystem.out.print("Enter anything..."); 
String s = cin.nextLine(); 
Cystem.console.makeInvisible(); 
Cystem.console.shutdown(); 

// Author: Allan Kranz (allan.kranz@unbc.ca) 
```
