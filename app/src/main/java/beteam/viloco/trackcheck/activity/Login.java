package beteam.viloco.trackcheck.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import beteam.viloco.trackcheck.R;
import beteam.viloco.trackcheck.dto.UserDTO;
import beteam.viloco.trackcheck.repositorios.LogErrorRepositorio;
import beteam.viloco.trackcheck.servicio.CatalogoServicio;
import beteam.viloco.trackcheck.servicio.NegocioServicio;
import beteam.viloco.trackcheck.util.CustomException;
import beteam.viloco.trackcheck.util.Extensions;

public class Login extends AppCompatActivity {
    private UserLoginTask task = null;
    private EditText txtUsuario;
    private EditText txtPassword;
    private View mProgressView;
    private View mFormView;
    private Boolean isCookie = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            txtUsuario = (EditText) findViewById(R.id.Login_Usuario);
            txtPassword = (EditText) findViewById(R.id.Login_Contrasenia);
            txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            Button btnEntrar = (Button) findViewById(R.id.Login_Entrar);
            btnEntrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            mFormView = findViewById(R.id.Login_Form);
            mProgressView = findViewById(R.id.Login_Progress);

            CatalogoServicio catalogoServicio = new CatalogoServicio(getBaseContext());
            UserDTO user = catalogoServicio.ObtieneUnicoUserAutenticado();
            if (user != null) {
                isCookie = true;
                showProgress(true);
                task = new UserLoginTask(user, this);
                task.execute((Void) null);
            }
//        } catch (CustomException ex) {
//            BaseClass.ToastAlert(ex.getMessage(), getBaseContext());
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, getBaseContext());
            BaseClass.ToastAlert("Error interno de sistema", getBaseContext());
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.master_menu, menu);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_gps).setVisible(false);
        menu.findItem(R.id.action_sync).setVisible(false);
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.action_exitapp).setVisible(false);
        menu.findItem(R.id.action_getcatalogs).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void attemptLogin() {
        if (task != null) {
            return;
        }

        txtUsuario.setError(null);
        txtPassword.setError(null);

        String mUser = txtUsuario.getText().toString();
        String mPassword = txtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(mUser)) {
            txtUsuario.setError(getString(R.string.Login_CampoObligatorio));
            focusView = txtUsuario;
            cancel = true;
        }

        if (TextUtils.isEmpty(mPassword)) {
            txtPassword.setError(getString(R.string.Login_CampoObligatorio));
            focusView = txtPassword;
            cancel = true;
        }

        if (!Extensions.isConnectionAvailable(getBaseContext())) {
            BaseClass.ToastAlert(getString(R.string.Mensaje_SinConeccion), getBaseContext());
            focusView = txtPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            UserDTO user = new UserDTO();
            user.UserName = mUser;
            user.Password = mPassword;
            task = new UserLoginTask(user, this);
            task.execute((Void) null);
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private UserDTO user;
        private Activity activity;

        UserLoginTask(UserDTO user, Activity activity) {
            this.user = user;
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            NegocioServicio negocioServicio = new NegocioServicio(getBaseContext());

            boolean isauth = false;
            try {
                if (!isCookie) {
                    user = negocioServicio.AutenticaUsuario(user);
                }
                if (user.Id != 0) {
                    isauth = true;
                    BaseClass.usuarioLogged = user;
                    if (!isCookie) {
                        CatalogoServicio catalogoServicio = new CatalogoServicio(getBaseContext());
                        catalogoServicio.InsertaUnicoUserAutenticado(user);
                    }
                }
            } catch (CustomException ex) {
                BaseClass.ToastAlert(ex.getMessage(), getBaseContext());
            } catch (Exception ex) {
                LogErrorRepositorio.ArmaLogError(ex, getBaseContext());
                BaseClass.ToastAlert(getString(R.string.Mensaje_ErrorInterno), getBaseContext());
                return false;
            }

            return isauth;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            task = null;
            showProgress(false);

            if (success) {
                activity.startActivity(new Intent(activity, Master.class));
                activity.finish();
            } else {
                txtPassword.setError(getString(R.string.Login_DatosIncorrectos));
                txtPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
            showProgress(false);
        }
    }
}
