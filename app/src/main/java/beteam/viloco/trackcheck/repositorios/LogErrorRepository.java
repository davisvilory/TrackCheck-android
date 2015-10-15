package beteam.viloco.trackcheck.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Xml;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import beteam.viloco.trackcheck.R;
import beteam.viloco.trackcheck.activity.BaseClass;
import beteam.viloco.trackcheck.dto.AjaxResponse;
import beteam.viloco.trackcheck.dto.Constantes;
import beteam.viloco.trackcheck.dto.LogErrorDTO;
import beteam.viloco.trackcheck.model.DatabaseHelper;
import beteam.viloco.trackcheck.util.CustomException;
import beteam.viloco.trackcheck.util.Extensions;

public class LogErrorRepository {
    private Context mContext;

    public LogErrorRepository(Context context) {
        this.mContext = context;
    }

    public static void BuildLogError(Exception ex, Context context) {
        LogErrorDTO error = new LogErrorDTO();

        try {
            error.ErrorInterno = ex.toString() + " Device: " + Build.MODEL + " " + Build.VERSION.SDK_INT + " " + Build.BRAND;
            error.ErrorResumido = ex.getMessage() == null ? "" : ex.getMessage();
            error.Fecha = new Date();
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            error.Modulo = stackTraceElements[3].getClassName() + "." + stackTraceElements[3].getMethodName();

            RegistraErrorTXT(error, context);
        } catch (Exception e) {
            RegistraErrorTXT(error, context);
        }
    }

    private static void RegistraErrorTXT(LogErrorDTO error, Context context) {
        File file = context.getFileStreamPath(Constantes.LogErrorNombreArchivo);

        try {
            //FileOutputStream fos = null;
            if (!file.exists()) {
                //new File(context.getFilesDir(), Constantes.LogErrorNombreArchivo);
                FileOutputStream fos = context.openFileOutput(Constantes.LogErrorNombreArchivo, Context.MODE_APPEND);
                XmlSerializer serializer = Xml.newSerializer();
                StringWriter writer = new StringWriter();
                serializer.setOutput(writer);
                serializer.startDocument("UTF-8", true);
                serializer.startTag(null, "logerror");
                serializer.endTag(null, "logerror");
                serializer.endDocument();
                serializer.flush();
                String dataWrite = writer.toString();
                fos.write(dataWrite.getBytes());
                fos.close();
            }

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            Element root = document.getDocumentElement();

            Element errorTag = document.createElement("error");

            Element modulo = document.createElement("modulo");
            modulo.appendChild(document.createTextNode(error.Modulo));
            errorTag.appendChild(modulo);

            Element fecha = document.createElement("fecha");
            fecha.appendChild(document.createTextNode(DateFormat.format("yyyy-MM-dd hh:mm:ss", error.Fecha).toString()));
            errorTag.appendChild(fecha);

            Element resumido = document.createElement("resumido");
            resumido.appendChild(document.createTextNode(error.ErrorResumido.replace("'", "").replace("\"", "")));
            errorTag.appendChild(resumido);

            Element interno = document.createElement("interno");
            interno.appendChild(document.createTextNode(error.ErrorInterno.replace("'", "").replace("\"", "")));
            errorTag.appendChild(interno);

            root.appendChild(errorTag);

            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

//            String ExceptionLog = String.format("<logerror><modulo>%s</modulo><fecha>%s</fecha><resumido>%s</resumido><interno>%s</interno></logerror>%n",
//                    error.Modulo, DateFormat.format("yyyy-MM-dd hh:mm:ss", error.Fecha),
//                    error.ErrorResumido.replace("'", "").replace("\"", ""),
//                    error.ErrorInterno.replace("'", "").replace("\"", ""));

            //fos = context.openFileOutput(Constantes.LogErrorNombreArchivo, Context.MODE_APPEND);
            //fos.write(ExceptionLog.getBytes());
            //fos.close();
        } catch (DOMException ex) { //Error producido por que aun no esta formateado el archivo
            file.delete();
            RegistraErrorTXT(error, context);
        } catch (Exception e) {
            BaseClass.ToastAlert(context.getString(R.string.Mensaje_ErrorInterno), context);
        }
    }

    public boolean SendLogError() throws CustomException {
        if (!Extensions.isConnectionAvailable(mContext)) return false;

        try {
            SoapObject request = new SoapObject(Constantes.WS_TARGET_NAMESPACE, Constantes.WSMETHOD_LogError);
            File file = mContext.getFileStreamPath(Constantes.LogErrorNombreArchivo);
            if (!file.exists()) return true;

            FileInputStream fis = new FileInputStream(file);
            StringBuilder fileContent = new StringBuilder();
            byte[] buffer = new byte[1024];
            int n;
            while ((n = fis.read(buffer)) != -1) {
                fileContent.append(new String(buffer, 0, n));
            }
            fis.close();
            request.addProperty(Constantes.ParametroLogError_error, fileContent.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            System.setProperty("http.keepAlive", "false");
            HttpTransportSE httpTransport = new HttpTransportSE(Constantes.SOAP_ADDRESS_MOBILE, 40000);

            httpTransport.reset();
            httpTransport.call(Constantes.WS_TARGET_NAMESPACE + Constantes.WSMETHOD_LogError, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            AjaxResponse ajaxResponse = Extensions.ConvertSoap(response, AjaxResponse.class, mContext);
            if (ajaxResponse.Success) {
                return file.delete();
            }
        } catch (SocketTimeoutException ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("No se conecto con el servidor");
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("No se pudo enviar los datos");
        }

        return false;
    }
}
