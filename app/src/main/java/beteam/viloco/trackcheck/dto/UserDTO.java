package beteam.viloco.trackcheck.dto;

import java.util.List;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class UserDTO implements KvmSerializable {
    public int Id;
    public int IdUserType;
    public String UserName = "";
    public String FirstName = "";
    public String LastName = "";
    public String MiddleName = "";
    public String Password = "";
    public List<DataDTO> Data;
    public UserTypeDTO UserType;
    public List<UserDTO> User1;
    public List<UserDTO> User2;

    public UserDTO() {
    }

    public void setInnerText(String var1) {

    }

    public String getInnerText() {
        return "";
    }

    public int getPropertyCount() {
        return 7;
    }

    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return Id;
            case 1:
                return IdUserType;
            case 2:
                return UserName;
            case 3:
                return FirstName;
            case 4:
                return LastName;
            case 5:
                return MiddleName;
            case 6:
                return Password;
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
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdUserType";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "UserName";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "FirstName";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "LastName";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MiddleName";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Password";
                break;
            default:
                break;
        }
    }

    public PropertyInfo getPropertyInfo(int index) {
        PropertyInfo info = new PropertyInfo();
        switch (index) {
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Id";
                break;
            case 1:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdUserType";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "UserName";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "FirstName";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "LastName";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MiddleName";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Password";
                break;
            default:
                break;
        }
        return info;
    }

    public void setProperty(int index, Object value) {
        switch (index) {
            case 0:
                Id = Integer.parseInt(value.toString());
                break;
            case 1:
                IdUserType = Integer.parseInt(value.toString());
                break;
            case 2:
                UserName = value.toString();
                break;
            case 3:
                FirstName = value.toString();
                break;
            case 4:
                LastName = value.toString();
                break;
            case 5:
                MiddleName = value.toString();
                break;
            case 6:
                Password = value.toString();
                break;
            default:
                break;
        }
    }
}
