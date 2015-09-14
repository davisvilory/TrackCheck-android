package beteam.viloco.trackcheck.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import beteam.viloco.trackcheck.dto.AjaxResponse;
import beteam.viloco.trackcheck.dto.Constantes;
import beteam.viloco.trackcheck.dto.UserDTO;
import beteam.viloco.trackcheck.model.DatabaseHelper;
import beteam.viloco.trackcheck.util.CustomException;
import beteam.viloco.trackcheck.util.Extensions;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Map;

public class UserRepositorio {
    private Context context;

    public UserRepositorio(Context context) {
        this.context = context;
    }

    public UserDTO AutenticaUsuario(UserDTO user) throws CustomException{
        SoapObject request = new SoapObject(Constantes.WS_TARGET_NAMESPACE, Constantes.WSMETHOD_AutenticaUser);
        PropertyInfo info = new PropertyInfo();
        info.setType(user.getClass());
        info.setValue(user);
        info.setName(Constantes.ParametroAutenticaUser_user);
        request.addProperty(info);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(Constantes.WS_TARGET_NAMESPACE, "UserDTO", UserDTO.class);

        HttpTransportSE httpTransport = new HttpTransportSE(Constantes.SOAP_ADDRESS_NEGOCIO);

        try {
            httpTransport.call(Constantes.WS_TARGET_NAMESPACE + Constantes.WSMETHOD_AutenticaUser, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            AjaxResponse ajaxResponse = Extensions.ConvertSoap(response, AjaxResponse.class, context);

            if (ajaxResponse.Success)
                user = (UserDTO)ajaxResponse.ReturnedObject;
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, context);
        }

        return user;
    }

    public UserDTO ReadFirstByPredicate(Map<String, String> searchparams) throws CustomException{
        UserDTO userDTO = new UserDTO();

        String query = "SELECT *"
                + " FROM " + Constantes.User + " AS Us"
                + " WHERE 1=1";

        for (Map.Entry<String, String> param : searchparams.entrySet()) {
            query += " AND " + param.getKey() + " = " + param.getValue();
        }

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    userDTO = Extensions.ConvertCursorToComplexType(cursor, UserDTO.class, context);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, context);
        }

        return userDTO;
    }

    public UserDTO ObtieneUnicoUserAutenticado() throws CustomException{
        UserDTO userDTO = null;

        String query = "SELECT *"
                + " FROM " + Constantes.Autenticado + " AS Us"
                + " WHERE 1=1 LIMIT 1";

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    userDTO = Extensions.ConvertCursorToComplexType(cursor, UserDTO.class, context);
                } while (cursor.moveToNext());
            }

            db.close();
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, context);
        }

        return userDTO;
    }

    public boolean InsertaUnicoUserAutenticado(UserDTO user) throws CustomException{
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(user.getPropertyInfo(0).name, user.Id);
            values.put(user.getPropertyInfo(1).name, user.IdUserType);
            values.put(user.getPropertyInfo(2).name, user.UserName);
            values.put(user.getPropertyInfo(3).name, user.FirstName);
            values.put(user.getPropertyInfo(4).name, user.LastName);
            values.put(user.getPropertyInfo(5).name, user.MiddleName);
            values.put(user.getPropertyInfo(6).name, user.Password);
            db.insert(Constantes.Autenticado, null, values);

            db.close();
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, context);
        }

        return true;
    }

    public boolean BorraUnicoUserAutenticado(int Id) throws CustomException{
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean result = false;

        try {
            result = db.delete(Constantes.Autenticado, "Id = " + Id, null) > 0;

            db.close();
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, context);
        }

        return result;
    }
}
