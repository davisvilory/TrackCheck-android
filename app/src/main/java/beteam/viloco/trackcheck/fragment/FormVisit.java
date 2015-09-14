package beteam.viloco.trackcheck.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import beteam.viloco.trackcheck.R;
import beteam.viloco.trackcheck.activity.BaseClass;
import beteam.viloco.trackcheck.activity.Master;
import beteam.viloco.trackcheck.adapter.ExpandableHeightListView;
import beteam.viloco.trackcheck.adapter.PhotosListViewAdapter;
import beteam.viloco.trackcheck.adapter.SpinnerAdapter;
import beteam.viloco.trackcheck.dto.DataDTO;
import beteam.viloco.trackcheck.dto.DataPhotoDTO;
import beteam.viloco.trackcheck.dto.SpinnerCustom;
import beteam.viloco.trackcheck.dto.TerritoryDTO;
import beteam.viloco.trackcheck.dto.ZoneDTO;
import beteam.viloco.trackcheck.repositorios.LogErrorRepositorio;
import beteam.viloco.trackcheck.servicio.CatalogoServicio;
import beteam.viloco.trackcheck.util.CustomException;
import beteam.viloco.trackcheck.util.Extensions;
import beteam.viloco.trackcheck.util.GPSTracker;

public class FormVisit extends Fragment {
    private View formVisit;
    private Task task = null;
    private View mProgressView;
    private View mFormView;
    private static final int CAMERA_REQUEST = 1888;
    private static final int PICK_IMAGE = 111;
    private static DataDTO dataDTO;
    private GPSTracker gps;
    private PhotosListViewAdapter photosAdapter;
    private ExpandableHeightListView listView;
    private Boolean isLocationServicesActive = false;

    public FormVisit() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        new CatalogoServicio(getContext());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        formVisit = inflater.inflate(R.layout.fragment_form_visit, container, false);

