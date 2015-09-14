package beteam.viloco.trackcheck.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import beteam.viloco.trackcheck.R;
import beteam.viloco.trackcheck.activity.BaseClass;
import beteam.viloco.trackcheck.adapter.ExpandableHeightListView;
import beteam.viloco.trackcheck.adapter.VisitasAdapter;
import beteam.viloco.trackcheck.dto.DataDTO;
import beteam.viloco.trackcheck.repositorios.LogErrorRepositorio;
import beteam.viloco.trackcheck.servicio.CatalogoServicio;
import beteam.viloco.trackcheck.util.CustomException;

import java.util.List;

public class VisitPending extends Fragment {
    private Task task = null;
    private View mProgressView;
    private View mFormView;
    private ExpandableHeightListView listView;
    private List<DataDTO> listVisitas;
    private RelativeLayout ningun_resultado;
    private TextView txtNumeroSincronizar;

    public VisitPending() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visit_pending, container, false);

        try {
            ningun_resultado = (RelativeLayout) view.findViewById(R.id.lout_ningun_resultado);
            txtNumeroSincronizar = (TextView) view.findViewById(R.id.VisitPendingNumeroSincronizar);
            listView = (ExpandableHeightListView) view.findViewById(R.id.VisitPendingVisitas);

            mFormView = view.findViewById(R.id.VisitPendingLayout);
            mProgressView = view.findViewById(R.id.VisitPendingProgress);

            if (task == null) {
                showProgress(true);
                task = new Task(1);
                task.execute((Void) null);
            }
            //} catch (CustomException ex) {
            //    BaseClass.ToastAlert(ex.getMessage(), getBaseContext());
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, getContext());
            BaseClass.ToastAlert("Error interno de sistema", getContext());
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_gps).setVisible(false);
        menu.findItem(R.id.action_sync).setVisible(true);
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.action_exitapp).setVisible(false);
        menu.findItem(R.id.action_getcatalogs).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return false;
            case R.id.action_search:
                return false;
            case R.id.action_gps:
                return false;
            case R.id.action_sync:
                BaseClass.ShowConfirm("Confirmación", "Se enviarán las visitas pendientes, ¿Esta seguro de continuar?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SendVisitasPendientes();
                    }
                }, getContext());
                return true;
            case R.id.action_refresh:
                return false;
        }

        return false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

    public void SendVisitasPendientes() {
        try {
            WifiManager wifi = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
            if (!wifi.isWifiEnabled()) {
                BaseClass.showSettingsNetworkAlert(getContext());
                return;
            }

            BaseClass.ToastAlert("Enviando las visitas pendientes, por favor espere...", getContext());

            if (task == null) {
                showProgress(true);
                task = new Task(2);
                task.execute((Void) null);
            }
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, getContext());
            BaseClass.ToastAlert("Error interno de sistema", getContext());
        }
    }

    public class Task extends AsyncTask<Void, Void, Boolean> {
        int accion = 0;

        Task(int accion) {
            this.accion = accion;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            CatalogoServicio catalogoServicio = new CatalogoServicio(getContext());

            try {
                if (accion == 1)
                    listVisitas = catalogoServicio.ReadAllData();
                else if (accion == 2)
                    catalogoServicio.SendVisitasPendientes();
            } catch (CustomException ex) {
                BaseClass.ToastAlert(ex.getMessage(), getContext());
            } catch (Exception ex) {
                LogErrorRepositorio.ArmaLogError(ex, getContext());
                BaseClass.ToastAlert(getString(R.string.Mensaje_ErrorInterno), getContext());
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
                    if (listVisitas.size() == 0)
                        ningun_resultado.setVisibility(View.VISIBLE);
                    else
                        ningun_resultado.setVisibility(View.GONE);

                    txtNumeroSincronizar.setText(String.valueOf(listVisitas.size()));

                    listView.setAdapter(new VisitasAdapter(getContext(), listVisitas));
                    listView.setExpanded(true);
                } else if (accion == 2) {
                    if (task == null) {
                        showProgress(true);
                        task = new Task(1);
                        task.execute((Void) null);
                    }
                }
            } catch (Exception ex) {
                LogErrorRepositorio.ArmaLogError(ex, getContext());
                BaseClass.ToastAlert("Error interno de sistema", getContext());
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
            showProgress(false);
        }
    }
}
