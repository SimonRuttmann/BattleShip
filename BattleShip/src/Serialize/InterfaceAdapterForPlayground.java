package Serialize;

import com.google.gson.*;
import java.lang.reflect.Type;


/**
 * Creates an InterfaceAdapter for GSON to modify the created .json -Files
 * Modifies: CLASSNAME  The instantiated classname
 *           DATA       The data that maps the current JSON Object
 *
 * The InterfaceAdapter is practically used as TypeAdapter, as GSON saves IDrawables and IShips only as Interface-Type Objects
 * Therefore the attribute IDrawable[][] and all Ships used as IShip can not deserialized by GSON.
 * This method is based on: https://technology.finra.org/code/serialize-deserialize-interfaces-in-java.html
 */
public class InterfaceAdapterForPlayground implements JsonSerializer, JsonDeserializer {

    private static final String CLASSNAME = "CLASSNAME";
    private static final String DATA = "DATA";

    @Override
    public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {


            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
            String className = prim.getAsString();
            Class klass = getObjectClass(className);
            return jsonDeserializationContext.deserialize(jsonObject.get(DATA), klass);
        }


    @Override
    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CLASSNAME, o.getClass().getName());
        jsonObject.add(DATA, jsonSerializationContext.serialize(o));
        return jsonObject;
    }


    /****** Helper method to get the className of the object to be deserialized *****/
    public Class getObjectClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }
    }


}
