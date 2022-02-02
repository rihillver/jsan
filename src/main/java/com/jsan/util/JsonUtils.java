package com.jsan.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ParseProcess;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 
 * 简易包装于 com.alibaba.fastjson.JSON。
 *
 */
public class JsonUtils {

    public static void setDefaultTypeKey(String typeKey) {        
    	JSON.setDefaultTypeKey(typeKey);
    }
    
    public static Object parse(String text) {
        return JSON.parse(text);
    }

    public static Object parse(String text, int features) {
        return JSON.parse(text, features);
    }

    public static Object parse(byte[] input, Feature... features) {
        return JSON.parse(input, features);
    }

    public static Object parse(byte[] input, int off, int len, CharsetDecoder charsetDecoder, Feature... features) {
    	return JSON.parse(input, off, len, charsetDecoder, features);
    }

    public static Object parse(byte[] input, int off, int len, CharsetDecoder charsetDecoder, int features) {
    	return JSON.parse(input, off, len, charsetDecoder, features);
    }

    public static Object parse(String text, Feature... features) {
    	return JSON.parse(text, features);
    }

    public static JSONObject parseObject(String text, Feature... features) {
    	return JSON.parseObject(text, features);
    }

    public static JSONObject parseObject(String text) {
    	return JSON.parseObject(text);
    }

    /**
     * <pre>
     * String jsonStr = "[{\"id\":1001,\"name\":\"Jobs\"}]";
     * List&lt;Model&gt; models = JSON.parseObject(jsonStr, new TypeReference&lt;List&lt;Model&gt;&gt;() {});
     * </pre>
     * @param text json string
     * @param type type refernce
     * @param features
     * @return
     */
    // @SuppressWarnings("unchecked")
    public static <T> T parseObject(String text, TypeReference<T> type, Feature... features) {
    	return JSON.parseObject(text, type, features);
    }

    /**
     * 
     * This method deserializes the specified Json into an object of the specified class. It is not
     * suitable to use if the specified class is a generic type since it will not have the generic
     * type information because of the Type Erasure feature of Java. Therefore, this method should not
     * be used if the desired type is a generic type. Note that this method works fine if the any of
     * the fields of the specified object are generics, just the object itself should not be a
     * generic type. For the cases when the object is of generic type, invoke
     * {@link #parseObject(String, Type, Feature[])}. If you have the Json in a {@link InputStream} instead of
     * a String, use {@link #parseObject(InputStream, Type, Feature[])} instead.
     *
     * @param json the string from which the object is to be deserialized
     * @param clazz the class of T
     * @param features parser features
     * @return an object of type T from the string
     * classOfT
     */
    // @SuppressWarnings("unchecked")
    public static <T> T parseObject(String json, Class<T> clazz, Feature... features) {
    	return JSON.parseObject(json, clazz, features);
    }

    // @SuppressWarnings("unchecked")
    public static <T> T parseObject(String text, Class<T> clazz, ParseProcess processor, Feature... features) {
    	return JSON.parseObject(text, clazz, processor, features);
    }

    /**
     * This method deserializes the specified Json into an object of the specified type. This method
     * is useful if the specified object is a generic type. For non-generic objects, use
     * {@link #parseObject(String, Class, Feature[])} instead. If you have the Json in a {@link InputStream} instead of
     * a String, use {@link #parseObject(InputStream, Type, Feature[])} instead.
     *
     * @param <T> the type of the desired object
     * @param json the string from which the object is to be deserialized
     * @param type The specific genericized type of src. You can obtain this type by using the
     * {@link com.alibaba.fastjson.TypeReference} class. For example, to get the type for
     * {@code Collection<Foo>}, you should use:
     * <pre>
     * Type type = new TypeReference&lt;Collection&lt;Foo&gt;&gt;(){}.getType();
     * </pre>
     * @return an object of type T from the string
     */
    // @SuppressWarnings("unchecked")
    public static <T> T parseObject(String json, Type type, Feature... features) {
    	return JSON.parseObject(json, type, features);
    }

    // @SuppressWarnings("unchecked")
    public static <T> T parseObject(String input, Type clazz, ParseProcess processor, Feature... features) {
    	return JSON.parseObject(input, clazz, processor, features);
    }

    // @SuppressWarnings("unchecked")
    public static <T> T parseObject(String input, Type clazz, int featureValues, Feature... features) {
    	return JSON.parseObject(input, clazz, featureValues, features);
    }
    
    /**
     * @since 1.2.11
     */
    public static <T> T parseObject(String input, Type clazz, ParserConfig config, Feature... features) {
    	return JSON.parseObject(input, clazz, config, features);
    }

    public static <T> T parseObject(String input, Type clazz, ParserConfig config, int featureValues,
                                          Feature... features) {
    	return JSON.parseObject(input, clazz, config, featureValues, features);
    }

