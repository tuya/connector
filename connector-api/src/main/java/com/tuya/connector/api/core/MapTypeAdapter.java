package com.tuya.connector.api.core;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: connector
 * @description:
 * @author: milong
 * @create: 2021-11-17 10:54
 **/
public class MapTypeAdapter extends TypeAdapter<Object> {

    private Class clazz;

    public MapTypeAdapter() {
    }

    public MapTypeAdapter(Class clazz) {
        super();
        this.clazz = clazz;
    }


    @Override
    public void write(JsonWriter jsonWriter, Object o) {

    }

    @SneakyThrows
    @Override
    public Object read(JsonReader in) {
        JsonToken token = in.peek();
        switch (token) {
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList<Object>();
                in.beginArray();
                while (in.hasNext()) {
                    list.add(read(in));
                }
                in.endArray();
                return list;

            case BEGIN_OBJECT:
                Map<String, Object> map = new LinkedTreeMap<String, Object>();
                in.beginObject();
                while (in.hasNext()) {
                    map.put(in.nextName(), read(in));
                }
                in.endObject();
//                Result result = (Result) clazz.getDeclaredConstructor().newInstance();
//                result.setResult(map.get("result"));
//                result.setCode((Integer) map.get("code"));
//                result.setMsg((String) map.get("msg"));
//                result.setSuccess((Boolean) map.get("success"));
//                result.setT((Long) map.get("t"));
                return map;

            case STRING:
                return in.nextString();

            case NUMBER:
                /**
                 * 改写数字的处理逻辑，将数字值分为整型与浮点型。
                 */
                String numberStr = in.nextString();
                if (numberStr.contains(".") || numberStr.contains("e")
                        || numberStr.contains("E")) {
                    return Double.parseDouble(numberStr);
                }
                if (Long.parseLong(numberStr) <= Integer.MAX_VALUE) {
                    return Integer.parseInt(numberStr);
                }
                return Long.parseLong(numberStr);

            case BOOLEAN:
                return in.nextBoolean();

            case NULL:
                in.nextNull();
                return null;

            default:
                throw new IllegalStateException();
        }
    }

    public static void main(String[] args) {
        String json = "{\n" +
                "  \"z\": 999999999999999999,\n" +
                "  \"x\": 200,\n" +
                "  \"c\": 300,\n" +
                "  \"v\":\"中文\",\n" +
                "  \"b\":true\n" +
                "}";
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        System.out.println(gson.getAdapter(new TypeToken<Map<String, Object>>() {
        }));
        Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
        System.out.println(map);
    }
}
