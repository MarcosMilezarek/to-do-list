package devmarcos.todolist.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroValidacaoDTO> valorNuloException(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            erros.put(error.getField(), error.getDefaultMessage());
        });
        ErroValidacaoDTO dto = new ErroValidacaoDTO(req.getRequestURI(), "O campo precisa esta preenchido corretamente", Instant.now(), HttpStatus.BAD_REQUEST, erros);
        return ResponseEntity.badRequest().body(dto);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ErroDTO> jsonIncorreto(HttpMessageConversionException ex, HttpServletRequest request) {
        ErroDTO dto = new ErroDTO(request.getRequestURI(), "Json informado está incorreto", Instant.now(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.badRequest().body(dto);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(TaskNaoEncontradaException.class)
    public ResponseEntity<ErroDTO> taskNaoEncontrada(TaskNaoEncontradaException ex, HttpServletRequest request) {
        ErroDTO dto = new ErroDTO(request.getRequestURI(), ex.getMessage(), Instant.now(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroDTO> methodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        ErroDTO dto = new ErroDTO(request.getRequestURI(), ex.getMessage(), Instant.now(), HttpStatus.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(dto);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<ErroDTO> enderecoInvalido(NoResourceFoundException ex, HttpServletRequest request) {
        ErroDTO dto = new ErroDTO(request.getRequestURI(), "Caminho não existe", Instant.now(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(UsuarioNaoEncontrado.class)
    public ResponseEntity<ErroDTO> usuarioNaoEncontrado(UsuarioNaoEncontrado ex, HttpServletRequest request) {
        ErroDTO dto = new ErroDTO(request.getRequestURI(), ex.getMessage(), Instant.now(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
    }
}
