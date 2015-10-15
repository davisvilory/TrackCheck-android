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
import beteam.viloco.trackcheck.dto.BusinessTypeDTO;
import beteam.viloco.trackcheck.dto.Constantes;
import beteam.viloco.trackcheck.dto.Predicate;
import beteam.viloco.trackcheck.model.DatabaseHelper;
import beteam.viloco.trackcheck.util.CustomException;
import beteam.viloco.trackcheck.util.Extensions;

public class BusinessTypeRepository {
    private Context mContext;

    public BusinessTypeRepository(Context context) {
        this.mContext = context;
    }

    public ArrayList<BusinessTypeDTO> ReadAllByPredicate(ArrayList<Predicate> predicates) throws CustomException {
        ArrayList<BusinessTypeDTO> list = new ArrayList<>();

        String query = "SELECT * FROM " + DatabaseHelper.BusinessType + " WHERE 1=1";

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
                    list.add(Extensions.ConvertCursorToComplexType(cursor, BusinessTypeDTO.class, mContext));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public boolean GetBusinessTypes() throws CustomException {
        if (!Extensions.isConnectionAvailable(mContext)) return false;

        SoapObject request = new SoapObject(Constantes.WS_TARGET_NAMESPACE, Constantes.WSMETHOD_GetBusinessTypes);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        System.setProperty("http.keepAlive", "false");
        HttpTransportSE httpTransport = new HttpTransportSE(Constantes.SOAP_ADDRESS_MOBILE, 40000);
        boolean result = false;

        try {
            httpTransport.call(Constantes.WS_TARGET_NAMESPACE + Constantes.WSMETHOD_GetBusinessTypes, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            AjaxResponse ajaxResponse = Extensions.ConvertSoap(response, AjaxResponse.class, mContext);

            if (ajaxResponse.Success) {
                if (ajaxResponse.ReturnedObject instanceof ArrayList<?>) {
                    ArrayList<BusinessTypeDTO> list = ((ArrayList<BusinessTypeDTO>) ajaxResponse.ReturnedObject);
                    result = InsertBusinessTypes(list);
                }
            }

            httpTransport.reset();
            httpTransport.getServiceConnection().disconnect();
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("No se pudo obtener el catalogo de Tipos de Negocio");
        }

        return result;
    }

    public boolean InsertBusinessTypes(ArrayList<BusinessTypeDTO> dtos) throws CustomException {
        DeleteAll();
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            for (BusinessTypeDTO dto : dtos) {
                ContentValues values = new ContentValues();
                values.put(BusinessTypeDTO.IdCNProp, dto.Id);
                values.put(BusinessTypeDTO.NameCNProp, dto.Name);
                values.put(BusinessTypeDTO.DescriptionCNProp, dto.Description);
                db.insert(DatabaseHelper.BusinessType, null, values);
            }

            db.close();
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return true;
    }

    public boolean ExistsBusinessTypes() throws CustomException {
        String query = "SELECT COUNT(" + BusinessTypeDTO.IdCNProp + ")"
                + " FROM " + DatabaseHelper.BusinessType;

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
            db.delete(DatabaseHelper.BusinessType, null, null);
            db.close();
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }
    }
}
