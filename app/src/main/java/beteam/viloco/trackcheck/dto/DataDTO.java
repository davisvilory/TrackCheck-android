package beteam.viloco.trackcheck.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

public class DataDTO implements KvmSerializable {
    public int Id;
    public int IdUser;
    public int IdTerritory;
    public int IdZone;
    public String BusinessName = "";
    public double Latitude;
    public double Longitude;
    public String Street = "";
    public String Number = "";
    public String Colony = "";
    public String DelegationTown = "";
    public String City = "";
    public String State = "";
    public String PostalCode = "";
    public int Poster;
    public int ThermoRoshpack60x40;
    public int ThermoTi2260x40;
    public int ElectroGota;
    public int ElectroImagen;
    public int Banderola;
    public int Calcomanis;
    public int CaballeteCambioAceite;
    public int CaballeteVentaAceite;
    public int Lona2x1Roshpack;
    public int Lona2x1ImagenMecanico;
    public Date Date;
    public TerritoryDTO Territory;
    public UserDTO User;
    public ZoneDTO Zone;
    public Vector<DataPhotoDTO> DataPhoto;

    public DataDTO() {
    }

    public void setInnerText(String var1) {

    }

    public String getInnerText() {
        return "";
    }

    public DataDTO(
            int id,
            int iduser,
            int idterritory,
            int idzone,
            String businessname,
            double latitude,
            double longitude,
            String street,
            String number,
            String colony,
            String delegationtown,
            String city,
            String state,
            String postalcode,
            int poster,
            int thermoroshpack60x40,
            int thermoti2260x40,
            int electrogota,
            int electroimagen,
            int banderola,
            int calcomanis,
            int caballetecambioaceite,
            int caballeteventaaceite,
            int lona2x1roshpack,
            int lona2x1imagenmecanico,
            Date date,
            Vector<DataPhotoDTO> dataPhoto) {
        Id = id;
        IdUser = iduser;
        IdTerritory = idterritory;
        IdZone = idzone;
        BusinessName = businessname;
        Latitude = latitude;
        Longitude = longitude;
        Street = street;
        Number = number;
        Colony = colony;
        DelegationTown = delegationtown;
        City = city;
        State = state;
        PostalCode = postalcode;
        Poster = poster;
        ThermoRoshpack60x40 = thermoroshpack60x40;
        ThermoTi2260x40 = thermoti2260x40;
        ElectroGota = electrogota;
        ElectroImagen = electroimagen;
        Banderola = banderola;
        Calcomanis = calcomanis;
        CaballeteCambioAceite = caballetecambioaceite;
        CaballeteVentaAceite = caballeteventaaceite;
        Lona2x1Roshpack = lona2x1roshpack;
        Lona2x1ImagenMecanico = lona2x1imagenmecanico;
        Date = date;
        DataPhoto = dataPhoto;
    }

