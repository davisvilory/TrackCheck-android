package beteam.viloco.trackcheck.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
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
import beteam.viloco.trackcheck.repositorios.LogErrorRepository;
import beteam.viloco.trackcheck.servicio.CatalogoServicio;
import beteam.viloco.trackcheck.servicio.NegocioServicio;
import beteam.viloco.trackcheck.util.CustomException;
import beteam.viloco.trackcheck.util.Extensions;

public class Login extends AppCompatActivity {
    Context mContext;
    UserLoginTask task = null;
    EditText txtUsuario;
    EditText txtPassword;
    View mProgressView;
    View mFormView;
    Boolean isCookie = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = getBaseContext();
        new CatalogoServicio(mContext);

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

            UserDTO user = CatalogoServicio.getInstance().GetUserAuthenticated();
            if (user != null) {
                isCookie = true;
                showProgress(true);
                task = new UserLoginTask(user, this);
                task.execute((Void) null);
            }
        } catch (CustomException ex) {
            BaseClass.ToastAlert(ex.getMessage(), mContext);
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            BaseClass.ToastAlert(getString(R.string.Mensaje_ErrorInterno), mContext);
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
        menu.findItem(R.id.action_gps).setVisible(false);
        menu.findItem(R.id.action_sync).setVisible(false);
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.action_getcatalogs).setVisible(false);
        menu.findItem(R.id.action_update).setVisible(false);
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

        if (!Extensions.isConnectionAvailable(mContext)) {
            BaseClass.ToastAlert(getString(R.string.Mensaje_SinConeccion), mContext);
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
        UserDTO user;
        Activity activity;
        String message;

        UserLoginTask(UserDTO user, Activity activity) {
            this.user = user;
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            new NegocioServicio(mContext);

            try {
                if (!isCookie) {
                    user = NegocioServicio.getInstance().AutenticaUsuario(user);
                }
                if (user.Id != 0) {
                    BaseClass.usuarioLogged = user;
                    if (!isCookie) {
                        CatalogoServicio.getInstance().InsertUserAuthenticated(user);
                    }
                    return true;
                }
            } catch (CustomException ex) {
                message = ex.getMessage();
                return false;
            } catch (Exception ex) {
                LogErrorRepository.BuildLogError(ex, mContext);
                message = getString(R.string.Mensaje_ErrorInterno);
                return false;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            task = null;
            showProgress(false);

            if (success) {
                activity.startActivity(new Intent(activity, Master.class));
                activity.finish();
            } else {
                BaseClass.ToastAlert(message, mContext);
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
