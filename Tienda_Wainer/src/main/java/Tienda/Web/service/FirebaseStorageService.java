package Tienda.Web.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FirebaseStorageService {

    public String cargaImagen(MultipartFile archivoLocalCliente, String carpeta, Long id);

    // El BucketName es el <id_del_proyecto> + ".appspot.com"
    final String BucketName = "techshop-7f926.appspot.com";

    // Esta es la ruta básica de este proyecto Techshop
    final String rutaSuperiorStorage = "techshop";

    // Ubicación donde se encuentra el archivo de configuración Json
    final String rutaJsonFile = "firebase";

    // El nombre del archivo Json
    final String archivoJsonFile = "techshop-7f926-firebase-adminsdk-ftmwx-0442f8d411.json";
}
