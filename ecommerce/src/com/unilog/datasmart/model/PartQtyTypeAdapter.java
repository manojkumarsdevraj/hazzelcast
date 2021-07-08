package com.unilog.datasmart.model;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PartQtyTypeAdapter implements JsonDeserializer<PartQtyRuntime>, JsonSerializer<PartQtyRuntime>
{
    public PartQtyRuntime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        int runtime;
        try
        {
            runtime = json.getAsInt();
        }
        catch (NumberFormatException e)
        {
            runtime = 1;
        }
        return new PartQtyRuntime(runtime);
    }

    public JsonElement serialize(PartQtyRuntime src, Type typeOfSrc, JsonSerializationContext context)
    {
        return new JsonPrimitive(src.getValue());
    }
}