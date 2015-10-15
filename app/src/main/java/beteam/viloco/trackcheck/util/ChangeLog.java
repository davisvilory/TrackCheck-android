package beteam.viloco.trackcheck.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.ContextThemeWrapper;
import android.webkit.WebView;

import beteam.viloco.trackcheck.R;

public class ChangeLog {

    private final Context mContext;
    private String lastVersion, thisVersion;

    // this is the key for storing the version name in SharedPreferences
    private static final String VERSION_KEY = "targetprokeyversion";

    private static final String NO_VERSION = "";

    public ChangeLog(Context mContext) {
        this(mContext, PreferenceManager.getDefaultSharedPreferences(mContext));
    }

    public ChangeLog(Context mContext, SharedPreferences sp) {
        this.mContext = mContext;

        // get version numbers
        this.lastVersion = sp.getString(VERSION_KEY, NO_VERSION);
        try {
            this.thisVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            this.thisVersion = NO_VERSION;
        }
    }

    public String getLastVersion() {
        return this.lastVersion;
    }

    public String getThisVersion() {
        return this.thisVersion;
    }

    public boolean firstRun() {
        return !this.lastVersion.equals(this.thisVersion);
    }

    public boolean firstRunEver() {
        return NO_VERSION.equals(this.lastVersion);
    }

    public AlertDialog getLogDialog() {
        return this.getDialog(this.firstRunEver());
    }

    public AlertDialog getFullLogDialog() {
        return this.getDialog(true);
    }

    protected AlertDialog getDialog(boolean full) {
        WebView wv = new WebView(this.mContext);

        wv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        wv.loadDataWithBaseURL(null, this.getLog(full), "text/html", "UTF-8", null);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this.mContext, android.R.style.Theme_Dialog));
        builder.setTitle(
                mContext.getResources().getString(full ? R.string.changelog_full_title : R.string.changelog_title))
                .setView(wv).setCancelable(false)
                // OK button
                .setPositiveButton(mContext.getResources().getString(R.string.changelog_ok_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                updateVersionInPreferences();
                            }
                        });

        if (!full) {
            // "more ..." button
            builder.setNegativeButton(R.string.changelog_show_full,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getFullLogDialog().show();
                        }
                    });
        }

        return builder.create();
    }

    protected void updateVersionInPreferences() {
        // save new version number to preferences
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(VERSION_KEY, thisVersion);
        // on SDK-Versions > 9 you should use this:
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
        //editor.commit();
    }

    public String getLog() {
        return this.getLog(false);
    }

    public String getFullLog() {
        return this.getLog(true);
    }

    public static int NONE = 1;
    public static int ORDERED = 2;
    public static int UNORDERED = 3;

    private int listMode = NONE;
    private StringBuffer sb = null;
    private static final String EOCL = "END_OF_CHANGE_LOG";

    protected String getLog(boolean full) {
        // read changelog.txt file
        sb = new StringBuffer();
        try {
            InputStream ins = mContext.getResources().openRawResource(R.raw.changelog);
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));

            String line = null;
            boolean advanceToEOVS = false; // if true: ignore further version
            // sections
            while ((line = br.readLine()) != null) {
                line = line.trim();
                char marker = line.length() > 0 ? line.charAt(0) : 0;
                if (marker == '$') {
                    // begin of a version section
                    this.closeList();
                    String version = line.substring(1).trim();
                    // stop output?
                    if (!full) {
                        //if (this.lastVersion.equals(version)) {
                        //    advanceToEOVS = true;
                        //}
                        if (version.equals(EOCL)) {
                            advanceToEOVS = true;
                        }
                    }
                } else if (!advanceToEOVS) {
                    switch (marker) {
                        case '%':
                            // line contains version title
                            this.closeList();
                            sb.append("<div class='title'>").append(line.substring(1).trim()).append("</div>\n");
                            break;
                        case '_':
                            // line contains version title
                            this.closeList();
                            sb.append("<div class='subtitle'>").append(line.substring(1).trim()).append("</div>\n");
                            break;
                        case '!':
                            // line contains free text
                            this.closeList();
                            sb.append("<div class='freetext'>").append(line.substring(1).trim()).append("</div>\n");
                            break;
                        case '#':
                            // line contains numbered list item
                            this.openList(ORDERED);
                            sb.append("<li>").append(line.substring(1).trim()).append("</li>\n");
                            break;
                        case '*':
                            // line contains bullet list item
                            this.openList(UNORDERED);
                            sb.append("<li>").append(line.substring(1).trim()).append("</li>\n");
                            break;
                        default:
                            // no special character: just use line as is
                            this.closeList();
                            sb.append(line).append("\n");
                    }
                }
            }
            this.closeList();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    protected void openList(int listMode) {
        if (this.listMode != listMode) {
            closeList();
            if (listMode == ORDERED) {
                sb.append("<div class='list'><ol>\n");
            } else if (listMode == UNORDERED) {
                sb.append("<div class='list'><ul>\n");
            }
            this.listMode = listMode;
        }
    }

    protected void closeList() {
        if (this.listMode == ORDERED) {
            sb.append("</ol></div>\n");
        } else if (this.listMode == UNORDERED) {
            sb.append("</ul></div>\n");
        }
        this.listMode = NONE;
    }

    private static final String TAG = "ChangeLog";

    public void dontuseSetLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }
}