    public int getPropertyCount() {
        return 27;
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
                return BusinessName;
            case 5:
                return Latitude;
            case 6:
                return Longitude;
            case 7:
                return Street;
            case 8:
                return Number;
            case 9:
                return Colony;
            case 10:
                return DelegationTown;
            case 11:
                return City;
            case 12:
                return State;
            case 13:
                return PostalCode;
            case 14:
                return Poster;
            case 15:
                return ThermoRoshpack60x40;
            case 16:
                return ThermoTi2260x40;
            case 17:
                return ElectroGota;
            case 18:
                return ElectroImagen;
            case 19:
                return Banderola;
            case 20:
                return Calcomanis;
            case 21:
                return CaballeteCambioAceite;
            case 22:
                return CaballeteVentaAceite;
            case 23:
                return Lona2x1Roshpack;
            case 24:
                return Lona2x1ImagenMecanico;
            case 25:
                return Date;
            case 26:
                return DataPhoto;
            default:
                return null;
        }
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
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "BusinessName";
                break;
            case 5:
                info.type = double.class;
                info.name = "Latitude";
                break;
            case 6:
                info.type = double.class;
                info.name = "Longitude";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Street";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Number";
                break;
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Colony";
                break;
            case 10:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "DelegationTown";
                break;
            case 11:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "City";
                break;
            case 12:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "State";
                break;
            case 13:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PostalCode";
                break;
            case 14:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Poster";
                break;
            case 15:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "ThermoRoshpack60x40";
                break;
            case 16:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "ThermoTi2260x40";
                break;
            case 17:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "ElectroGota";
                break;
            case 18:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "ElectroImagen";
                break;
            case 19:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Banderola";
                break;
            case 20:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Calcomanis";
                break;
            case 21:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "CaballeteCambioAceite";
                break;
            case 22:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "CaballeteVentaAceite";
                break;
            case 23:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Lona2x1Roshpack";
                break;
            case 24:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Lona2x1ImagenMecanico";
                break;
            case 25:
                info.type = Date.getClass();
                info.name = "Date";
                break;
            case 26:
                info.type = PropertyInfo.VECTOR_CLASS;
                info.name = "DataPhoto";
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
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "BusinessName";
                break;
            case 5:
                info.type = double.class;
                info.name = "Latitude";
                break;
            case 6:
                info.type = double.class;
                info.name = "Longitude";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Street";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Number";
                break;
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Colony";
                break;
            case 10:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "DelegationTown";
                break;
            case 11:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "City";
                break;
            case 12:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "State";
                break;
            case 13:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "PostalCode";
                break;
            case 14:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Poster";
                break;
            case 15:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "ThermoRoshpack60x40";
                break;
            case 16:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "ThermoTi2260x40";
                break;
            case 17:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "ElectroGota";
                break;
            case 18:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "ElectroImagen";
                break;
            case 19:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Banderola";
                break;
            case 20:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Calcomanis";
                break;
            case 21:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "CaballeteCambioAceite";
                break;
            case 22:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "CaballeteVentaAceite";
                break;
            case 23:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Lona2x1Roshpack";
                break;
            case 24:
                info.type = PropertyInfo.INTEGER_CLASS;
                info.name = "Lona2x1ImagenMecanico";
                break;
            case 25:
                info.type = Date.getClass();
                info.name = "Date";
                break;
            case 26:
                info.type = PropertyInfo.VECTOR_CLASS;
                info.name = "DataPhoto";
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
                IdUser = Integer.parseInt(value.toString());
                break;
            case 2:
                IdTerritory = Integer.parseInt(value.toString());
                break;
            case 3:
                IdZone = Integer.parseInt(value.toString());
                break;
            case 4:
                BusinessName = value.toString();
                break;
            case 5:
                Latitude = Double.parseDouble(value.toString());
                break;
            case 6:
                Longitude = Double.parseDouble(value.toString());
                break;
            case 7:
                Street = value.toString();
                break;
            case 8:
                Number = value.toString();
                break;
            case 9:
                Colony = value.toString();
                break;
            case 10:
                DelegationTown = value.toString();
                break;
            case 11:
                City = value.toString();
                break;
            case 12:
                State = value.toString();
                break;
            case 13:
                PostalCode = value.toString();
                break;
            case 14:
                Poster = Integer.parseInt(value.toString());
                break;
            case 15:
                ThermoRoshpack60x40 = Integer.parseInt(value.toString());
                break;
            case 16:
                ThermoTi2260x40 = Integer.parseInt(value.toString());
                break;
            case 17:
                ElectroGota = Integer.parseInt(value.toString());
                break;
            case 18:
                ElectroImagen = Integer.parseInt(value.toString());
                break;
            case 19:
                Banderola = Integer.parseInt(value.toString());
                break;
            case 20:
                Calcomanis = Integer.parseInt(value.toString());
                break;
            case 21:
                CaballeteCambioAceite = Integer.parseInt(value.toString());
                break;
            case 22:
                CaballeteVentaAceite = Integer.parseInt(value.toString());
                break;
            case 23:
                Lona2x1Roshpack = Integer.parseInt(value.toString());
                break;
            case 24:
                Lona2x1ImagenMecanico = Integer.parseInt(value.toString());
                break;
            case 25:
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm", new Locale("es", "MX"));
                try {
                    Date = df.parse(value.toString().replace("T", " "));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 26:
                DataPhoto = (Vector<DataPhotoDTO>) value;
                break;
            default:
                break;
        }
    }
}
