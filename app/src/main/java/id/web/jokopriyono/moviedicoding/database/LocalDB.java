package id.web.jokopriyono.moviedicoding.database;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import id.web.jokopriyono.moviedicoding.api.carifilm.CariResponse;
import id.web.jokopriyono.moviedicoding.api.carifilm.ResultsItem;

public class LocalDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LokalDB.db";
    private static final int DATABASE_VERSION = 1;

    private LocalDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql;
        sql = "create table "+DB.TABLE_FILM+" ( "+DB.COLUMN_ID_FILM+" integer not null," +
                DB.COLUMN_TYPE+" int not null, " +
                DB.COLUMN_VOTE_COUNT+" text null, " +
                DB.COLUMN_VIDEO+" text null, " +
                DB.COLUMN_VOTE_AVERAGE+" text null, " +
                DB.COLUMN_TITLE+" text null, " +
                DB.COLUMN_POPULARITY+" text null, " +
                DB.COLUMN_POSTER_PATH+" text null, " +
                DB.COLUMN_ORIGINAL_LANG+" text null, " +
                DB.COLUMN_ORIGINAL_TITLE+" text null, " +
                DB.COLUMN_BACKDROP_PATH+" text null, " +
                DB.COLUMN_ADULT+" text null, " +
                DB.COLUMN_OVERVIEW+" text null, " +
                DB.COLUMN_RELEASE_DATE+" text null);";
        sqLiteDatabase.execSQL(sql);
    }

    private void insertFilm(Context context, int type, int idFilm, String voteCount, String video, String voteAverage, String title, String popularity,
                            String posterPath, String originalLang, String originalTitle, String backdropPath, String adult, String overview,
                            String releaseDate){
        LocalDB db = new LocalDB(context);
        SQLiteDatabase database = db.getWritableDatabase();
        database.execSQL("INSERT INTO "+DB.TABLE_FILM+" ( "
                +DB.COLUMN_TYPE + ","
                +DB.COLUMN_ID_FILM + ","
                +DB.COLUMN_VOTE_COUNT + ","
                +DB.COLUMN_VIDEO + ","
                +DB.COLUMN_VOTE_AVERAGE + ","
                +DB.COLUMN_TITLE + ","
                +DB.COLUMN_POPULARITY + ","
                +DB.COLUMN_POSTER_PATH + ","
                +DB.COLUMN_ORIGINAL_LANG + ","
                +DB.COLUMN_ORIGINAL_TITLE + ","
                +DB.COLUMN_BACKDROP_PATH + ","
                +DB.COLUMN_ADULT + ","
                +DB.COLUMN_OVERVIEW + ","
                +DB.COLUMN_RELEASE_DATE + " ) VALUES ("
                +type + ","
                +idFilm + ",'"
                +voteCount +"','"
                +video +"','"
                +voteAverage +"','"
                +title.replace("'", "''") +"','"
                +popularity +"','"
                +posterPath +"','"
                +originalLang +"','"
                +originalTitle.replace("'", "''") +"','"
                +backdropPath +"','"
                +adult +"','"
                +overview.replace("'", "''") +"','"
                +releaseDate +"');");
    }

    private int checkIdFilm(Context context, int idFilm){
        LocalDB db = new LocalDB(context);
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+DB.TABLE_FILM+" WHERE "+DB.COLUMN_ID_FILM +" = "+idFilm, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    private int getTypeFilm(Context context, int idFilm){
        LocalDB db = new LocalDB(context);
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+DB.TABLE_FILM+" WHERE "+DB.COLUMN_ID_FILM +" = "+idFilm, null);
        cursor.moveToFirst();
        int type = getIntValue(cursor, DB.COLUMN_TYPE);
        cursor.close();
        return type;
    }

    private void updateFilm(Context context, int idFilm, int type){
        LocalDB db = new LocalDB(context);
        SQLiteDatabase database = db.getReadableDatabase();
        database.rawQuery("UPDATE "+DB.TABLE_FILM+" SET "+DB.COLUMN_TYPE +" = "+type +
                " WHERE "+ DB.COLUMN_ID_FILM + " = " + idFilm, null);
    }

    public static void saveNowPlaying(Activity activity, CariResponse nowPlaying) {
        LocalDB localDB = new LocalDB(activity);
        int type = DB.TYPE_NOW_PLAYING;
        for (int i=0; i<nowPlaying.getResults().size(); i++) {
            if (localDB.checkIdFilm(activity, nowPlaying.getResults().get(i).getId()) != 0){
                switch (localDB.getTypeFilm(activity, nowPlaying.getResults().get(i).getId())){
                    case DB.TYPE_NOW_PLAYING:
                    case DB.TYPE_BOTH:
                        break;
                    case DB.TYPE_UP_COMING:
                    default:
                        type = DB.TYPE_BOTH;
                        localDB.updateFilm(activity, nowPlaying.getResults().get(i).getId(), type);
                        break;
                }
            } else {
                localDB.insertFilm(activity, type,
                        nowPlaying.getResults().get(i).getId(),
                        String.valueOf(nowPlaying.getResults().get(i).getVoteCount()),
                        nowPlaying.getResults().get(i).getVideo(),
                        nowPlaying.getResults().get(i).getVoteAverage().toString(),
                        nowPlaying.getResults().get(i).getTitle(),
                        nowPlaying.getResults().get(i).getPopularity().toString(),
                        nowPlaying.getResults().get(i).getPosterPath(),
                        nowPlaying.getResults().get(i).getOriginalLanguage(),
                        nowPlaying.getResults().get(i).getOriginalLanguage(),
                        nowPlaying.getResults().get(i).getBackdropPath(),
                        String.valueOf(nowPlaying.getResults().get(i).isAdult()),
                        nowPlaying.getResults().get(i).getOverview(),
                        nowPlaying.getResults().get(i).getReleaseDate());
            }
        }
    }

    public static void saveUpComing(Activity activity, CariResponse upComing) {
        LocalDB localDB = new LocalDB(activity);
        SQLiteDatabase db = localDB.getWritableDatabase();
        db.execSQL("DELETE FROM "+ DB.TABLE_FILM);
        for (int i=0; i<upComing.getResults().size(); i++){
            localDB.insertFilm(activity, DB.TYPE_UP_COMING,
                    upComing.getResults().get(i).getId(),
                    String.valueOf(upComing.getResults().get(i).getVoteCount()),
                    upComing.getResults().get(i).getVideo(),
                    upComing.getResults().get(i).getVoteAverage().toString(),
                    upComing.getResults().get(i).getTitle(),
                    upComing.getResults().get(i).getPopularity().toString(),
                    upComing.getResults().get(i).getPosterPath(),
                    upComing.getResults().get(i).getOriginalLanguage(),
                    upComing.getResults().get(i).getOriginalLanguage(),
                    upComing.getResults().get(i).getBackdropPath(),
                    String.valueOf(upComing.getResults().get(i).isAdult()),
                    upComing.getResults().get(i).getOverview(),
                    upComing.getResults().get(i).getReleaseDate());
        }

    }

    public static CariResponse getAllNowPlaying(Context context){
        CariResponse data = new CariResponse();
        List<ResultsItem> items = new ArrayList<>();
        LocalDB localDB = new LocalDB(context);
        SQLiteDatabase db = localDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DB.TABLE_FILM +
                " WHERE " + DB.COLUMN_TYPE + " = " + DB.TYPE_NOW_PLAYING +
                " OR " + DB.COLUMN_TYPE + " = " + DB.TYPE_BOTH, null);
        if (cursor.getCount()!=0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                ResultsItem isinya = new ResultsItem();
                isinya.setId(getIntValue(cursor, DB.COLUMN_ID_FILM));
                isinya.setTitle(getStringValue(cursor, DB.COLUMN_TITLE));
                isinya.setOverview(getStringValue(cursor, DB.COLUMN_OVERVIEW));
                isinya.setOriginalLanguage(getStringValue(cursor, DB.COLUMN_ORIGINAL_LANG));
                isinya.setOriginalTitle(getStringValue(cursor, DB.COLUMN_ORIGINAL_TITLE));
                isinya.setVideo(getStringValue(cursor, DB.COLUMN_VIDEO));
                isinya.setGenreIds(null);
                isinya.setPosterPath(getStringValue(cursor, DB.COLUMN_POSTER_PATH));
                isinya.setBackdropPath(getStringValue(cursor, DB.COLUMN_BACKDROP_PATH));
                isinya.setReleaseDate(getStringValue(cursor, DB.COLUMN_RELEASE_DATE));
                isinya.setVoteAverage(getDoubleValue(cursor, DB.COLUMN_VOTE_AVERAGE));
                isinya.setPopularity(getDoubleValue(cursor, DB.COLUMN_POPULARITY));
                isinya.setAdult(getStringValue(cursor, DB.COLUMN_ADULT));
                isinya.setVoteCount(getIntValue(cursor, DB.COLUMN_VOTE_COUNT));
                items.add(isinya);
            }
        }
        cursor.close();
        data.setResults(items);
        return data;
    }

    public static CariResponse getAllUpComing(Context context){
        CariResponse data = new CariResponse();
        List<ResultsItem> items = new ArrayList<>();
        LocalDB localDB = new LocalDB(context);
        SQLiteDatabase db = localDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DB.TABLE_FILM +
                " WHERE " + DB.COLUMN_TYPE + " = " + DB.TYPE_UP_COMING +
                " OR " + DB.COLUMN_TYPE + " = " + DB.TYPE_BOTH, null);
        if (cursor.getCount()!=0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                ResultsItem isinya = new ResultsItem();
                isinya.setId(getIntValue(cursor, DB.COLUMN_ID_FILM));
                isinya.setTitle(getStringValue(cursor, DB.COLUMN_TITLE));
                isinya.setOverview(getStringValue(cursor, DB.COLUMN_OVERVIEW));
                isinya.setOriginalLanguage(getStringValue(cursor, DB.COLUMN_ORIGINAL_LANG));
                isinya.setOriginalTitle(getStringValue(cursor, DB.COLUMN_ORIGINAL_TITLE));
                isinya.setVideo(getStringValue(cursor, DB.COLUMN_VIDEO));
                isinya.setGenreIds(null);
                isinya.setPosterPath(getStringValue(cursor, DB.COLUMN_POSTER_PATH));
                isinya.setBackdropPath(getStringValue(cursor, DB.COLUMN_BACKDROP_PATH));
                isinya.setReleaseDate(getStringValue(cursor, DB.COLUMN_RELEASE_DATE));
                isinya.setVoteAverage(getDoubleValue(cursor, DB.COLUMN_VOTE_AVERAGE));
                isinya.setPopularity(getDoubleValue(cursor, DB.COLUMN_POPULARITY));
                isinya.setAdult(getStringValue(cursor, DB.COLUMN_ADULT));
                isinya.setVoteCount(getIntValue(cursor, DB.COLUMN_VOTE_COUNT));
                items.add(isinya);
            }
        }
        cursor.close();
        data.setResults(items);
        return data;
    }

    private static String getStringValue(Cursor record, String column) {
        try {
            int count=record.getColumnCount();
            for (int counter = 0; counter < count; counter++) {
                if (record.getColumnName(counter).equals(column)){
                    if (record.getString(counter)!=null)
                        return record.getString(counter);
                    else
                        break;
                }
            }
            return "";
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    private static Integer getIntValue(Cursor record, String column) {
        try {
            int count=record.getColumnCount();
            for (int counter = 0; counter < count; counter++) {
                if (record.getColumnName(counter).equals(column)){
                    return record.getInt(counter);
                }
            }
            return 0;
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    private static Double getDoubleValue(Cursor record, String column) {
        try {
            int count=record.getColumnCount();
            for (int counter = 0; counter < count; counter++) {
                if (record.getColumnName(counter).equals(column)){
                    return record.getDouble(counter);
                }
            }
            return 0.0;
        } catch (Exception e){
            e.printStackTrace();
            return 0.0;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}