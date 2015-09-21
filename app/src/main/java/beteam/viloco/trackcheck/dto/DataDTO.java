package beteam.viloco.trackcheck.dto;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;

import java.util.Hashtable;

public class DataDTO implements KvmSerializable {
    public int Id;
    public static String IdCNProp = "Id";
    public int IdUser;
    public static String IdUserCNProp = "IdUser";
    public int IdTerritory;
    public static String IdTerritoryCNProp = "IdTerritory";
    public int IdZone;
    public static String IdZoneCNProp = "IdZone";
    public int IdBusinessType;
    public static String IdBusinessTypeCNProp = "IdBusinessType";
    public String SerialNumber = "";
    public static String SerialNumberCNProp = "SerialNumber";
    public String BusinessName = "";
    public static String BusinessNameCNProp = "BusinessName";
    public double Latitude;
    public static String LatitudeCNProp = "Latitude";
    public double Longitude;
    public static String LongitudeCNProp = "Longitude";
    public String Street = "";
    public static String StreetCNProp = "Street";
    public String Number = "";
    public static String NumberCNProp = "Number";
    public String Colony = "";
    public static String ColonyCNProp = "Colony";
    public String DelegationTown = "";
    public static String DelegationTownCNProp = "DelegationTown";
    public String City = "";
    public static String CityCNProp = "City";
    public String State = "";
    public static String StateCNProp = "State";
    public String PostalCode = "";
    public static String PostalCodeCNProp = "PostalCode";
    public int Poster;
    public static String PosterCNProp = "Poster";
    public int ThermoRoshpack60x40;
    public static String ThermoRoshpack60x40CNProp = "ThermoRoshpack60x40";
    public int ThermoTi2260x40;
    public static String ThermoTi2260x40CNProp = "ThermoTi2260x40";
    public int ElectroGota;
    public static String ElectroGotaCNProp = "ElectroGota";
    public int ElectroImagen;
    public static String ElectroImagenCNProp = "ElectroImagen";
    public int Banderola;
    public static String BanderolaCNProp = "Banderola";
    public int Calcomanis;
    public static String CalcomanisCNProp = "Calcomanis";
    public int CaballeteCambioAceite;
    public static String CaballeteCambioAceiteCNProp = "CaballeteCambioAceite";
    public int CaballeteVentaAceite;
    public static String CaballeteVentaAceiteCNProp = "CaballeteVentaAceite";
    public int Lona2x1Roshpack;
    public static String Lona2x1RoshpackCNProp = "Lona2x1Roshpack";
    public int Lona2x1ImagenMecanico;
    public static String Lona2x1ImagenMecanicoCNProp = "Lona2x1ImagenMecanico";
    public Date Date;
    public static String DateCNProp = "Date";
    public BusinessTypeDTO BusinessType;
    public TerritoryDTO Territory;
    public UserDTO User;
    public ZoneDTO Zone;
    public ArrayList<DataPhotoDTO> DataPhoto;

    /*Custom*/
    public int IdServer;
    public static String IdServerCNProp = "IdServer";
    public int DataPhotoCount;
    public static String DataPhotoCountCNProp = "DataPhotoCount";

    public DataDTO() {
    }

    public void setInnerText(String var1) {

    }

    public String getInnerText() {
        return "";
    }

