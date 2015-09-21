package beteam.viloco.trackcheck.dto;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.util.Hashtable;

public class LogErrorDTO implements KvmSerializable {
    public int IDLogError;
    public static String IDLogErrorCNProp = "IDLogError";
    public String Modulo = "";
    public static String ModuloCNProp = "Modulo";
    public String ErrorResumido = "";
    public static String ErrorResumidoCNProp = "ErrorResumido";
    public String ErrorInterno = "";
    public static String ErrorInternoCNProp = "ErrorInterno";
    public Date Fecha;
    public static String FechaCNProp = "Fecha";

    public LogErrorDTO() {
    }

    public void setInnerText(String var1) {

    }

    public String getInnerText() {
        return "";
    }

    public int getPropertyCount() {
        return 5;
    }

    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return IDLogError;
            case 1:
                return Modulo;
            case 2:
                return ErrorResumido;
            case 3:
                return ErrorInterno;
            case 4:
                return Fecha;
            default:
                break;
        }

        return null;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch (index) {
            case 0:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IDLogError";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Modulo";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ErrorResumido";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "ErrorInterno";
                break;
            case 4:
                info.type = Date.class;
                info.name = "Fecha";
                break;
            default:
                break;
        }
    }

    public void setProperty(int index, Object value) {
        switch (index) {
            case 0:
                IDLogError = Integer.parseInt(value.toString());
                break;
            case 1:
                Modulo = value.toString();
                break;
            case 2:
                ErrorResumido = value.toString();
                break;
            case 3:
                ErrorInterno = value.toString();
                break;
            case 4:
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm", new Locale("es", "MX"));
                try {
                    Fecha = df.parse(value.toString().replace("T", " "));
                } catch (ParseException ex) {
                    //Error al convertir la fecha
                }
                break;
            default:
                break;
        }
    }
}
