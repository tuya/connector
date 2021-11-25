package com.tuya.connector.api.core.convert;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.tuya.connector.api.core.MapTypeAdapter;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @program: connector
 * @description:
 * @author: milong
 * @create: 2021-11-17 17:37
 **/
public final class MilongConverterFactory extends Converter.Factory {

    public static MilongConverterFactory create() {
        return create(new Gson());
    }

    public static MilongConverterFactory create(Gson gson) {
        return new MilongConverterFactory(gson);
    }

    private final Gson gson;

    private MilongConverterFactory(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        }
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody,
            ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = null;
        try {
            System.out.println(type.getTypeName());
            adapter = new MapTypeAdapter(Class.forName("com.tuya.connector.api.model.Result"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new GsonResponseBodyConverter<>(gson, adapter);
    }

    @Override
    public Converter<?,
            RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }
}
