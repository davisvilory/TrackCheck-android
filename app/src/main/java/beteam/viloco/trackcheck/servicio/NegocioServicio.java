package beteam.viloco.trackcheck.servicio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import beteam.viloco.trackcheck.dto.AjaxResponse;
import beteam.viloco.trackcheck.dto.Constantes;
import beteam.viloco.trackcheck.dto.UserDTO;
import beteam.viloco.trackcheck.repositorios.LogErrorRepository;
import beteam.viloco.trackcheck.repositorios.UserRepository;
import beteam.viloco.trackcheck.util.CustomException;
import beteam.viloco.trackcheck.util.Extensions;

public class NegocioServicio {
    private static Context mContext;
    private static NegocioServicio instance;

    public NegocioServicio(Context context) {
        mContext = context;
        if (instance == null) instance = this;
    }

    public static NegocioServicio getInstance() {
        return instance;
    }

    /****
     * Login
     ****/
    public UserDTO AutenticaUsuario(UserDTO user) throws CustomException {
        UserRepository repo = new UserRepository(mContext);
        return repo.AuthenticateUser(user);
    }

    public boolean ExistsUpdate() throws CustomException {
        if (!Extensions.isConnectionAvailable(mContext))
            throw new CustomException("No hay acceso a internet. No se pudo verificar si existe una nueva versión");

        boolean result = false;

        try {
            SoapObject request = new SoapObject(Constantes.WS_TARGET_NAMESPACE, Constantes.WSMETHOD_ExistsUpdate);
            String versionName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            request.addProperty(Constantes.ParametroExistsUpdate_version, versionName);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(Constantes.SOAP_ADDRESS_MOBILE, 40000);

            httpTransport.call(Constantes.WS_TARGET_NAMESPACE + Constantes.WSMETHOD_ExistsUpdate, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            AjaxResponse ajaxResponse = Extensions.ConvertSoap(response, AjaxResponse.class, mContext);

            if (ajaxResponse.Success) {
                if (ajaxResponse.ReturnedObject instanceof Boolean) {
                    result = (boolean) ajaxResponse.ReturnedObject;
                }
            }
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("No se pudo verificar si existe una nueva versión");
        }

        return result;
    }

    public void UpdateApp() throws CustomException {
        try {
            if (!Extensions.isConnectionAvailable(mContext))
                throw new CustomException("No hay acceso a internet. No se pudo obtener la ultima versión");

            URL url = new URL(Constantes.UrlApkLastVersion);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            //c.setDoOutput(true);
            c.connect();

            String PATH = Environment.getExternalStorageDirectory() + "/download/";
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, Constantes.ApkName);
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(PATH + Constantes.ApkName)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            throw new CustomException("No se pudo obtener la ultima versión");
        }
    }
}
