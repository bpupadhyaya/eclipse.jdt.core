package org.eclipse.jdt.core.tests.runtime;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
import org.eclipse.jdt.core.tests.runtime.RuntimeConstants;

import java.io.*;

import java.util.Vector;
import java.util.Enumeration;

/**
 * A standard VM launcher launches an external standard VM with
 * the given arguments on the same machine.
 */
public class StandardVMLauncher extends LocalVMLauncher implements RuntimeConstants {
	String batchFileName;
/**
 * Creates a new StandardVMLauncher that launches a JDK VM
 * on the same machine.
 */
public StandardVMLauncher() {
	super();
}
/**
 * Builds the actual boot class path that is going to be passed to the VM.
 */
protected String buildBootClassPath() {
	StringBuffer bootPathString = new StringBuffer();
	char pathSeparator = File.pathSeparatorChar;
	
	if (this.bootPath != null) {
		// Add boot class path given by client
		int length = this.bootPath.length;
		for (int i = 0; i < length; i++){
			bootPathString.append(this.bootPath[i]);
			bootPathString.append(pathSeparator);
		}
	} else {
		// Add regular rt.jar
		bootPathString.append(this.vmPath);
		bootPathString.append(File.separator);
		if (!(this.vmPath.toLowerCase().endsWith("jre") || this.vmPath.toLowerCase().endsWith("jre" + File.separator))) {
			bootPathString.append("jre");
			bootPathString.append(File.separator);
		}
		bootPathString.append("lib");
		bootPathString.append(File.separator);
		bootPathString.append("rt.jar");
		bootPathString.append(pathSeparator);
	}
	
	// Add boot class path directory if needed
	if (this.evalTargetPath != null && TARGET_HAS_FILE_SYSTEM) {
		bootPathString.append(this.evalTargetPath);
		bootPathString.append(File.separatorChar);
		bootPathString.append(BOOT_CLASSPATH_DIRECTORY);
	}

	return bootPathString.toString();
}
/**
 * Checks that the version of the VM is the correct JDK version.
 * @exception com.ibm.jdt.eval.launcher.TargetException if the version is not the correct one.
 */
private void checkVMVersion() throws TargetException {
	/* TO DO: 
		- Launch the VM with "-version" option
		- Check that the ouput is:
			java version "1.2"
			Classic VM (build JDK-1.2-V, native threads)
		- Shutdown the VM
	*/
}
/**
 * Returns the name of the batch file used to launch the VM.
 */
public String getBatchFileName() {
	return this.batchFileName;
}
/**
 * @see LocalVMLauncher#getCommandLine
 */
public String[] getCommandLine() {	
	Vector commandLine= new Vector();
	
	// VM binary
	commandLine.addElement(
		this.vmPath + 
		(this.vmPath.endsWith(File.separator) ? "" : File.separator) + 
		"bin" + 
		File.separator + 
		"javaw");

	// VM arguments
	if (this.vmArguments != null) {
		for (int i = 0; i < this.vmArguments.length; i++) {
			commandLine.addElement(this.vmArguments[i]);
		}
	}

	// debug mode
	if (this.debugPort != -1) {
		commandLine.addElement("-Xdebug");
		commandLine.addElement("-Xnoagent");
		//commandLine.addElement("-Djava.compiler=NONE");
		commandLine.addElement(
			"-Xrunjdwp:transport=dt_socket,address=" +
			this.debugPort +
			",server=y,suspend=n");
	}

	// boot classpath
	commandLine.addElement("-Xbootclasspath:" + buildBootClassPath());

	// regular classpath
	commandLine.addElement("-classpath");
	commandLine.addElement(buildClassPath());

	// code snippet runner class
	if (this.evalPort != -1) {
		commandLine.addElement(CODE_SNIPPET_RUNNER_CLASS_NAME);
	}
	
	// code snippet runner arguments
	if (this.evalPort != -1) {
		commandLine.addElement(EVALPORT_ARG);
		commandLine.addElement(Integer.toString(this.evalPort));
		if (TARGET_HAS_FILE_SYSTEM) {
			commandLine.addElement(CODESNIPPET_CLASSPATH_ARG);
			commandLine.addElement(this.evalTargetPath + File.separator + REGULAR_CLASSPATH_DIRECTORY);
			commandLine.addElement(CODESNIPPET_BOOTPATH_ARG);
			commandLine.addElement(this.evalTargetPath + File.separator + BOOT_CLASSPATH_DIRECTORY);
		}
	}

	// program class
	if (this.programClass != null) {
		commandLine.addElement(this.programClass);
	}
	
	// program arguments
	if (this.programArguments != null) {
		for (int i=0;i<this.programArguments.length;i++) {
			commandLine.addElement(this.programArguments[i]);
		}
	}

	String[] result;
	if (this.batchFileName!= null) {
		// Write to batch file if specified
		writeBatchFile(this.batchFileName, commandLine);
		result = new String[] {this.batchFileName};
	} else {
		result = new String[commandLine.size()];
		commandLine.copyInto(result);
	}

	// check for spaces in result
	for (int i = 0; i < result.length; i++) {
		String argument = result[i];
		if (argument.indexOf(' ') != -1) {
			result[i] = "\"" + argument + "\"";
		}
	}
	
	return result;
}
/**
 * Sets the name of the batch file used to launch the VM.
 * When this option is set, the launcher writes the command line to the given batch file, 
 * and it launches the  batch file. This causes a DOS console to be opened. Note it 
 * doesn't delete the batch file when done.
 */
public void setBatchFileName(String batchFileName) {
	this.batchFileName = batchFileName;
}
private void writeBatchFile(String fileName, Vector commandLine) {
	FileOutputStream output = null;
	try {
		output = new FileOutputStream(fileName);
		PrintWriter out= new PrintWriter(output);
		for (Enumeration e = commandLine.elements(); e.hasMoreElements();) {
			out.print((String)e.nextElement());
			out.print(" ");
		}
		out.println("pause");
		out.close();
	} catch (IOException e) {
		e.printStackTrace();
		if (output != null) {
			try {
				output.close();
			} catch (IOException e2) {
			}
		}
	}
}
}
