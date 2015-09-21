package beteam.viloco.trackcheck.repositorios;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import beteam.viloco.trackcheck.dto.DataPhotoDTO;
import beteam.viloco.trackcheck.dto.Predicate;
import beteam.viloco.trackcheck.model.DatabaseHelper;
import beteam.viloco.trackcheck.util.CustomException;
import beteam.viloco.trackcheck.util.Extensions;

import java.util.ArrayList;

public class DataPhotoRepository {
    private Context context;

    public DataPhotoRepository(Context context) {
        this.context = context;
    }

    public ArrayList<DataPhotoDTO> ReadAllByPredicate(ArrayList<Predicate> predicates) throws CustomException {
        ArrayList<DataPhotoDTO> list = new ArrayList<>();

        String query = "SELECT * FROM " + DatabaseHelper.DataPhoto + " WHERE 1=1";

        if (predicates != null)
            for (Predicate predicate : predicates) {
                query += " " + predicate.Comparison + " " + predicate.Column + " = " + predicate.Value;
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
            LogErrorRepository.BuildLogError(ex, context);
            throw new CustomException("Hubo un error al consultar la base");
        }

        return list;
    }
}
