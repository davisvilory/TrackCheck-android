package beteam.viloco.trackcheck.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import beteam.viloco.trackcheck.dto.AjaxResponse;
import beteam.viloco.trackcheck.dto.Constantes;
import beteam.viloco.trackcheck.dto.Predicate;
import beteam.viloco.trackcheck.dto.UserDTO;
import beteam.viloco.trackcheck.model.DatabaseHelper;
import beteam.viloco.trackcheck.util.CustomException;
import beteam.viloco.trackcheck.util.Extensions;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class UserRepository {
    private Context context;

    public UserRepository(Context context) {
        this.context = context;
    }

    public UserDTO AuthenticateUser(UserDTO user) throws CustomException {
        SoapObject request = new SoapObject(Constantes.WS_TARGET_NAMESPACE, Constantes.WSMETHOD_AutenticaUser);
        PropertyInfo info = new PropertyInfo();
        info.setType(user.getClass());
        info.setValue(user);
        info.setName(Constantes.ParametroAutenticaUser_user);
        request.addProperty(info);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.addMapping(Constantes.WS_TARGET_NAMESPACE, UserDTO.class.getSimpleName(), UserDTO.class);

        HttpTransportSE httpTransport = new HttpTransportSE(Constantes.SOAP_ADDRESS_MOBILE, 40000);

        try {
            httpTransport.call(Constantes.WS_TARGET_NAMESPACE + Constantes.WSMETHOD_AutenticaUser, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            AjaxResponse ajaxResponse = Extensions.ConvertSoap(response, AjaxResponse.class, context);

            user = null;
            if (ajaxResponse.Success)
                if (ajaxResponse.ReturnedObject instanceof UserDTO)
                    user = (UserDTO) ajaxResponse.ReturnedObject;
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, context);
            throw new CustomException("No se pudo autenticar al usuario");
        }

        return user;
    }

    public UserDTO ReadFirstByPredicate(ArrayList<Predicate> predicates) throws CustomException {
        UserDTO userDTO = new UserDTO();

        String query = "SELECT *"
                + " FROM " + DatabaseHelper.User + " AS Us"
                + " WHERE 1=1";

        if (predicates != null)
            for (Predicate predicate : predicates) {
                query += " " + predicate.Comparison + " " + predicate.Column + " = " + predicate.Value;
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
            LogErrorRepository.BuildLogError(ex, context);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return userDTO;
    }

    public UserDTO GetUserAuthenticated() throws CustomException {
        UserDTO userDTO = null;

        String query = "SELECT *"
                + " FROM " + DatabaseHelper.Autenticado + " AS Us"
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
            LogErrorRepository.BuildLogError(ex, context);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return userDTO;
    }

    public boolean InsertUserAuthenticated(UserDTO user) throws CustomException {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(UserDTO.IdCNProp, user.Id);
            values.put(UserDTO.IdUserTypeCNProp, user.IdUserType);
            values.put(UserDTO.UserNameCNProp, user.UserName);
            values.put(UserDTO.FirstNameCNProp, user.FirstName);
            values.put(UserDTO.LastNameCNProp, user.LastName);
            values.put(UserDTO.MiddleNameCNProp, user.MiddleName);
            values.put(UserDTO.PasswordCNProp, user.Password);
            db.insert(DatabaseHelper.Autenticado, null, values);

            db.close();
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, context);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return true;
    }

    public boolean DeleteUserAuthenticated(int Id) throws CustomException {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean result;

        try {
            result = db.delete(DatabaseHelper.Autenticado, UserDTO.IdCNProp + " = " + Id, null) > 0;

            db.close();
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, context);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return result;
    }
}
