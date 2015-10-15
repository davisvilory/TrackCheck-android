package beteam.viloco.trackcheck.servicio;

import android.content.Context;

import java.util.ArrayList;

import beteam.viloco.trackcheck.dto.BusinessTypeDTO;
import beteam.viloco.trackcheck.dto.DataDTO;
import beteam.viloco.trackcheck.dto.DataPhotoDTO;
import beteam.viloco.trackcheck.dto.ModulosDTO;
import beteam.viloco.trackcheck.dto.TerritoryDTO;
import beteam.viloco.trackcheck.dto.UserDTO;
import beteam.viloco.trackcheck.dto.ZoneDTO;
import beteam.viloco.trackcheck.repositorios.BusinessTypeRepository;
import beteam.viloco.trackcheck.repositorios.DataRepository;
import beteam.viloco.trackcheck.repositorios.LogErrorRepository;
import beteam.viloco.trackcheck.repositorios.TerritoryRepository;
import beteam.viloco.trackcheck.repositorios.UserRepository;
import beteam.viloco.trackcheck.repositorios.ZoneRepository;
import beteam.viloco.trackcheck.util.CustomException;

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
    public UserDTO GetUserAuthenticated() throws CustomException {
        UserRepository repo = new UserRepository(mContext);
        return repo.GetUserAuthenticated();
    }

    public boolean InsertUserAuthenticated(UserDTO user) throws CustomException {
        UserRepository repo = new UserRepository(mContext);
        return repo.InsertUserAuthenticated(user);
    }

    public boolean DeleteUserAuthenticated(int Id) throws CustomException {
        UserRepository repo = new UserRepository(mContext);
        return repo.DeleteUserAuthenticated(Id);
    }

    public ArrayList<ModulosDTO> GetModules() throws CustomException {
        ArrayList<ModulosDTO> list = new ArrayList<>();

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
        DataRepository repo = new DataRepository(mContext);
        return repo.InsertData(dataDTO);
    }

    public ArrayList<DataDTO> GetDataPending(int IdUser) throws CustomException {
        DataRepository repo = new DataRepository(mContext);
        return repo.GetDataPending(IdUser);
    }

    public int SendDataPending(DataDTO dataDTO) throws CustomException {
        DataRepository repo = new DataRepository(mContext);
        return repo.SendDataPending(dataDTO);
    }

    public int SendDataPhotoPending(DataPhotoDTO dataPhotoDTO) throws CustomException {
        DataRepository repo = new DataRepository(mContext);
        return repo.SendDataPhotoPending(dataPhotoDTO);
    }

    public DataDTO ReadFirstData() throws CustomException {
        DataRepository repo = new DataRepository(mContext);
        return repo.ReadFirstByPredicate(null);
    }

    public boolean DeleteDataAndPhoto(int Id) throws CustomException{
        DataRepository repo = new DataRepository(mContext);
        return repo.DeleteDataAndPhoto(Id);
    }

    /*Territory*/
    public boolean ExistsTerritories() throws CustomException {
        TerritoryRepository repo = new TerritoryRepository(mContext);
        return repo.ExistsTerritories();
    }

    public ArrayList<TerritoryDTO> ReadAllTerritory() throws CustomException {
        TerritoryRepository repo = new TerritoryRepository(mContext);
        return repo.ReadAllByPredicate(null);
    }

    public boolean GetTerritories() throws CustomException {
        TerritoryRepository repo = new TerritoryRepository(mContext);
        return repo.GetTerritories();
    }

    /*Zone*/
    public boolean ExistsZones() throws CustomException {
        ZoneRepository repo = new ZoneRepository(mContext);
        return repo.ExistsZones();
    }

    public ArrayList<ZoneDTO> ReadAllZone() throws CustomException {
        ZoneRepository repo = new ZoneRepository(mContext);
        return repo.ReadAllByPredicate(null);
    }

    public boolean GetZones() throws CustomException {
        ZoneRepository repo = new ZoneRepository(mContext);
        return repo.GetZones();
    }

    /*BusinessType*/
    public boolean ExistsBusinessTypes() throws CustomException {
        BusinessTypeRepository repo = new BusinessTypeRepository(mContext);
        return repo.ExistsBusinessTypes();
    }

    public ArrayList<BusinessTypeDTO> ReadAllBusinessType() throws CustomException {
        BusinessTypeRepository repo = new BusinessTypeRepository(mContext);
        return repo.ReadAllByPredicate(null);
    }

    public boolean GetBusinessTypes() throws CustomException {
        BusinessTypeRepository repo = new BusinessTypeRepository(mContext);
        return repo.GetBusinessTypes();
    }

    /*LogError*/
    public boolean SendLogError() throws CustomException {
        LogErrorRepository repo = new LogErrorRepository(mContext);
        return repo.SendLogError();
    }
}
