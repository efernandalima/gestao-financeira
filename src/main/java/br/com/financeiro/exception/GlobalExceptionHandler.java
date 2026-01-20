package br.com.financeiro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleResourceNotFound(
          ResourceNotFoundException ex) {
    Map<String, Object> erro = new HashMap<>();
    erro.put("timestamp", LocalDateTime.now());
    erro.put("status", HttpStatus.NOT_FOUND.value());
    erro.put("erro", "Recurso não encontrado");
    erro.put("mensagem", ex.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgument(
          IllegalArgumentException ex) {
    Map<String, Object> erro = new HashMap<>();
    erro.put("timestamp", LocalDateTime.now());
    erro.put("status", HttpStatus.BAD_REQUEST.value());
    erro.put("erro", "Requisição inválida");
    erro.put("mensagem", ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationErrors(
          MethodArgumentNotValidException ex) {
    Map<String, String> erros = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String campo = ((FieldError) error).getField();
      String mensagem = error.getDefaultMessage();
      erros.put(campo, mensagem);
    });

    Map<String, Object> resposta = new HashMap<>();
    resposta.put("timestamp", LocalDateTime.now());
    resposta.put("status", HttpStatus.BAD_REQUEST.value());
    resposta.put("erro", "Erro de validação");
    resposta.put("mensagens", erros);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGenericException(
          Exception ex) {
    Map<String, Object> erro = new HashMap<>();
    erro.put("timestamp", LocalDateTime.now());
    erro.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    erro.put("erro", "Erro interno do servidor");
    erro.put("mensagem", ex.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
  }
}
