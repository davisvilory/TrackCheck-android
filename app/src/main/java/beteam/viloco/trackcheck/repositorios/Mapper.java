package beteam.viloco.trackcheck.repositorios;

import beteam.viloco.trackcheck.dto.AjaxResponse;
import beteam.viloco.trackcheck.dto.DataDTO;
import beteam.viloco.trackcheck.dto.DataPhotoDTO;
import beteam.viloco.trackcheck.dto.LogErrorDTO;
import beteam.viloco.trackcheck.dto.TerritoryDTO;
import beteam.viloco.trackcheck.dto.UserDTO;
import beteam.viloco.trackcheck.dto.UserTypeDTO;
import beteam.viloco.trackcheck.dto.ZoneDTO;

public class Mapper {
    public static Object InstanceOfObjectType(String tClass) {
        Object object = null;

        if (tClass.equalsIgnoreCase(AjaxResponse.class.getSimpleName())) {
            object = new AjaxResponse();
        } else if (tClass.equalsIgnoreCase(DataDTO.class.getSimpleName())) {
            object = new DataDTO();
        } else if (tClass.equalsIgnoreCase(DataPhotoDTO.class.getSimpleName())) {
            object = new DataPhotoDTO();
        } else if (tClass.equalsIgnoreCase(LogErrorDTO.class.getSimpleName())) {
            object = new LogErrorDTO();
        } else if (tClass.equalsIgnoreCase(TerritoryDTO.class.getSimpleName())) {
            object = new TerritoryDTO();
        } else if (tClass.equalsIgnoreCase(UserDTO.class.getSimpleName())) {
            object = new UserDTO();
        } else if (tClass.equalsIgnoreCase(UserTypeDTO.class.getSimpleName())) {
            object = new UserTypeDTO();
        } else if (tClass.equalsIgnoreCase(ZoneDTO.class.getSimpleName())) {
            object = new ZoneDTO();
        }

        return object;
    }
}
