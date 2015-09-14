package beteam.viloco.trackcheck.repositorios;

import android.content.Context;

public class FotosRepositorio {
    private Context context;

    public FotosRepositorio(Context context) {
        this.context = context;
    }

//    public boolean EnviaFoto(FotosDTO fotosDTO) {
//        SoapObject request = new SoapObject(Constantes.WS_TARGET_NAMESPACE, Constantes.WSMETHOD_EnviaFoto);
//        PropertyInfo info = new PropertyInfo();
//        info.setType(fotosDTO.getClass());
//        info.setValue(fotosDTO);
//        info.setName(Constantes.EnviaFotoFoto);
//        request.addProperty(info);
//
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        envelope.dotNet = true;
//        envelope.setOutputSoapObject(request);
//        envelope.addMapping(Constantes.WS_TARGET_NAMESPACE, "FotosDTO", FotosDTO.class);
//
//        HttpTransportSE httpTransport = new HttpTransportSE(Constantes.SOAP_ADDRESS_NEGOCIO);
//
//        try {
//            httpTransport.call(Constantes.WS_TARGET_NAMESPACE + Constantes.WSMETHOD_EnviaFoto, envelope);
//            envelope.getResponse();
//            return true;
//        } catch (Exception ex) {
//            LogErrorRepositorio.ArmaLogError(ex, context);
//        }
//        return false;
//    }

//    public List<FotosDTO> ReadAll() throws CustomException {
//        SoapObject request = new SoapObject(Constantes.WS_TARGET_NAMESPACE, Constantes.WSMETHOD_ObtieneFotos);
//
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        envelope.dotNet = true;
//        envelope.setOutputSoapObject(request);
//
//        HttpTransportSE httpTransport = new HttpTransportSE(Constantes.SOAP_ADDRESS_NEGOCIO);
//        List<FotosDTO> list = new ArrayList<>();
//
//        try {
//            httpTransport.call(Constantes.WS_TARGET_NAMESPACE + Constantes.WSMETHOD_ObtieneFotos, envelope);
//
//            SoapObject response = (SoapObject) envelope.getResponse();
//
//            list = Extensions.ConvertSoapToList(response, FotosDTO.class, this.context);
//        } catch (Exception ex) {
//            LogErrorRepositorio.ArmaLogError(ex, this.context);
//        }
//
//        return list;
//    }
}
