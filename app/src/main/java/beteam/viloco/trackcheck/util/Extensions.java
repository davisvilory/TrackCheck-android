package beteam.viloco.trackcheck.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import beteam.viloco.trackcheck.repositorios.LogErrorRepository;
import beteam.viloco.trackcheck.repositorios.Mapper;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Extensions {
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null && ni.isConnected() && ni.isConnectedOrConnecting() && ni.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public static String EmptyOrNullOrWhiteSpace(String s) {
        return (s != null && (!"null".equals(s)) && (!TextUtils.isEmpty(s))) ? s : "";
    }

    public static Bitmap decodeFile(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int newWidth = width;
        int newHeight = height;
        final int desiredSize = 700; //pixels
        float scale = 1;

        while (newWidth > desiredSize && newHeight > desiredSize) {
            scale -= 0.1;
            newWidth = (int) (newWidth * scale);
            newHeight = (int) (newHeight * scale);
        }

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    public static Bitmap getScaledBitmap(String picturePath, int width, int height) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(picturePath, sizeOptions);
    }

    public static Bitmap getScaledBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static <T> ArrayList<T> ConvertSoapToList(SoapObject object, Class<T> klazz, Context context) throws Exception {
        int count = object.getPropertyCount();
        ArrayList<T> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            SoapObject soapObject = (SoapObject) object.getProperty(i);

            list.add((T) FillSoapObjectType(soapObject, Mapper.InstanceOfObjectType(klazz.getSimpleName()), context));
        }

        return list;
    }

    public static <T> T ConvertSoap(SoapObject object, Class<T> klazz, Context context) throws Exception {
        return (T) FillSoapObjectType(object, Mapper.InstanceOfObjectType(klazz.getSimpleName()), context);
    }

    private static Object FillSoapObjectType(SoapObject soapObject, Object object, Context context) throws Exception {
        int propertyCount = soapObject.getPropertyCount();
        Field[] fields = object.getClass().getDeclaredFields();

        for (int j = 0; j < propertyCount; j++) {
            PropertyInfo pi = new PropertyInfo();
            soapObject.getPropertyInfo(j, pi);

            if (pi.getValue() != null) {
                if (pi.type == SoapPrimitive.class) {
                    for (Field field : fields) {
                        if (field.getName().equals(pi.name)) {
                            try {
                                if (field.getType() == Integer.TYPE)
                                    field.setInt(object, Integer.parseInt(soapObject.getProperty(j).toString()));
                                else if (field.getType() == Double.TYPE)
                                    field.setDouble(object, Double.parseDouble(soapObject.getProperty(j).toString()));
                                else if (field.getType() == Float.TYPE)
                                    field.setFloat(object, Float.parseFloat(soapObject.getProperty(j).toString()));
                                else if (field.getType() == Boolean.class)
                                    field.set(object, Boolean.parseBoolean(soapObject.getProperty(j).toString()));
                                else if (field.getType() == String.class)
                                    field.set(object, soapObject.getProperty(j).toString());
                                break;
                            } catch (IllegalAccessException ex) {
                                LogErrorRepository.BuildLogError(ex, context);
                            }
                        }
                    }
                } else if (pi.type == SoapObject.class) {
                    for (Field field : fields) {
                        if (field.getName().equals(pi.name)) {
                            try {
                                if (field.getType() == Object.class) {
                                    SoapObject ot = (SoapObject) soapObject.getProperty(j);
                                    if (ot.getName().toLowerCase().contains("arrayof")) {
                                        field.set(object, ConvertSoapToList(ot, Mapper.InstanceOfObjectType(ot.getName().toLowerCase().replace("arrayof", "")).getClass(), context));
                                    } else
                                        field.set(object, FillSoapObjectType(ot, Mapper.InstanceOfObjectType(ot.getName()), context));
                                } else if (field.getType() == List.class) {
                                    //TODO
                                } else {
                                    Object tempObject = Mapper.InstanceOfObjectType(field.getClass().getSimpleName());
                                    if (tempObject == null)
                                        field.set(object, null);
                                    else
                                        field.set(object, FillSoapObjectType((SoapObject) soapObject.getProperty(j), Mapper.InstanceOfObjectType(field.getClass().getSimpleName()), context));
                                }
                                break;
                            } catch (IllegalAccessException ex) {
                                LogErrorRepository.BuildLogError(ex, context);
                            }
                        }
                    }
                } else {
                    for (Field field : fields) {
                        if (field.getName().equals(pi.name)) {
                            try {
                                field.set(object, soapObject.getProperty(j));
                            } catch (IllegalAccessException ex) {
                                LogErrorRepository.BuildLogError(ex, context);
                            }
                        }
                    }
                }
            }
        }

        return object;
    }

    public static <T> T ConvertCursorToComplexType(Cursor cursor, Class<T> klazz, Context context) throws IOException, ClassNotFoundException {
        Object object = Mapper.InstanceOfObjectType(klazz.getSimpleName());

        Field[] fields = object.getClass().getDeclaredFields();
        int columnCount = cursor.getColumnCount();

        for (int j = 0; j < columnCount; j++) {
            if (!cursor.isNull(j)) {
                for (Field field : fields) {
                    if (field.getName().equals(cursor.getColumnName(j))) {
                        try {
                            if (field.getType() == Integer.TYPE)
                                field.setInt(object, Integer.parseInt(cursor.getString(j)));
                            else if (field.getType() == Double.TYPE)
                                field.setDouble(object, Double.parseDouble(cursor.getString(j)));
                            else if (field.getType() == Float.TYPE)
                                field.setFloat(object, Float.parseFloat(cursor.getString(j)));
                            else if (field.getType() == BigDecimal.class)
                                field.set(object, new BigDecimal(cursor.getString(j)));
                            else if (field.getType() == String.class)
                                field.set(object, cursor.getString(j));
                            else if (field.getType() == Date.class)
                                field.set(object, new Date(cursor.getLong(j)));
                            break;
                        } catch (IllegalAccessException ex) {
                            LogErrorRepository.BuildLogError(ex, context);
                        }
                    }
                }
            }
        }

        return (T) object;
    }

    public static ContentValues ComplexTypeInsert(Object object, Context context) throws IOException, ClassNotFoundException {
        ContentValues values = new ContentValues();

        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                if (field.getType() == Double.TYPE)
                    values.put(field.getName(), field.get(object).toString());
                else if (field.getType() == Float.TYPE)
                    values.put(field.getName(), field.get(object).toString());
                else if (field.getType() == String.class ||
                        field.getType() == Integer.TYPE ||
                        field.getType() == Short.TYPE ||
                        field.getType() == BigDecimal.class)
                    values.put(field.getName(), field.get(object).toString());
            } catch (Exception ex) {
                LogErrorRepository.BuildLogError(ex, context);
            }
        }

        return values;
    }
}
