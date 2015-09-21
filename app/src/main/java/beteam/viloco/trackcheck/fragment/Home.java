package beteam.viloco.trackcheck.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.ArrayList;

import beteam.viloco.trackcheck.R;
import beteam.viloco.trackcheck.activity.BaseClass;
import beteam.viloco.trackcheck.activity.Login;
import beteam.viloco.trackcheck.repositorios.LogErrorRepository;
import beteam.viloco.trackcheck.servicio.CatalogoServicio;
import beteam.viloco.trackcheck.util.CustomException;

public class Home extends Fragment {
    Context mContext;
    Task mTask = null;
    View mProgressView;
    View mFormView;
    ArrayList<Integer> mAction;

    public Home() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        new CatalogoServicio(getContext());
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        try {
            mFormView = view.findViewById(R.id.HomeForm);
            mProgressView = view.findViewById(R.id.HomeProgress);
            mAction = new ArrayList<>();

            if (!CatalogoServicio.getInstance().ExistsZones()) {
                DoAction(1);
            }
            if (!CatalogoServicio.getInstance().ExistsTerritories()) {
                DoAction(2);
            }
            if (!CatalogoServicio.getInstance().ExistsBusinessTypes()) {
                DoAction(3);
            }
        } catch (CustomException ex) {
            BaseClass.ToastAlert(ex.getMessage(), mContext);
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            BaseClass.ToastAlert(getString(R.string.Mensaje_ErrorInterno), mContext);
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_gps).setVisible(false);
        menu.findItem(R.id.action_sync).setVisible(false);
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.action_getcatalogs).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
//                case R.id.action_exitapp:
//                    BaseClass.ShowConfirm("Confirmación", "¿Desea cerrar sesión?", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            try {
//                                CatalogoServicio.getInstance().DeleteUserAuthenticated(BaseClass.usuarioLogged.Id);
//                                BaseClass.usuarioLogged = null;
//                                startActivity(new Intent(getActivity(), Login.class));
//                                getActivity().finish();
//                            } catch (CustomException ex) {
//                                BaseClass.ToastAlert(ex.getMessage(), mContext);
//                            } catch (Exception ex) {
//                                LogErrorRepository.BuildLogError(ex, mContext);
//                                BaseClass.ToastAlert(getString(R.string.Mensaje_ErrorInterno), mContext);
//                            }
//                        }
//                    }, getContext());
//                    return true;
                case R.id.action_getcatalogs:
                    DoAction(1);
                    DoAction(2);
                    DoAction(3);
                    return true;
            }
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            BaseClass.ToastAlert(getString(R.string.Mensaje_ErrorInterno), mContext);
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

    private void DoAction(int action) {
        if (!mAction.contains(action))
            mAction.add(action);
        String message = "";
        if (mTask == null) {
            if (action == 1) message = "Obteniendo catalogo de Zonas";
            else if (action == 2) message = "Obteniendo catalogo de Territorios";
            else if (action == 3) message = "Obteniendo catalogo de Tipos de Negocio";
            BaseClass.ToastAlert(message, mContext);
            showProgress(true);
            mTask = new Task(action);
            mTask.execute((Void) null);
        }
    }

    public class Task extends AsyncTask<Void, Void, Boolean> {
        int accion = 0;
        String message;

        Task(int accion) {
            this.accion = accion;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mAction.remove((Integer) accion);
                if (accion == 1) {
                    CatalogoServicio.getInstance().GetZones();
                } else if (accion == 2) {
                    CatalogoServicio.getInstance().GetTerritories();
                } else if (accion == 3) {
                    CatalogoServicio.getInstance().GetBusinessTypes();
                }
            } catch (CustomException ex) {
                message = ex.getMessage();
                return false;
            } catch (Exception ex) {
                LogErrorRepository.BuildLogError(ex, mContext);
                message = getString(R.string.Mensaje_ErrorInterno);
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mTask = null;
            showProgress(false);

            try {
                if (success) {
                    if (accion == 1) {
                        BaseClass.ToastAlert("Zonas obtenidas exitosamente", mContext);
                    } else if (accion == 2) {
                        BaseClass.ToastAlert("Territorios obtenidos exitosamente", mContext);
                    } else if (accion == 3) {
                        BaseClass.ToastAlert("Tipos de Negocios obtenidos exitosamente", mContext);
                    }
                } else {
                    BaseClass.ToastAlert(message, mContext);
                }
                if (mAction.size() > 0) {
                    DoAction(mAction.get(0));
                }
            } catch (Exception ex) {
                LogErrorRepository.BuildLogError(ex, mContext);
                BaseClass.ToastAlert(getString(R.string.Mensaje_ErrorInterno), mContext);
            }
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            showProgress(false);
        }
    }
}