/*******************************************************************************
 * Copyright (c) 2007, 2015 BEA Systems, Inc. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    wharley@bea.com - initial API and implementation
 *    IBM Corporation - fix for 342598
 *    IBM Corporation - Java 8 support
 *    het@google.com - Bug 427943 - The method org.eclipse.jdt.internal.compiler.apt.model.Factory.getPrimitiveType does not throw IllegalArgumentException
 *******************************************************************************/

package org.eclipse.jdt.internal.compiler.apt.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.apt.dispatch.BaseProcessingEnvImpl;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.lookup.AnnotationBinding;
import org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.jdt.internal.compiler.lookup.BaseTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.ElementValuePair;
import org.eclipse.jdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.eclipse.jdt.internal.compiler.lookup.TypeVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.WildcardBinding;

/**
 * Creates javax.lang.model wrappers around JDT internal compiler bindings.
 */
public class Factory {
	
	// using auto-boxing to take advantage of caching, if any.
	// the dummy value picked here falls within the caching range.
	public static final Byte DUMMY_BYTE = 0; 
	public static final Character DUMMY_CHAR = '0'; 
	public static final Double DUMMY_DOUBLE = 0d;
	public static final Float DUMMY_FLOAT = 0f;
	public static final Integer DUMMY_INTEGER = 0;  
	public static final Long DUMMY_LONG = 0l;
	public static final Short DUMMY_SHORT = 0;

	private final BaseProcessingEnvImpl _env;
	public static List<? extends AnnotationMirror> EMPTY_ANNOTATION_MIRRORS = Collections.emptyList();
	
	/**
	 * This object should only be constructed by the BaseProcessingEnvImpl.
	 */
	public Factory(BaseProcessingEnvImpl env) {
		_env = env;
	}

	/**
	 * Convert an array of compiler annotation bindings into a list of AnnotationMirror
	 * @return a non-null, possibly empty, unmodifiable list.
	 */
	public List<? extends AnnotationMirror> getAnnotationMirrors(AnnotationBinding[] annotations) {
		if (null == annotations || 0 == annotations.length) {
			return Collections.emptyList();
		}
		List<AnnotationMirror> list = new ArrayList<>(annotations.length);
		for (AnnotationBinding annotation : annotations) {
			if (annotation == null) continue;
			list.add(newAnnotationMirror(annotation));
		}
		return Collections.unmodifiableList(list);
	}
	
	@SuppressWarnings("unchecked") // for the cast to A
	public <A extends Annotation> A[] getAnnotationsByType(AnnotationBinding[] annoInstances, Class<A> annotationClass) {
		A[] result = getAnnotations(annoInstances, annotationClass, false);
		return result == null ? (A[]) Array.newInstance(annotationClass, 0) : result;
	}
	
	
	public <A extends Annotation> A getAnnotation(AnnotationBinding[] annoInstances, Class<A> annotationClass) {
		A[] result = getAnnotations(annoInstances, annotationClass, true);
		return result == null ? null : result[0];
	}

	@SuppressWarnings("unchecked") // for cast of newProxyInstance() to A
	private <A extends Annotation> A[] getAnnotations(AnnotationBinding[] annoInstances, Class<A> annotationClass, boolean justTheFirst) {
		if(annoInstances == null || annoInstances.length == 0 || annotationClass == null ) 
			return null;

		String annoTypeName = annotationClass.getName();
		if(annoTypeName == null ) return null;

		List<A> list = new ArrayList<>(annoInstances.length);
		for(AnnotationBinding annoInstance : annoInstances) {
			if (annoInstance == null)
				continue;
			
			AnnotationMirrorImpl annoMirror = createAnnotationMirror(annoTypeName, annoInstance);
			if (annoMirror != null) {
				list.add((A)Proxy.newProxyInstance(annotationClass.getClassLoader(), new Class[]{ annotationClass }, annoMirror));
				if (justTheFirst) break;
			}
		}
		A [] result = (A[]) Array.newInstance(annotationClass, list.size());
		return list.size() > 0 ? (A[]) list.toArray(result) :  null;
	}

