package com.paypal.core;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * SDKUtil class holds utility methods for processing data transformation
 * 
 */
public class SDKUtil {

	/**
	 * Pattern for replacing Ampersand '&' character
	 */
	private static final Pattern AMPERSAND_REPLACE = Pattern
			.compile("&((?!amp;)(?!lt;)(?!gt;)(?!apos;)(?!quot;))");

/**
	 * Pattern for replacing Lesser-than '<' character
	 */
	private static final Pattern LESSERTHAN_REPLACE = Pattern.compile("<");

	/**
	 * Pattern for replacing Greater-than '>' character
	 */
	private static final Pattern GREATERTHAN_REPLACE = Pattern.compile(">");

	/**
	 * Pattern for replacing Quote '"' character
	 */
	private static final Pattern QUOT_REPLACE = Pattern.compile("\"");

	/**
	 * Pattern for replacing Apostrophe ''' character
	 */
	private static final Pattern APOSTROPHE_REPLACE = Pattern.compile("'");

	/**
	 * Ampersand escape
	 */
	private static final String AMPERSAND = "&amp;";

	/**
	 * Greater than escape
	 */
	private static final String GREATERTHAN = "&gt;";

	/**
	 * Lesser than escape
	 */
	private static final String LESSERTHAN = "&lt;";

	/**
	 * Quot escape
	 */
	private static final String QUOT = "&quot;";

	/**
	 * Apostrophe escape
	 */
	private static final String APOSTROPHE = "&apos;";

	/**
	 * Method replaces invalid XML entities with proper escapes, this method
	 * does not depend on regular expressions
	 * 
	 * @param textContent
	 *            Original text
	 * @return Replaced text
	 */
	public static String escapeInvalidXmlChars(String textContent) {
		StringBuilder stringBuilder = null;
		String response = null;
		if (textContent != null) {
			stringBuilder = new StringBuilder();
			int contentLength = textContent.toCharArray().length;
			for (int i = 0; i < contentLength; i++) {
				char ch = textContent.charAt(i);
				if (ch == '&') {
					if (i != (contentLength - 1)) {
						if (!(i + 3 > contentLength - 1)
								&& (textContent.charAt(i + 1) == 'g' || textContent
										.charAt(i + 1) == 'l')
								&& textContent.charAt(i + 2) == 't'
								&& textContent.charAt(i + 3) == ';') {
							stringBuilder.append(ch);
						} else if (!(i + 4 > contentLength - 1)
								&& textContent.charAt(i + 1) == 'a'
								&& textContent.charAt(i + 2) == 'm'
								&& textContent.charAt(i + 3) == 'p'
								&& textContent.charAt(i + 4) == ';') {
							stringBuilder.append(ch);
						} else if (!(i + 5 > contentLength - 1)
								&& ((textContent.charAt(i + 1) == 'q'
										&& (textContent.charAt(i + 2) == 'u'
												&& textContent.charAt(i + 3) == 'o' && textContent
												.charAt(i + 4) == 't') || (textContent
										.charAt(i + 1) == 'a' && (textContent
										.charAt(i + 2) == 'p'
										&& textContent.charAt(i + 3) == 'o' && textContent
										.charAt(i + 4) == 's'))
										&& textContent.charAt(i + 5) == ';'))) {
							stringBuilder.append(ch);
						} else {
							stringBuilder.append(AMPERSAND);
						}
					} else {
						stringBuilder.append(AMPERSAND);
					}
				} else if (ch == '<') {
					stringBuilder.append(LESSERTHAN);
				} else if (ch == '>') {
					stringBuilder.append(GREATERTHAN);
				} else if (ch == '"') {
					stringBuilder.append(QUOT);
				} else if (ch == '\'') {
					stringBuilder.append(APOSTROPHE);
				} else {
					stringBuilder.append(ch);
				}
			}
			response = stringBuilder.toString();
		}
		return response;
	}

	/**
	 * Method replaces invalid XML entities with proper escapes, this method
	 * depends on regular expressions
	 * 
	 * @param textContent
	 *            Original text
	 * @return Replaced text
	 */
	public static String escapeInvalidXmlCharsRegex(String textContent) {
		String response = "";
		if (textContent != null && textContent.length() != 0) {
			response = APOSTROPHE_REPLACE.matcher(
					QUOT_REPLACE.matcher(
							GREATERTHAN_REPLACE.matcher(
									LESSERTHAN_REPLACE.matcher(
											AMPERSAND_REPLACE.matcher(
													textContent).replaceAll(
													AMPERSAND)).replaceAll(
											LESSERTHAN))
									.replaceAll(GREATERTHAN)).replaceAll(QUOT))
					.replaceAll(APOSTROPHE);
		}
		return response;
	}

