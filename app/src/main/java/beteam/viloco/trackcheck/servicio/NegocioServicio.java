package beteam.viloco.trackcheck.servicio;

import android.content.Context;

import beteam.viloco.trackcheck.dto.UserDTO;
import beteam.viloco.trackcheck.repositorios.UserRepository;
import beteam.viloco.trackcheck.util.CustomException;

public class NegocioServicio {
    private static Context mContext;
    private static NegocioServicio instance;

    public NegocioServicio(Context context) {
        mContext = context;
        if (instance == null) instance = this;
    }

    public static NegocioServicio getInstance() {
        return instance;
    }

    /****
     * Login
     ****/
    public UserDTO AutenticaUsuario(UserDTO user) throws CustomException {
        UserRepository repo = new UserRepository(mContext);
        return repo.AuthenticateUser(user);
    }
}
