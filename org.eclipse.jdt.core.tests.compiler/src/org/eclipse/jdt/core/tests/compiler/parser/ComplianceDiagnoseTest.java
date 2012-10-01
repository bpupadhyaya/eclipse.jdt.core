/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This is an implementation of an early-draft specification developed under the Java
 * Community Process (JCP) and is made available for testing and evaluation purposes
 * only. The code is not compatible with any specification of the JCP.
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core.tests.compiler.parser;

import junit.framework.Test;

import org.eclipse.jdt.core.tests.compiler.regression.AbstractRegressionTest;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;

public class ComplianceDiagnoseTest extends AbstractRegressionTest {
	public ComplianceDiagnoseTest(String name) {
		super(name);
	}
// Static initializer to specify tests subset using TESTS_* static variables
// All specified tests which does not belong to the class are skipped...
static {
//	TESTS_NAMES = new String[] { "test0061" };
//	TESTS_NUMBERS = new int[] { 50 };
//	TESTS_RANGE = new int[] { 21, 50 };
}
public static Test suite() {
	return buildAllCompliancesTestSuite(testClass());
}
public static Class testClass() {
	return ComplianceDiagnoseTest.class;
}
public void runComplianceParserTest(
	String[] testFiles,
	String expected13ProblemLog,
	String expected14ProblemLog,
	String expected15ProblemLog){
	if(this.complianceLevel == ClassFileConstants.JDK1_3) {
		this.runNegativeTest(testFiles, expected13ProblemLog);
	} else if(this.complianceLevel == ClassFileConstants.JDK1_4) {
		this.runNegativeTest(testFiles, expected14ProblemLog);
	} else if(this.complianceLevel >= ClassFileConstants.JDK1_5) {
		this.runNegativeTest(testFiles, expected15ProblemLog);
	}
}
public void runComplianceParserTest(
		String[] testFiles,
		String expected13ProblemLog,
		String expected14ProblemLog,
		String expected15ProblemLog,
		String expected17ProblemLog){
		if(this.complianceLevel == ClassFileConstants.JDK1_3) {
			this.runNegativeTest(testFiles, expected13ProblemLog);
		} else if(this.complianceLevel == ClassFileConstants.JDK1_4) {
			this.runNegativeTest(testFiles, expected14ProblemLog);
		} else if(this.complianceLevel < ClassFileConstants.JDK1_7) {
			this.runNegativeTest(testFiles, expected15ProblemLog);
		} else {
			this.runNegativeTest(testFiles, expected17ProblemLog);
		}
	}
public void runComplianceParserTest(
		String[] testFiles,
		String expected13ProblemLog,
		String expected14ProblemLog,
		String expected15ProblemLog,
		String expected16ProblemLog,
		String expected17ProblemLog){
		if (this.complianceLevel == ClassFileConstants.JDK1_3) {
			this.runNegativeTest(testFiles, expected13ProblemLog);
		} else if(this.complianceLevel == ClassFileConstants.JDK1_4) {
			this.runNegativeTest(testFiles, expected14ProblemLog);
		} else if(this.complianceLevel == ClassFileConstants.JDK1_5) {
			this.runNegativeTest(testFiles, expected15ProblemLog);
		} else if(this.complianceLevel == ClassFileConstants.JDK1_6) {
			this.runNegativeTest(testFiles, expected16ProblemLog);
		} else if(this.complianceLevel < ClassFileConstants.JDK1_8) {
			this.runNegativeTest(testFiles, expected17ProblemLog);
		}
	}
public void test0001() {
	String[] testFiles = new String[] {
		"X.java",
		"import static aaa.BBB.*;\n" +
		"public class X {\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	import static aaa.BBB.*;\n" +
		"	^^^^^^^^^^^^^^^^^^^^^^^^\n" +
		"Syntax error, static imports are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	import static aaa.BBB.*;\n" +
		"	              ^^^\n" +
		"The import aaa cannot be resolved\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	import static aaa.BBB.*;\n" +
		"	              ^^^\n" +
		"The import aaa cannot be resolved\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0002() {
	String[] testFiles = new String[] {
		"X.java",
		"import static aaa.BBB.CCC;\n" +
		"public class X {\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	import static aaa.BBB.CCC;\n" +
		"	^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
		"Syntax error, static imports are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	import static aaa.BBB.CCC;\n" +
		"	              ^^^\n" +
		"The import aaa cannot be resolved\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	import static aaa.BBB.CCC;\n" +
		"	              ^^^\n" +
		"The import aaa cannot be resolved\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0003() {
	String[] testFiles = new String[] {
		"X.java",
		"public enum X {\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public enum X {\n" +
		"	       ^^^^\n" +
		"Syntax error on token \"enum\", interface expected\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0004() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	void foo(){\n" +
		"		for(String o: c) {\n" +
		"		}\n" +
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 3)\n" +
		"	for(String o: c) {\n" +
		"	    ^^^^^^^^^^^\n" +
		"Syntax error, \'for each\' statements are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 3)\n" +
		"	for(String o: c) {\n" +
		"	              ^\n" +
		"c cannot be resolved to a variable\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 3)\n" +
		"	for(String o: c) {\n" +
		"	              ^\n" +
		"c cannot be resolved to a variable\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0005() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	void foo(Z ... arg){\n" +
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	void foo(Z ... arg){\n" +
		"	         ^^^^^^^^^\n" +
		"Syntax error, varargs are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	void foo(Z ... arg){\n" +
		"	         ^\n" +
		"Z cannot be resolved to a type\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	void foo(Z ... arg){\n" +
		"	         ^\n" +
		"Z cannot be resolved to a type\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0006() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X <T1 extends String, T2> extends Y {\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extends String, T2> extends Y {\n" +
		"	                ^^^^^^^^^^^^^^^^^^^^^\n" +
		"Syntax error, type parameters are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extends String, T2> extends Y {\n" +
		"	                                               ^\n" +
		"Y cannot be resolved to a type\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. WARNING in X.java (at line 1)\n" +
		"	public class X <T1 extends String, T2> extends Y {\n" +
		"	                           ^^^^^^\n" +
		"The type parameter T1 should not be bounded by the final type String. Final types cannot be further extended\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extends String, T2> extends Y {\n" +
		"	                                               ^\n" +
		"Y cannot be resolved to a type\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0007() {
	String[] testFiles = new String[] {
		"X.java",
		"public interface X <T1 extends String, T2> extends Y {\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public interface X <T1 extends String, T2> extends Y {\n" +
		"	                    ^^^^^^^^^^^^^^^^^^^^^\n" +
		"Syntax error, type parameters are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	public interface X <T1 extends String, T2> extends Y {\n" +
		"	                                                   ^\n" +
		"Y cannot be resolved to a type\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. WARNING in X.java (at line 1)\n" +
		"	public interface X <T1 extends String, T2> extends Y {\n" +
		"	                               ^^^^^^\n" +
		"The type parameter T1 should not be bounded by the final type String. Final types cannot be further extended\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	public interface X <T1 extends String, T2> extends Y {\n" +
		"	                                                   ^\n" +
		"Y cannot be resolved to a type\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0008() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	public <T1 extends String, T2> int foo(){\n" +
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	public <T1 extends String, T2> int foo(){\n" +
		"	        ^^^^^^^^^^^^^^^^^^^^^\n" +
		"Syntax error, type parameters are only available if source level is 1.5 or greater\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. WARNING in X.java (at line 2)\n" +
		"	public <T1 extends String, T2> int foo(){\n" +
		"	                   ^^^^^^\n" +
		"The type parameter T1 should not be bounded by the final type String. Final types cannot be further extended\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	public <T1 extends String, T2> int foo(){\n" +
		"	                                   ^^^^^\n" +
		"This method must return a result of type int\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0009() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	public <T1 extends String, T2> X(){\n" +
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	public <T1 extends String, T2> X(){\n" +
		"	        ^^^^^^^^^^^^^^^^^^^^^\n" +
		"Syntax error, type parameters are only available if source level is 1.5 or greater\n" +
		"----------\n";

	String expected15ProblemLog =
		"----------\n" +
		"1. WARNING in X.java (at line 2)\n" +
		"	public <T1 extends String, T2> X(){\n" +
		"	                   ^^^^^^\n" +
		"The type parameter T1 should not be bounded by the final type String. Final types cannot be further extended\n" +
		"----------\n";

	if(this.complianceLevel < ClassFileConstants.JDK1_5) {
		this.runNegativeTest(testFiles, expected13ProblemLog);
	} else {
		runConformTest(
			true,
			testFiles,
			expected15ProblemLog,
			null, null,
			JavacTestOptions.Excuse.EclipseHasSomeMoreWarnings);
	}
}
public void test0010() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	Z<Y1, Y2> var;\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	Z<Y1, Y2> var;\n" +
		"	^\n" +
		"Z cannot be resolved to a type\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	Z<Y1, Y2> var;\n" +
		"	  ^^^^^^\n" +
		"Syntax error, parameterized types are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 2)\n" +
		"	Z<Y1, Y2> var;\n" +
		"	  ^^\n" +
		"Y1 cannot be resolved to a type\n" +
		"----------\n" +
		"4. ERROR in X.java (at line 2)\n" +
		"	Z<Y1, Y2> var;\n" +
		"	      ^^\n" +
		"Y2 cannot be resolved to a type\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	Z<Y1, Y2> var;\n" +
		"	^\n" +
		"Z cannot be resolved to a type\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	Z<Y1, Y2> var;\n" +
		"	  ^^\n" +
		"Y1 cannot be resolved to a type\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 2)\n" +
		"	Z<Y1, Y2> var;\n" +
		"	      ^^\n" +
		"Y2 cannot be resolved to a type\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0011() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	public X(){\n" +
		"		<Y1, Y2>this(null);\n" +
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 3)\n" +
		"	<Y1, Y2>this(null);\n" +
		"	 ^^^^^^\n" +
		"Syntax error, parameterized types are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 3)\n" +
		"	<Y1, Y2>this(null);\n" +
		"	 ^^\n" +
		"Y1 cannot be resolved to a type\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 3)\n" +
		"	<Y1, Y2>this(null);\n" +
		"	     ^^\n" +
		"Y2 cannot be resolved to a type\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 3)\n" +
		"	<Y1, Y2>this(null);\n" +
		"	 ^^\n" +
		"Y1 cannot be resolved to a type\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 3)\n" +
		"	<Y1, Y2>this(null);\n" +
		"	     ^^\n" +
		"Y2 cannot be resolved to a type\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 3)\n" +
		"	<Y1, Y2>this(null);\n" +
		"	        ^^^^^^^^^^^\n" +
		"The constructor X(null) is undefined\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0012() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"  void foo() {\n" +
		"    assert true;\n" +
		"  }\n" +
		"}\n" +
		"\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. WARNING in X.java (at line 3)\n" +
		"	assert true;\n" +
		"	^^^^^^\n" +
		"\'assert\' should not be used as an identifier, since it is a reserved keyword from source level 1.4 on\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 3)\n" +
		"	assert true;\n" +
		"	^^^^^^\n" +
		"Syntax error on token \"assert\", AssignmentOperator expected after this token\n" +
		"----------\n";
	String expected14ProblemLog =
		"";

	String expected15ProblemLog =
		expected14ProblemLog;

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0013() {
	String[] testFiles = new String[] {
		"X.java",
		"import static aaa.*\n" +
		"public class X {\n" +
		"}\n" +
		"\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	import static aaa.*\n" +
		"	^^^^^^^^^^^^^^^^^\n" +
		"Syntax error, static imports are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	import static aaa.*\n" +
		"	              ^^^\n" +
		"The import aaa cannot be resolved\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 1)\n" +
		"	import static aaa.*\n" +
		"	                  ^\n" +
		"Syntax error on token \"*\", ; expected after this token\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	import static aaa.*\n" +
		"	              ^^^\n" +
		"The import aaa cannot be resolved\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	import static aaa.*\n" +
		"	                  ^\n" +
		"Syntax error on token \"*\", ; expected after this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0014() {
	String[] testFiles = new String[] {
		"X.java",
		"public enum X \n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public enum X \n" +
		"}\n" +
		"	       ^^^^^^^^^\n" +
		"Syntax error on tokens, delete these tokens\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public enum X \n" +
		"	            ^\n" +
		"Syntax error on token \"X\", { expected after this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0015() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	void foo(){\n" +
		"		for(String o: c) {\n" +
		"			#\n" +
		"		}\n" +
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 3)\n" +
		"	for(String o: c) {\n" +
		"	    ^^^^^^^^^^^\n" +
		"Syntax error, \'for each\' statements are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 4)\n" +
		"	#\n" +
		"	^\n" +
		"Syntax error on token \"Invalid Character\", delete this token\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 4)\n" +
		"	#\n" +
		"	^\n" +
		"Syntax error on token \"Invalid Character\", delete this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0016() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	void foo(Z ... arg){\n" +
		"	}\n" +
		"	#\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	void foo(Z ... arg){\n" +
		"	         ^^^^^^^^^\n" +
		"Syntax error, varargs are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	void foo(Z ... arg){\n" +
		"	         ^\n" +
		"Z cannot be resolved to a type\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 4)\n" +
		"	#\n" +
		"	^\n" +
		"Syntax error on token \"Invalid Character\", delete this token\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	void foo(Z ... arg){\n" +
		"	         ^\n" +
		"Z cannot be resolved to a type\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 4)\n" +
		"	#\n" +
		"	^\n" +
		"Syntax error on token \"Invalid Character\", delete this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0017() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X <T1 extends String, T2> extends Y {\n" +
		"	#\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extends String, T2> extends Y {\n" +
		"	                ^^^^^^^^^^^^^^^^^^^^^\n" +
		"Syntax error, type parameters are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extends String, T2> extends Y {\n" +
		"	                                               ^\n" +
		"Y cannot be resolved to a type\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 2)\n" +
		"	#\n" +
		"	^\n" +
		"Syntax error on token \"Invalid Character\", delete this token\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. WARNING in X.java (at line 1)\n" +
		"	public class X <T1 extends String, T2> extends Y {\n" +
		"	                           ^^^^^^\n" +
		"The type parameter T1 should not be bounded by the final type String. Final types cannot be further extended\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extends String, T2> extends Y {\n" +
		"	                                               ^\n" +
		"Y cannot be resolved to a type\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 2)\n" +
		"	#\n" +
		"	^\n" +
		"Syntax error on token \"Invalid Character\", delete this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0018() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	public <T1 extends String, T2> int foo(){\n" +
		"	}\n" +
		"	#\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	public <T1 extends String, T2> int foo(){\n" +
		"	        ^^^^^^^^^^^^^^^^^^^^^\n" +
		"Syntax error, type parameters are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 4)\n" +
		"	#\n" +
		"	^\n" +
		"Syntax error on token \"Invalid Character\", delete this token\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. WARNING in X.java (at line 2)\n" +
		"	public <T1 extends String, T2> int foo(){\n" +
		"	                   ^^^^^^\n" +
		"The type parameter T1 should not be bounded by the final type String. Final types cannot be further extended\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 4)\n" +
		"	#\n" +
		"	^\n" +
		"Syntax error on token \"Invalid Character\", delete this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0019() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	Z<Y1, Y2> var;\n" +
		"	#\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	Z<Y1, Y2> var;\n" +
		"	^\n" +
		"Z cannot be resolved to a type\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	Z<Y1, Y2> var;\n" +
		"	  ^^^^^^\n" +
		"Syntax error, parameterized types are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 2)\n" +
		"	Z<Y1, Y2> var;\n" +
		"	  ^^\n" +
		"Y1 cannot be resolved to a type\n" +
		"----------\n" +
		"4. ERROR in X.java (at line 2)\n" +
		"	Z<Y1, Y2> var;\n" +
		"	      ^^\n" +
		"Y2 cannot be resolved to a type\n" +
		"----------\n" +
		"5. ERROR in X.java (at line 3)\n" +
		"	#\n" +
		"	^\n" +
		"Syntax error on token \"Invalid Character\", delete this token\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	Z<Y1, Y2> var;\n" +
		"	^\n" +
		"Z cannot be resolved to a type\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	Z<Y1, Y2> var;\n" +
		"	  ^^\n" +
		"Y1 cannot be resolved to a type\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 2)\n" +
		"	Z<Y1, Y2> var;\n" +
		"	      ^^\n" +
		"Y2 cannot be resolved to a type\n" +
		"----------\n" +
		"4. ERROR in X.java (at line 3)\n" +
		"	#\n" +
		"	^\n" +
		"Syntax error on token \"Invalid Character\", delete this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0020() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"  void foo() {\n" +
		"    assert true;\n" +
		"    #\n" +
		"  }\n" +
		"}\n" +
		"\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. WARNING in X.java (at line 3)\n" +
		"	assert true;\n" +
		"	^^^^^^\n" +
		"\'assert\' should not be used as an identifier, since it is a reserved keyword from source level 1.4 on\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 3)\n" +
		"	assert true;\n" +
		"	^^^^^^\n" +
		"Syntax error on token \"assert\", AssignmentOperator expected after this token\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 4)\n" +
		"	#\n" +
		"	^\n" +
		"Syntax error on token \"Invalid Character\", delete this token\n" +
		"----------\n";
	String expected14ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 4)\n" +
		"	#\n" +
		"	^\n" +
		"Syntax error on token \"Invalid Character\", delete this token\n" +
		"----------\n";

	String expected15ProblemLog =
		expected14ProblemLog;

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
//TODO (david) suspicious behavior
public void test0021() {
	String[] testFiles = new String[] {
		"X.java",
		"import staic aaa.*;\n" +
		"public class X {\n" +
		"}\n" +
		"\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	import staic aaa.*;\n" +
		"	       ^^^^^\n" +
		"The import staic cannot be resolved\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	import staic aaa.*;\n" +
		"	             ^^^\n" +
		"Syntax error on token \"aaa\", delete this token\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	import staic aaa.*;\n" +
		"	       ^^^^^\n" +
		"Syntax error on token \"staic\", static expected\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	import staic aaa.*;\n" +
		"	       ^^^^^\n" +
		"The import staic cannot be resolved\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
//TODO (david) suspicious behavior
public void test0022() {
	String[] testFiles = new String[] {
		"X.java",
		"import static aaa.*.*;\n" +
		"public class X {\n" +
		"}\n" +
		"\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	import static aaa.*.*;\n" +
		"	^^^^^^^^^^^^^^^^^\n" +
		"Syntax error, static imports are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	import static aaa.*.*;\n" +
		"	              ^^^\n" +
		"The import aaa cannot be resolved\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 1)\n" +
		"	import static aaa.*.*;\n" +
		"	                   ^^\n" +
		"Syntax error on tokens, delete these tokens\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	import static aaa.*.*;\n" +
		"	              ^^^\n" +
		"The import aaa cannot be resolved\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	import static aaa.*.*;\n" +
		"	                  ^\n" +
		"Syntax error on token \"*\", Identifier expected\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0023() {
	String[] testFiles = new String[] {
		"X.java",
		"import static for;\n" +
		"public class X {\n" +
		"}\n" +
		"\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	import static for;\n" +
		"	       ^^^^^^^^^^\n" +
		"Syntax error on tokens, Name expected instead\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
			"----------\n" + 
			"1. ERROR in X.java (at line 1)\n" + 
			"	import static for;\n" + 
			"	              ^^^\n" + 
			"Syntax error on token \"for\", invalid Name\n" + 
			"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}

//TODO (david) reenable once bug is fixed
public void _test0024() {
	String[] testFiles = new String[] {
		"X.java",
		"import static {aaa};\n" +
		"public class X {\n" +
		"}\n" +
		"\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	import static {aaa};\n" +
		"	       ^^^^^^^^^^^^\n" +
		"Syntax error on tokens, Name expected instead\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	import static {aaa};\n" +
		"	              ^^^^^\n" +
		"Syntax error on tokens, Name expected instead\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0025() {
	String[] testFiles = new String[] {
		"X.java",
		"static aaa.*;\n" +
		"public class X {\n" +
		"}\n" +
		"\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	static aaa.*;\n" +
		"	^^^^^^\n" +
		"Syntax error on token \"static\", import expected\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	static aaa.*;\n" +
		"	^^^^^^\n" +
		"Syntax error on token \"static\", import expected before this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0026() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	void foo(){\n" +
		"		for(Object o ? c){\n" +
		"		}\n" +
		"	}\n" +
		"}\n" +
		"\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 3)\n" +
		"	for(Object o ? c){\n" +
		"	    ^^^^^^\n" +
		"Syntax error on token \"Object\", ( expected\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 3)\n" +
		"	for(Object o ? c){\n" +
		"	           ^^^\n" +
		"Syntax error on token(s), misplaced construct(s)\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 3)\n" +
		"	for(Object o ? c){\n" +
		"	                ^\n" +
		"Syntax error, insert \"AssignmentOperator Expression\" to complete Assignment\n" +
		"----------\n" +
		"4. ERROR in X.java (at line 3)\n" +
		"	for(Object o ? c){\n" +
		"	                ^\n" +
		"Syntax error, insert \"; ; ) Statement\" to complete BlockStatements\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 3)\n" +
		"	for(Object o ? c){\n" +
		"	             ^\n" +
		"Syntax error on token \"?\", : expected\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0027() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	void foo(){\n" +
		"		for(Object o : switch){\n" +
		"		}\n" +
		"	}\n" +
		"}\n" +
		"\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 3)\n" +
		"	for(Object o : switch){\n" +
		"	           ^\n" +
		"Syntax error, insert \"; ; ) Statement\" to complete BlockStatements\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 3)\n" +
		"	for(Object o : switch){\n" +
		"	               ^^^^^^\n" +
		"Syntax error on token \"switch\", invalid Expression\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0028() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	void foo(int ... ){\n" +
		"	}\n" +
		"}\n" +
		"\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X {\n" +
		"	               ^\n" +
		"Syntax error, insert \"}\" to complete ClassBody\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	void foo(int ... ){\n" +
		"	             ^^^\n" +
		"Syntax error on token \"...\", invalid VariableDeclaratorId\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 4)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error on token \"}\", delete this token\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X {\n" +
		"	               ^\n" +
		"Syntax error, insert \"}\" to complete ClassBody\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	void foo(int ... ){\n" +
		"	             ^^^\n" +
		"Syntax error on token \"...\", VariableDeclaratorId expected after this token\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 4)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error on token \"}\", delete this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0029() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	void foo(int ... for){\n" +
		"	}\n" +
		"}\n" +
		"\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X {\n" +
		"	               ^\n" +
		"Syntax error, insert \"}\" to complete ClassBody\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	void foo(int ... for){\n" +
		"	             ^^^^^^^\n" +
		"Syntax error on tokens, VariableDeclaratorId expected instead\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 4)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error on token \"}\", delete this token\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X {\n" +
		"	               ^\n" +
		"Syntax error, insert \"}\" to complete ClassBody\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	void foo(int ... for){\n" +
		"	                 ^^^\n" +
		"Syntax error on token \"for\", invalid VariableDeclaratorId\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 4)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error on token \"}\", delete this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void _test0030() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	void foo(int .. aaa){\n" +
		"	}\n" +
		"}\n" +
		"\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X {\n" +
		"	               ^\n" +
		"Syntax error, insert \"}\" to complete ClassBody\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	void foo(int .. aaa){\n" +
		"	             ^^\n" +
		"Syntax error on tokens, delete these tokens\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 4)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error on token \"}\", delete this token\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X {\n" +
		"	               ^\n" +
		"Syntax error, insert \"}\" to complete ClassBody\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	void foo(int .. aaa){\n" +
		"	             ^^\n" +
		"Syntax error on tokens, delete these tokens\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 4)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error on token \"}\", delete this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0031() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	void foo(int ... aaa bbb){\n" +
		"	}\n" +
		"}\n" +
		"\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X {\n" +
		"	               ^\n" +
		"Syntax error, insert \"}\" to complete ClassBody\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	void foo(int ... aaa bbb){\n" +
		"	         ^^^^^^^^^^^\n" +
		"Syntax error, varargs are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 2)\n" +
		"	void foo(int ... aaa bbb){\n" +
		"	             ^^^^^^^\n" +
		"Syntax error on token(s), misplaced construct(s)\n" +
		"----------\n" +
		"4. ERROR in X.java (at line 4)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error on token \"}\", delete this token\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X {\n" +
		"	               ^\n" +
		"Syntax error, insert \"}\" to complete ClassBody\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	void foo(int ... aaa bbb){\n" +
		"	                     ^^^\n" +
		"Syntax error on token \"bbb\", delete this token\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 4)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error on token \"}\", delete this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void _test0032() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X <T1 extends String, T2 extends Y {\n" +
		"	\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" + 
		"1. ERROR in X.java (at line 1)\n" + 
		"	public class X <T1 extends String, T2 extends Y {\n" + 
		"	               ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
		"Syntax error on token(s), misplaced construct(s)\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 1)\n" + 
		"	public class X <T1 extends String, T2 extends Y {\n" + 
		"	                                              ^\n" + 
		"Y cannot be resolved to a type\n" + 
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. WARNING in X.java (at line 1)\n" +
		"	public class X <T1 extends String, T2 extends Y {\n" +
		"	                           ^^^^^^\n" +
		"The type parameter T1 should not be bounded by the final type String. Final types cannot be further extended\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extends String, T2 extends Y {\n" +
		"	                                              ^\n" +
		"Syntax error, insert \">\" to complete ReferenceType1\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extends String, T2 extends Y {\n" +
		"	                                              ^\n" +
		"Y cannot be resolved to a type\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0033() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X T1 extends String, T2> extends Y {\n" +
		"	\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X T1 extends String, T2> extends Y {\n" +
		"	               ^^\n" +
		"Syntax error on token \"T1\", delete this token\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	public class X T1 extends String, T2> extends Y {\n" +
		"	                          ^^^^^^^^^^^^^^^^^^^\n" +
		"Syntax error on tokens, delete these tokens\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X T1 extends String, T2> extends Y {\n" +
		"	             ^\n" +
		"Syntax error on token \"X\", < expected after this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0034() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X <T1 extnds String, T2> extends Y {\n" +
		"	\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extnds String, T2> extends Y {\n" +
		"	               ^\n" +
		"Syntax error on token \"<\", { expected\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extnds String, T2> extends Y {\n" +
		"	                ^^^^^^^^^^^^^^^^^^^^^\n" +
		"Syntax error on tokens, InterfaceHeaderName expected instead\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extnds String, T2> extends Y {\n" +
		"	                   ^^^^^^\n" +
		"extnds cannot be resolved to a type\n" +
		"----------\n" +
		"4. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extnds String, T2> extends Y {\n" +
		"	                          ^^^^^^\n" +
		"Syntax error on token \"String\", delete this token\n" +
		"----------\n" +
		"5. ERROR in X.java (at line 3)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error, insert \"}\" to complete ClassBody\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extnds String, T2> extends Y {\n" +
		"	                   ^^^^^^\n" +
		"Syntax error on token \"extnds\", extends expected\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extnds String, T2> extends Y {\n" +
		"	                   ^^^^^^\n" +
		"extnds cannot be resolved to a type\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0035() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X <T1 extends for, T2> extends Y {\n" +
		"	\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extends for, T2> extends Y {\n" +
		"	               ^^^^^^^^^^^^^^^^^^^^\n" +
		"Syntax error on tokens, delete these tokens\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public class X <T1 extends for, T2> extends Y {\n" +
		"	                           ^^^\n" +
		"Syntax error on token \"for\", invalid ReferenceType\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0036() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	public <T1 extends String, T2> foo(){\n" +
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	public <T1 extends String, T2> foo(){\n" +
		"	        ^^^^^^^^^^^^^^^^^^^^^\n" +
		"Syntax error, type parameters are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	public <T1 extends String, T2> foo(){\n" +
		"	                               ^^^^^\n" +
		"Return type for the method is missing\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. WARNING in X.java (at line 2)\n" +
		"	public <T1 extends String, T2> foo(){\n" +
		"	                   ^^^^^^\n" +
		"The type parameter T1 should not be bounded by the final type String. Final types cannot be further extended\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	public <T1 extends String, T2> foo(){\n" +
		"	                               ^^^^^\n" +
		"Return type for the method is missing\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0037() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	public <T1 extnds String, T2> int foo(){\n" +
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	public <T1 extnds String, T2> int foo(){\n" +
		"	       ^^^\n" +
		"Syntax error on token(s), misplaced construct(s)\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	public <T1 extnds String, T2> int foo(){\n" +
		"	        ^^\n" +
		"T1 cannot be resolved to a type\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 2)\n" +
		"	public <T1 extnds String, T2> int foo(){\n" +
		"	                            ^\n" +
		"Syntax error on token \">\", ; expected\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	public <T1 extnds String, T2> int foo(){\n" +
		"	        ^^\n" +
		"T1 cannot be resolved to a type\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	public <T1 extnds String, T2> int foo(){\n" +
		"	           ^^^^^^\n" +
		"Syntax error on token \"extnds\", extends expected\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0038() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	public <T1 extends String T2> int foo(){\n" +
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	public <T1 extends String T2> int foo(){\n" +
		"	       ^^^^^^^^^^^\n" +
		"Syntax error on token(s), misplaced construct(s)\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	public <T1 extends String T2> int foo(){\n" +
		"	                            ^\n" +
		"Syntax error on token \">\", ; expected\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	public <T1 extends String T2> int foo(){\n" +
		"	                          ^^\n" +
		"Syntax error on token \"T2\", delete this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0039() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	Z Y1, Y2> var;\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	Z Y1, Y2> var;\n" +
		"	^\n" +
		"Z cannot be resolved to a type\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	Z Y1, Y2> var;\n" +
		"	        ^\n" +
		"Syntax error on token \">\", , expected\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	Z Y1, Y2> var;\n" +
		"	^\n" +
		"Z cannot be resolved to a type\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	Z Y1, Y2> var;\n" +
		"	        ^\n" +
		"Syntax error on token \">\", , expected\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0040() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	Z <Y1, Y2 var;\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	Z <Y1, Y2 var;\n" +
		"	  ^^^^^^^\n" +
		"Syntax error on token(s), misplaced construct(s)\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	Z <Y1, Y2 var;\n" +
		"	       ^^\n" +
		"Y2 cannot be resolved to a type\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	Z <Y1, Y2 var;\n" +
		"	       ^^\n" +
		"Syntax error, insert \">\" to complete ReferenceType1\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	Z <Y1, Y2 var;\n" +
		"	       ^^\n" +
		"Y2 cannot be resolved to a type\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0041() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	Z <Y1, for Y2> var;\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	Z <Y1, for Y2> var;\n" +
		"	  ^^^^^^^^^^^^\n" +
		"Syntax error on tokens, delete these tokens\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	Z <Y1, for Y2> var;\n" +
		"	       ^^^\n" +
		"Syntax error on token \"for\", delete this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0042() {
	String[] testFiles = new String[] {
		"X.java",
		"void ___eval() {\n" +
		"	new Runnable() {\n" +
		"		int ___run() throws Throwable {\n" +
		"			return blah;\n" +
		"		}\n" +
		"		private String blarg;\n" +
		"		public void run() {\n" +
		"		}\n" +
		"	};\n" +
		"}\n" +
		"public class X {\n" +
		"	private static int x;\n" +
		"	private String blah;\n" +
		"	public static void main(String[] args) {\n" +
		"	}\n" +
		"	public void hello() {\n" +
		"	}\n" +
		"	public boolean blah() {\n" +
		"		return false;\n" +
		"	}\n" +
		"	public void foo() {\n" +
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 7)\n" +
		"	public void run() {\n" +
		"		}\n" +
		"	};\n" +
		"}\n" +
		"	       ^^^^^^^^^^^^^^^^^^^^^^\n" +
		"Syntax error on tokens, delete these tokens\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	void ___eval() {\n" +
		"	^^^^\n" +
		"Syntax error on token \"void\", @ expected\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 1)\n" +
		"	void ___eval() {\n" +
		"	             ^\n" +
		"Syntax error on token \")\", delete this token\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 9)\n" +
		"	};\n" +
		"	^\n" +
		"Syntax error on token \"}\", { expected\n" +
		"----------\n" +
		"4. ERROR in X.java (at line 23)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error, insert \"}\" to complete ClassBody\n" +
		"----------\n" +
		"5. ERROR in X.java (at line 23)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error, insert \"}\" to complete MemberValue\n" +
		"----------\n" +
		"6. ERROR in X.java (at line 23)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error, insert \")\" to complete Modifiers\n" +
		"----------\n" +
		"7. ERROR in X.java (at line 23)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error, insert \"enum Identifier\" to complete EnumHeader\n" +
		"----------\n" +
		"8. ERROR in X.java (at line 23)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error, insert \"EnumBody\" to complete CompilationUnit\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=72942
 */
public void test0043() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"}\n" +
		"public static void foo(){}\n" +
		"\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error on token \"}\", delete this token\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 3)\n" +
		"	public static void foo(){}\n" +
		"	                         ^\n" +
		"Syntax error, insert \"}\" to complete ClassBody\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		expected13ProblemLog;

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=62472
 */
public void test0044() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	public <T> X(T t){\n" +
		"		System.out.println(t);\n" +
		"	}\n" +
		"	}\n" +
		"	public static void main(String[] args) {\n" +
		"		class Local extends X {\n" +
		"			Local() {\n" +
		"				<String>super(\"SUCCESS\");\n" +
		"			}\n" +
		"		}\n" +
		"		new Local();\n" +
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" + 
		"1. ERROR in X.java (at line 2)\n" + 
		"	public <T> X(T t){\n" + 
		"	        ^\n" + 
		"Syntax error, type parameters are only available if source level is 1.5 or greater\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 5)\n" + 
		"	}\n" + 
		"	^\n" + 
		"Syntax error on token \"}\", delete this token\n" + 
		"----------\n" + 
		"3. ERROR in X.java (at line 9)\n" + 
		"	<String>super(\"SUCCESS\");\n" + 
		"	 ^^^^^^\n" + 
		"Syntax error, type parameters are only available if source level is 1.5 or greater\n" + 
		"----------\n" + 
		"4. ERROR in X.java (at line 9)\n" + 
		"	<String>super(\"SUCCESS\");\n" + 
		"	 ^^^^^^\n" + 
		"Syntax error, parameterized types are only available if source level is 1.5 or greater\n" + 
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 5)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error on token \"}\", delete this token\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=62472
 */
public void test0045() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	public void foo(){\n" +
		"	}\n" +
		"	}\n" +
		"	public void bar() {\n" +
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 4)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error on token \"}\", delete this token\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		expected13ProblemLog;

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74519
 */
public void test0046() {
	String[] testFiles = new String[] {
		"X.java",
		"public @interface X {\n" +
		"	String annName();\n" +
		"}"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	public @interface X {\n" +
		"	                  ^\n" +
		"Syntax error, annotation declarations are only available if source level is 1.5 or greater\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog = "";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74519
 */
public void test0047() {
	String[] testFiles = new String[] {
		"A.java",
		"public @interface A {}",
		"X.java",
		"@A public class X {\n" +
		"}"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in A.java (at line 1)\n" +
		"	public @interface A {}\n" +
		"	                  ^\n" +
		"Syntax error, annotation declarations are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	@A public class X {\n" +
		"	^^\n" +
		"Syntax error, annotations are only available if source level is 1.5 or greater\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog = "";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0048() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	void foo(X ... arg[]){\n" +
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	void foo(X ... arg[]){\n" +
		"	         ^^^^^^^^^\n" +
		"Syntax error, varargs are only available if source level is 1.5 or greater\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	void foo(X ... arg[]){\n" +
		"	               ^^^\n" +
		"Extended dimensions are illegal for a variable argument\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0049() {
	String[] testFiles = new String[] {
		"X.java",
		"@interface MyAnn {\n" +
		"	String value1() default \"\";\n" +
		"	String value2();\n" +
		"}\n" +
		"class ZZZ {}		\n" +
		"public @MyAnn(\"\",\"\") class Test {		\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 1)\n" +
		"	@interface MyAnn {\n" +
		"	           ^^^^^\n" +
		"Syntax error, annotation declarations are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 6)\n" +
		"	public @MyAnn(\"\",\"\") class Test {		\n" +
		"	              ^^\n" +
		"Syntax error, insert \")\" to complete Modifier\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 6)\n" +
		"	public @MyAnn(\"\",\"\") class Test {		\n" +
		"	              ^^\n" +
		"The attribute value is undefined for the annotation type MyAnn\n" +
		"----------\n" +
		"4. ERROR in X.java (at line 6)\n" +
		"	public @MyAnn(\"\",\"\") class Test {		\n" +
		"	                           ^^^^\n" +
		"The public type Test must be defined in its own file\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 6)\n" +
		"	public @MyAnn(\"\",\"\") class Test {		\n" +
		"	              ^^\n" +
		"The attribute value is undefined for the annotation type MyAnn\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 6)\n" +
		"	public @MyAnn(\"\",\"\") class Test {		\n" +
		"	                ^\n" +
		"Syntax error on token \",\", < expected\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 6)\n" +
		"	public @MyAnn(\"\",\"\") class Test {		\n" +
		"	                           ^^^^\n" +
		"The public type Test must be defined in its own file\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0050() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	void foo(List<String>... args) {}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	void foo(List<String>... args) {}\n" +
		"	         ^^^^^^^^^^^^^^^^^^^^\n" +
		"Syntax error, varargs are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	void foo(List<String>... args) {}\n" +
		"	         ^^^^\n" +
		"List cannot be resolved to a type\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 2)\n" +
		"	void foo(List<String>... args) {}\n" +
		"	              ^^^^^^\n" +
		"Syntax error, parameterized types are only available if source level is 1.5 or greater\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	void foo(List<String>... args) {}\n" +
		"	         ^^^^\n" +
		"List cannot be resolved to a type\n" +
		"----------\n";
	String expected17ProblemLog = 		
		"----------\n" + 
		"1. ERROR in X.java (at line 2)\n" + 
		"	void foo(List<String>... args) {}\n" + 
		"	         ^^^^\n" + 
		"List cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. WARNING in X.java (at line 2)\n" + 
		"	void foo(List<String>... args) {}\n" + 
		"	                         ^^^^\n" + 
		"Type safety: Potential heap pollution via varargs parameter args\n" + 
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog,
		expected17ProblemLog
	);
}
public void test0051() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	void foo(java.util.List2<String>... args) {}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	void foo(java.util.List2<String>... args) {}\n" +
		"	         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
		"Syntax error, varargs are only available if source level is 1.5 or greater\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 2)\n" +
		"	void foo(java.util.List2<String>... args) {}\n" +
		"	         ^^^^^^^^^^^^^^^\n" +
		"java.util.List2 cannot be resolved to a type\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 2)\n" +
		"	void foo(java.util.List2<String>... args) {}\n" +
		"	                         ^^^^^^\n" +
		"Syntax error, parameterized types are only available if source level is 1.5 or greater\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" +
		"	void foo(java.util.List2<String>... args) {}\n" +
		"	         ^^^^^^^^^^^^^^^\n" +
		"java.util.List2 cannot be resolved to a type\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=154811
public void test0052() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	void foo1() {\n" +
		"		class Y  {\n" +
		"		}\n" +
		"		void foo2() {\n" +
		"		}\n" +
		"		class Z<T> { \n" +
		"		}\n" +
		"	}\n" +
		"} \n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 5)\n" +
		"	void foo2() {\n" +
		"	^^^^\n" +
		"Syntax error on token \"void\", new expected\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 7)\n" +
		"	class Z<T> { \n" +
		"	^^^^^\n" +
		"Syntax error on token \"class\", invalid AssignmentOperator\n" +
		"----------\n" +
		"3. ERROR in X.java (at line 7)\n" +
		"	class Z<T> { \n" +
		"	         ^\n" +
		"Syntax error on token \">\", ; expected\n" +
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		"----------\n" +
		"1. ERROR in X.java (at line 5)\n" +
		"	void foo2() {\n" +
		"	^^^^\n" +
		"Syntax error on token \"void\", new expected\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 6)\n" +
		"	}\n" +
		"	^\n" +
		"Syntax error, insert \";\" to complete Statement\n" +
		"----------\n";

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=42243
public void test0053() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	public static void main(String[] args) {\n" +
		"		assert true;\n" +
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" +
		"1. WARNING in X.java (at line 3)\n" +
		"	assert true;\n" +
		"	^^^^^^\n" +
		"\'assert\' should not be used as an identifier, since it is a reserved keyword from source level 1.4 on\n" +
		"----------\n" +
		"2. ERROR in X.java (at line 3)\n" +
		"	assert true;\n" +
		"	^^^^^^\n" +
		"Syntax error on token \"assert\", AssignmentOperator expected after this token\n" +
		"----------\n";
	String expected14ProblemLog =
		"";

	String expected15ProblemLog =
		expected14ProblemLog;

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
public void test0054() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	public static void main(String[] args) {\n" +
		"		try (int i = 0) {};\n" +
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
		"----------\n" + 
		"1. ERROR in X.java (at line 3)\n" + 
		"	try (int i = 0) {};\n" + 
		"	     ^^^^^^^^^\n" + 
		"Resource specification not allowed here for source level below 1.7\n" + 
		"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		expected14ProblemLog;

	String expected17ProblemLog = 
		"----------\n" + 
		"1. ERROR in X.java (at line 3)\n" + 
		"	try (int i = 0) {};\n" + 
		"	     ^^^\n" + 
		"The resource type int does not implement java.lang.AutoCloseable\n" + 
		"----------\n";
	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog,
		expected17ProblemLog
	);
}
// test that use of multi-catch is flagged accordingly
public void test0055() {
	if(this.complianceLevel >= ClassFileConstants.JDK1_7) {
		return;
	}
	String[] testFiles = new String[] {
		"X.java",
		"import java.io.*;\n" +
		"public class X {\n" +
		"	public static void main(String[] args) {\n" +
		"		try {\n" + 
		"			System.out.println();\n" + 
		"			Reader r = new FileReader(args[0]);\n" + 
		"			r.read();\n" + 
		"		} catch(IOException | RuntimeException e) {\n" + 
		"			e.printStackTrace();\n" + 
		"		}\n" + 
		"	}\n" +
		"}\n"
	};

	String expected13ProblemLog =
			"----------\n" + 
			"1. ERROR in X.java (at line 8)\n" + 
			"	} catch(IOException | RuntimeException e) {\n" + 
			"	        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
			"Multi-catch parameters are not allowed for source level below 1.7\n" + 
			"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		expected14ProblemLog;

	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog
	);
}
// rethrow should not be precisely computed in 1.6- 
public void test0056() {
	String[] testFiles = new String[] {
		"X.java",
		"public class X {\n" +
		"	public static void main(String[] args) {\n" + 
		"		try {\n" + 
		"			throw new DaughterOfFoo();\n"+
		"		} catch(Foo e) {\n" + 
		"			try {\n" +
		"				throw e;\n" +
		"			} catch (SonOfFoo e1) {\n" +
		"			 	e1.printStackTrace();\n" +
		"			} catch (Foo e1) {}\n" +
		"		}\n" + 
		"	}\n" + 
		"}\n"+
		"class Foo extends Exception {}\n"+
		"class SonOfFoo extends Foo {}\n"+
		"class DaughterOfFoo extends Foo {}\n"
	};

	String expected13ProblemLog =
			"----------\n" + 
			"1. WARNING in X.java (at line 14)\n" + 
			"	class Foo extends Exception {}\n" + 
			"	      ^^^\n" + 
			"The serializable class Foo does not declare a static final serialVersionUID field of type long\n" + 
			"----------\n" + 
			"2. WARNING in X.java (at line 15)\n" + 
			"	class SonOfFoo extends Foo {}\n" + 
			"	      ^^^^^^^^\n" + 
			"The serializable class SonOfFoo does not declare a static final serialVersionUID field of type long\n" + 
			"----------\n" + 
			"3. WARNING in X.java (at line 16)\n" + 
			"	class DaughterOfFoo extends Foo {}\n" + 
			"	      ^^^^^^^^^^^^^\n" + 
			"The serializable class DaughterOfFoo does not declare a static final serialVersionUID field of type long\n" + 
			"----------\n";
	String expected14ProblemLog =
		expected13ProblemLog;

	String expected15ProblemLog =
		expected14ProblemLog;

	String expected17ProblemLog = 
			"----------\n" + 
			"1. ERROR in X.java (at line 8)\n" + 
			"	} catch (SonOfFoo e1) {\n" + 
			"	         ^^^^^^^^\n" + 
			"Unreachable catch block for SonOfFoo. This exception is never thrown from the try statement body\n" + 
			"----------\n" + 
			"2. WARNING in X.java (at line 14)\n" + 
			"	class Foo extends Exception {}\n" + 
			"	      ^^^\n" + 
			"The serializable class Foo does not declare a static final serialVersionUID field of type long\n" + 
			"----------\n" + 
			"3. WARNING in X.java (at line 15)\n" + 
			"	class SonOfFoo extends Foo {}\n" + 
			"	      ^^^^^^^^\n" + 
			"The serializable class SonOfFoo does not declare a static final serialVersionUID field of type long\n" + 
			"----------\n" + 
			"4. WARNING in X.java (at line 16)\n" + 
			"	class DaughterOfFoo extends Foo {}\n" + 
			"	      ^^^^^^^^^^^^^\n" + 
			"The serializable class DaughterOfFoo does not declare a static final serialVersionUID field of type long\n" + 
			"----------\n";
	runComplianceParserTest(
		testFiles,
		expected13ProblemLog,
		expected14ProblemLog,
		expected15ProblemLog,
		expected17ProblemLog
	);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=383714
public void test0057() {
	if(this.complianceLevel >= ClassFileConstants.JDK1_8) {
		return;
	}
	String[] testFiles = new String[] {
		"X.java",
		"interface I {\n" +
		"  public void foo() default { System.out.println(); }\n" +
		"}\n"
	};

	String expectedProblemLog =
			"----------\n" + 
			"1. ERROR in X.java (at line 2)\n" + 
			"	public void foo() default { System.out.println(); }\n" + 
			"	                           ^^^^^^^^^^^^^^^^^^^^^^^\n" + 
			"Default methods are allowed only at source level 1.8 or above\n" + 
			"----------\n";

	runComplianceParserTest(
		testFiles,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog
	);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=383714
public void test0058() {
	if(this.complianceLevel >= ClassFileConstants.JDK1_8) {
		return;
	}
	String[] testFiles = new String[] {
		"X.java",
		"interface I {\n" +
		"  void foo(int p);\n" +
		"}\n" +
		"public class X {\n" +
		"  I i = System::exit;\n" +
		"}\n"
	};

	String expectedProblemLog =
			"----------\n" + 
			"1. ERROR in X.java (at line 5)\n" + 
			"	I i = System::exit;\n" + 
			"	      ^^^^^^^^^^^^\n" + 
			"Method references are allowed only at source level 1.8 or above\n" + 
			"----------\n";

	runComplianceParserTest(
		testFiles,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog
	);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=383714
public void test0059() {
	if(this.complianceLevel >= ClassFileConstants.JDK1_8) {
		return;
	}
	String[] testFiles = new String[] {
		"X.java",
		"interface I {\n" +
		"  void foo(int p);\n" +
		"}\n" +
		"class Y {\n" +
		"   static void goo(int x) {\n" +
		"   }\n" +
		"}\n" +
		"public class X extends Y {\n" +
		"  I i = super::goo;\n" +
		"}\n"
	};

	String expectedProblemLog =
			"----------\n" + 
			"1. ERROR in X.java (at line 9)\n" + 
			"	I i = super::goo;\n" + 
			"	      ^^^^^^^^^^\n" + 
			"Method references are allowed only at source level 1.8 or above\n" + 
			"----------\n";

	runComplianceParserTest(
		testFiles,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog
	);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=383714
public void test0060() {
	if(this.complianceLevel >= ClassFileConstants.JDK1_8) {
		return;
	}
	String[] testFiles = new String[] {
		"X.java",
		"interface I {\n" +
		"  void foo(int p);\n" +
		"}\n" +
		"class Y {\n" +
		"   void goo(int x) {\n" +
		"   }\n" +
		"}\n" +
		"public class X extends Y {\n" +
		"  I i = new Y()::goo;\n" +
		"}\n"
	};

	String expectedProblemLog =
			"----------\n" + 
			"1. ERROR in X.java (at line 9)\n" + 
			"	I i = new Y()::goo;\n" + 
			"	      ^^^^^^^^^^^^\n" + 
			"Method references are allowed only at source level 1.8 or above\n" + 
			"----------\n";

	runComplianceParserTest(
		testFiles,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog
	);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=383714
public void test0061() {
	if(this.complianceLevel >= ClassFileConstants.JDK1_8) {
		return;
	}
	String[] testFiles = new String[] {
		"X.java",
		"interface I {\n" +
		"  void foo(int p);\n" +
		"}\n" +
		"class Y {\n" +
		"   void goo(int x) {\n" +
		"   }\n" +
		"   Y() {}\n" +
		"   Y(int x) {}\n" +
		"}\n" +
		"public class X extends Y {\n" +
		"  I i = Y::new;\n" +
		"}\n"
	};

	String expectedProblemLog =
			"----------\n" + 
			"1. ERROR in X.java (at line 11)\n" + 
			"	I i = Y::new;\n" + 
			"	      ^^^^^^^\n" + 
			"Constructor references are allowed only at source level 1.8 or above\n" + 
			"----------\n";

	runComplianceParserTest(
		testFiles,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog
	);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=383714
public void test0062() {
	if(this.complianceLevel >= ClassFileConstants.JDK1_8) {
		return;
	}
	String[] testFiles = new String[] {
		"X.java",
		"interface I {\n" +
		"  int foo(int p);\n" +
		"}\n" +
		"public class X {\n" +
		"  I i = p -> 10 + 20 + 30;\n" +
		"}\n"
	};

	String expectedProblemLog =
			"----------\n" + 
			"1. ERROR in X.java (at line 5)\n" + 
			"	I i = p -> 10 + 20 + 30;\n" + 
			"	      ^^^^^^^^^^^^^^^^^\n" + 
			"Lambda expressions are allowed only at source level 1.8 or above\n" + 
			"----------\n";

	runComplianceParserTest(
		testFiles,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog
	);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=381358
public void test0063() {
	if (this.complianceLevel <= ClassFileConstants.JDK1_4 || this.complianceLevel >= ClassFileConstants.JDK1_8) {
		return;
	}
	String[] testFiles = new String[] {
		"X.java",
		"interface I {\n" +
		"  int foo(int p);\n" +
		"}\n" +
		"public class X<T> {\n" +
		"  I i = X<String>::foo;\n" +
		"  I i2 = (p) -> 10;\n" +
		"  public static int foo(int p) {\n" +
		"	return p;\n" +
		"  }\n" +
		"}\n"
	};

	String expectedProblemLog =
			"----------\n" + 
			"1. ERROR in X.java (at line 5)\n" + 
			"	I i = X<String>::foo;\n" + 
			"	      ^^^^^^^^^^^^^^\n" + 
			"Method references are allowed only at source level 1.8 or above\n" + 
			"----------\n" + 
			"2. ERROR in X.java (at line 6)\n" + 
			"	I i2 = (p) -> 10;\n" + 
			"	       ^^^^^^^^^\n" + 
			"Lambda expressions are allowed only at source level 1.8 or above\n" + 
				"----------\n";

	runComplianceParserTest(
		testFiles,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog,
		expectedProblemLog
	);
}
// https://bugs.eclipse.org/bugs/show_bug.cgi?id=383913#c22
public void test0064() {
	if (this.complianceLevel >= ClassFileConstants.JDK1_8) {
		return;
	}
	String[] source = new String[] {
		"X.java",
		"class X {\n" +
		"	void foo(X this){}\n" +
		"}"
	};
	String expectedProblemLog =
			"----------\n" +
			"1. ERROR in X.java (at line 2)\n" +
			"	void foo(X this){}\n" +
			"	           ^^^^\n" +
			"Explicit declaration of 'this' parameter is allowed only at source level 1.8 or above\n" +
			"----------\n";
	runComplianceParserTest(
			source,
			expectedProblemLog,
			expectedProblemLog,
			expectedProblemLog,
			expectedProblemLog,
			expectedProblemLog
		);
}
}
