package beteam.viloco.trackcheck.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import beteam.viloco.trackcheck.dto.AjaxResponse;
import beteam.viloco.trackcheck.dto.Constantes;
import beteam.viloco.trackcheck.dto.DataDTO;
import beteam.viloco.trackcheck.dto.DataPhotoDTO;
import beteam.viloco.trackcheck.model.DatabaseHelper;
import beteam.viloco.trackcheck.util.Extensions;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class DataRepositorio {
    private Context context;

    public DataRepositorio(Context context) {
        this.context = context;
    }

    public boolean InsertaData(DataDTO dataDTO) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //String query = "";

//        query = "SELECT * FROM " + Constants.CategoryItem + " WHERE "
//                + CategoryItem.CategoryItemID + " = " + orderitem.CategoryItemID;
//
//        CategoryItemDTO categoryitem = new CategoryItemDTO();
//
//        try {
//            Cursor cursor = db.rawQuery(query, null);
//
//            if (cursor.moveToFirst()) {
//                do {
//                    categoryitem.CategoryItemID = Integer.parseInt(cursor.getString(0));
//                    categoryitem.CategoryID = Integer.parseInt(cursor.getString(1));
//                    categoryitem.Title = cursor.getString(2);
//                    categoryitem.Cost = new BigDecimal((Float.parseFloat(cursor.getString(3)) / 100) + "");
//                } while (cursor.moveToNext());
//            }
//        } catch (Exception ex) {
//            LogErrorRepositorio.ArmaLogError(ex, context);
//        }

        try {
            ContentValues values = new ContentValues();
            values.put(dataDTO.getPropertyInfo(1).name, dataDTO.IdUser);
            values.put(dataDTO.getPropertyInfo(2).name, dataDTO.IdTerritory);
            values.put(dataDTO.getPropertyInfo(3).name, dataDTO.IdZone);
            values.put(dataDTO.getPropertyInfo(4).name, dataDTO.BusinessName);
            values.put(dataDTO.getPropertyInfo(5).name, dataDTO.Latitude);
            values.put(dataDTO.getPropertyInfo(6).name, dataDTO.Longitude);
            values.put(dataDTO.getPropertyInfo(7).name, dataDTO.Street);
            values.put(dataDTO.getPropertyInfo(8).name, dataDTO.Number);
            values.put(dataDTO.getPropertyInfo(9).name, dataDTO.Colony);
            values.put(dataDTO.getPropertyInfo(10).name, dataDTO.DelegationTown);
            values.put(dataDTO.getPropertyInfo(11).name, dataDTO.City);
            values.put(dataDTO.getPropertyInfo(12).name, dataDTO.State);
            values.put(dataDTO.getPropertyInfo(13).name, dataDTO.PostalCode);
            values.put(dataDTO.getPropertyInfo(14).name, dataDTO.Poster);
            values.put(dataDTO.getPropertyInfo(15).name, dataDTO.ThermoRoshpack60x40);
            values.put(dataDTO.getPropertyInfo(16).name, dataDTO.ThermoTi2260x40);
            values.put(dataDTO.getPropertyInfo(17).name, dataDTO.ElectroGota);
            values.put(dataDTO.getPropertyInfo(18).name, dataDTO.ElectroImagen);
            values.put(dataDTO.getPropertyInfo(19).name, dataDTO.Banderola);
            values.put(dataDTO.getPropertyInfo(20).name, dataDTO.Calcomanis);
            values.put(dataDTO.getPropertyInfo(21).name, dataDTO.CaballeteCambioAceite);
            values.put(dataDTO.getPropertyInfo(22).name, dataDTO.CaballeteVentaAceite);
            values.put(dataDTO.getPropertyInfo(23).name, dataDTO.Lona2x1Roshpack);
            values.put(dataDTO.getPropertyInfo(24).name, dataDTO.Lona2x1ImagenMecanico);
            values.put(dataDTO.getPropertyInfo(25).name, dataDTO.Date.getTime());
            dataDTO.Id = (int) db.insert(Constantes.Data, null, values);

            if (dataDTO.Id > 0) {
                for (int i = 0; i < dataDTO.DataPhoto.size(); i++) {
                    values = new ContentValues();
                    values.put(dataDTO.DataPhoto.get(i).getPropertyInfo(1).name, dataDTO.Id);
                    values.put(dataDTO.DataPhoto.get(i).getPropertyInfo(2).name, dataDTO.DataPhoto.get(i).Photo);
                    db.insert(Constantes.DataPhoto, null, values);
                }
            } else {
                return false;
            }

            db.close();
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, context);
        }

        return true;
    }

    public List<DataDTO> ReadAllByPredicate(Map<String, String> searchparams) {
        List<DataDTO> list = new ArrayList<>();

        String query = "SELECT * FROM " + Constantes.Data + " WHERE 1=1";

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
                    DataDTO dataDTO = Extensions.ConvertCursorToComplexType(cursor, DataDTO.class, context);
                    DataPhotoRepositorio dataPhotoRepositorio = new DataPhotoRepositorio(context);
                    Map<String, String> searchparamsPhoto = new HashMap<String, String>();
                    searchparamsPhoto.put("IdData", String.valueOf(dataDTO.Id));
                    dataDTO.DataPhoto = dataPhotoRepositorio.ReadAllByPredicate(searchparamsPhoto);
                    list.add(dataDTO);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, context);
        }

        return list;
    }

    public void SendVisitasPendientes() {
        List<DataDTO> list = ReadAllByPredicate(null);

        for (int i = 0; i < list.size(); i++) {
            DataDTO dataDTO = list.get(i);
            Vector<DataPhotoDTO> dataPhotoDTOlist = dataDTO.DataPhoto;
            dataDTO.DataPhoto = null;
            SoapObject request = new SoapObject(Constantes.WS_TARGET_NAMESPACE, Constantes.WSMETHOD_UploadData);
            PropertyInfo info = new PropertyInfo();
            info.setType(dataDTO.getClass());
            info.setValue(dataDTO);
            info.setName(Constantes.ParametroUploadData_data);
            request.addProperty(info);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.implicitTypes = true;
            envelope.addMapping(Constantes.WS_TARGET_NAMESPACE, DataDTO.class.getSimpleName(), DataDTO.class);

            MarshalFloat marshalDouble = new MarshalFloat();
            marshalDouble.register(envelope);
            MarshalDate md = new MarshalDate();
            md.register(envelope);

            HttpTransportSE httpTransport = new HttpTransportSE(Constantes.SOAP_ADDRESS_CATALOGOS);

            try {
                httpTransport.call(Constantes.WS_TARGET_NAMESPACE + Constantes.WSMETHOD_UploadData, envelope);
                SoapObject response = (SoapObject) envelope.getResponse();

                AjaxResponse ajaxResponse = Extensions.ConvertSoap(response, AjaxResponse.class, context);

                if (ajaxResponse.Success) {
                    dataDTO.Id = (int) ajaxResponse.ReturnedObject;

                    for (int j = 0; j < dataPhotoDTOlist.size(); j++){
                        DataPhotoDTO dataPhotoDTO = dataPhotoDTOlist.get(j);
                        dataPhotoDTO.IdData = dataDTO.Id;
                        request = new SoapObject(Constantes.WS_TARGET_NAMESPACE, Constantes.WSMETHOD_UploadDataPhoto);
                        info = new PropertyInfo();
                        info.setType(dataPhotoDTO.getClass());
                        info.setValue(dataPhotoDTO);
                        info.setName(Constantes.ParametroUploadData_dataPhoto);
                        request.addProperty(info);

                        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        envelope.dotNet = true;
                        envelope.setOutputSoapObject(request);
                        envelope.implicitTypes = true;
                        envelope.addMapping(Constantes.WS_TARGET_NAMESPACE, DataPhotoDTO.class.getSimpleName(), DataPhotoDTO.class);

                        httpTransport = new HttpTransportSE(Constantes.SOAP_ADDRESS_CATALOGOS);

                        try {
                            httpTransport.call(Constantes.WS_TARGET_NAMESPACE + Constantes.WSMETHOD_UploadDataPhoto, envelope);
                            response = (SoapObject) envelope.getResponse();

                            ajaxResponse = Extensions.ConvertSoap(response, AjaxResponse.class, context);

                            if (ajaxResponse.Success) {
                                dataPhotoDTO.Id = (int) ajaxResponse.ReturnedObject;
                            }
                        } catch (Exception ex) {
                            LogErrorRepositorio.ArmaLogError(ex, context);
                        }
                    }
                }
            } catch (Exception ex) {
                LogErrorRepositorio.ArmaLogError(ex, context);
            }
        }
    }
}
