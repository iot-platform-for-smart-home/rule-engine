package SMSPlugin.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class SMSData {
    private JsonObject jsonObj ;
    private String phones ;
    private String text ;

    public SMSData(JsonObject jsonObject){
        this.jsonObj = jsonObject;
        this.phones = "";
        JsonArray array = jsonObj.get("phone").getAsJsonArray();
        for(JsonElement element:array)
        {
            this.phones=this.phones.concat(element.getAsString()+",");
        }
        this.phones = this.phones.substring(0,this.phones.length() - 1);
        this.text = jsonObject.get("text").getAsString();
    }
}
