package app.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import app.entities.Meta;
import app.entities.Usuario;
import app.enums.StatusMeta;
import app.exceptions.MetaNotFoundException;
import app.repositories.MetaRepository;
import app.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MetaService {

	@Autowired
	private MetaRepository metaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	/**
	 * Retorna todas as metas cadastradas.
	 */
	public List<Meta> listAll() {
		return metaRepository.findAll();
	}

	/**
	 * Busca uma meta pelo ID. Lança exceção se não for encontrada.
	 */
	public Meta findById(Long id) {
		return metaRepository.findById(id).orElseThrow(() -> new MetaNotFoundException(id));
	}

	/**
	 * Salva uma meta no banco de dados, garantindo que o usuário existe.
	 */
	public Meta save(Meta meta) {
		Usuario usuario = usuarioRepository.findById(meta.getUsuario().getId())
				.orElseThrow(() -> new DataIntegrityViolationException("Usuário não encontrado."));

		if (metaRepository.existsByDescricaoAndUsuario(meta.getDescricao(), usuario)) {
			throw new DataIntegrityViolationException("Já existe uma meta com essa descrição para este usuário.");
		}

		meta.setUsuario(usuario);
		return metaRepository.save(meta);
	}

	/**
	 * Atualiza uma meta existente pelo ID. Lança exceção se não for encontrada.
	 */
	public Meta update(Long id, Meta metaAtualizada) {
		Meta metaExistente = findById(id);

		if (metaAtualizada.getUsuario() != null && metaAtualizada.getUsuario().getId() != null) {
			Usuario usuario = usuarioRepository.findById(metaAtualizada.getUsuario().getId())
					.orElseThrow(() -> new DataIntegrityViolationException("Usuário não encontrado."));
			metaExistente.setUsuario(usuario);
		}

		metaExistente.setDescricao(metaAtualizada.getDescricao());
		metaExistente.setValorObjetivo(metaAtualizada.getValorObjetivo());
		metaExistente.setValorAtual(metaAtualizada.getValorAtual());
		metaExistente.setDataInicio(metaAtualizada.getDataInicio());
		metaExistente.setDataTermino(metaAtualizada.getDataTermino());
		metaExistente.setStatus(metaAtualizada.getStatus());

		return metaRepository.save(metaExistente);
	}

	/**
	 * Exclui uma meta pelo ID. Lança exceção se não for encontrada.
	 */
	public void deleteById(Long id) {
		if (!metaRepository.existsById(id)) {
			throw new MetaNotFoundException(id);
		}
		metaRepository.deleteById(id);
	}
}
