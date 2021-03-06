package com.jsan.util;

/*
 * 复制于 org.springframework.util.Assert。
 * 
 */

import java.util.Collection;
import java.util.Map;

//import org.springframework.util.CollectionUtils;
//import org.springframework.util.ObjectUtils;
//import org.springframework.util.StringUtils;

/**
 * Assertion utility class that assists in validating arguments.
 *
 * <p>
 * Useful for identifying programmer errors early and clearly at runtime.
 *
 * <p>
 * For example, if the contract of a public method states it does not allow {@code null} arguments, {@code Assert} can be used to validate that contract. Doing this clearly indicates a contract violation when it occurs and protects the class's invariants.
 *
 * <p>
 * Typically used to validate method arguments rather than configuration properties, to check for cases that are usually programmer errors rather than configuration errors. In contrast to configuration initialization code, there is usually no point in falling back to defaults in such methods.
 *
 * <p>
 * This class is similar to JUnit's assertion library. If an argument value is deemed invalid, an {@link IllegalArgumentException} is thrown (typically). For example:
 *
 * <pre class="code">
 * Assert.notNull(clazz, &quot;The class must not be null&quot;);
 * Assert.isTrue(i &gt; 0, &quot;The value must be greater than zero&quot;);
 * </pre>
 *
 * <p>
 * Mainly for internal use within the framework; consider <a href="http://commons.apache.org/proper/commons-lang/">Apache's Commons Lang</a> for a more comprehensive suite of {@code String} utilities.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @author Colin Sampaleanu
 * @author Rob Harrop
 * @author Sam Brannen
 * @since 1.1.2
 */
public abstract class Assert {