    public int getPropertyCount() {
        return 28;
    }

    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return Id;
            case 1:
                return IdUser;
            case 2:
                return IdTerritory;
            case 3:
                return IdZone;
            case 4:
                return IdBusinessType;
            case 5:
                return SerialNumber;
            case 6:
                return BusinessName;
            case 7:
                return Latitude;
            case 8:
                return Longitude;
            case 9:
                return Street;
            case 10:
                return Number;
            case 11:
                return Colony;
            case 12:
                return DelegationTown;
            case 13:
                return City;
            case 14:
                return State;
            case 15:
                return PostalCode;
            case 16:
                return Poster;
            case 17:
                return ThermoRoshpack60x40;
            case 18:
                return ThermoTi2260x40;
            case 19:
                return ElectroGota;
            case 20:
                return ElectroImagen;
            case 21:
                return Banderola;
            case 22:
                return Calcomanis;
            case 23:
                return CaballeteCambioAceite;
            case 24:
                return CaballeteVentaAceite;
            case 25:
                return Lona2x1Roshpack;
            case 26:
                return Lona2x1ImagenMecanico;
            case 27:
                return Date;
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
                info.name = "IdUser";
                break;
            case 2:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdTerritory";
                break;
            case 3:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdZone";
                break;
            case 4:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "IdBusinessType";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "SerialNumber";
                break;
            case 6:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "BusinessName";
                break;
            case 7:
                info.type = double.class;
                info.name = "Latitude";
                break;
            case 8:
                info.type = double.class;
                info.name = "Longitude";
                break;
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Street";
                break;
            case 10:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Number";
                break;
            case 11:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Colony";
                break;
            case 12:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "DelegationTown";
                break;
            case 13:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "City";
                break;
            case 14:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "State";
                break;
            case 15:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PostalCode";
                break;
            case 16:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Poster";
                break;
            case 17:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "ThermoRoshpack60x40";
                break;
            case 18:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "ThermoTi2260x40";
                break;
            case 19:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "ElectroGota";
                break;
            case 20:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "ElectroImagen";
                break;
            case 21:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Banderola";
                break;
            case 22:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Calcomanis";
                break;
            case 23:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "CaballeteCambioAceite";
                break;
            case 24:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "CaballeteVentaAceite";
                break;
            case 25:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Lona2x1Roshpack";
                break;
            case 26:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Lona2x1ImagenMecanico";
                break;
            case 27:
                info.type = Date.class;
                info.name = "Date";
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
                IdUser = Integer.parseInt(value.toString());
                break;
            case 2:
                IdTerritory = Integer.parseInt(value.toString());
                break;
            case 3:
                IdZone = Integer.parseInt(value.toString());
                break;
            case 4:
                IdBusinessType = Integer.parseInt(value.toString());
                break;
            case 5:
                SerialNumber = value.toString();
                break;
            case 6:
                BusinessName = value.toString();
                break;
            case 7:
                Latitude = Double.parseDouble(value.toString());
                break;
            case 8:
                Longitude = Double.parseDouble(value.toString());
                break;
            case 9:
                Street = value.toString();
                break;
            case 10:
                Number = value.toString();
                break;
            case 11:
                Colony = value.toString();
                break;
            case 12:
                DelegationTown = value.toString();
                break;
            case 13:
                City = value.toString();
                break;
            case 14:
                State = value.toString();
                break;
            case 15:
                PostalCode = value.toString();
                break;
            case 16:
                Poster = Integer.parseInt(value.toString());
                break;
            case 17:
                ThermoRoshpack60x40 = Integer.parseInt(value.toString());
                break;
            case 18:
                ThermoTi2260x40 = Integer.parseInt(value.toString());
                break;
            case 19:
                ElectroGota = Integer.parseInt(value.toString());
                break;
            case 20:
                ElectroImagen = Integer.parseInt(value.toString());
                break;
            case 21:
                Banderola = Integer.parseInt(value.toString());
                break;
            case 22:
                Calcomanis = Integer.parseInt(value.toString());
                break;
            case 23:
                CaballeteCambioAceite = Integer.parseInt(value.toString());
                break;
            case 24:
                CaballeteVentaAceite = Integer.parseInt(value.toString());
                break;
            case 25:
                Lona2x1Roshpack = Integer.parseInt(value.toString());
                break;
            case 26:
                Lona2x1ImagenMecanico = Integer.parseInt(value.toString());
                break;
            case 27:
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm", new Locale("es", "MX"));
                try {
                    Date = df.parse(value.toString().replace("T", " "));
                } catch (ParseException ex) {
                    //Error al convertir la fecha
                }
                break;
            default:
                break;
        }
    }
}
