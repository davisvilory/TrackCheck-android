package beteam.viloco.trackcheck.servicio;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import beteam.viloco.trackcheck.dto.DataDTO;
import beteam.viloco.trackcheck.dto.ModulosDTO;
import beteam.viloco.trackcheck.dto.TerritoryDTO;
import beteam.viloco.trackcheck.dto.UserDTO;
import beteam.viloco.trackcheck.dto.ZoneDTO;
import beteam.viloco.trackcheck.repositorios.DataRepositorio;
import beteam.viloco.trackcheck.repositorios.TerritoryRepositorio;
import beteam.viloco.trackcheck.repositorios.UserRepositorio;
import beteam.viloco.trackcheck.repositorios.ZoneRepositorio;
import beteam.viloco.trackcheck.util.CustomException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CatalogoServicio {
    private static Context mContext;
    private static CatalogoServicio instance;

    public CatalogoServicio(Context context) {
        mContext = context;
        if (instance == null) instance = this;
    }

    public static CatalogoServicio getInstance() {
        return instance;
    }

    /*User*/
    public UserDTO ObtieneUnicoUserAutenticado() throws CustomException {
        UserRepositorio repo = new UserRepositorio(mContext);
        return repo.ObtieneUnicoUserAutenticado();
    }

    public boolean InsertaUnicoUserAutenticado(UserDTO user) throws CustomException {
        UserRepositorio repo = new UserRepositorio(mContext);
        return repo.InsertaUnicoUserAutenticado(user);
    }

    public boolean BorraUnicoUserAutenticado(int Id) throws CustomException {
        UserRepositorio repo = new UserRepositorio(mContext);
        return repo.BorraUnicoUserAutenticado(Id);
    }

    public List<ModulosDTO> ObtieneModulos() throws CustomException {
        List<ModulosDTO> list = new ArrayList<>();

        ModulosDTO modulosDTO = new ModulosDTO();
        modulosDTO.Nombre = "Inicio";
        modulosDTO.IDModulo = 100;
        list.add(modulosDTO);
        modulosDTO = new ModulosDTO();
        modulosDTO.Nombre = "Registrar Visita";
        modulosDTO.IDModulo = 101;
        list.add(modulosDTO);
        modulosDTO = new ModulosDTO();
        modulosDTO.Nombre = "Sincronizar visitas";
        modulosDTO.IDModulo = 102;
        list.add(modulosDTO);
        modulosDTO = new ModulosDTO();
        modulosDTO.Nombre = "Cerrar Sesión";
        modulosDTO.IDModulo = 900;
        list.add(modulosDTO);

        return list;
    }

    /*Data*/
    public boolean InsertaData(DataDTO dataDTO) throws CustomException {
        DataRepositorio repo = new DataRepositorio(mContext);

        for (int i = 0; i < dataDTO.DataPhoto.size(); i++) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            dataDTO.DataPhoto.get(i).bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            dataDTO.DataPhoto.get(i).Photo = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);
        }

        return repo.InsertaData(dataDTO);
    }

    public List<DataDTO> ReadAllData() throws CustomException {
        DataRepositorio repo = new DataRepositorio(mContext);
        return repo.ReadAllByPredicate(null);
    }

    public void SendVisitasPendientes() throws CustomException {
        DataRepositorio repo = new DataRepositorio(mContext);
        repo.SendVisitasPendientes();
    }

    /*Territory*/
    public boolean ExisteTerritories() throws CustomException {
        TerritoryRepositorio territoryRepositorio = new TerritoryRepositorio(mContext);
        return territoryRepositorio.ExisteTerritories();
    }

    public List<TerritoryDTO> ReadAllTerritory() throws CustomException {
        TerritoryRepositorio repo = new TerritoryRepositorio(mContext);
        return repo.ReadAllByPredicate(null);
    }

    public boolean ObtieneTerritories() throws CustomException {
        TerritoryRepositorio repo = new TerritoryRepositorio(mContext);
        return repo.ObtieneTerritories();
    }

    /*Zone*/
    public boolean ExisteZones() throws CustomException {
        ZoneRepositorio zoneRepositorio = new ZoneRepositorio(mContext);
        return zoneRepositorio.ExisteZones();
    }

    public List<ZoneDTO> ReadAllZone() throws CustomException {
        ZoneRepositorio repo = new ZoneRepositorio(mContext);
        return repo.ReadAllByPredicate(null);
    }

    public boolean ObtieneZones() throws CustomException {
        ZoneRepositorio repo = new ZoneRepositorio(mContext);
        return repo.ObtieneZones();
    }
}
