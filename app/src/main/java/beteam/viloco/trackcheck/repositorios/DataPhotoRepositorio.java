package beteam.viloco.trackcheck.repositorios;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import beteam.viloco.trackcheck.dto.Constantes;
import beteam.viloco.trackcheck.dto.DataPhotoDTO;
import beteam.viloco.trackcheck.model.DatabaseHelper;
import beteam.viloco.trackcheck.util.Extensions;

import java.util.Map;
import java.util.Vector;

public class DataPhotoRepositorio {
    private Context context;

    public DataPhotoRepositorio(Context context) {
        this.context = context;
    }

    public Vector<DataPhotoDTO> ReadAllByPredicate(Map<String, String> searchparams) {
        Vector<DataPhotoDTO> list = new Vector<>();

        String query = "SELECT * FROM " + Constantes.DataPhoto + " WHERE 1=1";

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
                    list.add(Extensions.ConvertCursorToComplexType(cursor, DataPhotoDTO.class, context));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, context);
        }

        return list;
    }
}
