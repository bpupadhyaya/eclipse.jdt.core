/*******************************************************************************
 * Copyright (c) 2000, 2001, 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

public abstract class NumberLiteral extends Literal {
	char[] source;
public NumberLiteral(char[] token, int s, int e) {
	this(s,e) ;
	source = token ;
}
public NumberLiteral(int s, int e) {
	super (s,e) ;
}
public boolean isValidJavaStatement(){
	//should never be reach, but with a bug in the ast tree....
	//see comment on the Statement class
	
	return false ;}
public char[] source(){
	return source;}
public String toStringExpression(){

	return new String(source);}
}
