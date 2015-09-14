package beteam.viloco.trackcheck.dto;

import java.util.List;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class UserTypeDTO implements KvmSerializable {
    public int Id;
    public String Name = "";
    public String Description = "";
    public List<UserDTO> User;

    public UserTypeDTO() {
    }

    public void setInnerText(String var1) {

    }

    public String getInnerText() {
        return "";
    }

    public UserTypeDTO(
            int id,
            String name,
            String description) {
        Id = id;
        Name = name;
        Description = description;
    }

    public int getPropertyCount() {
        return 3;
    }

    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return Id;
            case 1:
                return Name;
            case 2:
                return Description;
            default:
                break;
        }

        return null;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch (index) {
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Id";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Name";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Description";
                break;
            default:
                break;
        }
    }

    public void setProperty(int index, Object value) {
        switch (index) {
            case 0:
                Id = Integer.parseInt(value.toString());
                break;
            case 1:
                Name = value.toString();
                break;
            case 2:
                Description = value.toString();
                break;
            default:
                break;
        }
    }
}
