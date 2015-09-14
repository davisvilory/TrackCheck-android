package beteam.viloco.trackcheck.repositorios;

import android.content.Context;

import beteam.viloco.trackcheck.activity.BaseClass;
import beteam.viloco.trackcheck.dto.Constantes;
import beteam.viloco.trackcheck.dto.LogErrorDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class LogErrorRepositorio {
    public static void ArmaLogError(Exception ex, Context context) {
        LogErrorDTO error = new LogErrorDTO();

        try {
            error.ErrorInterno = ex.toString();
            error.ErrorResumido = ex.getMessage() == null ? "" : ex.getMessage();
            error.Fecha = new Date();
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            error.Modulo = stackTraceElements[3].getClassName() + "." + stackTraceElements[3].getMethodName();

            //Guardar en la base

            RegistraErrorTXT(error, context);
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

            String ExceptionLog = String.format("Modulo: %s, Fecha: %s, Mensaje: %s, Excepcion: %s%n",
                    error.Modulo, android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", error.Fecha),
                    error.ErrorResumido.replace("'", "").replace("\"", ""),
                    error.ErrorInterno.replace("'", "").replace("\"", ""));

            outputStream = context.openFileOutput(Constantes.LogErrorNombreArchivo, Context.MODE_APPEND);
            outputStream.write(ExceptionLog.getBytes());
            outputStream.close();
        } catch (Exception e) {
            BaseClass.ToastAlert("Error interno de sistema", context);
        }
    }
}