	private AnnotationMirrorImpl createAnnotationMirror(String annoTypeName, AnnotationBinding annoInstance) {
		ReferenceBinding binding = annoInstance.getAnnotationType();
		if (binding != null && binding.isAnnotationType() ) {
			char[] qName;
			if (binding.isMemberType()) {
				annoTypeName = annoTypeName.replace('$', '.');
				qName = CharOperation.concatWith(binding.enclosingType().compoundName, binding.sourceName, '.');
				CharOperation.replace(qName, '$', '.');
			} else {
				qName = CharOperation.concatWith(binding.compoundName, '.');
			}
			if(annoTypeName.equals(new String(qName)) ){
				return (AnnotationMirrorImpl)_env.getFactory().newAnnotationMirror(annoInstance);
			}
		}
		return null;
	}

	private static void appendModifier(Set<Modifier> result, int modifiers, int modifierConstant, Modifier modifier) {
		if ((modifiers & modifierConstant) != 0) {
			result.add(modifier);
		}
	}
	
	private static void decodeModifiers(Set<Modifier> result, int modifiers, int[] checkBits) {
		if (checkBits == null) return;
		for (int i = 0, max = checkBits.length; i < max; i++) {
			switch(checkBits[i]) {
				case ClassFileConstants.AccPublic :
					appendModifier(result, modifiers, checkBits[i], Modifier.PUBLIC);
					break;
				case ClassFileConstants.AccProtected:
					appendModifier(result, modifiers, checkBits[i], Modifier.PROTECTED);
					break;
				case ClassFileConstants.AccPrivate :
					appendModifier(result, modifiers, checkBits[i], Modifier.PRIVATE);
					break;
				case ClassFileConstants.AccAbstract :
					appendModifier(result, modifiers, checkBits[i], Modifier.ABSTRACT);
					break;
				case ExtraCompilerModifiers.AccDefaultMethod :
					try {
						appendModifier(result, modifiers, checkBits[i], Modifier.valueOf("DEFAULT")); //$NON-NLS-1$
					} catch(IllegalArgumentException iae) {
						// Don't have JDK 1.8, just ignore and proceed.
					}
					break;
				case ClassFileConstants.AccStatic :
					appendModifier(result, modifiers, checkBits[i], Modifier.STATIC);
					break;
				case ClassFileConstants.AccFinal :
					appendModifier(result, modifiers, checkBits[i], Modifier.FINAL);
					break;
				case ClassFileConstants.AccSynchronized :
					appendModifier(result, modifiers, checkBits[i], Modifier.SYNCHRONIZED);
					break;
				case ClassFileConstants.AccNative :
					appendModifier(result, modifiers, checkBits[i], Modifier.NATIVE);
					break;
				case ClassFileConstants.AccStrictfp :
					appendModifier(result, modifiers, checkBits[i], Modifier.STRICTFP);
					break;
				case ClassFileConstants.AccTransient :
					appendModifier(result, modifiers, checkBits[i], Modifier.TRANSIENT);
					break;
				case ClassFileConstants.AccVolatile :
					appendModifier(result, modifiers, checkBits[i], Modifier.VOLATILE);
					break;
			}
		}
	}
	
    public static Object getMatchingDummyValue(final Class<?> expectedType){
    	if( expectedType.isPrimitive() ){
    		if(expectedType == boolean.class)
    			return Boolean.FALSE;
    		else if( expectedType == byte.class )
    			return DUMMY_BYTE;
    		else if( expectedType == char.class )
    			return DUMMY_CHAR;
    		else if( expectedType == double.class)
    			return DUMMY_DOUBLE;
    		else if( expectedType == float.class )
    			return DUMMY_FLOAT;
    		else if( expectedType == int.class )
    			return DUMMY_INTEGER;
    		else if( expectedType == long.class )
    			return DUMMY_LONG;
    		else if(expectedType == short.class)
    			return DUMMY_SHORT;
    		else // expectedType == void.class. can this happen?
    			return DUMMY_INTEGER; // anything would work
    	}
    	else
    		return null;
    }
    
