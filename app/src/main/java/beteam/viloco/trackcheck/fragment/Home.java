package beteam.viloco.trackcheck.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import beteam.viloco.trackcheck.R;
import beteam.viloco.trackcheck.activity.BaseClass;
import beteam.viloco.trackcheck.activity.Login;
import beteam.viloco.trackcheck.repositorios.LogErrorRepositorio;
import beteam.viloco.trackcheck.servicio.CatalogoServicio;
import beteam.viloco.trackcheck.util.CustomException;

public class Home extends Fragment {
    Context context;
    private Task task = null;
    private View mProgressView;
    private View mFormView;
    private int mAction = 0;

    public Home() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        try {
            mFormView = view.findViewById(R.id.HomeForm);
            mProgressView = view.findViewById(R.id.HomeProgress);

            CatalogoServicio catalogoServicio = new CatalogoServicio(context);
            if (!catalogoServicio.ExisteZones()) {
                DoAction(1, "Obteniendo catalogo de Zonas");
            }
            if (!catalogoServicio.ExisteTerritories()) {
                DoAction(2, "Obteniendo catalogo de Territorios");
            }
        } catch (CustomException ex) {
            BaseClass.ToastAlert(ex.getMessage(), context);
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, context);
            BaseClass.ToastAlert("Error interno de sistema", context);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_gps).setVisible(false);
        menu.findItem(R.id.action_sync).setVisible(false);
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.action_exitapp).setVisible(true);
        menu.findItem(R.id.action_getcatalogs).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            CatalogoServicio catalogoServicio = new CatalogoServicio(context);

            switch (item.getItemId()) {
                case R.id.action_settings:
                    return false;
                case R.id.action_search:
                    return false;
                case R.id.action_gps:
                    return false;
                case R.id.action_sync:
                    return false;
                case R.id.action_refresh:
                    return false;
                case R.id.action_exitapp:
                    catalogoServicio.BorraUnicoUserAutenticado(BaseClass.usuarioLogged.Id);
                    BaseClass.usuarioLogged = null;
                    startActivity(new Intent(getActivity(), Login.class));
                    getActivity().finish();
                    return true;
                case R.id.action_getcatalogs:
                    DoAction(1, "Obteniendo catalogo de Zonas");
                    DoAction(2, "Obteniendo catalogo de Territorios");
                    return true;
            }
        } catch (CustomException ex) {
            BaseClass.ToastAlert(ex.getMessage(), context);
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, context);
            BaseClass.ToastAlert("Error interno de sistema", context);
        }

        return false;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void DoAction(int action, String message) {
        mAction = action;
        if (task == null) {
            BaseClass.ToastAlert(message, context);
            showProgress(true);
            task = new Task(action);
            task.execute((Void) null);
        }
    }

    public class Task extends AsyncTask<Void, Void, Boolean> {
        int accion = 0;

        Task(int accion) {
            this.accion = accion;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            CatalogoServicio catalogoServicio = new CatalogoServicio(context);

            try {
                if (accion == 1) {
                    if (mAction == 1)
                        mAction = 0;
                    catalogoServicio.ObtieneZones();
                } else if (accion == 2) {
                    if (mAction == 2)
                        mAction = 0;
                    catalogoServicio.ObtieneTerritories();
                }
            } catch (CustomException ex) {
                BaseClass.ToastAlert(ex.getMessage(), context);
            } catch (Exception ex) {
                LogErrorRepositorio.ArmaLogError(ex, context);
                BaseClass.ToastAlert(getString(R.string.Mensaje_ErrorInterno), context);
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            task = null;
            showProgress(false);

            try {
                if (accion == 1) {
                    BaseClass.ToastAlert("Zonas obtenidas exitosamente", context);
                    if (mAction == 2) {
                        DoAction(mAction, "Obteniendo catalogo de Territorios");
                    }
                } else if (accion == 2) {
                    BaseClass.ToastAlert("Territorios obtenidos exitosamente", context);
                    if (mAction == 1) {
                        DoAction(mAction, "Obteniendo catalogo de Zonas");
                    }
                }
            } catch (Exception ex) {
                LogErrorRepositorio.ArmaLogError(ex, context);
                BaseClass.ToastAlert("Error interno de sistema", context);
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
            showProgress(false);
        }
    }
}