	/**
	 * Assert a boolean expression, throwing {@code IllegalArgumentException} if the test result is {@code false}.
	 * 
	 * <pre class="code">
	 * Assert.isTrue(i &gt; 0, &quot;The value must be greater than zero&quot;);
	 * </pre>
	 * 
	 * @param expression
	 *            a boolean expression
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if expression is {@code false}
	 */
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Assert a boolean expression, throwing {@code IllegalArgumentException} if the test result is {@code false}.
	 * 
	 * <pre class="code">
	 * Assert.isTrue(i &gt; 0);
	 * </pre>
	 * 
	 * @param expression
	 *            a boolean expression
	 * @throws IllegalArgumentException
	 *             if expression is {@code false}
	 */
	public static void isTrue(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	/**
	 * Assert that an object is {@code null} .
	 * 
	 * <pre class="code">
	 * Assert.isNull(value, &quot;The value must be null&quot;);
	 * </pre>
	 * 
	 * @param object
	 *            the object to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the object is not {@code null}
	 */
	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Assert that an object is {@code null} .
	 * 
	 * <pre class="code">
	 * Assert.isNull(value);
	 * </pre>
	 * 
	 * @param object
	 *            the object to check
	 * @throws IllegalArgumentException
	 *             if the object is not {@code null}
	 */
	public static void isNull(Object object) {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	/**
	 * Assert that an object is not {@code null} .
	 * 
	 * <pre class="code">
	 * Assert.notNull(clazz, &quot;The class must not be null&quot;);
	 * </pre>
	 * 
	 * @param object
	 *            the object to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the object is {@code null}
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Assert that an object is not {@code null} .
	 * 
	 * <pre class="code">
	 * Assert.notNull(clazz);
	 * </pre>
	 * 
	 * @param object
	 *            the object to check
	 * @throws IllegalArgumentException
	 *             if the object is {@code null}
	 */
	public static void notNull(Object object) {
		notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}

	/**
	 * Assert that the given String is not empty; that is, it must not be {@code null} and not the empty String.
	 * 
	 * <pre class="code">
	 * Assert.hasLength(name, &quot;Name must not be empty&quot;);
	 * </pre>
	 * 
	 * @param text
	 *            the String to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @see StringUtils#hasLength
	 * @throws IllegalArgumentException
	 *             if the text is empty
	 */
	public static void hasLength(String text, String message) {
		if (!StringUtils_hasLength(text)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Assert that the given String is not empty; that is, it must not be {@code null} and not the empty String.
	 * 
	 * <pre class="code">
	 * Assert.hasLength(name);
	 * </pre>
	 * 
	 * @param text
	 *            the String to check
	 * @see StringUtils#hasLength
	 * @throws IllegalArgumentException
	 *             if the text is empty
	 */
	public static void hasLength(String text) {
		hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not be {@code null} and must contain at least one non-whitespace character.
	 * 
	 * <pre class="code">
	 * Assert.hasText(name, &quot;'name' must not be empty&quot;);
	 * </pre>
	 * 
	 * @param text
	 *            the String to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @see StringUtils#hasText
	 * @throws IllegalArgumentException
	 *             if the text does not contain valid text content
	 */
	public static void hasText(String text, String message) {
		if (!StringUtils_hasText(text)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not be {@code null} and must contain at least one non-whitespace character.
	 * 
	 * <pre class="code">
	 * Assert.hasText(name, &quot;'name' must not be empty&quot;);
	 * </pre>
	 * 
	 * @param text
	 *            the String to check
	 * @see StringUtils#hasText
	 * @throws IllegalArgumentException
	 *             if the text does not contain valid text content
	 */
	public static void hasText(String text) {
		hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}

	/**
	 * Assert that the given text does not contain the given substring.
	 * 
	 * <pre class="code">
	 * Assert.doesNotContain(name, &quot;rod&quot;, &quot;Name must not contain 'rod'&quot;);
	 * </pre>
	 * 
	 * @param textToSearch
	 *            the text to search
	 * @param substring
	 *            the substring to find within the text
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the text contains the substring
	 */
	public static void doesNotContain(String textToSearch, String substring, String message) {
		if (StringUtils_hasLength(textToSearch) && StringUtils_hasLength(substring) && textToSearch.contains(substring)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Assert that the given text does not contain the given substring.
	 * 
	 * <pre class="code">
	 * Assert.doesNotContain(name, &quot;rod&quot;);
	 * </pre>
	 * 
	 * @param textToSearch
	 *            the text to search
	 * @param substring
	 *            the substring to find within the text
	 * @throws IllegalArgumentException
	 *             if the text contains the substring
	 */
	public static void doesNotContain(String textToSearch, String substring) {
		doesNotContain(textToSearch, substring, "[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
	}

	/**
	 * Assert that an array has elements; that is, it must not be {@code null} and must have at least one element.
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(array, &quot;The array must have elements&quot;);
	 * </pre>
	 * 
	 * @param array
	 *            the array to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the object array is {@code null} or has no elements
	 */
	public static void notEmpty(Object[] array, String message) {
		if (ObjectUtils_isEmpty(array)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Assert that an array has elements; that is, it must not be {@code null} and must have at least one element.
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(array);
	 * </pre>
	 * 
	 * @param array
	 *            the array to check
	 * @throws IllegalArgumentException
	 *             if the object array is {@code null} or has no elements
	 */
	public static void notEmpty(Object[] array) {
		notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
	}

	/**
	 * Assert that an array has no null elements. Note: Does not complain if the array is empty!
	 * 
	 * <pre class="code">
	 * Assert.noNullElements(array, &quot;The array must have non-null elements&quot;);
	 * </pre>
	 * 
	 * @param array
	 *            the array to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the object array contains a {@code null} element
	 */
	public static void noNullElements(Object[] array, String message) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throw new IllegalArgumentException(message);
				}
			}
		}
	}

	/**
	 * Assert that an array has no null elements. Note: Does not complain if the array is empty!
	 * 
	 * <pre class="code">
	 * Assert.noNullElements(array);
	 * </pre>
	 * 
	 * @param array
	 *            the array to check
	 * @throws IllegalArgumentException
	 *             if the object array contains a {@code null} element
	 */
	public static void noNullElements(Object[] array) {
		noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
	}

	/**
	 * Assert that a collection has elements; that is, it must not be {@code null} and must have at least one element.
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(collection, &quot;Collection must have elements&quot;);
	 * </pre>
	 * 
	 * @param collection
	 *            the collection to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the collection is {@code null} or has no elements
	 */
	public static void notEmpty(Collection<?> collection, String message) {
		if (CollectionUtils_isEmpty(collection)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Assert that a collection has elements; that is, it must not be {@code null} and must have at least one element.
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(collection, &quot;Collection must have elements&quot;);
	 * </pre>
	 * 
	 * @param collection
	 *            the collection to check
	 * @throws IllegalArgumentException
	 *             if the collection is {@code null} or has no elements
	 */
	public static void notEmpty(Collection<?> collection) {
		notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
	}

	/**
	 * Assert that a Map has entries; that is, it must not be {@code null} and must have at least one entry.
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(map, &quot;Map must have entries&quot;);
	 * </pre>
	 * 
	 * @param map
	 *            the map to check
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the map is {@code null} or has no entries
	 */
	public static void notEmpty(Map<?, ?> map, String message) {
		if (CollectionUtils_isEmpty(map)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Assert that a Map has entries; that is, it must not be {@code null} and must have at least one entry.
	 * 
	 * <pre class="code">
	 * Assert.notEmpty(map);
	 * </pre>
	 * 
	 * @param map
	 *            the map to check
	 * @throws IllegalArgumentException
	 *             if the map is {@code null} or has no entries
	 */
	public static void notEmpty(Map<?, ?> map) {
		notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
	}

	/**
	 * Assert that the provided object is an instance of the provided class.
	 * 
	 * <pre class="code">
	 * Assert.instanceOf(Foo.class, foo);
	 * </pre>
	 * 
	 * @param clazz
	 *            the required class
	 * @param obj
	 *            the object to check
	 * @throws IllegalArgumentException
	 *             if the object is not an instance of clazz
	 * @see Class#isInstance
	 */
	public static void isInstanceOf(Class<?> clazz, Object obj) {
		isInstanceOf(clazz, obj, "");
	}

	/**
	 * Assert that the provided object is an instance of the provided class.
	 * 
	 * <pre class="code">
	 * Assert.instanceOf(Foo.class, foo);
	 * </pre>
	 * 
	 * @param type
	 *            the type to check against
	 * @param obj
	 *            the object to check
	 * @param message
	 *            a message which will be prepended to the message produced by the function itself, and which may be used to provide context. It should normally end in ":" or "." so that the generated message looks OK when appended to it.
	 * @throws IllegalArgumentException
	 *             if the object is not an instance of clazz
	 * @see Class#isInstance
	 */
	public static void isInstanceOf(Class<?> type, Object obj, String message) {
		notNull(type, "Type to check against must not be null");
		if (!type.isInstance(obj)) {
			throw new IllegalArgumentException((StringUtils_hasLength(message) ? message + " " : "") + "Object of class [" + (obj != null ? obj.getClass().getName() : "null") + "] must be an instance of " + type);
		}
	}

	/**
	 * Assert that {@code superType.isAssignableFrom(subType)} is {@code true}.
	 * 
	 * <pre class="code">
	 * Assert.isAssignable(Number.class, myClass);
	 * </pre>
	 * 
	 * @param superType
	 *            the super type to check
	 * @param subType
	 *            the sub type to check
	 * @throws IllegalArgumentException
	 *             if the classes are not assignable
	 */
	public static void isAssignable(Class<?> superType, Class<?> subType) {
		isAssignable(superType, subType, "");
	}

	/**
	 * Assert that {@code superType.isAssignableFrom(subType)} is {@code true}.
	 * 
	 * <pre class="code">
	 * Assert.isAssignable(Number.class, myClass);
	 * </pre>
	 * 
	 * @param superType
	 *            the super type to check against
	 * @param subType
	 *            the sub type to check
	 * @param message
	 *            a message which will be prepended to the message produced by the function itself, and which may be used to provide context. It should normally end in ":" or "." so that the generated message looks OK when appended to it.
	 * @throws IllegalArgumentException
	 *             if the classes are not assignable
	 */
	public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
		notNull(superType, "Type to check against must not be null");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			throw new IllegalArgumentException((StringUtils_hasLength(message) ? message + " " : "") + subType + " is not assignable to " + superType);
		}
	}

	/**
	 * Assert a boolean expression, throwing {@code IllegalStateException} if the test result is {@code false}. Call isTrue if you wish to throw IllegalArgumentException on an assertion failure.
	 * 
	 * <pre class="code">
	 * Assert.state(id == null, &quot;The id property must not already be initialized&quot;);
	 * </pre>
	 * 
	 * @param expression
	 *            a boolean expression
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws IllegalStateException
	 *             if expression is {@code false}
	 */
	public static void state(boolean expression, String message) {
		if (!expression) {
			throw new IllegalStateException(message);
		}
	}

	/**
	 * Assert a boolean expression, throwing {@link IllegalStateException} if the test result is {@code false}.
	 * <p>
	 * Call {@link #isTrue(boolean)} if you wish to throw {@link IllegalArgumentException} on an assertion failure.
	 * 
	 * <pre class="code">
	 * Assert.state(id == null);
	 * </pre>
	 * 
	 * @param expression
	 *            a boolean expression
	 * @throws IllegalStateException
	 *             if the supplied expression is {@code false}
	 */
	public static void state(boolean expression) {
		state(expression, "[Assertion failed] - this state invariant must be true");
	}

	// ==================================================

	/**
	 * Check that the given {@code CharSequence} is neither {@code null} nor of length 0.
	 * <p>
	 * Note: this method returns {@code true} for a {@code CharSequence} that purely consists of whitespace.
	 * <p>
	 * 
	 * <pre class="code">
	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength("") = false
	 * StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 * 
	 * @param str
	 *            the {@code CharSequence} to check (may be {@code null})
	 * @return {@code true} if the {@code CharSequence} is not {@code null} and has length
	 * @see #hasText(String)
	 */
	private static boolean StringUtils_hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * Check that the given {@code String} is neither {@code null} nor of length 0.
	 * <p>
	 * Note: this method returns {@code true} for a {@code String} that purely consists of whitespace.
	 * 
	 * @param str
	 *            the {@code String} to check (may be {@code null})
	 * @return {@code true} if the {@code String} is not {@code null} and has length
	 * @see #hasLength(CharSequence)
	 * @see #hasText(String)
	 */
	private static boolean StringUtils_hasLength(String str) {
		return StringUtils_hasLength((CharSequence) str);
	}

	/**
	 * Check whether the given {@code CharSequence} contains actual <em>text</em>.
	 * <p>
	 * More specifically, this method returns {@code true} if the {@code CharSequence} is not {@code null}, its length is greater than 0, and it contains at least one non-whitespace character.
	 * <p>
	 * 
	 * <pre class="code">
	 * StringUtils.hasText(null) = false
	 * StringUtils.hasText("") = false
	 * StringUtils.hasText(" ") = false
	 * StringUtils.hasText("12345") = true
	 * StringUtils.hasText(" 12345 ") = true
	 * </pre>
	 * 
	 * @param str
	 *            the {@code CharSequence} to check (may be {@code null})
	 * @return {@code true} if the {@code CharSequence} is not {@code null}, its length is greater than 0, and it does not contain whitespace only
	 * @see Character#isWhitespace
	 */
	private static boolean StringUtils_hasText(CharSequence str) {
		if (!StringUtils_hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given {@code String} contains actual <em>text</em>.
	 * <p>
	 * More specifically, this method returns {@code true} if the {@code String} is not {@code null}, its length is greater than 0, and it contains at least one non-whitespace character.
	 * 
	 * @param str
	 *            the {@code String} to check (may be {@code null})
	 * @return {@code true} if the {@code String} is not {@code null}, its length is greater than 0, and it does not contain whitespace only
	 * @see #hasText(CharSequence)
	 */
	private static boolean StringUtils_hasText(String str) {
		return StringUtils_hasText((CharSequence) str);
	}

	/**
	 * Determine whether the given array is empty: i.e. {@code null} or of zero length.
	 * 
	 * @param array
	 *            the array to check
	 * @see #isEmpty(Object)
	 */
	private static boolean ObjectUtils_isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}

	/**
	 * Return {@code true} if the supplied Collection is {@code null} or empty. Otherwise, return {@code false}.
	 * 
	 * @param collection
	 *            the Collection to check
	 * @return whether the given Collection is empty
	 */
	private static boolean CollectionUtils_isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	/**
	 * Return {@code true} if the supplied Map is {@code null} or empty. Otherwise, return {@code false}.
	 * 
	 * @param map
	 *            the Map to check
	 * @return whether the given Map is empty
	 */
	private static boolean CollectionUtils_isEmpty(Map<?, ?> map) {
		return (map == null || map.isEmpty());
	}

}