	public TypeMirror getReceiverType(MethodBinding binding) {
		if (binding != null) {
			if (binding.receiver != null) {
				return _env.getFactory().newTypeMirror(binding.receiver);
			}
			if (binding.declaringClass != null) {
				if (!binding.isStatic() && (!binding.isConstructor() || binding.declaringClass.isMemberType())) {
					return _env.getFactory().newTypeMirror(binding.declaringClass);	
				}
			}
		}
		return NoTypeImpl.NO_TYPE_NONE;
	}
    
	public static Set<Modifier> getModifiers(int modifiers, ElementKind kind) {
		return getModifiers(modifiers, kind, false);
	}
	/**
	 * Convert from the JDT's ClassFileConstants flags to the Modifier enum.
	 */
	public static Set<Modifier> getModifiers(int modifiers, ElementKind kind, boolean isFromBinary)
	{
		EnumSet<Modifier> result = EnumSet.noneOf(Modifier.class);
		switch(kind) {
			case CONSTRUCTOR :
			case METHOD :
				// modifiers for methods
				decodeModifiers(result, modifiers, new int[] {
					ClassFileConstants.AccPublic,
					ClassFileConstants.AccProtected,
					ClassFileConstants.AccPrivate,
					ClassFileConstants.AccAbstract,
					ClassFileConstants.AccStatic,
					ClassFileConstants.AccFinal,
					ClassFileConstants.AccSynchronized,
					ClassFileConstants.AccNative,
					ClassFileConstants.AccStrictfp,
					ExtraCompilerModifiers.AccDefaultMethod
				});
				break;
			case FIELD :
			case ENUM_CONSTANT :
				// for fields
				decodeModifiers(result, modifiers, new int[] {
					ClassFileConstants.AccPublic,
					ClassFileConstants.AccProtected,
					ClassFileConstants.AccPrivate,
					ClassFileConstants.AccStatic,
					ClassFileConstants.AccFinal,
					ClassFileConstants.AccTransient,
					ClassFileConstants.AccVolatile
				});
				break;
			case ENUM :
				if (isFromBinary) {
					decodeModifiers(result, modifiers, new int[] {
						ClassFileConstants.AccPublic,
						ClassFileConstants.AccProtected,
						ClassFileConstants.AccFinal,
						ClassFileConstants.AccPrivate,
						ClassFileConstants.AccAbstract,
						ClassFileConstants.AccStatic,
						ClassFileConstants.AccStrictfp
					});
				} else {
					// enum from source cannot be explicitly abstract
					decodeModifiers(result, modifiers, new int[] {
						ClassFileConstants.AccPublic,
						ClassFileConstants.AccProtected,
						ClassFileConstants.AccFinal,
						ClassFileConstants.AccPrivate,
						ClassFileConstants.AccStatic,
						ClassFileConstants.AccStrictfp
					});
				}
				break;
			case ANNOTATION_TYPE :
			case INTERFACE :
			case CLASS :
				// for type
				decodeModifiers(result, modifiers, new int[] {
					ClassFileConstants.AccPublic,
					ClassFileConstants.AccProtected,
					ClassFileConstants.AccAbstract,
					ClassFileConstants.AccFinal,
					ClassFileConstants.AccPrivate,
					ClassFileConstants.AccStatic,
					ClassFileConstants.AccStrictfp
				});
				break;
			default:
				break;
		}
		return Collections.unmodifiableSet(result);
	}

	public AnnotationMirror newAnnotationMirror(AnnotationBinding binding)
	{
		return new AnnotationMirrorImpl(_env, binding);
	}
	