    // @SuppressWarnings("unchecked")
    public static <T> T parseObject(String input, Type clazz, ParserConfig config, ParseProcess processor,
                                          int featureValues, Feature... features) {
    	return JSON.parseObject(input, clazz, config, processor, featureValues, features);
    }

    // @SuppressWarnings("unchecked")
    public static <T> T parseObject(byte[] bytes, Type clazz, Feature... features) {
    	return JSON.parseObject(bytes, clazz, features);
    }
    
    /**
     * @since 1.2.11
     */
    // @SuppressWarnings("unchecked")
    public static <T> T parseObject(byte[] bytes, int offset, int len, Charset charset, Type clazz, Feature... features) {
    	return JSON.parseObject(bytes, offset, len, charset, clazz, features);
    }

    // @SuppressWarnings("unchecked")
    public static <T> T parseObject(byte[] input, //
                                    int off, //
                                    int len, //
                                    CharsetDecoder charsetDecoder, //
                                    Type clazz, //
                                    Feature... features) {
    	return JSON.parseObject(input, off, len, charsetDecoder, clazz, features);
    }

    // @SuppressWarnings("unchecked")
    public static <T> T parseObject(char[] input, int length, Type clazz, Feature... features) {
    	return JSON.parseObject(input, length, clazz, features);
    }
    
    /**
     * @since 1.2.11
     */
    // @SuppressWarnings("unchecked")
    public static <T> T parseObject(InputStream is, //
                                    Type type, //
                                    Feature... features) throws IOException {
    	return JSON.parseObject(is, type, features);
    }
    
    /**
     * @since 1.2.11
     */
    // @SuppressWarnings("unchecked")
    public static <T> T parseObject(InputStream is, //
                                    Charset charset, //
                                    Type type, //
                                    Feature... features) throws IOException {
    	return JSON.parseObject(is, charset, type, features);
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
    	return JSON.parseObject(text, clazz);
    }

    public static JSONArray parseArray(String text) {
    	return JSON.parseArray(text);
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
    	return JSON.parseArray(text, clazz);
    }

    public static List<Object> parseArray(String text, Type[] types) {
    	return JSON.parseArray(text, types);
    }

    /**
     * This method serializes the specified object into its equivalent Json representation. Note that this method works fine if the any of the object fields are of generic type,
     * just the object itself should not be of a generic type. If you want to write out the object to a
     * {@link Writer}, use {@link #writeJSONString(Writer, Object, SerializerFeature[])} instead.
     *
     * @param object the object for which json representation is to be created setting for fastjson
     * @return Json representation of {@code object}.
     */
    public static String toJSONString(Object object) {
        return JSON.toJSONString(object);
    }

    public static String toJSONString(Object object, SerializerFeature... features) {
    	return JSON.toJSONString(object, features);
    }
    
    /**
     * @since 1.2.11
     */
    public static String toJSONString(Object object, int defaultFeatures, SerializerFeature... features) {
    	return JSON.toJSONString(object, defaultFeatures, features);
    }

    /**
     * @since 1.1.14
     */
    public static String toJSONStringWithDateFormat(Object object, String dateFormat,
                                                          SerializerFeature... features) {
    	return JSON.toJSONStringWithDateFormat(object, dateFormat, features);
    }

    public static String toJSONString(Object object, SerializeFilter filter, SerializerFeature... features) {
    	return JSON.toJSONString(object, filter, features);
    }

    public static String toJSONString(Object object, SerializeFilter[] filters, SerializerFeature... features) {
    	return JSON.toJSONString(object, filters, features);
    }

    public static byte[] toJSONBytes(Object object, SerializerFeature... features) {
    	return JSON.toJSONBytes(object, features);
    }
    
    /**
     * @since 1.2.11 
     */
    public static byte[] toJSONBytes(Object object, int defaultFeatures, SerializerFeature... features) {
    	return JSON.toJSONBytes(object, defaultFeatures, features);
    }

    public static String toJSONString(Object object, SerializeConfig config, SerializerFeature... features) {
    	return JSON.toJSONString(object, config, features);
    }

    public static String toJSONString(Object object, //
                                      SerializeConfig config, //
                                      SerializeFilter filter, //
                                      SerializerFeature... features) {
    	return JSON.toJSONString(object, config, filter, features);
    }

    public static String toJSONString(Object object, //
                                      SerializeConfig config, //
                                      SerializeFilter[] filters, //
                                      SerializerFeature... features) {
    	return JSON.toJSONString(object, config, filters, features);
    }
    
    /**
     * @since 1.2.9
     * @return
     */
    public static String toJSONString(Object object, // 
                                      SerializeConfig config, // 
                                      SerializeFilter[] filters, // 
                                      String dateFormat, //
                                      int defaultFeatures, // 
                                      SerializerFeature... features) {
    	return JSON.toJSONString(object, config, filters, dateFormat, defaultFeatures, features);
    }

    /**
     * @deprecated
     */
    public static String toJSONStringZ(Object object, SerializeConfig mapping, SerializerFeature... features) {
    	return JSON.toJSONStringZ(object, mapping, features);
    }

