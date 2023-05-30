package tads.eaj.ufrn.aulamvccrud.util;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class Upload {
    public static void saveFile(String uploadDir, String fileName,
            MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
         
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
         
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {        
            throw new IOException("Could not save image file: " + fileName, ioe);
        }      
    }

}


/*
 boolean sucessoUpload = false;
		if (!imagem.isEmpty()) {
		String nomeArquivo = imagem.getOriginalFilename();
		try {
		// Criando o diretório para armazenar o arquivo
		String workspaceProjeto = "C:\\Users\\Felipe\\Documents\\Faculdade\\3 Semestre\\PROGRAMACAO WEB\\ProvaPW-main\\ProvaPW-main\\src\\main\\resources\\static\\images";
		File dir = new File(workspaceProjeto);
		if (!dir.exists()) {
		dir.mkdirs();
		}
		// Criando o arquivo no diretório
		File serverFile = new File(dir.getAbsolutePath() + File.separator + nomeArquivo);
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
		stream.write(imagem.getBytes());
		stream.close();
		System. out .println("Arquivo armazenado em:" + serverFile.getAbsolutePath());
		System. out .println("Você fez o upload do arquivo " + nomeArquivo + " com sucesso");
		sucessoUpload = true;
		} catch (Exception e) {
		System. out .println("Você falhou em carregar o arquivo " + nomeArquivo + " => " + e.getMessage());
		}
		} else {
		System. out .println("Você falhou em carregar o arquivo porque ele está vazio ");
		}
		return sucessoUpload;
		}
 */