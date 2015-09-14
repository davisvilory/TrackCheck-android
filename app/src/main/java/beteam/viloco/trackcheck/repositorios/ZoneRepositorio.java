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
import java.util.List;
import java.util.Map;

import beteam.viloco.trackcheck.dto.AjaxResponse;
import beteam.viloco.trackcheck.dto.Constantes;
import beteam.viloco.trackcheck.dto.ZoneDTO;
import beteam.viloco.trackcheck.model.DatabaseHelper;
import beteam.viloco.trackcheck.util.CustomException;
import beteam.viloco.trackcheck.util.Extensions;

public class ZoneRepositorio {
    private Context context;

    public ZoneRepositorio(Context context) {
        this.context = context;
    }

    public List<ZoneDTO> ReadAllByPredicate(Map<String, String> searchparams) throws CustomException {
        List<ZoneDTO> list = new ArrayList<>();

        String query = "SELECT * FROM " + Constantes.Zone + " WHERE 1=1";

        if (searchparams != null)
            for (Map.Entry<String, String> param : searchparams.entrySet()) {
                query += " AND " + param.getKey() + " = " + param.getValue();
            }

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    list.add(Extensions.ConvertCursorToComplexType(cursor, ZoneDTO.class, context));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, context);
        }

        return list;
    }

    public boolean ObtieneZones() throws CustomException {
        SoapObject request = new SoapObject(Constantes.WS_TARGET_NAMESPACE, Constantes.WSMETHOD_GetZones);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(Constantes.SOAP_ADDRESS_CATALOGOS);
        boolean result = false;

        try {
            httpTransport.call(Constantes.WS_TARGET_NAMESPACE + Constantes.WSMETHOD_GetZones, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            AjaxResponse ajaxResponse = Extensions.ConvertSoap(response, AjaxResponse.class, context);

            if (ajaxResponse.Success) {
                ArrayList<ZoneDTO> list = (ArrayList<ZoneDTO>) ajaxResponse.ReturnedObject;

                result = InsertaZones(list);
            }
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, this.context);
            throw new CustomException("No se pudo obtener el catalogo de Zonas");
        }

        return result;
    }

    public boolean InsertaZones(ArrayList<ZoneDTO> zoneDTOArrayList) throws CustomException {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            for (ZoneDTO zone : zoneDTOArrayList) {
                ContentValues values = new ContentValues();
                values.put(zone.getPropertyInfo(1).name, zone.Name);
                values.put(zone.getPropertyInfo(2).name, zone.Description);
                db.insert(Constantes.Zone, null, values);
            }

            db.close();
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, context);
        }

        return true;
    }

    public boolean ExisteZones() throws CustomException {
        String query = "SELECT COUNT(Id)"
                + " FROM " + Constantes.Zone;

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean result = false;

        try {
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                result = cursor.getInt(0) > 0;
            }

            db.close();
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, context);
        }

        return result;
    }
}
