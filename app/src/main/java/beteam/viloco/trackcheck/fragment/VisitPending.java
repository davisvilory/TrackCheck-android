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
import beteam.viloco.trackcheck.dto.DataPhotoDTO;
import beteam.viloco.trackcheck.repositorios.LogErrorRepository;
import beteam.viloco.trackcheck.servicio.CatalogoServicio;
import beteam.viloco.trackcheck.util.CustomException;

import java.util.ArrayList;
import java.util.List;

public class VisitPending extends Fragment {
    Context mContext;
    Task task = null;
    View mProgressView;
    View mFormView;
    ExpandableHeightListView listView;
    List<DataDTO> listVisitas;
    RelativeLayout ningun_resultado;
    TextView txtNumeroSincronizar;
    DataDTO mDataDTO;
    ArrayList<DataPhotoDTO> mDataPhotoDTO;
    int mIntents = 0;

    public VisitPending() {
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
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, getContext());
            BaseClass.ToastAlert(getString(R.string.Mensaje_ErrorInterno), getContext());
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.action_gps).setVisible(false);
        menu.findItem(R.id.action_sync).setVisible(true);
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.action_getcatalogs).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.action_sync:
                    BaseClass.ShowConfirm("Confirmación", "Se enviarán las visitas pendientes, ¿Esta seguro de continuar?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SendVisitasPendientes();
                        }
                    }, getContext());
                    return true;
            }
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            BaseClass.ToastAlert(getString(R.string.Mensaje_ErrorInterno), mContext);
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
            mIntents = 1;
            if (task == null) {
                showProgress(true);
                task = new Task(2);
                task.execute((Void) null);
            }
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, getContext());
            BaseClass.ToastAlert(getString(R.string.Mensaje_ErrorInterno), getContext());
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
                if (accion == 1)
                    listVisitas = CatalogoServicio.getInstance().GetDataPending();
                else if (accion == 2) {
                    mDataDTO = CatalogoServicio.getInstance().ReadFirstData();
                    if (mDataDTO != null) {
                        mDataPhotoDTO = mDataDTO.DataPhoto;

                        if (mDataDTO.IdServer == 0) {
                            mDataDTO.IdServer = CatalogoServicio.getInstance().SendDataPending(mDataDTO);
                            if (mDataDTO.IdServer <= 0) {
                                if (mIntents > 3)
                                    message = "No se pudo enviar la visita, se superó el máximo de intentos";
                                else
                                    message = "No se pudo enviar la visita, intento número " + mIntents;
                                return false;
                            }
                        }
                    } else {
                        message = "Ya no hay visitas que enviar, proceso de sincronización terminado";
                        return true;
                    }
                } else if (accion == 3) {
                    if (mDataPhotoDTO.size() > 0) {
                        DataPhotoDTO dataPhotoDTO = mDataPhotoDTO.get(0);
                        if (dataPhotoDTO.IdServer == 0) {
                            mIntents++;
                            dataPhotoDTO.IdData = mDataDTO.IdServer;
                            dataPhotoDTO.IdServer = CatalogoServicio.getInstance().SendDataPhotoPending(dataPhotoDTO);
                            if (dataPhotoDTO.IdServer <= 0) {
                                if (mIntents > 3)
                                    message = "No se pudo enviar la foto, se superó el máximo de intentos";
                                else
                                    message = "No se pudo enviar la foto, intento número " + mIntents;
                                return false;
                            }
                        }
                        mIntents = 0;
                        mDataPhotoDTO.remove(0);
                    }
                }
            } catch (CustomException ex) {
                message = ex.getMessage();
                return false;
            } catch (Exception ex) {
                LogErrorRepository.BuildLogError(ex, getContext());
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
                    if (success) {
                        if (message == null || message.equals("")) {
                            BaseClass.ToastAlert("Se envio la visita del negocio " + mDataDTO.BusinessName + ", Enviando las fotos...", getContext());
                            mIntents = 0;
                            if (task == null) {
                                showProgress(true);
                                task = new Task(3);
                                task.execute((Void) null);
                            }
                        } else {
                            BaseClass.ToastAlert(message, getContext());

                            if (task == null) {
                                showProgress(true);
                                task = new Task(1);
                                task.execute((Void) null);
                            }
                        }
                    } else {
                        if (message == null || message.equals(""))
                            getString(R.string.Mensaje_ErrorInterno);
                        BaseClass.ToastAlert(message, getContext());
                        mIntents++;
                        if (mIntents <= 3) {
                            if (task == null) {
                                showProgress(true);
                                task = new Task(2);
                                task.execute((Void) null);
                            }
                        } else {
                            if (task == null) {
                                showProgress(true);
                                task = new Task(1);
                                task.execute((Void) null);
                            }
                        }
                    }
                } else if (accion == 3) {
                    if (success) {
                        BaseClass.ToastAlert("Se envio la foto de la visita " + mDataDTO.BusinessName + ", Revisando si hay más fotos...", getContext());

                        if (mDataPhotoDTO.size() > 0) {
                            if (task == null) {
                                showProgress(true);
                                task = new Task(3);
                                task.execute((Void) null);
                            }
                        } else {
                            BaseClass.ToastAlert("Se enviaron todas las fotos de la visita " + mDataDTO.BusinessName + ", Revisando si hay más visitas...", getContext());

                            //Se borra primero de la base
                            if (CatalogoServicio.getInstance().DeleteDataAndPhoto(mDataDTO.Id)) {
                                BaseClass.ToastAlert("Se borro con éxito la visita", getContext());
                                mDataDTO = null;
                            }

                            if (task == null) {
                                showProgress(true);
                                task = new Task(2);
                                task.execute((Void) null);
                            }
                        }
                    } else {
                        if (message == null || message.equals(""))
                            getString(R.string.Mensaje_ErrorInterno);
                        BaseClass.ToastAlert(message, getContext());

                        if (mIntents <= 3) {
                            if (mDataPhotoDTO.size() > 0) {
                                if (task == null) {
                                    showProgress(true);
                                    task = new Task(3);
                                    task.execute((Void) null);
                                }
                            }
                        } else {
                            if (task == null) {
                                showProgress(true);
                                task = new Task(1);
                                task.execute((Void) null);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                LogErrorRepository.BuildLogError(ex, getContext());
                BaseClass.ToastAlert(getString(R.string.Mensaje_ErrorInterno), getContext());
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
            showProgress(false);
            BaseClass.ToastAlert("Se canceló el proceso", getContext());
        }
    }
}
