package app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.Categoria;
import app.exceptions.CategoriaNotFoundException;
import app.repositories.CategoriaRepository;
import app.repositories.UsuarioRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	/**
	 * Retorna todas as categorias cadastradas.
	 */
	public List<Categoria> findAll() {
		return categoriaRepository.findAll();
	}

	/**
	 * Retorna as categorias de um usuário específico pelo ID do usuário.
	 */
	public List<Categoria> findByUsuarioId(Long usuarioId) {
		if (!usuarioRepository.existsById(usuarioId)) {
			throw new CategoriaNotFoundException("Usuário não encontrado para o ID: " + usuarioId);
		}
		return categoriaRepository.findByUsuarioId(usuarioId);
	}

	/**
	 * Busca uma categoria pelo ID. Lança exceção se não for encontrada.
	 */
	public Categoria findById(Long id) {
		return categoriaRepository.findById(id).orElseThrow(() -> new CategoriaNotFoundException(id));
	}

	/**
	 * Salva uma categoria no banco de dados.
	 */
	public Categoria save(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	/**
	 * Exclui uma categoria pelo ID. Lança exceção se não for encontrada.
	 */
	public void deleteById(Long id) {
		if (!categoriaRepository.existsById(id)) {
			throw new CategoriaNotFoundException(id);
		}
		categoriaRepository.deleteById(id);
	}
}