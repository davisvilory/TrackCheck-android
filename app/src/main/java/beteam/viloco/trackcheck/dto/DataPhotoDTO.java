package beteam.viloco.trackcheck.dto;

import android.graphics.Bitmap;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class DataPhotoDTO implements KvmSerializable {
    public int Id;
    public int IdData;
    public String Photo = "";
    public DataDTO Data;

    /***
     * Custom
     ***/
    public Bitmap bitmap;

    public DataPhotoDTO() {
    }

    public void setInnerText(String var1) {

    }

    public String getInnerText() {
        return "";
    }

    public DataPhotoDTO(
            int id,
            int iddata,
            String photo) {
        Id = id;
        IdData = iddata;
        Photo = photo;
    }

    public int getPropertyCount() {
        return 3;
    }

    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return Id;
            case 1:
                return IdData;
            case 2:
                return Photo;
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
                info.name = "IdData";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Photo";
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
                info.name = "IdData";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Photo";
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
                IdData = Integer.parseInt(value.toString());
                break;
            case 2:
                Photo = value.toString();
                break;
            default:
                break;
        }
    }
}
