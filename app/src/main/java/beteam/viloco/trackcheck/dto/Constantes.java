package beteam.viloco.trackcheck.dto;

public class Constantes {
    public static final String LogErrorNombreArchivo = "LogErrorApp";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TrackCheckDB.db";

    public static final String Autenticado = "Autenticado";
    public static final String Data = "Data";
    public static final String DataPhoto = "DataPhoto";
    public static final String LogError = "LogError";
    public static final String Territory = "Territory";
    public static final String User = "User";
    public static final String UserType = "UserType";
    public static final String Zone = "Zone";

    //WebService
    public static final String WS_TARGET_NAMESPACE = "http://tempuri.org/";
    public static final String SOAP_ADDRESS_NEGOCIO = "http://192.168.1.153:5000/ws/WsNegocio.asmx";
    public static final String SOAP_ADDRESS_CATALOGOS = "http://192.168.1.153:5000/ws/WsCatalogos.asmx";
    //public static final String SOAP_ADDRESS_NEGOCIO = "http://ipsisportal.homeip.net/TrackCheck/ws/WsNegocio.asmx";
    //public static final String SOAP_ADDRESS_CATALOGOS = "http://ipsisportal.homeip.net/TrackCheck/ws/WsCatalogos.asmx";

    public static final String WSMETHOD_AutenticaUser = "AutenticaUser";
    public static final String WSMETHOD_UploadData = "UploadData";
    public static final String WSMETHOD_UploadDataPhoto = "UploadDataPhoto";
    public static final String WSMETHOD_GetZones = "GetZones";
    public static final String WSMETHOD_GetTerritories = "GetTerritories";

    public static final String ParametroAutenticaUser_user = "user";
    public static final String ParametroUploadData_data = "data";
    public static final String ParametroUploadData_dataPhoto = "dataPhoto";
}
