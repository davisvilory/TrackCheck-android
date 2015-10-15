package beteam.viloco.trackcheck.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import beteam.viloco.trackcheck.dto.AjaxResponse;
import beteam.viloco.trackcheck.dto.Constantes;
import beteam.viloco.trackcheck.dto.Predicate;
import beteam.viloco.trackcheck.dto.TerritoryDTO;
import beteam.viloco.trackcheck.model.DatabaseHelper;
import beteam.viloco.trackcheck.util.CustomException;
import beteam.viloco.trackcheck.util.Extensions;

public class TerritoryRepository {
    private Context mContext;

    public TerritoryRepository(Context context) {
        this.mContext = context;
    }

    public ArrayList<TerritoryDTO> ReadAllByPredicate(ArrayList<Predicate> predicates) throws CustomException {
        ArrayList<TerritoryDTO> list = new ArrayList<>();

        String query = "SELECT * FROM " + DatabaseHelper.Territory + " WHERE 1=1";

        if (predicates != null)
            for (Predicate predicate : predicates) {
                query += " " + predicate.Comparison + " " + predicate.Column + " = " + predicate.Value;
            }

        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    list.add(Extensions.ConvertCursorToComplexType(cursor, TerritoryDTO.class, mContext));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public boolean GetTerritories() throws CustomException {
        if (!Extensions.isConnectionAvailable(mContext)) return false;

        SoapObject request = new SoapObject(Constantes.WS_TARGET_NAMESPACE, Constantes.WSMETHOD_GetTerritories);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        System.setProperty("http.keepAlive", "false");
        HttpTransportSE httpTransport = new HttpTransportSE(Constantes.SOAP_ADDRESS_MOBILE, 40000);
        boolean result = false;

        try {
            httpTransport.call(Constantes.WS_TARGET_NAMESPACE + Constantes.WSMETHOD_GetTerritories, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            AjaxResponse ajaxResponse = Extensions.ConvertSoap(response, AjaxResponse.class, mContext);

            if (ajaxResponse.Success) {
                if (ajaxResponse.ReturnedObject instanceof ArrayList<?>) {
                    ArrayList<TerritoryDTO> list = ((ArrayList<TerritoryDTO>) ajaxResponse.ReturnedObject);
                    result = InsertTerritories(list);
                }
            }

            httpTransport.reset();
            httpTransport.getServiceConnection().disconnect();
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("No se pudo obtener el catalogo de Territorios");
        }

        return result;
    }

    public boolean InsertTerritories(ArrayList<TerritoryDTO> dtos) throws CustomException {
        DeleteAll();
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            for (TerritoryDTO dto : dtos) {
                ContentValues values = new ContentValues();
                values.put(TerritoryDTO.IdCNProp, dto.Id);
                values.put(TerritoryDTO.NameCNProp, dto.Name);
                values.put(TerritoryDTO.DescriptionCNProp, dto.Description);
                db.insert(DatabaseHelper.Territory, null, values);
            }

            db.close();
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return true;
    }

    public boolean ExistsTerritories() throws CustomException {
        String query = "SELECT COUNT(Id)"
                + " FROM " + DatabaseHelper.Territory;

        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean result = false;

        try {
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                result = cursor.getInt(0) > 0;
            }

            db.close();
            cursor.close();
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return result;
    }

    public void DeleteAll() throws CustomException {
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.delete(DatabaseHelper.Territory, null, null);
            db.close();
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }
    }
}