    public static byte[] toJSONBytes(Object object, SerializeConfig config, SerializerFeature... features) {
    	return JSON.toJSONBytes(object, config, features);
    }
    
    /**
     * @since 1.2.11 
     */
    public static byte[] toJSONBytes(Object object, SerializeConfig config, int defaultFeatures, SerializerFeature... features) {
    	return JSON.toJSONBytes(object, config, features);
    }

    public static String toJSONString(Object object, boolean prettyFormat) {
    	return JSON.toJSONString(object, prettyFormat);
    }
    
    /**
     * @deprecated use writeJSONString
     */
    public static void writeJSONStringTo(Object object, Writer writer, SerializerFeature... features) {
        JSON.writeJSONStringTo(object, writer, features);
    }

    /**
     * This method serializes the specified object into its equivalent json representation.
     *
     * @param writer Writer to which the json representation needs to be written
     * @param object the object for which json representation is to be created setting for fastjson
     * @param features serializer features
     * @since 1.2.11
     */
    public static void writeJSONString(Writer writer, Object object, SerializerFeature... features) {
        JSON.writeJSONString(writer, object, features);
    }
    
    /**
     * @since 1.2.11 
     */
    public static void writeJSONString(Writer writer, Object object, int defaultFeatures, SerializerFeature... features) {
        JSON.writeJSONString(writer, object, defaultFeatures, features);
    }
    
    /**
     * write object as json to OutputStream
     * @param os output stream
     * @param object
     * @param features serializer features
     * @since 1.2.11
     * @throws IOException
     */
    public static final int writeJSONString(OutputStream os, // 
                                             Object object, // 
                                             SerializerFeature... features) throws IOException {
        return JSON.writeJSONString(os, object, features);
    }
    
    /**
     * @since 1.2.11 
     */
    public static final int writeJSONString(OutputStream os, // 
                                            Object object, // 
                                            int defaultFeatures, //
                                            SerializerFeature... features) throws IOException {
    	return JSON.writeJSONString(os, object, defaultFeatures, features);
   }
    
    public static final int writeJSONString(OutputStream os, // 
                                             Charset charset, // 
                                             Object object, // 
                                             SerializerFeature... features) throws IOException {
    	return JSON.writeJSONString(os, charset, object, features);
    }
    
    public static final int writeJSONString(OutputStream os, // 
                                             Charset charset, // 
                                             Object object, // 
                                             SerializeConfig config, //
                                             SerializeFilter[] filters, //
                                             String dateFormat, //
                                             int defaultFeatures, //
                                             SerializerFeature... features) throws IOException {
    	return JSON.writeJSONString(os, charset, object, config, filters, dateFormat, defaultFeatures, features);
    }

    // ======================================
//    @Override
//    public String toString() {
//        return toJSONString();
//    }
//
//    public String toJSONString() {
//        SerializeWriter out = new SerializeWriter();
//        try {
//            new JSONSerializer(out).write(this);
//            return out.toString();
//        } finally {
//            out.close();
//        }
//    }
//
//    public void writeJSONString(Appendable appendable) {
//        SerializeWriter out = new SerializeWriter();
//        try {
//            new JSONSerializer(out).write(this);
//            appendable.append(out.toString());
//        } catch (IOException e) {
//            throw new JSONException(e.getMessage(), e);
//        } finally {
//            out.close();
//        }
//    }

    /**
     * This method serializes the specified object into its equivalent representation as a tree of
     * {@link JSONObject}s. 
     *
     */
    public static Object toJSON(Object javaObject) {
        return JSON.toJSON(javaObject);
    }

    /**
     * @deprecated
     */
    public static Object toJSON(Object javaObject, ParserConfig parserConfig) {
    	return JSON.toJSON(javaObject, parserConfig);
    }
    
    //// @SuppressWarnings("unchecked")
    public static Object toJSON(Object javaObject, SerializeConfig config) {
    	return JSON.toJSON(javaObject, config);
    }

    public static <T> T toJavaObject(JSON json, Class<T> clazz) {
        return JSON.toJavaObject(json, clazz);
    }
    
//    /**
//     * @since 1.2.9
//     */
//    public <T> T toJavaObject(Class<T> clazz) {
//        return TypeUtils.cast(this, clazz, ParserConfig.getGlobalInstance());
//    }
//
//    /**
//     * @since 1.2.33
//     */
//    public <T> T toJavaObject(Type type) {
//        return TypeUtils.cast(this, type, ParserConfig.getGlobalInstance());
//    }
//
//    /**
//     * @since 1.2.33
//     */
//    public <T> T toJavaObject(TypeReference typeReference) {
//        Type type = typeReference != null ? typeReference.getType() : null;
//        return TypeUtils.cast(this, type, ParserConfig.getGlobalInstance());
//    }
    
   
    public static <T> void handleResovleTask(DefaultJSONParser parser, T value) {
        JSON.handleResovleTask(parser, value);
    }
}