	/**
	 * Create a new element that knows what kind it is even if the binding is unresolved.
	 */
	public Element newElement(Binding binding, ElementKind kindHint) {
		if (binding == null)
			return null;
		switch (binding.kind()) {
		case Binding.FIELD:
		case Binding.LOCAL:
		case Binding.VARIABLE:
			return new VariableElementImpl(_env, (VariableBinding) binding);
		case Binding.TYPE:
		case Binding.GENERIC_TYPE:
			ReferenceBinding referenceBinding = (ReferenceBinding)binding;
			if ((referenceBinding.tagBits & TagBits.HasMissingType) != 0) {
				return new ErrorTypeElement(this._env, referenceBinding);
			}
			if (CharOperation.equals(referenceBinding.sourceName, TypeConstants.PACKAGE_INFO_NAME)) {
				return new PackageElementImpl(_env, referenceBinding.fPackage);
			}
			return new TypeElementImpl(_env, referenceBinding, kindHint);
		case Binding.METHOD:
			return new ExecutableElementImpl(_env, (MethodBinding)binding);
		case Binding.RAW_TYPE:
		case Binding.PARAMETERIZED_TYPE:
			return new TypeElementImpl(_env, ((ParameterizedTypeBinding)binding).genericType(), kindHint);
		case Binding.PACKAGE:
			return new PackageElementImpl(_env, (PackageBinding)binding);
		case Binding.TYPE_PARAMETER:
			return new TypeParameterElementImpl(_env, (TypeVariableBinding)binding);
			// TODO: fill in the rest of these
		case Binding.IMPORT:
		case Binding.ARRAY_TYPE:
		case Binding.BASE_TYPE:
		case Binding.WILDCARD_TYPE:
		case Binding.INTERSECTION_TYPE:
			throw new UnsupportedOperationException("NYI: binding type " + binding.kind()); //$NON-NLS-1$
		}
		return null;
	}
	
	public Element newElement(Binding binding) {
		return newElement(binding, null);
	}

	/**
	 * Convenience method - equivalent to {@code (PackageElement)Factory.newElement(binding)}
	 */
	public PackageElement newPackageElement(PackageBinding binding)
	{
		return new PackageElementImpl(_env, binding);
	}
	
	public NullType getNullType() {
		return NoTypeImpl.NULL_TYPE;
	}