	/**
	 * Method replaces invalid XML entities with proper escapes, this method
	 * depends on regular expressions
	 * 
	 * @param intContent
	 *            Integer object
	 * @return Replaced text
	 */
	public static String escapeInvalidXmlCharsRegex(Integer intContent) {
		String response = null;
		String textContent = null;
		if (intContent != null) {
			textContent = intContent.toString();
			response = escapeInvalidXmlCharsRegex(textContent);
		}
		return response;
	}

	/**
	 * Method replaces invalid XML entities with proper escapes, this method
	 * depends on regular expressions
	 * 
	 * @param boolContent
	 *            Boolean object
	 * @return Replaced text
	 */
	public static String escapeInvalidXmlCharsRegex(Boolean boolContent) {
		String response = null;
		String textContent = null;
		if (boolContent != null) {
			textContent = boolContent.toString();
			response = escapeInvalidXmlCharsRegex(textContent);
		}
		return response;
	}

	/**
	 * Method replaces invalid XML entities with proper escapes, this method
	 * depends on regular expressions
	 * 
	 * @param doubleContent
	 *            Double object
	 * @return Replaced text
	 */
	public static String escapeInvalidXmlCharsRegex(Double doubleContent) {
		String response = null;
		String textContent = null;
		if (doubleContent != null) {
			textContent = doubleContent.toString();
			response = escapeInvalidXmlCharsRegex(textContent);
		}
		return response;
	}

	/**
	 * Formats the URI path for REST calls.
	 * 
	 * @param pattern
	 *            URI pattern with place holders for replacement strings
	 * @param parameters
	 *            Replacement objects
	 * @return Formatted URI path
	 */
	public static String formatURIPath(String pattern, Object[] parameters) {
		String formattedPath = null;

		if (pattern != null) {
			if (parameters != null && parameters.length == 1
					&& parameters[0] instanceof Map<?, ?>) {

				// Form a object array using the passed Map
				parameters = splitParameters(pattern, (Map<?, ?>) parameters[0]);
			}
			// Perform a simple message formatting
			String fString = MessageFormat.format(pattern, parameters);

			// Process the resultant string for removing nulls
			formattedPath = removeNullsInQS(fString);
		}
		return formattedPath;
	}

	private static String removeNullsInQS(String fString) {
		if (fString != null && fString.length() != 0) {
			String[] parts = fString.split("\\?");

			// Process the query string part
			if (parts.length == 2) {
				String queryString = parts[1];
				String[] querys = queryString.split("&");
				if (querys.length > 0) {
					StringBuilder strBuilder = new StringBuilder();
					for (String query : querys) {
						String[] valueSplit = query.split("=");
						if (valueSplit.length == 2) {
							if (valueSplit[1].trim().equalsIgnoreCase("null")) {
								continue;
							} else {
								strBuilder.append(query).append("&");
							}
						} else if (valueSplit.length < 2) {
							continue;
						}
					}
					fString = (!strBuilder.toString().endsWith("&")) ? strBuilder
							.toString() : strBuilder.toString().substring(0,
							strBuilder.toString().length() - 1);
				}

				// append the query string delimiter
				fString = (parts[0].trim() + "?") + fString;
			}
		}
		return fString;
	}

	/**
	 * Split the URI and form a Object array using the query string and values
	 * in the provided map. The return object array is populated only if the map
	 * contains valid value for the query name. The object array contains null
	 * values if there is no value found in the map
	 * 
	 * @param pattern
	 *            URI pattern
	 * @param containerMap
	 *            Map containing the query name and value
	 * @return Object array
	 */
	private static Object[] splitParameters(String pattern,
			Map<?, ?> containerMap) {
		List<Object> objectList = new ArrayList<Object>();
		String[] query = pattern.split("\\?");
		if (query != null && query.length == 2 && query[1].contains("={")) {
			String[] queries = query[1].split("&");
			if (queries != null) {
				for (String q : queries) {
					String[] params = q.split("=");
					if (params != null && params.length == 2) {
						String key = params[0].trim();
						if (containerMap.containsKey(key)) {
							Object object = containerMap.get(key);
							objectList.add(object);
						} else {
							objectList.add(null);
						}
					}
				}
			}
		}
		return objectList.toArray();
	}
}