        try {
            formVisit.findViewById(R.id.FormVisitCapturar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TakePhoto();
                }
            });
            formVisit.findViewById(R.id.FormVisitGaleria).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GaleriaPhoto();
                }
            });
            formVisit.findViewById(R.id.FormVisitEnviar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Enviar();
                }
            });

            mFormView = formVisit.findViewById(R.id.FormVisitForm);
            mProgressView = formVisit.findViewById(R.id.FormVisitProgress);

            gps = new GPSTracker(getContext());
            dataDTO = new DataDTO();
            dataDTO.DataPhoto = new Vector<>();
            listView = (ExpandableHeightListView) formVisit.findViewById(R.id.FormVisitPhotos);
            photosAdapter = new PhotosListViewAdapter(getContext(), dataDTO.DataPhoto);
            listView.setAdapter(photosAdapter);
            listView.setExpanded(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    photosAdapter.deleteItem(position);
                    photosAdapter.notifyDataSetChanged();
                    listView.setExpanded(true);
                }
            });

            List<SpinnerCustom> list = new ArrayList<>();

            List<TerritoryDTO> territoryDTOList = CatalogoServicio.getInstance().ReadAllTerritory();
            for (int i = 0; i < territoryDTOList.size(); i++) {
                TerritoryDTO territoryDTO = territoryDTOList.get(i);
                list.add(new SpinnerCustom(territoryDTO.Id, territoryDTO.Name));
            }

            ((Spinner) formVisit.findViewById(R.id.FormVisitTerritory)).setAdapter(new SpinnerAdapter(getContext(), list));

            list = new ArrayList<>();
            List<ZoneDTO> zoneDTOList = CatalogoServicio.getInstance().ReadAllZone();
            for (int i = 0; i < zoneDTOList.size(); i++) {
                ZoneDTO zoneDTO = zoneDTOList.get(i);
                list.add(new SpinnerCustom(zoneDTO.Id, zoneDTO.Name));
            }

            ((Spinner) formVisit.findViewById(R.id.FormVisitZone)).setAdapter(new SpinnerAdapter(getContext(), list));

            list = new ArrayList<>();
            for (int i = 0; i < 11; i++) {
                list.add(new SpinnerCustom(i, "" + i));
            }
            ((Spinner) formVisit.findViewById(R.id.FormVisitPoster)).setAdapter(new SpinnerAdapter(getContext(), list));
            ((Spinner) formVisit.findViewById(R.id.FormVisitThermoRoshpack60x40)).setAdapter(new SpinnerAdapter(getContext(), list));
            ((Spinner) formVisit.findViewById(R.id.FormVisitThermoTi2260x40)).setAdapter(new SpinnerAdapter(getContext(), list));
            ((Spinner) formVisit.findViewById(R.id.FormVisitElectroGota)).setAdapter(new SpinnerAdapter(getContext(), list));
            ((Spinner) formVisit.findViewById(R.id.FormVisitElectroImagen)).setAdapter(new SpinnerAdapter(getContext(), list));
            ((Spinner) formVisit.findViewById(R.id.FormVisitBanderola)).setAdapter(new SpinnerAdapter(getContext(), list));
            ((Spinner) formVisit.findViewById(R.id.FormVisitCalcomanis)).setAdapter(new SpinnerAdapter(getContext(), list));
            ((Spinner) formVisit.findViewById(R.id.FormVisitCaballeteCambioAceite)).setAdapter(new SpinnerAdapter(getContext(), list));
            ((Spinner) formVisit.findViewById(R.id.FormVisitCaballeteVentaAceite)).setAdapter(new SpinnerAdapter(getContext(), list));
            ((Spinner) formVisit.findViewById(R.id.FormVisitLona2x1Roshpack)).setAdapter(new SpinnerAdapter(getContext(), list));
            ((Spinner) formVisit.findViewById(R.id.FormVisitLona2x1ImagenMecanico)).setAdapter(new SpinnerAdapter(getContext(), list));

            CheckSources();
        } catch (CustomException ex) {
            BaseClass.ToastAlert(ex.getMessage(), getContext());
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, getContext());
            BaseClass.ToastAlert("Error interno de sistema", getContext());
        }

        return formVisit;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();

        try {
            gps.stopUsingGPS();
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, getContext());
            BaseClass.ToastAlert("Error interno de sistema", getContext());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_gps).setVisible(true);
        menu.findItem(R.id.action_sync).setVisible(false);
        menu.findItem(R.id.action_refresh).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return false;
            case R.id.action_search:
                return false;
            case R.id.action_gps:
                BaseClass.ShowConfirm("Confirmación", "Si vuelve a obtenr su ubicación, se sobreescribiran los datos de la dirección, ¿Esta seguro de querer continuar?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckSources();
                    }
                }, getContext());
                return true;
            case R.id.action_sync:
                return false;
            case R.id.action_refresh:
                RefreshForm((ViewGroup) mFormView);
                return false;
        }

        return false;
    }

    public void CheckSources() {
        gps.stopUsingGPS();
        gps.getLocation();

        if (!Extensions.isConnectionAvailable(getContext())) {
            BaseClass.showSettingsNetworkAlert(getContext());
            isLocationServicesActive = false;
        } else {
            if (!gps.isGPSEnabled()) {
                gps.showSettingsGPSAlert();
                isLocationServicesActive = false;
            } else {
                if (gps.canGetLocation()) {
                    try {
                        dataDTO.Latitude = gps.getLatitude();
                        dataDTO.Longitude = gps.getLongitude();
                        //((TextView) formVisit.findViewById(R.id.FormVisitLatitud)).setText(String.valueOf(dataDTO.Latitude));
                        //((TextView) formVisit.findViewById(R.id.FormVisitLongitud)).setText(String.valueOf(dataDTO.Longitude));

                        Geocoder gcd = new Geocoder(getContext(), new Locale("es", "MX"));
                        List<Address> addresses;
                        addresses = gcd.getFromLocation(dataDTO.Latitude, dataDTO.Longitude, 1);
                        if (addresses != null && addresses.size() > 0) {
                            dataDTO.Street = Extensions.EmptyOrNullOrWhiteSpace(addresses.get(0).getAddressLine(0));
                            dataDTO.Number = Extensions.EmptyOrNullOrWhiteSpace(addresses.get(0).getFeatureName());
                            dataDTO.Colony = Extensions.EmptyOrNullOrWhiteSpace(addresses.get(0).getAddressLine(1));
                            dataDTO.City = Extensions.EmptyOrNullOrWhiteSpace(addresses.get(0).getLocality());
                            dataDTO.DelegationTown = Extensions.EmptyOrNullOrWhiteSpace(addresses.get(0).getSubAdminArea());
                            dataDTO.State = Extensions.EmptyOrNullOrWhiteSpace(addresses.get(0).getAdminArea());
                            dataDTO.PostalCode = Extensions.EmptyOrNullOrWhiteSpace(addresses.get(0).getAddressLine(2).split(" ")[0]);
                            ((TextView) formVisit.findViewById(R.id.FormVisitStreet)).setText(dataDTO.Street);
                            ((TextView) formVisit.findViewById(R.id.FormVisitNumber)).setText(dataDTO.Number);
                            ((TextView) formVisit.findViewById(R.id.FormVisitColony)).setText(dataDTO.Colony);
                            ((TextView) formVisit.findViewById(R.id.FormVisitCity)).setText(dataDTO.City);
                            ((TextView) formVisit.findViewById(R.id.FormVisitDelegationTown)).setText(dataDTO.DelegationTown);
                            ((TextView) formVisit.findViewById(R.id.FormVisitState)).setText(dataDTO.State);
                            ((TextView) formVisit.findViewById(R.id.FormVisitPostalCode)).setText(dataDTO.PostalCode);
                        }
                        isLocationServicesActive = true;
                    } catch (Exception ex) {
                        LogErrorRepositorio.ArmaLogError(ex, getContext());
                        BaseClass.ShowAlert("Servicio no disponible",
                                "Servicio no disponible, Es posible que los servicios de google play no esten instalados o esten siendo bloqueados por otra aplicación, para obtener la direccion aproximada asegurece de tener instalado Google Play Services y dar click de nuevo al boton superior derecho. Si aun sigue sin poder obtener la dirección aproximada capture manualmente la dirección.",
                                getContext());
                        isLocationServicesActive = true;
                    }
                } else
                    isLocationServicesActive = false;
            }
        }
    }

    private void TakePhoto() {
        try {
//            if (PackageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
//
//            }
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            //        } catch (CustomException ex) {
//            BaseClass.ToastAlert(ex.getMessage(), getContext());
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, getContext());
            BaseClass.ToastAlert("Error interno de sistema", getContext());
        }
    }

    private void GaleriaPhoto() {
        try {
//            if (PackageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
//
//            }
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
            //        } catch (CustomException ex) {
//            BaseClass.ToastAlert(ex.getMessage(), getContext());
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, getContext());
            BaseClass.ToastAlert("Error interno de sistema", getContext());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    if (resultCode == Master.RESULT_OK) {
                        if (data == null) return;
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        photo = Extensions.decodeFile(photo);
                        //photo = Bitmap.createScaledBitmap(photo, (int) (photo.getWidth() * 0.5), (int) (photo.getHeight() * 0.5), false);
                        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        //photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        DataPhotoDTO dataPhotoDTO = new DataPhotoDTO();
                        //dataPhotoDTO.Photo = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                        dataPhotoDTO.bitmap = photo;
                        dataDTO.DataPhoto.add(dataPhotoDTO);
                        photosAdapter.setListData(dataDTO.DataPhoto);
                        photosAdapter.notifyDataSetChanged();
                        listView.setExpanded(true);
                    }
                    break;
                case PICK_IMAGE:
                    if (resultCode == Master.RESULT_OK) {
                        if (data == null) return;
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        Bitmap photo = Extensions.getScaledBitmap(picturePath, 1000, 1000);
                        photo = Extensions.decodeFile(photo);
                        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        //photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        DataPhotoDTO dataPhotoDTO = new DataPhotoDTO();
                        //dataPhotoDTO.Photo = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                        dataPhotoDTO.bitmap = photo;
                        dataDTO.DataPhoto.add(dataPhotoDTO);
                        photosAdapter.setListData(dataDTO.DataPhoto);
                        photosAdapter.notifyDataSetChanged();
                        listView.setExpanded(true);
                    }
                    break;
            }
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, getContext());
            BaseClass.ToastAlert("Error interno de sistema", getContext());
        }
    }

    private void Enviar() {
        try {
            if (task != null) {
                return;
            }
            if (!isLocationServicesActive) {
                BaseClass.ShowAlert("Ubicación no encontrada",
                        "No ha habilitado los servicios de internet y GPS. Para poder guardar es necesario obtener su ubicación aproximada. Utilice el boton de la parte superior derecha",
                        getContext());
                return;
            }

            EditText txtBusiness = (EditText) formVisit.findViewById(R.id.FormVisitBusiness);
            EditText txtStreet = (EditText) formVisit.findViewById(R.id.FormVisitStreet);
            EditText txtNumber = (EditText) formVisit.findViewById(R.id.FormVisitNumber);
            EditText txtColony = (EditText) formVisit.findViewById(R.id.FormVisitColony);
            EditText txtDelegation = (EditText) formVisit.findViewById(R.id.FormVisitDelegationTown);
            EditText txtCity = (EditText) formVisit.findViewById(R.id.FormVisitCity);
            EditText txtState = (EditText) formVisit.findViewById(R.id.FormVisitState);
            EditText txtPostalCode = (EditText) formVisit.findViewById(R.id.FormVisitPostalCode);
            Spinner ddlTerritory = (Spinner) formVisit.findViewById(R.id.FormVisitTerritory);
            Spinner ddlZone = (Spinner) formVisit.findViewById(R.id.FormVisitZone);
            Spinner ddlPoster = (Spinner) formVisit.findViewById(R.id.FormVisitPoster);
            Spinner ddlThermoRoshpack60x40 = (Spinner) formVisit.findViewById(R.id.FormVisitThermoRoshpack60x40);
            Spinner ddlThermoTi2260x40 = (Spinner) formVisit.findViewById(R.id.FormVisitThermoTi2260x40);
            Spinner ddlElectroGota = (Spinner) formVisit.findViewById(R.id.FormVisitElectroGota);
            Spinner ddlElectroImagen = (Spinner) formVisit.findViewById(R.id.FormVisitElectroImagen);
            Spinner ddlBanderola = (Spinner) formVisit.findViewById(R.id.FormVisitBanderola);
            Spinner ddlCalcomanis = (Spinner) formVisit.findViewById(R.id.FormVisitCalcomanis);
            Spinner ddlCaballeteCambioAceite = (Spinner) formVisit.findViewById(R.id.FormVisitCaballeteCambioAceite);
            Spinner ddlCaballeteVentaAceite = (Spinner) formVisit.findViewById(R.id.FormVisitCaballeteVentaAceite);
            Spinner ddlLona2x1Roshpack = (Spinner) formVisit.findViewById(R.id.FormVisitLona2x1Roshpack);
            Spinner ddlLona2x1ImagenMecanico = (Spinner) formVisit.findViewById(R.id.FormVisitLona2x1ImagenMecanico);

            txtBusiness.setError(null);
            txtStreet.setError(null);
            txtNumber.setError(null);
            txtColony.setError(null);
            txtDelegation.setError(null);
            txtCity.setError(null);
            txtState.setError(null);
            txtPostalCode.setError(null);

            dataDTO.IdUser = BaseClass.usuarioLogged.Id;
            dataDTO.IdTerritory = Integer.parseInt(String.valueOf(((SpinnerCustom) ddlTerritory.getSelectedItem()).Id));
            dataDTO.IdZone = Integer.parseInt(String.valueOf(((SpinnerCustom) ddlZone.getSelectedItem()).Id));
            dataDTO.BusinessName = txtBusiness.getText().toString();
            dataDTO.Latitude = gps.getLatitude();
            dataDTO.Longitude = gps.getLongitude();
            dataDTO.Street = txtStreet.getText().toString();
            dataDTO.Number = txtNumber.getText().toString();
            dataDTO.Colony = txtColony.getText().toString();
            dataDTO.DelegationTown = txtDelegation.getText().toString();
            dataDTO.City = txtCity.getText().toString();
            dataDTO.State = txtState.getText().toString();
            dataDTO.PostalCode = txtPostalCode.getText().toString();
            dataDTO.Poster = Integer.parseInt(String.valueOf(((SpinnerCustom) ddlPoster.getSelectedItem()).Id));
            dataDTO.ThermoRoshpack60x40 = Integer.parseInt(String.valueOf(((SpinnerCustom) ddlThermoRoshpack60x40.getSelectedItem()).Id));
            dataDTO.ThermoTi2260x40 = Integer.parseInt(String.valueOf(((SpinnerCustom) ddlThermoTi2260x40.getSelectedItem()).Id));
            dataDTO.ElectroGota = Integer.parseInt(String.valueOf(((SpinnerCustom) ddlElectroGota.getSelectedItem()).Id));
            dataDTO.ElectroImagen = Integer.parseInt(String.valueOf(((SpinnerCustom) ddlElectroImagen.getSelectedItem()).Id));
            dataDTO.Banderola = Integer.parseInt(String.valueOf(((SpinnerCustom) ddlBanderola.getSelectedItem()).Id));
            dataDTO.Calcomanis = Integer.parseInt(String.valueOf(((SpinnerCustom) ddlCalcomanis.getSelectedItem()).Id));
            dataDTO.CaballeteCambioAceite = Integer.parseInt(String.valueOf(((SpinnerCustom) ddlCaballeteCambioAceite.getSelectedItem()).Id));
            dataDTO.CaballeteVentaAceite = Integer.parseInt(String.valueOf(((SpinnerCustom) ddlCaballeteVentaAceite.getSelectedItem()).Id));
            dataDTO.Lona2x1Roshpack = Integer.parseInt(String.valueOf(((SpinnerCustom) ddlLona2x1Roshpack.getSelectedItem()).Id));
            dataDTO.Lona2x1ImagenMecanico = Integer.parseInt(String.valueOf(((SpinnerCustom) ddlLona2x1ImagenMecanico.getSelectedItem()).Id));
            dataDTO.Date = Calendar.getInstance().getTime();

            boolean cancel = false;
            View focusView = null;

            if (TextUtils.isEmpty(dataDTO.BusinessName)) {
                txtBusiness.setError(getString(R.string.Login_CampoObligatorio));
                focusView = txtBusiness;
                cancel = true;
            }

            if (TextUtils.isEmpty(dataDTO.Street)) {
                txtStreet.setError(getString(R.string.Login_CampoObligatorio));
                focusView = txtStreet;
                cancel = true;
            }

            if (TextUtils.isEmpty(dataDTO.Number)) {
                txtNumber.setError(getString(R.string.Login_CampoObligatorio));
                focusView = txtNumber;
                cancel = true;
            }

            if (TextUtils.isEmpty(dataDTO.Colony)) {
                txtColony.setError(getString(R.string.Login_CampoObligatorio));
                focusView = txtColony;
                cancel = true;
            }

            if (TextUtils.isEmpty(dataDTO.DelegationTown)) {
                txtDelegation.setError(getString(R.string.Login_CampoObligatorio));
                focusView = txtDelegation;
                cancel = true;
            }

            if (TextUtils.isEmpty(dataDTO.City)) {
                txtCity.setError(getString(R.string.Login_CampoObligatorio));
                focusView = txtCity;
                cancel = true;
            }

            if (TextUtils.isEmpty(dataDTO.State)) {
                txtState.setError(getString(R.string.Login_CampoObligatorio));
                focusView = txtState;
                cancel = true;
            }

            if (TextUtils.isEmpty(dataDTO.PostalCode)) {
                txtPostalCode.setError(getString(R.string.Login_CampoObligatorio));
                focusView = txtPostalCode;
                cancel = true;
            }

            if (dataDTO.DataPhoto.isEmpty()) {
                BaseClass.ToastAlert("Falta tomar la(s) foto(s)", getContext());
                focusView = formVisit.findViewById(R.id.FormVisitPhotos);
                cancel = true;
            }

            if (cancel) {
                focusView.requestFocus();
            } else {
                BaseClass.ShowConfirm("Confirmación", "¿Esta seguro de querer guardar los datos de su visita?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        task = new Task(dataDTO);
                        task.execute((Void) null);
                    }
                }, getContext());
            }
            //        } catch (CustomException ex) {
