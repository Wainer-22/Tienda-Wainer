//package Tienda.Web.service.impl;
//
//import com.google.auth.Credentials;
//import com.google.auth.ServiceAccountSigner;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.storage.*;
//import com.google.cloud.storage.BlobInfo;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.Storage.SignUrlOption;
//import com.google.cloud.storage.StorageOptions;
//import Tienda.Web.service.FirebaseStorageService;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.concurrent.TimeUnit;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//@Service
//public class FirebaseStorageServiceImpl implements FirebaseStorageService {
//
//    @Override
//    public String cargaImagen(MultipartFile archivoLocalCliente, String carpeta, Long id) {
//        try {
//            // 1. Obtener la extensión del archivo original
//            String extension = archivoLocalCliente.getOriginalFilename();
//
//            // 2. Generar nombre único para el archivo
//            String fileName = "img" + sacaNumero(id) + extension;
//
//            // 3. Convertir MultipartFile a File temporal
//            File file = this.convertToFile(archivoLocalCliente);
//
//            // 4. Subir a Firebase y obtener URL pública
//            String URL = this.uploadFile(file, carpeta, fileName);
//
//            // 5. Eliminar archivo temporal
//            file.delete();
//
//            return URL;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private String uploadFile(File file, String carpeta, String fileName) throws IOException {
//        // Se define el lugar y acceso al archivo .json de credenciales
//        ClassPathResource json = new ClassPathResource(rutaJsonFile + File.separator + archivoJsonFile);
//
//        // Configuración del blob (archivo en Firebase Storage)
//        BlobId blobId = BlobId.of(BucketName, rutaSuperiorStorage + "/" + carpeta + "/" + fileName);
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
//                .setContentType("media")
//                .build();
//
//        // Autenticación con Firebase
//        Credentials credentials = GoogleCredentials.fromStream(json.getInputStream());
//        Storage storage = StorageOptions.newBuilder()
//                .setCredentials(credentials)
//                .build()
//                .getService();
//
//        // Subida del archivo
//        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
//
//        // Generación de URL firmada (válida por 10 años)
//        String url = storage.signUrl(blobInfo,
//                3650, // 10 años en días
//                TimeUnit.DAYS,
//                SignUrlOption.signWith((ServiceAccountSigner) credentials))
//                .toString();
//        return url;
//    }
//
//}

package Tienda.Web.service.impl;

import com.google.auth.Credentials;
import com.google.auth.ServiceAccountSigner;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import Tienda.Web.service.FirebaseStorageService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {

    // Variables necesarias para la configuración de Firebase
    private final String rutaJsonFile = "firebase"; // Carpeta donde está tu archivo .json
    private final String archivoJsonFile = "techshop-7f926-firebase-adminsdk-ftmwx-0442f8d411.json"; // Nombre del archivo .json
    private final String BucketName = "techshop-7f926.appspot.com"; // Nombre del bucket
    private final String rutaSuperiorStorage = "imagenes"; // Carpeta dentro del bucket, si aplica

    @Override
    public String cargaImagen(MultipartFile archivoLocalCliente, String carpeta, Long id) {
        try {
            // 1. Obtener la extensión del archivo original
            String extension = obtenerExtension(archivoLocalCliente.getOriginalFilename());

            // 2. Generar nombre único para el archivo
            String fileName = "img" + sacaNumero(id) + extension;

            // 3. Convertir MultipartFile a File temporal
            File file = this.convertToFile(archivoLocalCliente);

            // 4. Subir a Firebase y obtener URL pública
            String URL = this.uploadFile(file, carpeta, fileName);

            // 5. Eliminar archivo temporal
            file.delete();

            return URL;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String uploadFile(File file, String carpeta, String fileName) throws IOException {
        // Se define el lugar y acceso al archivo .json de credenciales
        ClassPathResource json = new ClassPathResource(rutaJsonFile + File.separator + archivoJsonFile);

        // Configuración del blob (archivo en Firebase Storage)
        BlobId blobId = BlobId.of(BucketName, rutaSuperiorStorage + "/" + carpeta + "/" + fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("media")
                .build();

        // Autenticación con Firebase
        Credentials credentials = GoogleCredentials.fromStream(json.getInputStream());
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();

        // Subida del archivo
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        // Generación de URL firmada (válida por 10 años)
        return storage.signUrl(blobInfo,
                3650, // 10 años en días
                TimeUnit.DAYS,
                Storage.SignUrlOption.signWith((ServiceAccountSigner) credentials))
                .toString();
    }

    private File convertToFile(MultipartFile multipartFile) throws IOException {
        File convFile = File.createTempFile("temp", null);
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(multipartFile.getBytes());
        }
        return convFile;
    }

    private String sacaNumero(Long id) {
        return String.valueOf(id); // Simplemente convierte el ID a texto
    }

    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo != null && nombreArchivo.contains(".")) {
            return nombreArchivo.substring(nombreArchivo.lastIndexOf("."));
        }
        return ""; // Sin extensión
    }
}
