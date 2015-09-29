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
import beteam.viloco.trackcheck.dto.ZoneDTO;
import beteam.viloco.trackcheck.model.DatabaseHelper;
import beteam.viloco.trackcheck.util.CustomException;
import beteam.viloco.trackcheck.util.Extensions;

public class ZoneRepository {
    private Context mContext;

    public ZoneRepository(Context context) {
        this.mContext = context;
    }

    public ArrayList<ZoneDTO> ReadAllByPredicate(ArrayList<Predicate> predicates) throws CustomException {
        ArrayList<ZoneDTO> list = new ArrayList<>();

        String query = "SELECT * FROM " + DatabaseHelper.Zone + " WHERE 1=1";

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
                    list.add(Extensions.ConvertCursorToComplexType(cursor, ZoneDTO.class, mContext));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public boolean GetZones() throws CustomException {
        if (!Extensions.isConnectionAvailable(mContext)) return false;

        SoapObject request = new SoapObject(Constantes.WS_TARGET_NAMESPACE, Constantes.WSMETHOD_GetZones);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(Constantes.SOAP_ADDRESS_MOBILE, 40000);
        boolean result = false;

        try {
            httpTransport.call(Constantes.WS_TARGET_NAMESPACE + Constantes.WSMETHOD_GetZones, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            AjaxResponse ajaxResponse = Extensions.ConvertSoap(response, AjaxResponse.class, mContext);

            if (ajaxResponse.Success) {
                if (ajaxResponse.ReturnedObject instanceof ArrayList<?>) {
                    ArrayList<ZoneDTO> list = ((ArrayList<ZoneDTO>) ajaxResponse.ReturnedObject);
                    result = InsertZones(list);
                }
            }
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("No se pudo obtener el catalogo de Zonas");
        }

        return result;
    }

    public boolean InsertZones(ArrayList<ZoneDTO> dtos) throws CustomException {
        DeleteAll();
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            for (ZoneDTO dto : dtos) {
                ContentValues values = new ContentValues();
                values.put(ZoneDTO.IdCNProp, dto.Id);
                values.put(ZoneDTO.NameCNProp, dto.Name);
                values.put(ZoneDTO.DescriptionCNProp, dto.Description);
                db.insert(DatabaseHelper.Zone, null, values);
            }

            db.close();
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return true;
    }

    public boolean ExistsZones() throws CustomException {
        String query = "SELECT COUNT(" + ZoneDTO.IdCNProp + ")"
                + " FROM " + DatabaseHelper.Zone;

        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean result = false;

        try {
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                result = cursor.getInt(0) > 0;
            }

            db.close();
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return result;
    }

    public boolean DeleteAll() throws CustomException {
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean result = false;

        try {
            db.delete(DatabaseHelper.Zone, null, null);
            db.close();
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return result;
    }
}