//            BaseClass.ToastAlert(ex.getMessage(), getContext());
        } catch (Exception ex) {
            LogErrorRepositorio.ArmaLogError(ex, getContext());
            BaseClass.ToastAlert("Error interno de sistema", getContext());
        }
    }

    private void RefreshForm(ViewGroup group) {
        BaseClass.HideKeyboard(getActivity());
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View subgroup = group.getChildAt(i);
            if (subgroup instanceof EditText) {
                ((EditText) subgroup).setText("");
            }
            if (subgroup instanceof Spinner) {
                ((Spinner) subgroup).setSelection(0);
            }

            if (subgroup instanceof ViewGroup && (((ViewGroup) subgroup).getChildCount() > 0))
                RefreshForm((ViewGroup) subgroup);
        }

        dataDTO.DataPhoto = new Vector<>();
        photosAdapter.setListData(dataDTO.DataPhoto);
        photosAdapter.notifyDataSetChanged();
        listView.setExpanded(true);
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

    public class Task extends AsyncTask<Void, Void, Boolean> {
        private boolean enviado;
        private DataDTO dataDTO;

        Task(DataDTO dataDTO) {
            this.dataDTO = dataDTO;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                enviado = CatalogoServicio.getInstance().InsertaData(dataDTO);
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

            if (enviado) BaseClass.ToastAlert("Guardado exitoso", getContext());
            else BaseClass.ToastAlert("Hubo un error al guardar", getContext());
        }

        @Override
        protected void onCancelled() {
            task = null;
            showProgress(false);
        }
    }
}
