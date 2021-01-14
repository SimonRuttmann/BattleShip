package Player;
//Not in use
import Model.Ship.IShip;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ObjectDeserializer implements JsonDeserializer<IShip> {

    @Override
    public IShip deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        //String name = jsonObject.
        return null;



    }
}
