package beteam.viloco.trackcheck.dto;

public class Constantes {
    public static final String LogErrorNombreArchivo = "LogErrorApp";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TrackCheckDB.db";

    //WebService
    public static final String WS_TARGET_NAMESPACE = "http://ipsisdesa.org/";
    //public static final String SOAP_ADDRESS_MOBILE = "http://192.168.0.133:5000/ws/WsMobile.asmx";
    public static final String SOAP_ADDRESS_MOBILE = "http://tcheck.azurewebsites.net/ws/WsMobile.asmx";

    public static final String WSMETHOD_AutenticaUser = "AutenticaUser";
    public static final String WSMETHOD_UploadData = "UploadData";
    public static final String WSMETHOD_UploadDataPhoto = "UploadDataPhoto";
    public static final String WSMETHOD_GetZones = "GetZones";
    public static final String WSMETHOD_GetTerritories = "GetTerritories";
    public static final String WSMETHOD_GetBusinessTypes = "GetBusinessTypes";
    public static final String WSMETHOD_LogError = "LogError";
    public static final String WSMETHOD_ExistsUpdate = "ExistsUpdate";

    public static final String ParametroAutenticaUser_user = "user";
    public static final String ParametroUploadData_data = "data";
    public static final String ParametroUploadData_dataPhoto = "dataPhoto";
    public static final String ParametroLogError_error = "logerror";
    public static final String ParametroExistsUpdate_version = "version";

    public static final String UrlApkLastVersion = "http://tcheck.azurewebsites.net/recursos/targetpro.apk";
    public static final String ApkName = "targetpro.apk";
}