	public NoType getNoType(TypeKind kind)
	{
		switch (kind) {
		case NONE:
			return NoTypeImpl.NO_TYPE_NONE;
		case VOID:
			return NoTypeImpl.NO_TYPE_VOID;
		case PACKAGE:
			return NoTypeImpl.NO_TYPE_PACKAGE;
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Get a type mirror object representing the specified primitive type kind.
	 * @throw IllegalArgumentException if a non-primitive TypeKind is requested
	 */
	public PrimitiveTypeImpl getPrimitiveType(TypeKind kind)
	{
		switch (kind) {
		case BOOLEAN:
			return PrimitiveTypeImpl.BOOLEAN;
		case BYTE:
			return PrimitiveTypeImpl.BYTE;
		case CHAR:
			return PrimitiveTypeImpl.CHAR;
		case DOUBLE:
			return PrimitiveTypeImpl.DOUBLE;
		case FLOAT:
			return PrimitiveTypeImpl.FLOAT;
		case INT:
			return PrimitiveTypeImpl.INT;
		case LONG:
			return PrimitiveTypeImpl.LONG;
		case SHORT:
			return PrimitiveTypeImpl.SHORT;
		default:
			throw new IllegalArgumentException();
		}
	}
	
	public PrimitiveTypeImpl getPrimitiveType(BaseTypeBinding binding) {
		AnnotationBinding[] annotations = binding.getTypeAnnotations();
		if (annotations == null || annotations.length == 0) {
			return getPrimitiveType(PrimitiveTypeImpl.getKind(binding));
		}
		return new PrimitiveTypeImpl(_env, binding);
	}

	/**
	 * Given a binding of uncertain type, try to create the right sort of TypeMirror for it.
	 */
	public TypeMirror newTypeMirror(Binding binding) {
		switch (binding.kind()) {
		case Binding.FIELD:
		case Binding.LOCAL:
		case Binding.VARIABLE:
			// For variables, return the type of the variable
			return newTypeMirror(((VariableBinding)binding).type);
			
		case Binding.PACKAGE:
			return getNoType(TypeKind.PACKAGE);
			
		case Binding.IMPORT:
			throw new UnsupportedOperationException("NYI: import type " + binding.kind()); //$NON-NLS-1$

		case Binding.METHOD:
			return new ExecutableTypeImpl(_env, (MethodBinding) binding);
			
		case Binding.TYPE:
		case Binding.RAW_TYPE:
		case Binding.GENERIC_TYPE:
		case Binding.PARAMETERIZED_TYPE:
			ReferenceBinding referenceBinding = (ReferenceBinding) binding;
			if ((referenceBinding.tagBits & TagBits.HasMissingType) != 0) {
				return getErrorType(referenceBinding);
			}
			return new DeclaredTypeImpl(_env, (ReferenceBinding)binding);
			
		case Binding.ARRAY_TYPE:
			return new ArrayTypeImpl(_env, (ArrayBinding)binding);
			
		case Binding.BASE_TYPE:
			BaseTypeBinding btb = (BaseTypeBinding)binding;
			switch (btb.id) {
				case TypeIds.T_void:
					return getNoType(TypeKind.VOID);
				case TypeIds.T_null:
					return getNullType();
				default:
					return getPrimitiveType(btb);
			}

		case Binding.WILDCARD_TYPE:
		case Binding.INTERSECTION_TYPE: // TODO compatible, but shouldn't it really be an intersection type?
			return new WildcardTypeImpl(_env, (WildcardBinding) binding);

		case Binding.TYPE_PARAMETER:
			return new TypeVariableImpl(_env, (TypeVariableBinding) binding);
		}
		return null;
	}

	/**
	 * @param declaringElement the class, method, etc. that is parameterized by this parameter.
	 */
	public TypeParameterElement newTypeParameterElement(TypeVariableBinding variable, Element declaringElement)
	{
		return new TypeParameterElementImpl(_env, variable, declaringElement);
	}

    public ErrorType getErrorType(ReferenceBinding binding) {
		return new ErrorTypeImpl(this._env, binding);
	}

	/**
     * This method is derived from code in org.eclipse.jdt.apt.core.
     * 
     * This method is designed to be invoked by the invocation handler and anywhere that requires
     * a AnnotationValue (AnnotationMirror member values and default values from annotation member).
     * 
     * Regardless of the path, there are common primitive type conversion that needs to take place. 
     * The type conversions respect the type widening and narrowing rules from JLS 5.1.2 and 5.1.2.
     * 
     * The only question remains is what is the type of the return value when the type conversion fails?
     * When <code>avoidReflectException</code> is set to <code>true</code> 
     * Return <code>false</code> if the expected type is <code>boolean</code>
     * Return numeric 0 for all numeric primitive types and '0' for <code>char</code>
     * 
     * Otherwise:
     * Return the value unchanged. 
     *  
     * In the invocation handler case: 
     * The value returned by {@link java.lang.reflect.InvocationHandler#invoke} 
     * will be converted into the expected type by the {@link java.lang.reflect.Proxy}. 
     * If the value and the expected type does not agree, and the value is not null, 
     * a ClassCastException will be thrown. A NullPointerException will result if the 
     * expected type is a primitive type and the value is null.
     * This behavior causes annotation processors a lot of pain and the decision is
     * to not throw such unchecked exception. In the case where a ClassCastException or 
     * NullPointerException will be thrown return some dummy value. Otherwise, return 
     * the original value.
     * Chosen dummy values:  
     * Return <code>false</code> if the expected type is <code>boolean</code>
     * Return numeric 0 for all numeric primitive types and '0' for <code>char</code>
     * 
     * This behavior is triggered by setting <code>avoidReflectException</code> to <code>true</code>
     * 
     * Note: the new behavior deviates from what's documented in
     * {@link java.lang.reflect.InvocationHandler#invoke} and also deviates from 
     * Sun's implementation.
     *
     * @param value the current value from the annotation instance.
     * @param expectedType the expected type of the value.
     * 
     */
    public static Object performNecessaryPrimitiveTypeConversion(
    		final Class<?> expectedType,
    		final Object value,
    		final boolean avoidReflectException)
    {
    	assert expectedType.isPrimitive() : "expectedType is not a primitive type: " + expectedType.getName(); //$NON-NLS-1$
    	if( value == null)
    		return avoidReflectException ? getMatchingDummyValue(expectedType) : null;
    	// apply widening conversion based on JLS 5.1.2 and 5.1.3
    	final String typeName = expectedType.getName();
		final char expectedTypeChar = typeName.charAt(0);
		final int nameLen = typeName.length();
		// widening byte -> short, int, long, float or double
		// narrowing byte -> char
		if( value instanceof Byte )
		{
			final byte b = ((Byte)value).byteValue();
			switch( expectedTypeChar )
			{
			case 'b':
				if(nameLen == 4) // byte
					return value; // exact match.
				else 
					return avoidReflectException ? Boolean.FALSE : value;
			case 'c':
				return new Character((char)b); // narrowing.
			case 'd':
				return new Double(b); // widening.
			case 'f':
				return new Float(b); // widening.
			case 'i':
				return new Integer(b); // widening.
			case 'l':
				return new Long(b); // widening.
			case 's':
				return new Short(b); // widening.
			default:  				
				throw new IllegalStateException("unknown type " + expectedTypeChar); //$NON-NLS-1$
			}
		}
		// widening short -> int, long, float, or double 
		// narrowing short -> byte or char
		else if( value instanceof Short )
		{
			final short s = ((Short)value).shortValue();
			switch( expectedTypeChar )
			{
			case 'b':
				if(nameLen == 4) // byte
					return new Byte((byte)s); // narrowing.
				else
					return avoidReflectException ? Boolean.FALSE : value; // completely wrong.
			case 'c':
				return new Character((char)s); // narrowing.
			case 'd':
				return new Double(s); // widening.
			case 'f':
				return new Float(s); // widening.
			case 'i':
				return new Integer(s); // widening.
			case 'l':
				return new Long(s); // widening.
			case 's':
				return value; // exact match
			default:  				
				throw new IllegalStateException("unknown type " + expectedTypeChar); //$NON-NLS-1$
			}
		}
		// widening char -> int, long, float, or double 
		// narrowing char -> byte or short
		else if( value instanceof Character )
		{
			final char c = ((Character)value).charValue();
			switch( expectedTypeChar )
			{
			case 'b':
				if(nameLen == 4) // byte
					return new Byte((byte)c); // narrowing.
				else
					return avoidReflectException ? Boolean.FALSE : value; // completely wrong.
			case 'c':
				return value; // exact match
			case 'd':
				return new Double(c); // widening.
			case 'f':
				return new Float(c); // widening.
			case 'i':
				return new Integer(c); // widening.
			case 'l':
				return new Long(c); // widening.
			case 's':
				return new Short((short)c); // narrowing.
			default:  				
				throw new IllegalStateException("unknown type " + expectedTypeChar); //$NON-NLS-1$
			}
		}
		
		// widening int -> long, float, or double 
		// narrowing int -> byte, short, or char 
		else if( value instanceof Integer )
		{
			final int i = ((Integer)value).intValue();
			switch( expectedTypeChar )
			{    
			case 'b':
				if(nameLen == 4) // byte
					return new Byte((byte)i); // narrowing.
				else
					return avoidReflectException ? Boolean.FALSE : value; // completely wrong.
			case 'c':
				return new Character((char)i); // narrowing
			case 'd':
				return new Double(i); // widening.
			case 'f':
				return new Float(i); // widening.
			case 'i':
				return value; // exact match
			case 'l':
				return new Long(i); // widening.
			case 's':
				return new Short((short)i); // narrowing.
			default:  				
				throw new IllegalStateException("unknown type " + expectedTypeChar); //$NON-NLS-1$
			}
		}
		// widening long -> float or double
		else if( value instanceof Long )
		{
			final long l = ((Long)value).longValue();
			switch( expectedTypeChar )
			{
			case 'b': // both byte and boolean
			case 'c': 
			case 'i':
			case 's':
				// completely wrong.
				return avoidReflectException ? getMatchingDummyValue(expectedType) : value;
			case 'd':
				return new Double(l); // widening.
			case 'f':
				return new Float(l); // widening.			
			case 'l': 
				return value; // exact match.
		
			default:  				
				throw new IllegalStateException("unknown type " + expectedTypeChar); //$NON-NLS-1$
			}
		}
		
		// widening float -> double    		 
		else if( value instanceof Float )
		{
			final float f = ((Float)value).floatValue();
			switch( expectedTypeChar )
			{    		
			case 'b': // both byte and boolean
			case 'c': 
			case 'i':
			case 's':
			case 'l':
				// completely wrong.
				return avoidReflectException ? getMatchingDummyValue(expectedType) : value;
			case 'd':
				return new Double(f); // widening.
			case 'f':
				return value; // exact match.
			default:  				
				throw new IllegalStateException("unknown type " + expectedTypeChar); //$NON-NLS-1$
			}
		}
		else if( value instanceof Double ){
			if(expectedTypeChar == 'd' )
				return value; // exact match
			else{
				return avoidReflectException ? getMatchingDummyValue(expectedType) : value; // completely wrong.
			}
		}
		else if( value instanceof Boolean ){
			if( expectedTypeChar == 'b' && nameLen == 7) // "boolean".length() == 7
				return value;
			else
				return avoidReflectException ? getMatchingDummyValue(expectedType) : value; // completely wrong.
		}
		else // can't convert
			return avoidReflectException ? getMatchingDummyValue(expectedType) : value;
    }

    /**
     * Set an element of an array to the appropriate dummy value type
     * @param array
     * @param i
     * @param expectedLeafType
     */
	public static void setArrayMatchingDummyValue(Object array, int i, Class<?> expectedLeafType)
	{
		if (boolean.class.equals(expectedLeafType)) {
			Array.setBoolean(array, i, false);
		}
		else if (byte.class.equals(expectedLeafType)) {
			Array.setByte(array, i, DUMMY_BYTE);
		}
		else if (char.class.equals(expectedLeafType)) {
			Array.setChar(array, i, DUMMY_CHAR);
		}
		else if (double.class.equals(expectedLeafType)) {
			Array.setDouble(array, i, DUMMY_DOUBLE);
		}
		else if (float.class.equals(expectedLeafType)) {
			Array.setFloat(array, i, DUMMY_FLOAT);
		}
		else if (int.class.equals(expectedLeafType)) {
			Array.setInt(array, i, DUMMY_INTEGER);
		}
		else if (long.class.equals(expectedLeafType)) {
			Array.setLong(array, i, DUMMY_LONG);
		}
		else if (short.class.equals(expectedLeafType)) {
			Array.setShort(array, i, DUMMY_SHORT);
		}
		else {
			Array.set(array, i, null);
		}
	}

	/* Wrap repeating annotations into their container, return an array of bindings.
	   Incoming array is not modified. The resulting array may be null but will not contain null
	   entries.
	*/
	public static AnnotationBinding [] getPackedAnnotationBindings(AnnotationBinding [] annotations) {
		
		int length = annotations == null ? 0 : annotations.length;
		if (length == 0)
			return annotations;
		
		AnnotationBinding[] repackagedBindings = annotations; // only replicate if repackaging.
		for (int i = 0; i < length; i++) {
			AnnotationBinding annotation = repackagedBindings[i];
			if (annotation == null) continue;
			ReferenceBinding annotationType = annotation.getAnnotationType();
			if (!annotationType.isRepeatableAnnotationType())
				continue;
			ReferenceBinding containerType = annotationType.containerAnnotationType();
			if (containerType == null)
				continue; // FUBAR.
			MethodBinding [] values = containerType.getMethods(TypeConstants.VALUE);
			if (values == null || values.length != 1)
				continue; // FUBAR.
			MethodBinding value = values[0];
			if (value.returnType == null || value.returnType.dimensions() != 1 || TypeBinding.notEquals(value.returnType.leafComponentType(), annotationType))
				continue; // FUBAR
			
			// We have a kosher repeatable annotation with a kosher containing type. See if actually repeats.
			List<AnnotationBinding> containees = null;
			for (int j = i + 1; j < length; j++) {
				AnnotationBinding otherAnnotation = repackagedBindings[j];
				if (otherAnnotation == null) continue;
				if (otherAnnotation.getAnnotationType() == annotationType) { //$IDENTITY-COMPARISON$
					if (repackagedBindings == annotations)
						System.arraycopy(repackagedBindings, 0, repackagedBindings = new AnnotationBinding[length], 0, length);
					repackagedBindings[j] = null; // so it is not double packed.
					if (containees == null) {
						containees = new ArrayList<>();
						containees.add(annotation);
					}
					containees.add(otherAnnotation);
				}
			}
			if (containees != null) {
				ElementValuePair [] elementValuePairs = new ElementValuePair [] { new ElementValuePair(TypeConstants.VALUE, containees.toArray(), value) };
				repackagedBindings[i] = new AnnotationBinding(containerType, elementValuePairs);
			}
		}

		int finalTally = 0;
		for (int i = 0; i < length; i++) {
			if (repackagedBindings[i] != null)
				finalTally++;
		}

		if (repackagedBindings == annotations && finalTally == length) {
			return annotations;
		}

		annotations = new AnnotationBinding [finalTally];
		for (int i = 0, j = 0; i < length; i++) {
			if (repackagedBindings[i] != null)
				annotations[j++] = repackagedBindings[i];
		}
		return annotations;
	}
	
	/* Unwrap container annotations into the repeated annotations, return an array of bindings that includes the container and the containees.
	*/
	public static AnnotationBinding [] getUnpackedAnnotationBindings(AnnotationBinding [] annotations) {
		
		int length = annotations == null ? 0 : annotations.length;
		if (length == 0)
			return annotations;
		
		List<AnnotationBinding> unpackedAnnotations = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			AnnotationBinding annotation = annotations[i];
			if (annotation == null) continue;
			unpackedAnnotations.add(annotation);
			ReferenceBinding annotationType = annotation.getAnnotationType();
			
			MethodBinding [] values = annotationType.getMethods(TypeConstants.VALUE);
			if (values == null || values.length != 1)
				continue;
			MethodBinding value = values[0];
			
			if (value.returnType.dimensions() != 1)
				continue;
			
			TypeBinding containeeType = value.returnType.leafComponentType();
			if (containeeType == null || !containeeType.isAnnotationType() || !containeeType.isRepeatableAnnotationType())
				continue;
			
			if (containeeType.containerAnnotationType() != annotationType) //$IDENTITY-COMPARISON$
				continue;
			
			// We have a kosher container: unwrap the contained annotations.
			ElementValuePair [] elementValuePairs = annotation.getElementValuePairs();
			for (ElementValuePair elementValuePair : elementValuePairs) {
				if (CharOperation.equals(elementValuePair.getName(), TypeConstants.VALUE)) {
					Object [] containees = (Object []) elementValuePair.getValue();
					for (Object object : containees) {
						unpackedAnnotations.add((AnnotationBinding) object);
					}
					break;
				}
			}
		}
		return (AnnotationBinding[]) unpackedAnnotations.toArray(new AnnotationBinding [unpackedAnnotations.size()]);
	}	
}
