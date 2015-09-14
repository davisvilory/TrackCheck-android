package beteam.viloco.trackcheck.servicio;

import android.content.Context;

import beteam.viloco.trackcheck.dto.UserDTO;
import beteam.viloco.trackcheck.repositorios.UserRepositorio;
import beteam.viloco.trackcheck.util.CustomException;

public class NegocioServicio {
    private Context context;

    public NegocioServicio(Context context) {
        this.context = context;
    }

    /****
     * Login
     ****/
    public UserDTO AutenticaUsuario(UserDTO user) throws CustomException {
        UserRepositorio repo = new UserRepositorio(context);
        return repo.AutenticaUsuario(user);
    }
}
