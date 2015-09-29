package beteam.viloco.trackcheck.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import beteam.viloco.trackcheck.dto.AjaxResponse;
import beteam.viloco.trackcheck.dto.Constantes;
import beteam.viloco.trackcheck.dto.DataDTO;
import beteam.viloco.trackcheck.dto.DataPhotoDTO;
import beteam.viloco.trackcheck.dto.Predicate;
import beteam.viloco.trackcheck.model.DatabaseHelper;
import beteam.viloco.trackcheck.util.CustomException;
import beteam.viloco.trackcheck.util.Extensions;

import org.kobjects.isodate.IsoDate;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.TimeZone;

public class DataRepository {
    private Context mContext;

    public DataRepository(Context context) {
        this.mContext = context;
    }

    public DataDTO ReadFirstByPredicate(ArrayList<Predicate> predicates) throws CustomException {
        DataDTO dto = null;

        String query = "SELECT *"
                + " FROM " + DatabaseHelper.Data + " AS Us"
                + " WHERE 1=1";

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
                    dto = Extensions.ConvertCursorToComplexType(cursor, DataDTO.class, mContext);
                    DataPhotoRepository dataPhotoRepository = new DataPhotoRepository(mContext);
                    ArrayList<Predicate> predicatePhoto = new ArrayList<>();
                    predicatePhoto.add(new Predicate(DataPhotoDTO.IdDataCNProp, String.valueOf(dto.Id), Predicate.ComparisonPredicate.AND));
                    dto.DataPhoto = dataPhotoRepository.ReadAllByPredicate(predicatePhoto);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return dto;
    }

    public boolean InsertData(DataDTO dataDTO) throws CustomException {
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(DataDTO.IdUserCNProp, dataDTO.IdUser);
            values.put(DataDTO.IdTerritoryCNProp, dataDTO.IdTerritory);
            values.put(DataDTO.IdZoneCNProp, dataDTO.IdZone);
            values.put(DataDTO.IdBusinessTypeCNProp, dataDTO.IdBusinessType);
            values.put(DataDTO.BusinessNameCNProp, dataDTO.BusinessName);
            values.put(DataDTO.SerialNumberCNProp, dataDTO.SerialNumber);
            values.put(DataDTO.LatitudeCNProp, dataDTO.Latitude);
            values.put(DataDTO.LongitudeCNProp, dataDTO.Longitude);
            values.put(DataDTO.StreetCNProp, dataDTO.Street);
            values.put(DataDTO.NumberCNProp, dataDTO.Number.length() > 10 ? dataDTO.Number.substring(0, 10) : dataDTO.Number);
            values.put(DataDTO.ColonyCNProp, dataDTO.Colony);
            values.put(DataDTO.DelegationTownCNProp, dataDTO.DelegationTown);
            values.put(DataDTO.CityCNProp, dataDTO.City);
            values.put(DataDTO.StateCNProp, dataDTO.State);
            values.put(DataDTO.PostalCodeCNProp, dataDTO.PostalCode.length() > 5 ? dataDTO.PostalCode.substring(0, 5) : dataDTO.PostalCode);
            values.put(DataDTO.PosterCNProp, dataDTO.Poster);
            values.put(DataDTO.ThermoRoshpack60x40CNProp, dataDTO.ThermoRoshpack60x40);
            values.put(DataDTO.ThermoTi2260x40CNProp, dataDTO.ThermoTi2260x40);
            values.put(DataDTO.ElectroGotaCNProp, dataDTO.ElectroGota);
            values.put(DataDTO.ElectroImagenCNProp, dataDTO.ElectroImagen);
            values.put(DataDTO.BanderolaCNProp, dataDTO.Banderola);
            values.put(DataDTO.CalcomanisCNProp, dataDTO.Calcomanis);
            values.put(DataDTO.CaballeteCambioAceiteCNProp, dataDTO.CaballeteCambioAceite);
            values.put(DataDTO.CaballeteVentaAceiteCNProp, dataDTO.CaballeteVentaAceite);
            values.put(DataDTO.Lona2x1RoshpackCNProp, dataDTO.Lona2x1Roshpack);
            values.put(DataDTO.Lona2x1ImagenMecanicoCNProp, dataDTO.Lona2x1ImagenMecanico);
            values.put(DataDTO.DateCNProp, dataDTO.Date.getTime());
            dataDTO.Id = (int) db.insert(DatabaseHelper.Data, null, values);

            if (dataDTO.Id > 0) {
                for (int i = 0; i < dataDTO.DataPhoto.size(); i++) {
                    values = new ContentValues();
                    values.put(DataPhotoDTO.IdDataCNProp, dataDTO.Id);
                    values.put(DataPhotoDTO.PhotoCNProp, dataDTO.DataPhoto.get(i).Photo);
                    db.insert(DatabaseHelper.DataPhoto, null, values);
                }
            } else {
                throw new CustomException("Hubo un error al guardar la visita");
            }

            db.close();
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return true;
    }

    public ArrayList<DataDTO> ReadAllByPredicate(ArrayList<Predicate> predicates) throws CustomException {
        ArrayList<DataDTO> list = new ArrayList<>();

        String query = "SELECT * FROM " + DatabaseHelper.Data +
                " WHERE 1=1";

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
                    list.add(Extensions.ConvertCursorToComplexType(cursor, DataDTO.class, mContext));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return list;
    }

