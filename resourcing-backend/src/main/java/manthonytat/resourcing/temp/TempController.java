package manthonytat.resourcing.temp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import manthonytat.resourcing.exceptions.NotFoundException;
import manthonytat.resourcing.user.User;

@RestController
@RequestMapping("/temps")
public class TempController {
  @Autowired
  private TempService tempService;

  @GetMapping
  public ResponseEntity<List<TempDTO>> getAll(@RequestParam(required = false) Long jobId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) auth.getPrincipal();
    if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
      if (jobId != null) {
        List<TempDTO> filteredTemps = this.tempService.getAvailableTemps(jobId);
        return new ResponseEntity<List<TempDTO>>(filteredTemps, HttpStatus.OK);
      } else {
        List<TempDTO> allTemps = this.tempService.findAll();
        return new ResponseEntity<List<TempDTO>>(allTemps, HttpStatus.OK);
      }
    }

    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot access this api");
  }

  @GetMapping("/{id}")
  public ResponseEntity<TempDTO> getById(@PathVariable Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) auth.getPrincipal();

    if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
      Optional<TempDTO> foundTemp = this.tempService.findById(id);
      if (foundTemp.isPresent()) {
        return new ResponseEntity<TempDTO>(foundTemp.get(), HttpStatus.OK);
      }
      throw new NotFoundException(String.format("Temp with id: %d does not exist, could not find temp.", id));
    }

    if (user.getId().equals(id)) {
      Optional<TempDTO> foundTemp = this.tempService.findById(id);
      if (foundTemp.isPresent()) {
        return new ResponseEntity<TempDTO>(foundTemp.get(), HttpStatus.OK);
      }
      throw new NotFoundException(String.format("Temp with id: %d does not exist, could not find temp.", id));
    }

    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot access this profile");
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Temp> patch(@PathVariable Long id, @Valid @RequestBody TempUpdateDTO data) {
    Optional<Temp> updated = this.tempService.updateById(id, data);
    if (updated.isPresent()) {
      return new ResponseEntity<Temp>(updated.get(), HttpStatus.OK);
    }
    throw new NotFoundException(String.format("Temp with id: %d does not exist, could not update temp.", id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Temp> deleteById(@PathVariable Long id) {
    if (this.tempService.deleteById(id)) {
      return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
    throw new NotFoundException(String.format("Temp with id: %d does not exist, could not delete temp.", id));
  }
}
