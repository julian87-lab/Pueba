package com.prueba.app.controller;

import com.prueba.app.entity.Club;
import com.prueba.app.entity.Entrenador;
import com.prueba.app.entity.Jugador;
import com.prueba.app.entity.Asociacion;
import com.prueba.app.entity.Competicion;
import com.prueba.app.repository.ClubRepository;
import com.prueba.app.repository.EntrenadorRepository;
import com.prueba.app.repository.JugadorRepository;
import com.prueba.app.repository.AsociacionRepository;
import com.prueba.app.repository.CompeticionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ClubController {

    @Autowired private ClubRepository clubRepository;
    @Autowired private EntrenadorRepository entrenadorRepository;
    @Autowired private JugadorRepository jugadorRepository;
    @Autowired private AsociacionRepository asociacionRepository;
    @Autowired private CompeticionRepository competicionRepository;

    // ── Página principal: formulario de Club ──────────────
    @GetMapping("/")
    public String mostrarFormulario(Model model) {
        model.addAttribute("club", new Club());
        model.addAttribute("entrenadores", entrenadorRepository.findAll());
        model.addAttribute("asociaciones", asociacionRepository.findAll());
        model.addAttribute("competiciones", competicionRepository.findAll());
        model.addAttribute("jugadores", jugadorRepository.findAll()); // ← NUEVO
        return "index";
    }

    // ── Guardar Club ──────────────────────────────────────
    @PostMapping("/guardar-club")
    public String guardarClub(
            @ModelAttribute Club club,
            @RequestParam(required = false) Long entrenadorId,
            @RequestParam(required = false) Long asociacionId,
            @RequestParam(required = false) List<Long> competicionIds,
            @RequestParam(required = false) List<Long> jugadorIds) { // ← NUEVO

        if (entrenadorId != null) {
            entrenadorRepository.findById(entrenadorId)
                    .ifPresent(club::setEntrenador);
        }
        if (asociacionId != null) {
            asociacionRepository.findById(asociacionId)
                    .ifPresent(club::setAsociacion);
        }
        if (competicionIds != null && !competicionIds.isEmpty()) {
            club.setCompeticiones(competicionRepository.findAllById(competicionIds));
        }
        // ← NUEVO: asignar jugadores al club
        if (jugadorIds != null && !jugadorIds.isEmpty()) {
            club.setJugadores(jugadorRepository.findAllById(jugadorIds));
        } else {
            club.setJugadores(List.of()); // limpia jugadores si no seleccionó ninguno
        }

        clubRepository.save(club);
        return "redirect:/listar";
    }

    // ── Listar Clubs ──────────────────────────────────────
    @GetMapping("/listar")
    public String listarClubes(Model model) {
        model.addAttribute("clubes", clubRepository.findAll());
        return "listar";
    }

    // ── Eliminar Club ─────────────────────────────────────
    @GetMapping("/eliminar/{id}")
    public String eliminarClub(@PathVariable Long id) {
        clubRepository.deleteById(id);
        return "redirect:/listar";
    }

    // ── Editar Club ───────────────────────────────────────
    @GetMapping("/editar/{id}")
    public String editarClub(@PathVariable Long id, Model model) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club no encontrado: " + id));
        model.addAttribute("club", club);
        model.addAttribute("entrenadores", entrenadorRepository.findAll());
        model.addAttribute("asociaciones", asociacionRepository.findAll());
        model.addAttribute("competiciones", competicionRepository.findAll());
        model.addAttribute("jugadores", jugadorRepository.findAll()); // ← NUEVO
        return "index";
    }

    // ── ENTRENADORES ──────────────────────────────────────
    @GetMapping("/entrenadores")
    public String mostrarEntrenadores(Model model) {
        model.addAttribute("entrenador", new Entrenador());
        model.addAttribute("entrenadores", entrenadorRepository.findAll());
        return "entrenador";
    }

    @PostMapping("/guardar-entrenador")
    public String guardarEntrenador(@ModelAttribute Entrenador entrenador) {
        entrenadorRepository.save(entrenador);
        return "redirect:/entrenadores";
    }

    @GetMapping("/eliminar-entrenador/{id}")
    public String eliminarEntrenador(@PathVariable Long id) {
        entrenadorRepository.deleteById(id);
        return "redirect:/entrenadores";
    }

    // ── ASOCIACIONES ──────────────────────────────────────
    @GetMapping("/asociaciones")
    public String mostrarAsociaciones(Model model) {
        model.addAttribute("asociacion", new Asociacion());
        model.addAttribute("asociaciones", asociacionRepository.findAll());
        return "asociacion";
    }

    @PostMapping("/guardar-asociacion")
    public String guardarAsociacion(@ModelAttribute Asociacion asociacion) {
        asociacionRepository.save(asociacion);
        return "redirect:/asociaciones";
    }

    @GetMapping("/eliminar-asociacion/{id}")
    public String eliminarAsociacion(@PathVariable Long id) {
        asociacionRepository.deleteById(id);
        return "redirect:/asociaciones";
    }

    // ── COMPETICIONES ─────────────────────────────────────
    @GetMapping("/competiciones")
    public String mostrarCompeticiones(Model model) {
        model.addAttribute("competicion", new Competicion());
        model.addAttribute("competiciones", competicionRepository.findAll());
        return "competicion";
    }

    @PostMapping("/guardar-competicion")
    public String guardarCompeticion(@ModelAttribute Competicion competicion) {
        competicionRepository.save(competicion);
        return "redirect:/competiciones";
    }

    @GetMapping("/eliminar-competicion/{id}")
    public String eliminarCompeticion(@PathVariable Long id) {
        competicionRepository.deleteById(id);
        return "redirect:/competiciones";
    }

    // ── JUGADORES ─────────────────────────────────────────
    @GetMapping("/jugadores")
    public String mostrarJugadores(Model model) {
        model.addAttribute("jugador", new Jugador());
        model.addAttribute("jugadores", jugadorRepository.findAll());
        return "jugador";
    }

    @PostMapping("/guardar-jugador")
    public String guardarJugador(@ModelAttribute Jugador jugador) {
        jugadorRepository.save(jugador);
        return "redirect:/jugadores";
    }

    @GetMapping("/eliminar-jugador/{id}")
    public String eliminarJugador(@PathVariable Long id) {
        jugadorRepository.deleteById(id);
        return "redirect:/jugadores";
    }
}