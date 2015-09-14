package beteam.viloco.trackcheck.dto;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class FotosDTO implements KvmSerializable {
    public int IDFoto;
    public int IDUsuario;
    public String Direccion;
    public String Observaciones;
    public String Foto;

    public FotosDTO() {
    }

    public void setInnerText(String var1) {

    }

    public String getInnerText() {
        return "";
    }

    public FotosDTO(int idfoto, int idusuario, String direccion, String observaciones, String fotoarray) {
        IDFoto = idfoto;
        IDUsuario = idusuario;
        Direccion = direccion;
        Observaciones = observaciones;
        Foto = fotoarray;
    }

    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return IDFoto;
            case 1:
                return IDUsuario;
            case 2:
                return Direccion;
            case 3:
                return Observaciones;
            case 4:
                return Foto;
        }

        return null;
    }

    public int getPropertyCount() {
        return 5;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch(index)
        {
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IDFoto";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "IDUsuario";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Direccion";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Observaciones";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Foto";
                break;
            default:break;
        }
    }

    public void setProperty(int index, Object value) {
        switch(index)
        {
            case 0:
                IDFoto = Integer.parseInt(value.toString());
                break;
            case 1:
                IDUsuario = Integer.parseInt(value.toString());
                break;
            case 2:
                Direccion = value.toString();
                break;
            case 3:
                Observaciones = value.toString();
                break;
            case 4:
                Foto = value.toString();
                break;
            default:
                break;
        }
    }
}