    public ArrayList<DataDTO> GetDataPending() throws CustomException {
        ArrayList<DataDTO> list = new ArrayList<>();

        String query = "SELECT Count(Id) FROM " + DatabaseHelper.Data;

        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) > 0) {
                    query = "SELECT d.*, (SELECT Count(dp." + DataPhotoDTO.IdCNProp + ") FROM " + DatabaseHelper.DataPhoto +
                            " dp WHERE dp." + DataPhotoDTO.IdDataCNProp + " = d." + DataDTO.IdCNProp + ") " +
                            DataDTO.DataPhotoCountCNProp + " FROM " + DatabaseHelper.Data + " d";

                    try {
                        cursor = db.rawQuery(query, null);

                        if (cursor.moveToFirst()) {
                            do {
                                list.add(Extensions.ConvertCursorToComplexType(cursor, DataDTO.class, mContext));
                            } while (cursor.moveToNext());
                        }
                    } catch (Exception ex) {
                        LogErrorRepository.BuildLogError(ex, mContext);
                        throw new CustomException("Hubo un error al consultar la base");
                    }
                }
            }
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return list;
    }

    public int SendDataPending(DataDTO dataDTO) throws CustomException {
        if (!Extensions.isConnectionAvailable(mContext)) return 0;

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
        envelope.addMapping(Constantes.WS_TARGET_NAMESPACE, DataDTO.class.getSimpleName(), DataDTO.class);

        MarshalFloat marshalFloat = new MarshalFloat();
        marshalFloat.register(envelope);
        MarshalDate md = new MarshalDate();
        md.register(envelope);

        String fecha = IsoDate.dateToString(dataDTO.Date, 3);

        HttpTransportSE httpTransport = new HttpTransportSE(Constantes.SOAP_ADDRESS_MOBILE, 40000);

        try {
            httpTransport.call(Constantes.WS_TARGET_NAMESPACE + Constantes.WSMETHOD_UploadData, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            AjaxResponse ajaxResponse = Extensions.ConvertSoap(response, AjaxResponse.class, mContext);

            if (ajaxResponse.Success) {
                if (ajaxResponse.ReturnedObject instanceof Integer) {
                    dataDTO.IdServer = (int) ajaxResponse.ReturnedObject;

                    DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    try {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DataDTO.IdServerCNProp, dataDTO.IdServer);
                        db.update(DatabaseHelper.Data, contentValues, DataDTO.IdCNProp + "=" + dataDTO.Id, null);

                        db.close();
                    } catch (Exception ex) {
                        LogErrorRepository.BuildLogError(ex, mContext);
                        throw new CustomException("Hubo un error al consultar la base");
                    }

                    return dataDTO.IdServer;
                }
            }
        } catch (SocketTimeoutException ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("No se conecto con el servidor");
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("No se pudo enviar los datos");
        }

        return 0;
    }

    public int SendDataPhotoPending(DataPhotoDTO dataPhotoDTO) throws CustomException {
        if (!Extensions.isConnectionAvailable(mContext)) return 0;

        SoapObject request = new SoapObject(Constantes.WS_TARGET_NAMESPACE, Constantes.WSMETHOD_UploadDataPhoto);
        PropertyInfo info = new PropertyInfo();
        info.setType(dataPhotoDTO.getClass());
        info.setValue(dataPhotoDTO);
        info.setName(Constantes.ParametroUploadData_dataPhoto);
        request.addProperty(info);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        envelope.implicitTypes = true;
        envelope.addMapping(Constantes.WS_TARGET_NAMESPACE, DataPhotoDTO.class.getSimpleName(), DataPhotoDTO.class);

        HttpTransportSE httpTransport = new HttpTransportSE(Constantes.SOAP_ADDRESS_MOBILE, 40000);

        try {
            httpTransport.call(Constantes.WS_TARGET_NAMESPACE + Constantes.WSMETHOD_UploadDataPhoto, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            AjaxResponse ajaxResponse = Extensions.ConvertSoap(response, AjaxResponse.class, mContext);

            if (ajaxResponse.Success) {
                if (ajaxResponse.ReturnedObject instanceof Integer) {
                    dataPhotoDTO.IdServer = (int) ajaxResponse.ReturnedObject;

                    DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    try {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DataPhotoDTO.IdServerCNProp, dataPhotoDTO.IdServer);
                        db.update(DatabaseHelper.DataPhoto, contentValues, DataPhotoDTO.IdCNProp + "=" + dataPhotoDTO.Id, null);

                        db.close();
                    } catch (Exception ex) {
                        LogErrorRepository.BuildLogError(ex, mContext);
                        throw new CustomException("Hubo un error al consultar la base");
                    }

                    return dataPhotoDTO.IdServer;
                }
            }
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("No se pudo enviar los datos");
        }

        return 0;
    }

    public boolean DeleteDataAndPhoto(int Id) throws CustomException {
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean result = true;

        String query = "SELECT Id"
                + " FROM " + DatabaseHelper.DataPhoto + " AS Us"
                + " WHERE " + DataPhotoDTO.IdDataCNProp + "=" + Id;

        try {
            ArrayList<Integer> idsPhoto = new ArrayList<>();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    idsPhoto.add(cursor.getInt(0));
                } while (cursor.moveToNext());
            }

            for (int i = 0; i < idsPhoto.size(); i++) {
                if (db.delete(DatabaseHelper.DataPhoto, DataPhotoDTO.IdCNProp + " = " + idsPhoto.get(i), null) <= 0)
                    result = false;
            }

            if (db.delete(DatabaseHelper.Data, DataDTO.IdCNProp + " = " + Id, null) <= 0)
                result = false;

            db.close();
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return result;
    }
}
