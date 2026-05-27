package com.frontend.frontbiblioteca.WebServiceClient;

import com.frontend.frontbiblioteca.Model.Libro;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibroRestClient {
    private final RestTemplate restTemplate;

    //La url debe ser la misma que uso en mi backend
    private final String baseUrl = "http://localhost:8080/api/libros";

    public List<Libro> listarLibros() {
        //ForObject devuelve el objeto deserializado, nada más
        Libro [] librosArray = restTemplate.getForObject(baseUrl, Libro[].class);
        assert librosArray != null;
        return List.of(librosArray);
    }

    public ResponseEntity<Libro> crearNuevoLibro(Libro libro) {
        try{
            return restTemplate.postForEntity(baseUrl, libro, Libro.class);
        }
        catch(HttpStatusCodeException e){
            return ResponseEntity.status(e.getStatusCode()).build();
        }
        //Añadimos excepciones extras (backend apagado o no hay internet)
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    public ResponseEntity<Libro> actualizarLibro(String isbn, Libro libro) {
        //Exchange permite usar GET, POST, PUT Y DELETE
        //Al usarlo, debemos especificar cuál usar
        //Su estructura es url, metodoHttp, entidadPeticion, claseRespuesta
        try {
            //Envolvemos el objeto en una entidad http
            HttpEntity<Libro> requestEntity = new HttpEntity<>(libro);
            return restTemplate.exchange(baseUrl + "/" + isbn, HttpMethod.PUT, requestEntity, Libro.class);
        }
        catch (HttpStatusCodeException e){
            return ResponseEntity.status(e.getStatusCode()).build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    public ResponseEntity<Libro> eliminarLibro(String isbn) {
        try {
            //No es necesario envolver objeto en http porque la respuesta es noContent
            return restTemplate.exchange(baseUrl + "/" + isbn, HttpMethod.DELETE, null, Libro.class);
        }
        catch (HttpStatusCodeException e){
            return  ResponseEntity.status(e.getStatusCode()).build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
}
