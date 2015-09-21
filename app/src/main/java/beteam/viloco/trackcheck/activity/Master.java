package beteam.viloco.trackcheck.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import beteam.viloco.trackcheck.R;
import beteam.viloco.trackcheck.dto.ModulosDTO;
import beteam.viloco.trackcheck.fragment.FormVisit;
import beteam.viloco.trackcheck.fragment.FragmentDrawer;
import beteam.viloco.trackcheck.fragment.Home;
import beteam.viloco.trackcheck.fragment.VisitPending;
import beteam.viloco.trackcheck.repositorios.LogErrorRepository;
import beteam.viloco.trackcheck.servicio.CatalogoServicio;
import beteam.viloco.trackcheck.util.CustomException;

public class Master extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    protected FrameLayout frameLayout;
    FragmentDrawer drawerFragment;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        mContext = getBaseContext();
        new CatalogoServicio(mContext);

        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, mToolbar);
            drawerFragment.setDrawerListener(this);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                public void onBackStackChanged() {
                    int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
                    if (backStackEntryCount > 0) {
                        Fragment fragment = getSupportFragmentManager().getFragments().get(backStackEntryCount);
                        getSupportActionBar().setTitle(fragment.getTag());
                    } else { // no hay mas fragments
                        getSupportActionBar().setTitle(R.string.Nombre_Aplicacion);
                        BaseClass.ToastAlert("Utilice \"Back\" una vez más para salir de la aplicación", mContext);
                    }
                }
            });

            frameLayout = (FrameLayout) findViewById(R.id.container_body);
            displayView(drawerFragment.getItem(0));
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            BaseClass.ToastAlert(getString(R.string.Mensaje_ErrorInterno), mContext);
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
        menu.findItem(R.id.action_getcatalogs).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position, ModulosDTO item) {
        displayView(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void displayView(ModulosDTO item) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = null;
            final String title = item.Nombre;

            int backStackEntryCount = fragmentManager.getBackStackEntryCount();
            if (backStackEntryCount > 0) {
                Fragment fragmentLast = getSupportFragmentManager().getFragments().get(backStackEntryCount);
                if (fragmentLast.getTag().equals(item.Nombre)) {
                    return;
                }
            }

            switch (item.IDModulo) {
                case 100:
                    fragment = new Home();
                    break;
                case 101:
                    fragment = new FormVisit();
                    break;
                case 102:
                    fragment = new VisitPending();
                    break;
                case 900:
                    BaseClass.ShowConfirm("Confirmación", "¿Desea cerrar sesión?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                CatalogoServicio.getInstance().DeleteUserAuthenticated(BaseClass.usuarioLogged.Id);
                                BaseClass.usuarioLogged = null;
                                startActivity(new Intent(Master.this, Login.class));
                                finish();
                            } catch (CustomException ex) {
                                BaseClass.ToastAlert(ex.getMessage(), mContext);
                            } catch (Exception ex) {
                                LogErrorRepository.BuildLogError(ex, mContext);
                                BaseClass.ToastAlert(getString(R.string.Mensaje_ErrorInterno), mContext);
                            }
                        }
                    }, Master.this);
                    break;
                default:
                    break;
            }

            if (fragment != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment, title);
                fragmentTransaction.addToBackStack(null).commit();
                getSupportActionBar().setTitle(title);
            }
        } catch (Exception ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            BaseClass.ToastAlert(getString(R.string.Mensaje_ErrorInterno), mContext);
        }
    }
}