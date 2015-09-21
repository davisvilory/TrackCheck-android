package beteam.viloco.trackcheck.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import beteam.viloco.trackcheck.R;
import beteam.viloco.trackcheck.activity.BaseClass;
import beteam.viloco.trackcheck.dto.AjaxResponse;
import beteam.viloco.trackcheck.dto.Constantes;
import beteam.viloco.trackcheck.dto.DataDTO;
import beteam.viloco.trackcheck.dto.LogErrorDTO;
import beteam.viloco.trackcheck.model.DatabaseHelper;
import beteam.viloco.trackcheck.util.CustomException;
import beteam.viloco.trackcheck.util.Extensions;

import java.io.File;
import java.io.FileOutputStream;
import java.net.SocketTimeoutException;
import java.util.Date;

public class LogErrorRepository {
    public static void BuildLogError(Exception ex, Context context) {
        LogErrorDTO error = new LogErrorDTO();

        try {
            error.ErrorInterno = ex.toString();
            error.ErrorResumido = ex.getMessage() == null ? "" : ex.getMessage();
            error.Fecha = new Date();
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            error.Modulo = stackTraceElements[3].getClassName() + "." + stackTraceElements[3].getMethodName();

            //TODO
            // Guardar en la base

            RegistraErrorTXT(error, context);

            new Task(context, error).execute((Void) null);
        } catch (Exception e) {
            RegistraErrorTXT(error, context);
        }
    }

    private static void RegistraErrorTXT(LogErrorDTO error, Context context) {
        try {
            FileOutputStream outputStream;
            File file = context.getFileStreamPath(Constantes.LogErrorNombreArchivo);
            if (!file.exists()) {
                new File(context.getFilesDir(), Constantes.LogErrorNombreArchivo);
            }

            String ExceptionLog = String.format("Modulo: %s, Fecha: %s, Mensaje: %s, Exception: %s%n",
                    error.Modulo, android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", error.Fecha),
                    error.ErrorResumido.replace("'", "").replace("\"", ""),
                    error.ErrorInterno.replace("'", "").replace("\"", ""));

            outputStream = context.openFileOutput(Constantes.LogErrorNombreArchivo, Context.MODE_APPEND);
            outputStream.write(ExceptionLog.getBytes());
            outputStream.close();
        } catch (Exception e) {
            BaseClass.ToastAlert(context.getString(R.string.Mensaje_ErrorInterno), context);
        }
    }

    static class Task extends AsyncTask<Void, Void, Boolean> {
        Context mContext;
        LogErrorDTO mError;

        Task(Context context, LogErrorDTO errorDTO) {
            mContext = context;
            mError = errorDTO;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                SoapObject request = new SoapObject(Constantes.WS_TARGET_NAMESPACE, Constantes.WSMETHOD_LogError);
                PropertyInfo info = new PropertyInfo();
                info.setType(mError.getClass());
                info.setValue(mError);
                info.setName(Constantes.ParametroLogError_error);
                request.addProperty(info);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                envelope.addMapping(Constantes.WS_TARGET_NAMESPACE, LogErrorDTO.class.getSimpleName(), LogErrorDTO.class);

                MarshalDate md = new MarshalDate();
                md.register(envelope);

                HttpTransportSE httpTransport = new HttpTransportSE(Constantes.SOAP_ADDRESS_MOBILE, 40000);

                try {
                    httpTransport.call(Constantes.WS_TARGET_NAMESPACE + Constantes.WSMETHOD_LogError, envelope);

                    SoapObject response = (SoapObject) envelope.getResponse();

                    AjaxResponse ajaxResponse = Extensions.ConvertSoap(response, AjaxResponse.class, mContext);

                    if (ajaxResponse.Success)
                        return true;
                } catch (Exception ex) {
                }

                return false;
            } catch (Exception ex) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            try {

            } catch (Exception ex) {
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
